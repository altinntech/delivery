package microarch.delivery.core.application.usecases.create_new_order;


import libs.errs.Guard;
import libs.errs.Result;
import libs.errs.Error;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateNewOrderCommand {

    private final UUID orderId;
    private final String country;
    private final String city;
    private final String street;
    private final String house;
    private final String apartment;
    private final int volume;

    public static Result<CreateNewOrderCommand, Error> create(UUID basketId, String country, String city, String street,
                                                              String house, String apartment,int volume) {

        var err = Guard.againstNullOrEmpty(basketId, "basketId");
        if (err != null)
            return Result.failure(err);

        err = Guard.combine(
                Guard.againstNullOrEmpty(country,"country"),
                Guard.againstNullOrEmpty(city,"city"),
                Guard.againstNullOrEmpty(street,"street"),
                Guard.againstNullOrEmpty(house,"house"),
                Guard.againstNullOrEmpty(apartment,"apartment"),
                Guard.againstLessThan(volume,1,"volume")
        );

        if (err != null)
            return Result.failure(err);

        return Result.success(new CreateNewOrderCommand(basketId, country,city,street,house,apartment,volume));
    }


}
