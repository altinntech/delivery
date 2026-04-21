package microarch.delivery.adapters.in.http;

import lombok.RequiredArgsConstructor;
import microarch.delivery.adapters.in.http.api.GetOrdersApi;
import microarch.delivery.adapters.in.http.mappers.DeliveryMapper;
import microarch.delivery.adapters.in.http.model.Order;
import microarch.delivery.core.application.view.get_all_couriers.GetAllCouriersQuery;
import microarch.delivery.core.application.view.get_uncompleted_orders.GetUncompletedOrders;
import microarch.delivery.core.application.view.get_uncompleted_orders.GetUncompletedOrdersQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetOrdersController implements GetOrdersApi {

    private final GetUncompletedOrders getUncompletedOrdersView;

    @Override
    public ResponseEntity<List<Order>> getOrders() {

        var handleViewResult = this.getUncompletedOrdersView.handle(new GetUncompletedOrdersQuery());
        if (handleViewResult.isFailure()) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        var model = DeliveryMapper.INSTANCE.ordersToHttp(handleViewResult.getValue().orderDtoList());

        return ResponseEntity.ok(model);
    }
}
