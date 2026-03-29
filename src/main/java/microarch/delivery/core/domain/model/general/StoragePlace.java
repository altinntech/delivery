package microarch.delivery.core.domain.model.general;

import libs.ddd.BaseEntity;
import libs.errs.*;
import libs.errs.Error;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class StoragePlace extends BaseEntity<UUID> {

    private final String Name;
    private final int TotalVolume;
    private UUID OrderId;

    private StoragePlace(String name, int totalVolume) {
        super(UUID.randomUUID());
        this.Name = name;
        this.TotalVolume = totalVolume;
        this.OrderId = null;
    }

    public static Result<StoragePlace, Error> create(String name,int totalVolume ) {

        Error e = null;

        if ((e = Guard.againstLessOrEqual(totalVolume,0,"TotalVolume"))!=null) return Result.failure(e);
        if ((e = Guard.againstNullOrEmpty(name,"Name"))!=null) return Result.failure(e);

        return Result.success(new StoragePlace(name,totalVolume));
    }

    public boolean isAvailableForOrder (int orderVolume) {

        return (OrderId == null) && (orderVolume <= TotalVolume) && (orderVolume > 0);

    }

    public UnitResult<Error> placeNewOrder (UUID orderId,int orderVolume) {

        // Input params validation
        Error e = null;
        if ((e = Guard.againstNullOrEmpty(orderId,"orderId"))!=null) return UnitResult.failure(e);
        if (orderVolume <=0) return UnitResult.failure(GeneralErrors.valueMustBeGreaterOrEqual("orderVolume",orderVolume,0));

        // Domain requirements validation
        if (!isAvailableForOrder(orderVolume))
            return UnitResult.failure(Error.of("storage.place.is.too.small.for.this.order",
                "The storage space is too small for this order"));

        this.OrderId = orderId;

        return UnitResult.success();

    }

    public UnitResult<Error> extractOrder () {

        var e = Guard.againstNullOrEmpty(OrderId,"OrderId");
        if (e!=null) return UnitResult.failure(Error.of("storage.place.is.empty",
                "Can't extract order. The storage place is empty"));

        this.OrderId = null;

        return UnitResult.success();

    }

}
