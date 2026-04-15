package microarch.delivery.core.domain.model.general;

import libs.ddd.ValueObject;
import libs.errs.Guard;
import libs.errs.Result;
import libs.errs.Error;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address extends ValueObject<Address> {

    private final String country;
    private final String city;
    private final String street;
    private final String house;
    private final String apartment;

    public static Result<Address,Error> create (String country, String city, String street,
                                                String house, String apartment) {

        Error err = Guard.combine(
                Guard.againstNullOrEmpty(country,"country"),
                Guard.againstNullOrEmpty(city,"city"),
                Guard.againstNullOrEmpty(street,"street"),
                Guard.againstNullOrEmpty(house,"house"),
                Guard.againstNullOrEmpty(apartment,"apartment")
        );

        if (err != null)
            return Result.failure(err);

        return Result.success(new Address(country,city,street,house,apartment));
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(this.country,this.city,this.street,this.house,this.apartment);
    }
}
