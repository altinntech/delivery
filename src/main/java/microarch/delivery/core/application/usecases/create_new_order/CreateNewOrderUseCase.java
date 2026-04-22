package microarch.delivery.core.application.usecases.create_new_order;

import libs.errs.Error;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.GeoClient;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateNewOrderUseCase implements CreateNewOrder{

    private final OrderRepository orderRepository;
    private final GeoClient geoClient;

    public CreateNewOrderUseCase(OrderRepository orderRepository,GeoClient geoClient) {
        this.orderRepository = orderRepository;
        this.geoClient = geoClient;
    }

    @Override
    public UnitResult<Error> handle(CreateNewOrderCommand command) {

        var orderLocation = geoClient.getLocation(command.getAddress());
        if (orderLocation.isFailure()) return UnitResult.failure(orderLocation.getError());

        var newOrder = Order.create(command.getOrderId(),orderLocation.getValue(),command.getVolume());
        if (newOrder.isFailure()) return UnitResult.failure(newOrder.getError());

        if (!orderRepository.addOrder(newOrder.getValue())) return UnitResult.failure(Errors.orderSavingFailed());

        return UnitResult.success();
    }

    public static class Errors {
        public static Error orderSavingFailed() {
            return Error.of("order.saving.failed", "Can't save new order");
        }
    }
}
