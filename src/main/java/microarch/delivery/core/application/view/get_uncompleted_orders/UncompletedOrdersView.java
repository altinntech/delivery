package microarch.delivery.core.application.view.get_uncompleted_orders;

import libs.errs.Error;
import libs.errs.Result;
import microarch.delivery.adapters.out.postgres.OrderQueries;
import microarch.delivery.core.application.view.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UncompletedOrdersView implements GetUncompletedOrders{

    @Autowired
    OrderQueries orderQueries;

    @Override
    public Result<GetUncompletedOrdersResponse, Error> handle(GetUncompletedOrdersQuery query) {

        List<OrderDto> orders = orderQueries.findAllUncompletedOrders();
        if (orders.isEmpty()) return Result.failure(Errors.ordersNotFound());

        return Result.success(new GetUncompletedOrdersResponse(orders));
    }

    public static class Errors {
        public static Error ordersNotFound() {
            return Error.of("uncompleted.orders.not.found", "Uncompleted orders not found");
        }
    }
}
