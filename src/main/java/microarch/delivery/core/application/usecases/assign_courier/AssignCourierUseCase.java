package microarch.delivery.core.application.usecases.assign_courier;

import libs.errs.Error;
import libs.errs.Result;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.services.OrderDispatchingService;
import microarch.delivery.core.ports.CourierRepository;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AssignCourierUseCase implements AssignCourier{

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final OrderDispatchingService orderDispatchingService;

    public AssignCourierUseCase(OrderRepository orderRepository,
                                CourierRepository courierRepository,
                                OrderDispatchingService orderDispatchingService) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.orderDispatchingService = orderDispatchingService;
    }

    @Transactional
    @Override
    public Result<UUID, Error> handle(AssignCourierCommand assignCourierCommand) {

        var newOrder = orderRepository.findAnyOneCreated();
        if (newOrder.isEmpty()) return Result.failure(Errors.noOrdersForDispatching());

        List<Courier> couriers = courierRepository.findAllFreeCouriers();
        if (couriers.isEmpty()) return Result.failure(Errors.noFreeCouriersNow());

        Result<Courier,Error> courierResult = orderDispatchingService.dispatchOrder(newOrder.get(), couriers);
        if (courierResult.isFailure()) return Result.failure(courierResult.getError());

        if (!courierRepository.updateCourier(courierResult.getValue()) ||
                !orderRepository.updateOrder(newOrder.get()))
            return Result.failure(Errors.updateCouriersOrdersFailed());

        return Result.success(courierResult.getValue().getId());
    }

    public static class Errors {
        public static Error noOrdersForDispatching() {
            return Error.of("created.orders.not.found", "There is no new created orders");
        }
        public static Error noFreeCouriersNow() {
            return Error.of("couriers.not.found", "There is no free couriers right now");
        }
        public static Error updateCouriersOrdersFailed() {
            return Error.of("persistence.couriers.orders.failed", "Could not update Courier or Order");
        }
    }
}
