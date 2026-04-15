package microarch.delivery.core.application.usecases.create_new_order;


import libs.errs.Guard;
import libs.errs.Result;
import libs.errs.Error;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.domain.model.general.Address;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateNewOrderCommand {

    private final UUID orderId;
    private final Address address;
    private final int volume;

    public static Result<CreateNewOrderCommand, Error> create(UUID basketId, Address address ,int volume) {

        var err = Guard.combine(
                Guard.againstNullOrEmpty(basketId, "basketId"),
                Guard.againstNullValueObject(address,"address"),
                Guard.againstLessThan(volume,1,"volume")
        );
        if (err != null)
            return Result.failure(err);

        return Result.success(new CreateNewOrderCommand(basketId, address, volume));
    }


}
