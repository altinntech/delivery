package microarch.delivery.adapters.in.kafka;

import lombok.RequiredArgsConstructor;
import microarch.delivery.core.application.usecases.create_new_order.CreateNewOrder;
import microarch.delivery.core.application.usecases.create_new_order.CreateNewOrderCommand;
import microarch.delivery.core.domain.model.general.Address;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import queues.basket.BasketEventsProto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketsEventsConsumer {

    private final CreateNewOrder createNewOrderUseCase;

    @KafkaListener(topics = "${app.kafka.baskets-events-topic}")
    public void listen(byte[] message) {
        try {
            var event = BasketEventsProto.BasketConfirmedIntegrationEvent.parseFrom(message);

            BasketEventsProto.Address pAddress = event.getAddress();
            // Создаем команду
            var createCommandResult = CreateNewOrderCommand.create(
                    UUID.fromString(event.getBasketId()),
                    Address.create(pAddress.getCountry(),pAddress.getCity(),pAddress.getStreet(),pAddress.getHouse(),pAddress.getApartment()).getValue(),
                    event.getVolume());
            if (createCommandResult.isFailure()) {
                throw new RuntimeException("Invalid command: " + createCommandResult.getError());
            }
            var command = createCommandResult.getValue();

            // Обрабатываем команду
            var handleCommandResult = this.createNewOrderUseCase.handle(command);
            if (handleCommandResult.isFailure()) {
                throw new RuntimeException("Failed to handle command: " + handleCommandResult.getError());
            }

        } catch (com.google.protobuf.InvalidProtocolBufferException ex) {
            throw new RuntimeException("Failed to parse protobuf message", ex);
        }
    }
}
