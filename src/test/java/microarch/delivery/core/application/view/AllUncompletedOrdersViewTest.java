package microarch.delivery.core.application.view;

import microarch.delivery.adapters.out.postgres.PostgresIntegrationTestBase;
import microarch.delivery.core.application.view.get_uncompleted_orders.GetUncompletedOrdersQuery;
import microarch.delivery.core.application.view.get_uncompleted_orders.UncompletedOrdersView;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AllUncompletedOrdersViewTest extends PostgresIntegrationTestBase {

    @Autowired
    UncompletedOrdersView uncompletedOrdersView;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("Testing all uncompleted orders view")
    void testSuccessfullAllUncompletedOrdersView () {

        Order order1 = Order.create(UUID.randomUUID(),Location.random().getValue(),7).getValue();
        Order order2 = Order.create(UUID.randomUUID(),Location.random().getValue(),2).getValue();
        Order order3 = Order.create(UUID.randomUUID(),Location.random().getValue(),8).getValue();
        Order order4 = Order.create(UUID.randomUUID(),Location.random().getValue(),5).getValue();

        order1.assignCourier(UUID.randomUUID());
        order4.assignCourier(UUID.randomUUID());
        order4.finishOrder();

        orderRepository.addOrder(order1);
        orderRepository.addOrder(order2);
        orderRepository.addOrder(order3);
        orderRepository.addOrder(order4);

        GetUncompletedOrdersQuery getUncompletedOrdersQuery = new GetUncompletedOrdersQuery();
        var allUncompletedOrdersViewResult = uncompletedOrdersView.handle(getUncompletedOrdersQuery);

        assertThat(allUncompletedOrdersViewResult.isSuccess()).isTrue();
        assertThat(allUncompletedOrdersViewResult.getValue().orderDtoList().size()).isEqualTo(3);

    }

}
