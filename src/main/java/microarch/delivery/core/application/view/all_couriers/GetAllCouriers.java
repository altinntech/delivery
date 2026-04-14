package microarch.delivery.core.application.view.all_couriers;

import libs.errs.Error;
import libs.errs.Result;

public interface GetAllCouriers {

    Result<GetAllCouriersResponse, Error> handle (GetAllCouriersQuery query);

}
