package microarch.delivery.adapters.out.postgres;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование репозитория с миграциями")
@SpringBootTest
public class OrderRepositoryTest extends PostgresIntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
            assertThat(migrationCount).isEqualTo(1);

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


}
