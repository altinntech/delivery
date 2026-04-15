package microarch.delivery.core.application.view.get_uncompleted_orders;

import libs.errs.Error;
import libs.errs.Result;

public interface GetUncompletedOrders {

    Result<GetUncompletedOrdersResponse, Error> handle (GetUncompletedOrdersQuery query);

}
