package microarch.delivery.adapters.out.postgres;

import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.general.StoragePlace;
import microarch.delivery.core.ports.CourierRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CourierPersistence  implements CourierRepository {

    JdbcTemplate jdbcTemplate;

    public CourierPersistence(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public boolean addCourier(Courier c) {
        String sql = "INSERT INTO couriers (id,name,speed,current_l_x,current_l_y) VALUES (?, ?, ?, ?, ?)";
        // Returns the number of rows affected (usually 1)
        int countRows = jdbcTemplate.update(sql, c.getId(), c.getName(),c.getSpeed(), c.getCurrentLocation().getX(), c.getCurrentLocation().getY());
        if (countRows == 0) return false;
        if (countRows == 1 && !c.getStoragePlaces().isEmpty()) {
            sql = "INSERT INTO couriers_storageplaces (id,courier_id,name,volume,order_id) values (?,?,?,?,?)";
            // Пакетная вставка для производительности
            List<Object[]> batchArgs = c.getStoragePlaces().stream()
                    .map(item -> new Object[]{
                            item.getId(),
                            c.getId(),
                            item.getName(),
                            item.getTotalVolume(),
                            item.getOrderId()
                    })
                    .collect(Collectors.toList());

            var inserted = jdbcTemplate.batchUpdate(sql, batchArgs);
            if (inserted.length < c.getStoragePlaces().size()) return false;
        }
        return true;
    }

    @Override
    @Transactional
    public Optional<Courier> findById(UUID courierId) {

        List<StoragePlace> storagePlaces =
                jdbcTemplate.query("SELECT * FROM couriers_storageplaces WHERE courier_id = ?",
                        (rs, rowNum) -> {
                            var placeResult = StoragePlace.createFromDB(
                                    rs.getObject("id", UUID.class),
                                    rs.getString("name"),
                                    rs.getInt("volume"),
                                    rs.getObject("order_id", UUID.class));

                      return placeResult.getValue();
                    }, courierId);

        Courier courier = jdbcTemplate.queryForObject("SELECT * FROM couriers WHERE id = ?",
                (rs, rowNum) -> {
                    var courierResult = Courier.createFromDB(
                            rs.getObject("id", UUID.class),
                            rs.getString("name"),
                            rs.getInt("speed"),
                            rs.getInt("current_l_x"),
                            rs.getInt("current_l_y"),
                            storagePlaces);

            return courierResult.getValue();

        }, courierId);

        return courier == null ?  Optional.empty() : Optional.of(courier);
    }
}
