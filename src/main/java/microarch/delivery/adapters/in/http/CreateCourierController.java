package microarch.delivery.adapters.in.http;

import lombok.RequiredArgsConstructor;
import microarch.delivery.adapters.in.http.api.CreateCourierApi;
import microarch.delivery.adapters.in.http.model.CreateCourierResponse;
import microarch.delivery.adapters.in.http.model.NewCourier;
import microarch.delivery.core.application.usecases.create_new_courier.CreateNewCourier;
import microarch.delivery.core.application.usecases.create_new_courier.CreateNewCourierCommand;
import microarch.delivery.core.domain.model.general.Speed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateCourierController implements CreateCourierApi {

    private final CreateNewCourier createNewCourierUseCase;

    @Override
    public ResponseEntity<CreateCourierResponse> createCourier(NewCourier newCourier) {

        var createNewCourierCommandResult = CreateNewCourierCommand.create(newCourier.getName(), Speed.create(newCourier.getSpeed()).getValue());
        if (createNewCourierCommandResult.isFailure()) return ResponseEntity.badRequest().build();

        var handleCommandResult = createNewCourierUseCase.handle(createNewCourierCommandResult.getValue());
        if (handleCommandResult.isFailure()) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return ResponseEntity.ok(new CreateCourierResponse().courierId(handleCommandResult.getValue()));
    }
}
