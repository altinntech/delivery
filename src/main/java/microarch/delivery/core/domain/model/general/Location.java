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

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Location extends ValueObject<Location> {

    private int x;
    private int y;

    public static Result<Location, Error> create (int x,int y) {

        if (x < 1 || x > 10) return Result.failure(GeneralErrors.valueIsOutOfRange("X", x,1,10));
        if (y < 1 || y > 10) return Result.failure(GeneralErrors.valueIsOutOfRange("Y", y,1,10));

        var location = new Location(x,y);
        return Result.success(location);

    }

    public int distanceTo(Location otherLocation) {
        return Math.abs(this.x - otherLocation.x) + Math.abs(this.y - otherLocation.y);
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(this.x, this.y);
    }
}
