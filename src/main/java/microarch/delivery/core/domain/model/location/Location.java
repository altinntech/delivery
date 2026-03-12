package microarch.delivery.core.domain.model.location;

import libs.ddd.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Location extends ValueObject<Location> {

    private int x;
    private int y;

    public static Location create(int x, int y) throws IllegalArgumentException {
        if (x < 1 || x > 10)
            throw new IllegalArgumentException("x must be between 1 and 10");
        if (y < 1 || y > 10)
            throw new IllegalArgumentException("y must be between 1 and 10");
        return new Location(x, y);
    }

    public int distanceTo(Location otherLocation) {
        return Math.abs(this.x - otherLocation.x) + Math.abs(this.y - otherLocation.y);
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(this.x, this.y);
    }
}
