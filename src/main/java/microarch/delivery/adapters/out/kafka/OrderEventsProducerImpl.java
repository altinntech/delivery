package microarch.delivery.adapters.out.kafka;

import lombok.RequiredArgsConstructor;
import microarch.delivery.core.domain.model.order.events.OrderAssignedDomainEvent;
import microarch.delivery.core.domain.model.order.events.OrderCompletedDomainEvent;
import microarch.delivery.core.ports.OrderEventsProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import queues.order.events.OrderEventsProto.OrderAssignedIntegrationEvent;
import queues.order.events.OrderEventsProto.OrderCompletedIntegrationEvent;


@Component
@RequiredArgsConstructor
public class OrderEventsProducerImpl implements OrderEventsProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value("${app.kafka.orders-events-topic}")
    private String topic;

    @Override
    public void publish(OrderAssignedDomainEvent domainEvent) throws Exception {

        var integrationEvent = mapToProto(domainEvent);

        kafkaTemplate.send(topic, domainEvent.getOrderId().toString(), integrationEvent.toByteArray());

    }

    @Override
    public void publish(OrderCompletedDomainEvent domainEvent) throws Exception {

        var integrationEvent = mapToProto(domainEvent);
        kafkaTemplate.send(topic, domainEvent.getOrderId().toString(), integrationEvent.toByteArray());

    }

    private OrderAssignedIntegrationEvent mapToProto (OrderAssignedDomainEvent domainEvent) {
        return OrderAssignedIntegrationEvent.newBuilder().setOrderId(domainEvent.getOrderId().toString()).build();
    }

    private OrderCompletedIntegrationEvent mapToProto (OrderCompletedDomainEvent domainEvent) {
        return OrderCompletedIntegrationEvent.newBuilder().setOrderId(domainEvent.getOrderId().toString()).build();
    }
}
