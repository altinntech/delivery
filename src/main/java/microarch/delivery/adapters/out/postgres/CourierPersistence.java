package microarch.delivery.adapters.out.postgres;

import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.general.StoragePlace;
import microarch.delivery.core.ports.CourierRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
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
        int countRows = jdbcTemplate.update(sql, c.getId(), c.getName(),c.getSpeed().getValue(), c.getCurrentLocation().getX(), c.getCurrentLocation().getY());
        if (countRows == 0) return false;
        if (countRows == 1 && !c.getStoragePlaces().isEmpty()) {
            var inserted = insertStoragePlaces(c.getId(),c.getStoragePlaces());
            if (inserted.length < c.getStoragePlaces().size()) return false;
        }
        return true;
    }

    private int [] insertStoragePlaces (UUID courierId, List<StoragePlace> places) {

        String sql = "INSERT INTO couriers_storageplaces (id,courier_id,name,volume,order_id) values (?,?,?,?,?)";
        // Пакетная вставка для производительности
        List<Object[]> batchArgs = places.stream()
                .map(item -> new Object[]{
                        item.getId(),
                        courierId,
                        item.getName(),
                        item.getTotalVolume(),
                        item.getOrderId()
                })
                .collect(Collectors.toList());

        return jdbcTemplate.batchUpdate(sql, batchArgs);
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

    @Override
    @Transactional
    public boolean updateCourier(Courier c) {

      String sql = "UPDATE couriers SET name = ?,speed = ?,current_l_x = ?,current_l_y = ? WHERE id = ?";
      // Returns the number of rows affected (usually 1)
      int countRows = jdbcTemplate.update(sql, c.getName(),c.getSpeed().getValue(), c.getCurrentLocation().getX(), c.getCurrentLocation().getY(),c.getId());
      if (countRows == 0) return false;
      //Delete all old storage places
      sql = "DELETE from couriers_storageplaces where courier_id = ?";
      jdbcTemplate.update(sql,c.getId());
      //Add new storage places
      if (countRows == 1 && !c.getStoragePlaces().isEmpty()) {
         var inserted = insertStoragePlaces(c.getId(),c.getStoragePlaces());
         if (inserted.length < c.getStoragePlaces().size()) return false;
      }

      return true;
    }

    @Override
    public List<Courier> findAllFreeCouriers() {
        String sql = """
        SELECT
            c.id AS courier_id, c.name AS courier_name, c.speed, c.current_l_x, c.current_l_y,
            sp.id AS storage_id, sp.courier_id, sp.name AS storage_name, sp.volume, sp.order_id
        FROM couriers c
        LEFT JOIN couriers_storageplaces sp ON c.id = sp.courier_id
        WHERE NOT EXISTS (
                     SELECT 1 FROM couriers_storageplaces sp1
                     WHERE sp1.courier_id = c.id AND sp1.order_id is not null
                 )
        ORDER BY c.id, sp.id
       """;

        return jdbcTemplate.query(sql, new ResultSetExtractor<List<Courier>>() {
            @Override
            public List<Courier> extractData(ResultSet rs) throws SQLException {
                Map<UUID, Courier> couriesMap = new LinkedHashMap<>();
                Map<UUID, List<StoragePlace>> storagePlaceMap = new LinkedHashMap<>();
                while (rs.next()) {
                    UUID courierId = rs.getObject("courier_id", UUID.class);

                    // Добавляем позицию, если она есть (LEFT JOIN может дать NULL для courier_id)
                    UUID storageId = rs.getObject("storage_id", UUID.class);
                    List<StoragePlace> courierSP = storagePlaceMap.get(courierId);
                    if (!rs.wasNull()) {
                        var placeResult = StoragePlace.createFromDB(
                                storageId,
                                rs.getString("storage_name"),
                                rs.getInt("volume"),
                                rs.getObject("order_id", UUID.class));

                        if (courierSP == null) courierSP = new ArrayList<StoragePlace>();
                        courierSP.add(placeResult.getValue());
                        storagePlaceMap.put(courierId,courierSP);
                    }

                    var courierResult = Courier.createFromDB(
                                courierId,
                                rs.getString("courier_name"),
                                rs.getInt("speed"),
                                rs.getInt("current_l_x"),
                                rs.getInt("current_l_y"),
                                courierSP);
                    couriesMap.put(courierId,courierResult.getValue());

                }
                return new ArrayList<>(couriesMap.values());
            }
        });

    }
}
