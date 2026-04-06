package microarch.delivery.core.domain.services;

import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.order.Order;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderDispatchingServiceTest {

    @Test
    public void shouldDispatchOrder() {

        Order newOrder = Order.create(UUID.randomUUID(), Location.create(10, 10).getValue(), 5).getValue();
        Courier courier1 = Courier.create("Bob", 2, Location.create(1, 1).getValue()).getValue();
        Courier courier2 = Courier.create("Tom", 1, Location.create(1, 1).getValue()).getValue();
        Courier courier3 = Courier.create("Bill", 3, Location.create(1, 1).getValue()).getValue();

        ArrayList<Courier> allCouriers = new ArrayList<>();
        allCouriers.add(courier1);
        allCouriers.add(courier2);
        allCouriers.add(courier3);

        OrderDispatchingService orderDispatchingService = new FastestCourierService();

        Courier winner = orderDispatchingService.dispatchOrder(newOrder, allCouriers).getValue();

        assertThat(winner).isEqualTo(courier3);
        assertThat(newOrder.getCourierId()).isEqualTo(winner.getId());

    }

    @Test
    public void shouldNotDispatchOrderSizeTooBig() {

        Order newOrder = Order.create(UUID.randomUUID(), Location.create(10, 10).getValue(), 15).getValue();
        Courier courier1 = Courier.create("Bob", 2, Location.create(1, 1).getValue()).getValue();
        Courier courier2 = Courier.create("Tom", 1, Location.create(1, 1).getValue()).getValue();
        Courier courier3 = Courier.create("Bill", 3, Location.create(1, 1).getValue()).getValue();

        ArrayList<Courier> allCouriers = new ArrayList<>();
        allCouriers.add(courier1);
        allCouriers.add(courier2);
        allCouriers.add(courier3);

        OrderDispatchingService orderDispatchingService = new FastestCourierService();

        var winner = orderDispatchingService.dispatchOrder(newOrder, allCouriers);

        assertThat(winner.isSuccess()).isFalse();

    }

    @Test
    public void shouldNotDispatchOrderNoFreeCouriers() {

        Order firstOrder = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 5).getValue();
        Order newOrder = Order.create(UUID.randomUUID(), Location.create(10, 10).getValue(), 5).getValue();
        Courier courier1 = Courier.create("Bob", 2, Location.create(1, 1).getValue()).getValue();
        Courier courier2 = Courier.create("Tom", 1, Location.create(1, 1).getValue()).getValue();
        Courier courier3 = Courier.create("Bill", 3, Location.create(1, 1).getValue()).getValue();

        courier1.takeNewOrder(firstOrder.getId(), firstOrder.getVolume());
        courier2.takeNewOrder(firstOrder.getId(), firstOrder.getVolume());
        courier3.takeNewOrder(firstOrder.getId(), firstOrder.getVolume());

        ArrayList<Courier> allCouriers = new ArrayList<>();
        allCouriers.add(courier1);
        allCouriers.add(courier2);
        allCouriers.add(courier3);

        OrderDispatchingService orderDispatchingService = new FastestCourierService();

        var winner = orderDispatchingService.dispatchOrder(newOrder, allCouriers);

        assertThat(winner.isSuccess()).isFalse();

    }

}
