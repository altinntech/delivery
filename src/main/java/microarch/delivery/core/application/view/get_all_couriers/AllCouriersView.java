package microarch.delivery.core.application.view.get_all_couriers;

import libs.errs.Error;
import libs.errs.Result;
import microarch.delivery.adapters.out.postgres.CourierQueries;
import microarch.delivery.core.application.view.dto.CourierDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllCouriersView implements GetAllCouriers{

    @Autowired
    CourierQueries courierQueries;

    @Override
    public Result<GetAllCouriersResponse, Error> handle(GetAllCouriersQuery query) {

        List<CourierDto> couriers = courierQueries.findAllCouriers();
        if (couriers.isEmpty()) return Result.failure(Errors.couriersNotFound());

        return Result.success(new GetAllCouriersResponse(couriers));
    }

    public static class Errors {
        public static Error couriersNotFound() {
            return Error.of("couriers.not.found", "Couriers not found");
        }
    }

}
