package microarch.delivery.core.application.usecases.courier_move_order;

import libs.errs.Error;
import libs.errs.UnitResult;

public interface CourierMoveOrder {

    UnitResult<Error> handle(CourierMoveCommand command);

}
