package microarch.delivery.core.ports;

import microarch.delivery.core.domain.model.courier.Courier;

import java.util.Optional;
import java.util.UUID;

public interface CourierRepository {

    boolean addCourier(Courier newCourier);

    Optional<Courier> findById (UUID courierId);
}
