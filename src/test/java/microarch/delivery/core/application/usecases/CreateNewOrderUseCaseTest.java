package microarch.delivery.core.application.usecases;

import libs.errs.Result;
import microarch.delivery.core.application.usecases.create_new_order.CreateNewOrderCommand;
import microarch.delivery.core.application.usecases.create_new_order.CreateNewOrderUseCase;
import microarch.delivery.core.domain.model.general.Address;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.GeoClient;
import microarch.delivery.core.ports.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Any;
import org.testcontainers.shaded.com.github.dockerjava.core.dockerfile.DockerfileStatement;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class CreateNewOrderUseCaseTest {

    OrderRepository orderRepository = mock(OrderRepository.class);
    GeoClient geoClient = mock(GeoClient.class);

    @Test
    void CreateNewOrderCommand_ShouldBeSuccess_WithValidPaarms () {

        Order newOrder = Order.create(UUID.randomUUID(), Location.random().getValue(),5).getValue();

        Address address = Address.create("РФ","СПБ","Невский","35","25").getValue();

        Location location = Location.random().getValue();

        when(geoClient.getLocation(address)).thenReturn(Result.success(location));

        var createCommand = CreateNewOrderCommand.create(newOrder.getId(),address,newOrder.getVolume()).getValue();

        var createOrderUseCase = new CreateNewOrderUseCase(orderRepository,geoClient);

        createOrderUseCase.handle(createCommand);

        verify(orderRepository).addOrder(newOrder);
    }

}
