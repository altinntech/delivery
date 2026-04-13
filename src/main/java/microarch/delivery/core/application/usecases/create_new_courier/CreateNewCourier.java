package microarch.delivery.core.application.usecases.create_new_courier;

import libs.errs.Error;
import libs.errs.Result;
import libs.errs.UnitResult;
import microarch.delivery.core.application.usecases.create_new_order.CreateNewOrderCommand;

import java.util.UUID;

public interface CreateNewCourier {

    Result<UUID, Error> handle(CreateNewCourierCommand command);

}
