package microarch.delivery.core.application.eventhandlers;

import microarch.delivery.core.domain.model.order.events.OrderCompletedDomainEvent;
import microarch.delivery.core.ports.OrderEventsProducer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ProduceIntegrationMessageOnOrderCompletedHandler {

    private final OrderEventsProducer orderEventsProducer;

    public ProduceIntegrationMessageOnOrderCompletedHandler(OrderEventsProducer orderEventsProducer) {
        this.orderEventsProducer = orderEventsProducer;
    }

    @EventListener
    public void handle (OrderCompletedDomainEvent orderCompletedDomainEvent) throws Exception {
        System.out.println("Domain Event: " + orderCompletedDomainEvent.toString());
        orderEventsProducer.publish(orderCompletedDomainEvent);
    }

}
