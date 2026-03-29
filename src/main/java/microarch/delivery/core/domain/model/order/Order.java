package microarch.delivery.core.domain.model.order;

import libs.ddd.Aggregate;
import libs.errs.*;
import libs.errs.Error;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import microarch.delivery.core.domain.model.general.Location;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public final class Order extends Aggregate<UUID> {

    private final Location targetLocation;
    private final int volume;
    private OrderStatus status;
    private UUID courierId;

    private Order(UUID basketId, Location targetLocation, int volume) {
        super(basketId);
        this.targetLocation = targetLocation;
        this.volume = volume;
        this.status = OrderStatus.CREATED;
    }

    public static Result<Order,Error> create (UUID basketId, Location location, int volume) {

        Error e = null;
        if ((e = Guard.againstNullOrEmpty(basketId,"orderId"))!=null) return Result.failure(e);
        if (location == null) return Result.failure(GeneralErrors.valueIsRequired("location"));
        if (volume <= 0) return Result.failure(GeneralErrors.valueMustBeGreaterOrEqual("orderVolume",volume,0));

        return Result.success(new Order(basketId,location,volume));
    }

    public UnitResult<Error> assignCourier (UUID courierId) {
        Error e = null;
        if ( (e = Guard.againstNullOrEmpty(courierId,"courierId")) != null)
            return UnitResult.failure(GeneralErrors.valueIsRequired("courierId"));

        this.courierId = courierId;
        this.status = OrderStatus.ASSIGNED;

        return UnitResult.success();
    }

    public UnitResult<Error> finishOrder () {
        if ((status == null)||(status!=OrderStatus.ASSIGNED))
            return UnitResult.failure(Errors.orderNotInAssignedState());

        this.status = OrderStatus.COMPLETED;

        return UnitResult.success();
    }

    public static class Errors {
        public static Error orderNotInAssignedState() {
            return Error.of("order.not.in.assigned.state",
                    "Can't finish order. Order state not ASSIGNED");
        }
    }

}
