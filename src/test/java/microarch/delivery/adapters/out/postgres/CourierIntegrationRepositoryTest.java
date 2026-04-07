package microarch.delivery.adapters.out.postgres;

import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.general.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

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
    @DisplayName("Проверка на добавление заказа")
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


}
