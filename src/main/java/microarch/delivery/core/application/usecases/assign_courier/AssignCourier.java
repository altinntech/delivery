package microarch.delivery.core.application.usecases.assign_courier;

import libs.errs.Error;
import libs.errs.Result;

import java.util.UUID;

public interface AssignCourier {

    Result<UUID, Error> handle (AssignCourierCommand assignCourierCommand);

}
