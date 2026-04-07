package microarch.delivery.core.domain.model.courier;

import libs.ddd.Aggregate;
import libs.errs.*;
import libs.errs.Error;
import lombok.Getter;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.general.StoragePlace;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Courier extends Aggregate<UUID> {

    private final String name;
    private final int speed;
    private Location currentLocation;
    private final List<StoragePlace> storagePlaces;

    private Courier(String name, int speed, Location currentLocation, List<StoragePlace> storagePlaces) {
        super(UUID.randomUUID());
        this.name = name;
        this.speed = speed;
        this.currentLocation = currentLocation;
        this.storagePlaces = storagePlaces;
    }

    public static Result<Courier, Error> create(String name, int speed, Location location) {

        Error e = null;
        if ((e = Guard.againstNullOrEmpty(name, "name")) != null)
            return Result.failure(e);
        if (speed < 1)
            return Result.failure(GeneralErrors.valueMustBeGreaterOrEqual("speed", speed, 1));
        if (location == null)
            return Result.failure(GeneralErrors.valueIsRequired("location"));

        ArrayList<StoragePlace> storagePlaces = new ArrayList<>();
        storagePlaces.add(StoragePlace.create("Bag", 10).getValue());

        return Result.success(new Courier(name, speed, location,storagePlaces));
    }

    public static Result<Courier, Error> createFromDB(UUID id, String name, int speed, int x, int y, List<StoragePlace> places) {

        Error e = null;
        if ((e = Guard.againstNullOrEmpty(id, "courierId")) != null)
            return Result.failure(e);

        if ((e = Guard.againstNullOrEmpty(name, "name")) != null)
            return Result.failure(e);

        if (speed < 1)
            return Result.failure(GeneralErrors.valueMustBeGreaterOrEqual("speed", speed, 1));

        var locationResult = Location.create(x, y);
        if (locationResult.isFailure())
            return Result.failure(locationResult.getError());

        Courier courier = new Courier(name,speed,locationResult.getValue(),places);
        courier.id = id;

        return Result.success(courier);
    }

    public UnitResult<Error> addStoragePlace(String name, int volume) {

        var newStoragePlace = StoragePlace.create(name, volume);
        if (newStoragePlace.isFailure())
            return UnitResult.failure(newStoragePlace.getError());

        storagePlaces.add(newStoragePlace.getValue());

        return UnitResult.success();
    }

    public StoragePlace isAvailableForNewOrder(int orderVolume) {

        for (StoragePlace place : storagePlaces) {
            if (place.isAvailableForOrder(orderVolume))
                return place;
        }

        return null;
    }

    public UnitResult<Error> takeNewOrder(UUID orderId, int orderVolume) {

        StoragePlace placeForNewOrder = isAvailableForNewOrder(orderVolume);
        if (placeForNewOrder == null)
            return UnitResult.failure(Errors.courierHasNoPlaceForTakingNewOrder());

        return placeForNewOrder.placeNewOrder(orderId, orderVolume);

    }

    public UnitResult<Error> finishOrder(UUID orderId) {
        Error e = null;
        if ((e = Guard.againstNullOrEmpty(orderId, "orderId")) != null)
            return UnitResult.failure(e);

        for (StoragePlace place : storagePlaces) {
            if (!place.isEmpty() && place.getOrderId().equals(orderId)) {
                return place.extractOrder();
            }
        }

        return UnitResult.failure(Errors.courierHasNoSuchOrder(orderId));
    }

    public double countStepsToLocation(Location targetLocation) {
        if (targetLocation != null) {
            int distance = currentLocation.distanceTo(targetLocation);
            return (double) distance / speed;
        }
        return 0;
    }

    public void moveOneStepToLocation(Location target) {

        Objects.requireNonNull(target, "target");

        int difX = target.getX() - currentLocation.getX();
        int difY = target.getY() - currentLocation.getY();
        int cruisingRange = speed;

        int moveX = Math.max(-cruisingRange, Math.min(cruisingRange, difX));
        cruisingRange -= Math.abs(moveX);
        int moveY = Math.max(-cruisingRange, Math.min(cruisingRange, difY));

        this.currentLocation = Location.create(currentLocation.getX() + moveX, currentLocation.getY() + moveY)
                .getValueOrThrow();
    }

    public static class Errors {
        public static Error courierHasNoPlaceForTakingNewOrder() {
            return Error.of("courier.has.no.place.for.new.order", "Can't take the new order. There is no place for it");
        }

        public static Error courierHasNoSuchOrder(UUID orderId) {
            return Error.of("courier.has.no.such.order",
                    "Can't finish the order. There is no such order at courier: %s" + orderId.toString());
        }

    }

}
