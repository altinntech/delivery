package microarch.delivery.core.ports;

import microarch.delivery.core.domain.model.order.events.OrderAssignedDomainEvent;
import microarch.delivery.core.domain.model.order.events.OrderCompletedDomainEvent;

public interface OrderEventsProducer {

    void publish(OrderAssignedDomainEvent domainEvent) throws Exception;

    void publish(OrderCompletedDomainEvent domainEvent) throws Exception;

}
