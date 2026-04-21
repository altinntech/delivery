package microarch.delivery.adapters.in.http;

import libs.util.RandomHelper;
import lombok.RequiredArgsConstructor;
import microarch.delivery.adapters.in.http.api.CreateOrderApi;
import microarch.delivery.adapters.in.http.model.CreateOrderResponse;
import microarch.delivery.core.application.usecases.create_new_order.CreateNewOrder;
import microarch.delivery.core.application.usecases.create_new_order.CreateNewOrderCommand;
import microarch.delivery.core.domain.model.general.Address;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CreateOrderController implements CreateOrderApi {

    private final CreateNewOrder createNewOrderUseCase;

    @Override
    public ResponseEntity<CreateOrderResponse> createOrder() {

        var createCommandResult = CreateNewOrderCommand.create(UUID.randomUUID(), Address.random().getValue(), RandomHelper.generateRandomInteger(1,10));
        if (createCommandResult.isFailure()) return ResponseEntity.badRequest().build();

        var handleCommandResult = this.createNewOrderUseCase.handle(createCommandResult.getValue());
        if (handleCommandResult.isFailure()) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return ResponseEntity.ok(new CreateOrderResponse().orderId(createCommandResult.getValue().getOrderId()));
    }
}
