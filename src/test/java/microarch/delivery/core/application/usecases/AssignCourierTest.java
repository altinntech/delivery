package microarch.delivery.core.application.usecases;

import microarch.delivery.adapters.out.postgres.PostgresIntegrationTestBase;
import microarch.delivery.core.application.usecases.assign_courier.AssignCourier;
import microarch.delivery.core.application.usecases.assign_courier.AssignCourierCommand;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.general.Speed;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.domain.services.OrderDispatchingService;
import microarch.delivery.core.ports.CourierRepository;
import microarch.delivery.core.ports.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AssignCourierTest extends PostgresIntegrationTestBase {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CourierRepository courierRepository;

    @Autowired
    AssignCourier assignCourierUseCase;

    @Test
    @DisplayName("Testing Assign Order use case with postgres repository")
    void testSuccessfullDispatchingOrder () {

        Order order1 = Order.create(UUID.randomUUID(), Location.random().getValue(),5).getValue();
        Order order2 = Order.create(UUID.randomUUID(), Location.random().getValue(),2).getValue();
        Order order3 = Order.create(UUID.randomUUID(), Location.random().getValue(),7).getValue();

        orderRepository.addOrder(order1);
        orderRepository.addOrder(order2);
        orderRepository.addOrder(order3);

        Courier courier1 = Courier.create("First", Speed.create(1).getValue(),Location.random().getValue()).getValue();
        Courier courier2 = Courier.create("Second", Speed.create(1).getValue(),Location.random().getValue()).getValue();
        Courier courier3 = Courier.create("Third", Speed.create(1).getValue(),Location.random().getValue()).getValue();
        Courier courier4 = Courier.create("Fourth", Speed.create(1).getValue(),Location.random().getValue()).getValue();

        courierRepository.addCourier(courier1);
        courierRepository.addCourier(courier2);
        courierRepository.addCourier(courier3);
        courierRepository.addCourier(courier4);

        AssignCourierCommand assignCourierCommand = new AssignCourierCommand();
        var useCaseResult = assignCourierUseCase.handle(assignCourierCommand);

        Optional<Courier> assignedCourier = courierRepository.findById(useCaseResult.getValue());

        Optional<Order> assignedOrder = orderRepository.findById(assignedCourier.get().getStoragePlaces().getFirst().getOrderId());

        assertThat(useCaseResult.isSuccess()).isTrue();
        assertThat(assignedCourier.isPresent()).isTrue();
        assertThat(assignedOrder.isPresent()).isTrue();

        assertThat(assignedCourier.get().getStoragePlaces().getFirst().getOrderId()).isEqualTo(assignedOrder.get().getId());
        assertThat(assignedOrder.get().getCourierId()).isEqualTo(assignedCourier.get().getId());

    }

}
