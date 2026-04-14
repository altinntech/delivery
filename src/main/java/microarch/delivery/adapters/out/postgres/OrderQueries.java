package microarch.delivery.adapters.out.postgres;

import microarch.delivery.core.application.view.dto.CourierDto;
import microarch.delivery.core.application.view.dto.LocationDto;
import microarch.delivery.core.application.view.dto.OrderDto;
import microarch.delivery.core.domain.model.order.OrderStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class OrderQueries {

    JdbcTemplate jdbcTemplate;

    public OrderQueries(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrderDto> findAllUncompletedOrders() {

        return jdbcTemplate.query("SELECT * FROM Orders WHERE status_code = ? or status_code = ?", (rs, rowNum) -> {
            LocationDto locationDto = new LocationDto(rs.getInt("location_x"),rs.getInt("location_y"));
            return new OrderDto(
                    rs.getObject("id", UUID.class),
                    locationDto);
        }, OrderStatus.CREATED.getCode(),OrderStatus.ASSIGNED.getCode());

    }
}
