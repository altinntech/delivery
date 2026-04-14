package microarch.delivery.adapters.out.postgres;

import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.domain.model.order.OrderStatus;
import microarch.delivery.core.ports.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование репозитория заказов с миграциями")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrderRepositoryTest extends PostgresIntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Проверка успешного применения всех миграций")
    void testMigrationsAppliedSuccessfully() {
            // 1. Проверяем наличие таблицы истории
            assertThat(isTableExists("flyway_schema_history")).isTrue();

            // 2. Проверяем, что все миграции успешны
            Integer failedMigrations = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM flyway_schema_history WHERE success = false",
                    Integer.class
            );
            assertThat(failedMigrations).isZero();

            // 3. Проверяем количество примененных миграций
            Integer migrationCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM flyway_schema_history WHERE success = true",
                    Integer.class
            );
            assertThat(migrationCount).isGreaterThan(0);

            // 4. Проверяем конкретные объекты БД из миграций
            assertThat(isTableExists("orders")).isTrue();
            assertThat(isColumnExists("orders", "location_x")).isTrue();
//            assertThat(isIndexExists("users_email_key")).isTrue();

            // 5. Проверяем возможность работы с данными
//            jdbcTemplate.execute("INSERT INTO users (email, name) VALUES ('test@test.com', 'Test')");
//            Integer count = jdbcTemplate.queryForObject(
//                    "SELECT COUNT(*) FROM users WHERE email = 'test@test.com'",
//                    Integer.class
//            );
//            assertThat(count).isEqualTo(1);
        }

        private boolean isTableExists(String tableName) {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?",
                    Integer.class,
                    tableName
            );
            return count > 0;
        }

        private boolean isColumnExists(String tableName, String columnName) {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.columns WHERE table_name = ? AND column_name = ?",
                    Integer.class,
                    tableName, columnName
            );
            return count > 0;
        }

        private boolean isIndexExists(String indexName) {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM pg_indexes WHERE indexname = ?",
                    Integer.class,
                    indexName
            );
            return count > 0;
        }


        @Test
        @DisplayName("Проверка на добавление заказа")
        void CanAddOrder () {

            Order newOrder = Order.create(UUID.randomUUID(),
                    Location.create(5,5).getValue(),
                    5).getValue();

            orderRepository.addOrder(newOrder);

            var loaded = orderRepository.findById(newOrder.getId());

            // Assert
            assertThat(loaded).isPresent();
            assertThat(loaded.get().getStatus()).isEqualTo(newOrder.getStatus());
            assertThat(loaded.get().getTargetLocation()).isEqualTo(newOrder.getTargetLocation());
            assertThat(loaded.get().getVolume()).isEqualTo(newOrder.getVolume());
        }

    @Test
    @DisplayName("Проверка на изменение заказа")
    void CanUpdateOrder () {

        Order newOrder = Order.create(UUID.randomUUID(),
                Location.create(5,5).getValue(),
                5).getValue();

        Order updaterOrder = Order.create(newOrder.getId(),
                Location.create(2,8).getValue(),
                3).getValue();
        updaterOrder.assignCourier(UUID.randomUUID());

        orderRepository.addOrder(newOrder);

        boolean updated = orderRepository.updateOrder(updaterOrder);

        var loaded = orderRepository.findById(newOrder.getId());

        // Assert
        assertThat(loaded).isPresent();
        assertThat(updated).isTrue();
        assertThat(loaded.get().getStatus()).isEqualTo(updaterOrder.getStatus());
        assertThat(loaded.get().getTargetLocation()).isEqualTo(updaterOrder.getTargetLocation());
        assertThat(loaded.get().getVolume()).isEqualTo(updaterOrder.getVolume());
        assertThat(loaded.get().getCourierId()).isEqualTo(updaterOrder.getCourierId());
    }

    @Test
    @DisplayName("Проверка на поиск любого созданного заказа")
    void CanFindAnyCreatedOrder () {

        Order order1 = Order.create(UUID.randomUUID(),Location.create(5,5).getValue(),5).getValue();
        Order order2 = Order.create(UUID.randomUUID(),Location.create(5,5).getValue(),5).getValue();
        Order order3 = Order.create(UUID.randomUUID(),Location.create(5,5).getValue(),5).getValue();
        Order order4 = Order.create(UUID.randomUUID(),Location.create(5,5).getValue(),5).getValue();

        order1.assignCourier(UUID.randomUUID());
        order3.assignCourier(UUID.randomUUID());

        orderRepository.addOrder(order1);
        orderRepository.addOrder(order2);
        orderRepository.addOrder(order3);
        orderRepository.addOrder(order4);

        var loaded = orderRepository.findAnyOneCreated();

        // Assert
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getStatus()).isEqualTo(OrderStatus.CREATED);

    }

    @Test
    @DisplayName("Проверка на поиск всех назначенных заказов")
    void CanFindAllAssignedOrder () {

        Order order1 = Order.create(UUID.randomUUID(),Location.create(5,5).getValue(),5).getValue();
        Order order2 = Order.create(UUID.randomUUID(),Location.create(5,5).getValue(),5).getValue();
        Order order3 = Order.create(UUID.randomUUID(),Location.create(5,5).getValue(),5).getValue();
        Order order4 = Order.create(UUID.randomUUID(),Location.create(5,5).getValue(),5).getValue();

        order1.assignCourier(UUID.randomUUID());
        order3.assignCourier(UUID.randomUUID());

        orderRepository.addOrder(order1);
        orderRepository.addOrder(order2);
        orderRepository.addOrder(order3);
        orderRepository.addOrder(order4);

        var loaded = orderRepository.findAllAssigned();

        // Assert
        assertThat(loaded.size()).isEqualTo(2);
        assertThat(loaded.getFirst().getStatus()).isEqualTo(OrderStatus.ASSIGNED);
        assertThat(loaded.getLast().getStatus()).isEqualTo(OrderStatus.ASSIGNED);

    }

}
