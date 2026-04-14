package microarch.delivery.core.application.view.uncompleted_orders;

import libs.errs.Error;
import libs.errs.Result;

public interface GetUncompletedOrders {

    Result<GetUncompletedOrdersResponse, Error> handle (GetUncompletedOrdersQuery query);

}
