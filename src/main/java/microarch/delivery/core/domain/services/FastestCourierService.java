package microarch.delivery.core.domain.services;

import libs.errs.Error;
import libs.errs.Result;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.domain.model.order.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FastestCourierService implements OrderDispatchingService{

    @Override
    public Result<Courier, Error> dispatchOrder(Order order, List<Courier> allCouriers) {

        HashMap<Courier,Double> courierRating = new HashMap<>();

        if ((order == null)||(order.getStatus()!=OrderStatus.CREATED) ||
            (allCouriers==null)||(allCouriers.isEmpty()))
                return Result.failure(Errors.dispatchingInputValidationFailed());

        for (Courier courier : allCouriers) {
            if (courier.isAvailableForNewOrder(order.getVolume())!=null) {
                courierRating.put(courier,courier.countStepsToLocation(order.getTargetLocation()));
            }
        }

        Courier freeCourier = null;

        if (!courierRating.isEmpty()) {
            freeCourier = Collections.min(courierRating.entrySet(), Map.Entry.comparingByValue()).getKey();
        }

        if (freeCourier != null) {
            var result = freeCourier.takeNewOrder(order.getId(),order.getVolume());
            if (result.isFailure()) return Result.failure(result.getError());
            order.assignCourier(freeCourier.getId());
            return Result.success(freeCourier);
        }

        return Result.failure(Errors.noAvailableCouriers());

    }


    public static class Errors {
        public static Error dispatchingInputValidationFailed() {
            return Error.of("order.dispatching.service.validation.failed",
                    "Ошибка валидации входящих параметров сервиса диспатчеризации ");

        }
        public static Error noAvailableCouriers() {
            return Error.of("order.dispatching.service.no.available.couriers",
                    "В данный момент нет свободных курьеров для доставки");

        }
    }

}
