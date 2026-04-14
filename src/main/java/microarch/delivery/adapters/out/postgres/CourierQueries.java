package microarch.delivery.adapters.out.postgres;

import microarch.delivery.core.application.view.all_couriers.GetAllCouriersResponse;
import microarch.delivery.core.application.view.dto.CourierDto;
import microarch.delivery.core.application.view.dto.LocationDto;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.OrderStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CourierQueries {

    JdbcTemplate jdbcTemplate;

    public CourierQueries(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CourierDto> findAllCouriers() {

        return jdbcTemplate.query("SELECT * FROM Couriers", (rs, rowNum) -> {
            LocationDto locationDto = new LocationDto(rs.getInt("current_l_x"),rs.getInt("current_l_y"));
            return new CourierDto(
                    rs.getObject("id", UUID.class),
                    rs.getString("name"),
                    locationDto);
        });

    }
}
