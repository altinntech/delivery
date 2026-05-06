package microarch.delivery.core.application.eventhandlers;

import microarch.delivery.core.domain.model.order.events.OrderCompletedDomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ProduceIntegrationMessageOnOrderCompletedHandler {

    @EventListener
    public void handle (OrderCompletedDomainEvent orderCompletedDomainEvent) {
        System.out.println("Domain Event: " + orderCompletedDomainEvent.toString());
    }

}
