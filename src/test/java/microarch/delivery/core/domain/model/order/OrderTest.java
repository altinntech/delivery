package microarch.delivery.core.domain.model.order;

import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.general.StoragePlace;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.UUID;

import static microarch.delivery.core.domain.model.order.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    @DisplayName("Should create new order with valid params")
    public void shouldCreateNewValidOrder() {

        UUID basketId = UUID.randomUUID();
        Location location = Location.create(5, 5).getValue();
        var order = Order.create(basketId, location, 5);

        assertThat(order.isSuccess()).isTrue();
        assertThat(order.getValue().getTargetLocation()).isEqualTo(location);
        assertThat(order.getValue().getId()).isEqualTo(basketId);
        assertThat(order.getValue().getStatus()).isEqualTo(CREATED);
        assertThat(order.getValue().getVolume()).isEqualTo(5);

    }

    @ParameterizedTest
    @DisplayName("Should not create order with invalid params ")
    @CsvSource({ "550e8400-e29b-41d4-a716-446655440000,5,-1,10", ",5,5,5", ",0,0,5",
            "550e8400-e29b-41d4-a716-446655440000,0,0,5", "550e8400-e29b-41d4-a716-446655440000,5,5,0", ",5,5,0",
            ",0,0,-5" })
    public void shouldNotCreateOrderWithInvalidParams(String uuid, int x, int y, int volume) {

        Location targetLocation = null;
        UUID basketId = null;

        if (uuid != null)
            basketId = UUID.fromString(uuid);

        var location = Location.create(x, y);

        if (location.isSuccess())
            targetLocation = location.getValue();

        var order = Order.create(basketId, targetLocation, volume);

        assertThat(order.isSuccess()).isFalse();

    }

    @Test
    @DisplayName("Should assign courier to order")
    public void shouldAssignCourierToOrder() {

        UUID basketId = UUID.randomUUID();
        UUID courierId = UUID.randomUUID();
        Location location = Location.create(5, 5).getValue();
        var order = Order.create(basketId, location, 5);

        order.getValue().assignCourier(courierId);

        assertThat(order.isSuccess()).isTrue();
        assertThat(order.getValue().getTargetLocation()).isEqualTo(location);
        assertThat(order.getValue().getId()).isEqualTo(basketId);
        assertThat(order.getValue().getStatus()).isEqualTo(ASSIGNED);
        assertThat(order.getValue().getVolume()).isEqualTo(5);
        assertThat(order.getValue().getCourierId()).isEqualTo(courierId);

    }

    @Test
    @DisplayName("Should successfully finish the order")
    public void shouldFinishOrder() {

        UUID basketId = UUID.randomUUID();
        UUID courierId = UUID.randomUUID();
        Location location = Location.create(5, 5).getValue();
        var order = Order.create(basketId, location, 5);

        order.getValue().assignCourier(courierId);

        order.getValue().finishOrder();

        assertThat(order.isSuccess()).isTrue();
        assertThat(order.getValue().getTargetLocation()).isEqualTo(location);
        assertThat(order.getValue().getId()).isEqualTo(basketId);
        assertThat(order.getValue().getStatus()).isEqualTo(COMPLETED);
        assertThat(order.getValue().getVolume()).isEqualTo(5);
        assertThat(order.getValue().getCourierId()).isEqualTo(courierId);

    }

    @Test
    @DisplayName("Should not finish not assigned order")
    public void shouldNotFinishNotAssignedOrder() {

        UUID basketId = UUID.randomUUID();
        Location location = Location.create(5, 5).getValue();
        var order = Order.create(basketId, location, 5);

        var result = order.getValue().finishOrder();

        assertThat(order.isSuccess()).isTrue();
        assertThat(order.getValue().getTargetLocation()).isEqualTo(location);
        assertThat(order.getValue().getId()).isEqualTo(basketId);
        assertThat(order.getValue().getStatus()).isEqualTo(CREATED);
        assertThat(order.getValue().getVolume()).isEqualTo(5);

        assertThat(result.isSuccess()).isFalse();

    }

}
