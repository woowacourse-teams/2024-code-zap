package codezap.global;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class DatabaseCleaner extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        List<String> queries = getTruncateQueries(jdbcTemplate);
        truncate(queries, jdbcTemplate);
    }

    private List<String> getTruncateQueries(final JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList(
                "SELECT CONCAT('TRUNCATE TABLE ', TABLE_NAME, ';') " +
                        "FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_SCHEMA = DATABASE();",
                String.class
        );
    }

    private void truncate(List<String> queries, JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        queries.forEach(jdbcTemplate::execute);
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
    }
}
