package microarch.delivery.core.application.eventhandlers;

import microarch.delivery.core.domain.model.order.events.OrderAssignedDomainEvent;
import microarch.delivery.core.ports.OrderEventsProducer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ProduceIntegrationMessageOnOrderAssignedHandler {

    private final OrderEventsProducer orderEventsProducer;

    public ProduceIntegrationMessageOnOrderAssignedHandler(OrderEventsProducer orderEventsProducer) {
        this.orderEventsProducer = orderEventsProducer;
    }

    @EventListener
    public void handle (OrderAssignedDomainEvent orderAssignedDomainEvent) throws Exception {
        System.out.println("Domain Event: " + orderAssignedDomainEvent.toString());
        orderEventsProducer.publish(orderAssignedDomainEvent);
    }

}
