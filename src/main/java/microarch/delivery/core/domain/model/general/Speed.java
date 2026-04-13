package microarch.delivery.core.domain.model.general;

import libs.ddd.ValueObject;
import libs.errs.Error;
import libs.errs.GeneralErrors;
import libs.errs.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Speed extends ValueObject<Speed> {

    private static final int MIN_SPEED = 1;
    private final int value;

    public static Result<Speed, Error> create(int speedValue) {

        if (speedValue < MIN_SPEED)
            return Result.failure(GeneralErrors.valueMustBeGreaterOrEqual("speed", speedValue, MIN_SPEED));

        var speed = new Speed(speedValue);
        return Result.success(speed);

    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(value);
    }
}
