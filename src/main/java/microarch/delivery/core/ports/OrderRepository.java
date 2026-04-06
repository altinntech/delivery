package microarch.delivery.core.ports;

import microarch.delivery.core.domain.model.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    boolean addOrder(Order newOrder);

    boolean updateOrder(Order order);

    Optional<Order> findById(UUID orderId);

    Optional<Order> findAnyOneCreated();

    List<Order> findAllAssigned();

}
