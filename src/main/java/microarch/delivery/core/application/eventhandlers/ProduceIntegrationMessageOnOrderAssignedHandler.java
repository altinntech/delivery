package microarch.delivery.core.application.eventhandlers;

import microarch.delivery.core.domain.model.order.events.OrderAssignedDomainEvent;
import microarch.delivery.core.domain.model.order.events.OrderCompletedDomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ProduceIntegrationMessageOnOrderAssignedHandler {

    @EventListener
    public void handle (OrderAssignedDomainEvent orderAssignedDomainEvent) {
        System.out.println("Domain Event: " + orderAssignedDomainEvent.toString());
    }

}
