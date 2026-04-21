package microarch.delivery.adapters.in.http;

import lombok.RequiredArgsConstructor;
import microarch.delivery.adapters.in.http.api.GetCouriersApi;
import microarch.delivery.adapters.in.http.mappers.DeliveryMapper;
import microarch.delivery.adapters.in.http.model.Courier;
import microarch.delivery.adapters.in.http.model.CreateOrderResponse;
import microarch.delivery.core.application.view.get_all_couriers.GetAllCouriers;
import microarch.delivery.core.application.view.get_all_couriers.GetAllCouriersQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetCouriersController implements GetCouriersApi {

    private final GetAllCouriers getAllCouriersView;

    @Override
    public ResponseEntity<List<Courier>> getCouriers() {

        var handleViewResult = this.getAllCouriersView.handle(new GetAllCouriersQuery());
        if (handleViewResult.isFailure()) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        var model = DeliveryMapper.INSTANCE.couriersToHttp(handleViewResult.getValue().couriers());

        return ResponseEntity.ok(model);

    }
}
