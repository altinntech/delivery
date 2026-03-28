package microarch.delivery.core.domain.model.general;

import libs.errs.Error;
import libs.errs.UnitResult;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class StoragePlaceTest {

    @Test
    @DisplayName("Should create storage place with valid params")
    public void shouldCreateStoragePlaceWithValidParams() {

        var  storagePlace = StoragePlace.create("Bag",10);

        assertThat(storagePlace.isSuccess()).isTrue();
        assertThat(storagePlace.getValue().getName()).isEqualTo("Bag");
        assertThat(storagePlace.getValue().getTotalVolume()).isEqualTo(10);
    }

    @ParameterizedTest
    @DisplayName("Should not create storage place with invalid params ")
    @CsvSource({"Bag,0","Bag,-1",",5","'   ',5",",0",",-10","'  ',0","'   ',-2"})
    public void shouldNotCreateStoragePlaceWithInvalidParams(String name,int volume) {

        var  storagePlace = StoragePlace.create(name,volume);

        assertThat(storagePlace.isSuccess()).isFalse();
        assertThat(storagePlace.isFailure()).isTrue();

    }

    @Test
    @DisplayName("Place valid order in storage")
    public void placeValidOrder() {

        StoragePlace storagePlace = StoragePlace.create("Bag",10).getValue();
        UnitResult<Error> result = storagePlace.placeNewOrder(UUID.randomUUID(),7);

        assertThat(result.isSuccess()).isTrue();
        assertThat(storagePlace.getOrderId()).isNotNull();

    }

    @ParameterizedTest
    @DisplayName("Should not place order in small storage")
    @CsvSource({"10,20","1,5","5,0"})
    public void shouldNotPlaceOrderInSmallStorage(int storageSize, int orderSize) {

        StoragePlace storagePlace = StoragePlace.create("Bag",storageSize).getValue();
        UnitResult<Error> result = storagePlace.placeNewOrder(UUID.randomUUID(),orderSize);

        assertThat(result.isSuccess()).isFalse();
        assertThat(storagePlace.getOrderId()).isNull();

    }

    @Test
    @DisplayName("Should not place order in non empty storage")
    public void shouldNotPlaceOrderInNonEmptyStorage() {

        StoragePlace storagePlace = StoragePlace.create("Bag",10).getValue();
        UUID orderID = UUID.randomUUID();
        storagePlace.placeNewOrder(orderID,5);

        UnitResult<Error> result = storagePlace.placeNewOrder(UUID.randomUUID(),3);

        assertThat(result.isSuccess()).isFalse();
        assertThat(storagePlace.getOrderId()).isEqualTo(orderID);

    }

    @Test
    @DisplayName("Should extract order from storage")
    public void shouldExtractOrderFromStorage() {

        StoragePlace storagePlace = StoragePlace.create("Bag",10).getValue();
        storagePlace.placeNewOrder(UUID.randomUUID(),5);
        UnitResult<Error> result = storagePlace.extractOrder();

        assertThat(result.isSuccess()).isTrue();
        assertThat(storagePlace.getOrderId()).isNull();

    }

    @Test
    @DisplayName("Should not extract order from empty storage")
    public void shouldNotExtractOrderFromEmptyStorage() {

        StoragePlace storagePlace = StoragePlace.create("Bag",10).getValue();
        UnitResult<Error> result = storagePlace.extractOrder();

        assertThat(result.isSuccess()).isFalse();

    }


}
