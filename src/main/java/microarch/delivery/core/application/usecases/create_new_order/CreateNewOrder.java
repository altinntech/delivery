package microarch.delivery.core.application.usecases.create_new_order;

import libs.errs.UnitResult;
import libs.errs.Error;

public interface CreateNewOrder {

    UnitResult<Error> handle(CreateNewOrderCommand command);

}
