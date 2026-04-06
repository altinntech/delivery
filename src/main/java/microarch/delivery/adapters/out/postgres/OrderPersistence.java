package microarch.delivery.adapters.out.postgres;

import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderPersistence implements OrderRepository {

    JdbcTemplate jdbcTemplate;

    public OrderPersistence(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addOrder(Order o) {
        String sql = "INSERT INTO Orders (id,location_x,location_y,volume,status_code,courierId) VALUES (?, ?, ?, ?, ?, ?)";
        // Returns the number of rows affected (usually 1)
        int countRows = jdbcTemplate.update(sql, o.getId(), o.getTargetLocation().getX(), o.getTargetLocation().getY(),
                o.getVolume(), o.getStatus().getCode(), o.getCourierId());
        return countRows > 0;
    }

    @Override
    public boolean updateOrder(Order o) {
        String sql = "UPDATE Orders SET location_x = ?,location_y = ?,volume = ?,status_code = ?,courierId = ?) WHERE id = ?";
        // Returns the number of rows affected (usually 1)
        int countRows = jdbcTemplate.update(sql, o.getTargetLocation().getX(), o.getTargetLocation().getY(),
                o.getVolume(), o.getStatus().getCode(), o.getCourierId(), o.getId());
        return countRows > 0;
    }

    @Override
    public Optional<Order> findById(UUID orderId) {

        Order order = jdbcTemplate.queryForObject("SELECT * FROM Orders WHERE id = ?", (rs, rowNum) -> {
            var orderResult = Order.createFromDB(rs.getObject("id", UUID.class), rs.getInt("location_x"),
                    rs.getInt("location_y"), rs.getInt("volume"), rs.getInt("status_code"),
                    rs.getObject("courierId", UUID.class));

            return orderResult.getValue();

        }, orderId);

        return Optional.empty();
    }

    @Override
    public Optional<Order> findAnyOneCreated() {
        return Optional.empty();
    }

    @Override
    public List<Order> findAllAssigned() {
        return List.of();
    }
}
