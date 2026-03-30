package microarch.delivery.core.domain.model.courier;

import libs.errs.Error;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.general.StoragePlace;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class CourierTest {

    @Test
    @DisplayName("Should create new courier with valid params")
    public void shouldCreateNewCourier() {

        Location location = Location.create(5, 5).getValue();

        var  courier = Courier.create("Bob",5,location);

        assertThat(courier.isSuccess()).isTrue();
        assertThat(courier.getValue().getCurrentLocation()).isEqualTo(location);
        assertThat(courier.getValue().getStoragePlaces().size()).isEqualTo(1);
        assertThat(courier.getValue().getStoragePlaces().getFirst().getTotalVolume()).isEqualTo(10);
        assertThat(courier.getValue().getName()).isEqualTo("Bob");
        assertThat(courier.getValue().getSpeed()).isEqualTo(5);

    }

    @ParameterizedTest
    @DisplayName("Should not create courier with invalid params ")
    @CsvSource({"Bob,5,-1,10",
            "Bob,0,5,5",
            ",5,5,5",
            ",5,5,0",
            ",0,0,-5"
    })
    public void shouldNotCreateOrderWithInvalidParams(String name,int speed, int x,int y) {

        Location targetLocation = null;

        var location =  Location.create(x,y);

        if (location.isSuccess()) targetLocation = location.getValue();

        var  courier = Courier.create(name,speed,targetLocation);

        assertThat(courier.isSuccess()).isFalse();

    }

    @Test
    @DisplayName("Should add new storage place")
    public void shouldAddOneMoreStoragePlace () {

        Courier courier = Courier.create("Bob",5,Location.create(3,7).getValue()).getValue();
        courier.addStoragePlace("Front",5);
        courier.addStoragePlace("Back",15);

        assertThat(courier).isNotNull();
        assertThat(courier.getStoragePlaces().size()).isEqualTo(3);
        assertThat(courier.getStoragePlaces().stream().mapToInt(StoragePlace::getTotalVolume).sum()).isEqualTo(30);

    }

    @Test
    @DisplayName("Check if courier could take new order")
    public void isCanTakeNewOrder () {

        Courier courier = Courier.create("Bob",5,Location.create(3,7).getValue()).getValue();

        boolean canTakeNewOrder = courier.isAvailableForNewOrder(5) != null;

        assertThat(courier).isNotNull();
        assertThat(canTakeNewOrder).isTrue();

    }

    @Test
    @DisplayName("Could not take the new order")
    public void couldNotTakeTheNewOrder () {

        Courier courier = Courier.create("Bob",5,Location.create(3,7).getValue()).getValue();
        courier.addStoragePlace("Front",5);
        courier.addStoragePlace("Back",15);

        courier.takeNewOrder(UUID.randomUUID(),5);

        StoragePlace place = courier.isAvailableForNewOrder(20);

        assertThat(courier).isNotNull();
        assertThat(courier.getStoragePlaces().size()).isEqualTo(3);
        assertThat(courier.getStoragePlaces().stream().mapToInt(StoragePlace::getTotalVolume).sum()).isEqualTo(30);
        assertThat(place).isNull();

    }

    @Test
    @DisplayName("Take new order")
    public void shouldTakeTheNewOrder () {

        Courier courier = Courier.create("Bob",5,Location.create(3,7).getValue()).getValue();

        UUID orderId = UUID.randomUUID();
        UnitResult <Error> e = courier.takeNewOrder(orderId,5);

        StoragePlace place = courier.getStoragePlaces().getFirst();

        assertThat(courier).isNotNull();
        assertThat(place.getOrderId()).isEqualTo(orderId);
        assertThat(e.isSuccess()).isTrue();


    }

    @Test
    @DisplayName("Should finish order")
    public void shouldFinishOrder () {

        Courier courier = Courier.create("Bob",5,Location.create(3,7).getValue()).getValue();

        UUID orderId = UUID.randomUUID();
        
        UnitResult <Error> te = courier.takeNewOrder(orderId,5);

        UnitResult <Error> re = courier.finishOrder(orderId);
        
        assertThat(courier).isNotNull();
        assertThat(te.isSuccess()).isTrue();
        assertThat(re.isSuccess()).isTrue();
        assertThat(courier.getStoragePlaces().getFirst().getOrderId()).isNull();

    }

    @Test
    @DisplayName("Should not finish order")
    public void shouldNotFinishOrder () {

        Courier courier = Courier.create("Bob",5,Location.create(3,7).getValue()).getValue();

        UUID orderId = UUID.randomUUID();

        UnitResult <Error> re = courier.finishOrder(orderId);

        assertThat(courier).isNotNull();
        assertThat(re.isSuccess()).isFalse();

    }

}
