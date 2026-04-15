package microarch.delivery.core.application.view.get_all_couriers;

import libs.errs.Error;
import libs.errs.Result;

public interface GetAllCouriers {

    Result<GetAllCouriersResponse, Error> handle (GetAllCouriersQuery query);

}
