package microarch.delivery.core.application.usecases.courier_move_order;

import libs.errs.Error;
import libs.errs.GeneralErrors;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.CourierRepository;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourierMoveUseCase implements CourierMoveOrder{

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;

    public CourierMoveUseCase(OrderRepository orderRepository, CourierRepository courierRepository) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
    }

    @Transactional
    @Override
    public UnitResult<Error> handle(CourierMoveCommand command) {

        List<Order> assignedOrders = orderRepository.findAllAssigned();
        if (assignedOrders == null || assignedOrders.isEmpty())
            return UnitResult.failure(Errors.noAssignedOrders());

        for (Order o : assignedOrders) {
            Optional<Courier> courierResult = courierRepository.findById(o.getCourierId());
            if (!courierResult.isEmpty()) {
                Courier courier = courierResult.get();
                courier.moveOneStepToLocation(o.getTargetLocation());
                if (courier.getCurrentLocation().equals(o.getTargetLocation())) {
                    if (!o.finishOrder().isSuccess())
                        return UnitResult.failure(Errors.canNotFinishOrder(o.getId()));
                    if (!courier.finishOrder(o.getId()).isSuccess())
                        return UnitResult.failure(Errors.courierCouldNotExtractOrder(courier.getId(),o.getId()));
                }
                if (!courierRepository.updateCourier(courier)) UnitResult.failure(Errors.courierPersistenceError(courier.getId()));
                if (!orderRepository.updateOrder(o)) UnitResult.failure(Errors.orderPersistenceError(o.getId()));
            }
        }

        return UnitResult.success();
    }

    public static class Errors {
        public static Error noAssignedOrders() {
            return Error.of("assigned.orders.not.found", "There is no assigned orders");
        }
        public static Error canNotFinishOrder(UUID uuid) {
            return Error.of("can.not.finish.order", "Can not finish the order: " + uuid.toString() );
        }
        public static Error courierCouldNotExtractOrder(UUID courierId, UUID orderId) {
            return Error.of("courier.could.not.extract.order", "Courier: " + courierId.toString() +
                    "could not extract the order: " + orderId);
        }
        public static Error courierPersistenceError(UUID courierId) {
            return Error.of("courier.could.not.update", "Courier: " + courierId.toString());
        }
        public static Error orderPersistenceError(UUID orderId) {
            return Error.of("order.could.not.update", "Order: " + orderId.toString());
        }
    }


}
