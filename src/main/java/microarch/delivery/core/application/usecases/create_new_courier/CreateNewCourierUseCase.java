package microarch.delivery.core.application.usecases.create_new_courier;

import libs.errs.Error;
import libs.errs.Result;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.ports.CourierRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateNewCourierUseCase implements CreateNewCourier {

    private final CourierRepository courierRepository;

    public CreateNewCourierUseCase(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }
    
    @Override
    public Result<UUID, Error> handle(CreateNewCourierCommand command) {

        var courierLocation = Location.random();
        if (courierLocation.isFailure()) return Result.failure(courierLocation.getError());

        var newCourierResult = Courier.create (command.getName(),command.getSpeed(),courierLocation.getValue());
        if (newCourierResult.isFailure()) return Result.failure(newCourierResult.getError());

        Courier courier = newCourierResult.getValue();

        if (!courierRepository.addCourier(courier)) return Result.failure(Errors.courierSavingFailed());
        
        return Result.success(newCourierResult.getValue().getId());
    }

    public static class Errors {
        public static Error courierSavingFailed() {
            return Error.of("courier.saving.failed", "Can't save new courier");
        }
    }
}
