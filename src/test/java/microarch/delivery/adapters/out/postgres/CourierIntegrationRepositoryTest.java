package microarch.delivery.adapters.out.postgres;

import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.domain.model.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Тестирование репозитория курьеров")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CourierIntegrationRepositoryTest extends PostgresIntegrationTestBase{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CourierPersistence courierRepository;

    @Test
    @DisplayName("Проверка на добавление курьера")
    void CanAddCourier () {

        Courier courier = Courier.create("Bob",2,Location.create(3,8).getValue()).getValue();
        courier.addStoragePlace("TOP",5);

        boolean result = courierRepository.addCourier(courier);

        assertThat(result).isTrue();

        var loaded = courierRepository.findById(courier.getId());

        // Assert
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getName()).isEqualTo(courier.getName());
        assertThat(loaded.get().getCurrentLocation()).isEqualTo(courier.getCurrentLocation());
        assertThat(loaded.get().getStoragePlaces().size()).isEqualTo(courier.getStoragePlaces().size());
    }

    @Test
    @DisplayName("Проверка на изменение курьера")
    void CanUpdateCourier () {

        Courier courier = Courier.create("BOB",5, Location.create(5,5).getValue()).getValue();

        boolean added = courierRepository.addCourier(courier);

        courier.addStoragePlace("Top",5);
        courier.addStoragePlace("Back",20);
        courier.moveOneStepToLocation(Location.create(9,9).getValue());
        courier.takeNewOrder(UUID.randomUUID(),5);

        boolean updated = courierRepository.updateCourier(courier);

        var loaded = courierRepository.findById(courier.getId());

        // Assert
        assertThat(added).isTrue();
        assertThat(updated).isTrue();
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getName()).isEqualTo(courier.getName());
        assertThat(loaded.get().getCurrentLocation()).isEqualTo(courier.getCurrentLocation());
        assertThat(loaded.get().getStoragePlaces()).isEqualTo(courier.getStoragePlaces());
        assertThat(loaded.get().getStoragePlaces().getFirst().getOrderId()).isEqualTo(courier.getStoragePlaces().getFirst().getOrderId());

    }


    @Test
    @DisplayName("Проверка на поиск всех свободных курьеров")
    void CanFindAllFreeCouriers () {

        Courier courier1 = Courier.create("Bob",5,Location.create(1,1).getValue()).getValue();
        Courier courier2 = Courier.create("Tom",1,Location.create(7,3).getValue()).getValue();
        Courier courier3 = Courier.create("Pete",2,Location.create(2,6).getValue()).getValue();
        Courier courier4 = Courier.create("Jack",3,Location.create(8,8).getValue()).getValue();
        Courier courier5 = Courier.create("Sarah",1,Location.create(9,1).getValue()).getValue();

        courier1.takeNewOrder(UUID.randomUUID(),3);
        courier2.addStoragePlace("Top",10);
        courier3.addStoragePlace("Back",8);
        courier3.addStoragePlace("Bottom",10);

        courierRepository.addCourier(courier1);
        courierRepository.addCourier(courier2);
        courierRepository.addCourier(courier3);
        courierRepository.addCourier(courier4);
        courierRepository.addCourier(courier5);


        var loaded = courierRepository.findAllFreeCouriers();

        Optional<Courier> loadedCourier = loaded.stream()
                .filter(p -> courier3.getId().equals(p.getId()))
                .findFirst();

        // Assert
        assertThat(loaded.size()).isEqualTo(4);
        assertThat(loaded.contains(courier1)).isFalse();
        assertThat(loadedCourier.get().getStoragePlaces().size()).isEqualTo(3);


    }


}
