package microarch.delivery.core.domain.model.general;

import libs.ddd.ValueObject;
import libs.errs.GeneralErrors;
import libs.errs.Result;
import libs.errs.Error;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Location extends ValueObject<Location> {

    private int x;
    private int y;

    public final static int MIN_X = 1;
    public final static int MAX_X = 10;
    public final static int MIN_Y = 1;
    public final static int MAX_Y = 10;

    public static Result<Location, Error> create(int x, int y) {

        if (x < MIN_X || x > MAX_X)
            return Result.failure(GeneralErrors.valueIsOutOfRange("X", x, MIN_X, MAX_X));
        if (y < MIN_Y || y > MAX_X)
            return Result.failure(GeneralErrors.valueIsOutOfRange("Y", y, MIN_Y, MAX_Y));

        var location = new Location(x, y);
        return Result.success(location);

    }

    public int distanceTo(Location otherLocation) {
        return Math.abs(this.x - otherLocation.x) + Math.abs(this.y - otherLocation.y);
    }

    public static Result<Location,Error> random () {

        Random random = new Random();
        int x = random.nextInt(MAX_X - MIN_X + 1) + MIN_X;
        int y = random.nextInt(MAX_Y - MIN_Y + 1) + MIN_Y;

        return create(x,y);
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(this.x, this.y);
    }
}
