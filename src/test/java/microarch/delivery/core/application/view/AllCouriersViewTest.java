package microarch.delivery.core.application.view;

import microarch.delivery.adapters.out.postgres.CourierQueries;
import microarch.delivery.adapters.out.postgres.PostgresIntegrationTestBase;
import microarch.delivery.core.application.usecases.assign_courier.AssignCourierCommand;
import microarch.delivery.core.application.view.all_couriers.AllCouriersView;
import microarch.delivery.core.application.view.all_couriers.GetAllCouriersQuery;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.general.Speed;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.CourierRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AllCouriersViewTest extends PostgresIntegrationTestBase {

    @Autowired
    AllCouriersView couriersView;

    @Autowired
    CourierRepository courierRepository;

    @Test
    @DisplayName("Testing all couriers view")
    void testSuccessfullAllCouriesView () {

        Courier courier1 = Courier.create("First", Speed.create(1).getValue(),Location.random().getValue()).getValue();
        Courier courier2 = Courier.create("Second", Speed.create(1).getValue(),Location.random().getValue()).getValue();
        Courier courier3 = Courier.create("Third", Speed.create(1).getValue(),Location.random().getValue()).getValue();
        Courier courier4 = Courier.create("Fourth", Speed.create(1).getValue(),Location.random().getValue()).getValue();

        courierRepository.addCourier(courier1);
        courierRepository.addCourier(courier2);
        courierRepository.addCourier(courier3);
        courierRepository.addCourier(courier4);

        GetAllCouriersQuery getAllCouriersQuery = new GetAllCouriersQuery();

        var allCouriersViewResult = couriersView.handle(getAllCouriersQuery);

        assertThat(allCouriersViewResult.isSuccess()).isTrue();
        assertThat(allCouriersViewResult.getValue().couriers().size()).isEqualTo(4);


    }

}
