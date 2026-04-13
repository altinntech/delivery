package microarch.delivery.core.application.usecases;

import microarch.delivery.core.application.usecases.create_new_courier.CreateNewCourierCommand;
import microarch.delivery.core.application.usecases.create_new_courier.CreateNewCourierUseCase;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.general.Speed;
import microarch.delivery.core.ports.CourierRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class CreateNewCourierUseCaseTest {

    CourierRepository courierRepository = mock(CourierRepository.class);

    MockedStatic<UUID> uuidMock = mockStatic(UUID.class);

    @Test
    void CreateNewCourierCommand_ShouldBeSuccess_WithValidPaarms () {

        Speed speed = Speed.create(5).getValue();
        String courierName = "Bob";
        UUID fixedUuid = new UUID(156215L,51652612L);//UUIDfromString("550e8400-e29b-41d4-a716-446655440000");
        Location fixedLocation = Location.random().getValue();

        when(UUID.randomUUID()).thenReturn(fixedUuid);

        Courier newCourier = Courier.create(courierName, speed,fixedLocation).getValue();

        var createCommand = CreateNewCourierCommand.create(newCourier.getName(), speed).getValue();

        var createNewCourierUseCase = new CreateNewCourierUseCase(courierRepository);

        createNewCourierUseCase.handle(createCommand);

        verify(courierRepository).addCourier(newCourier);


    }

}
