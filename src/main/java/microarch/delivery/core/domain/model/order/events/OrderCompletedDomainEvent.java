package microarch.delivery.core.domain.model.order.events;

import libs.ddd.DomainEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import microarch.delivery.core.domain.model.order.Order;

import java.util.UUID;

@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@Getter
public class OrderCompletedDomainEvent extends DomainEvent {

    private final UUID orderId;

    public OrderCompletedDomainEvent(Order source) {
        super(source);
        this.orderId = source.getId();
    }
}
