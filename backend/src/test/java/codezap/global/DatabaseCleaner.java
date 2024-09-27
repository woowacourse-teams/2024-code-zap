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
        recreateFullTextIndex(jdbcTemplate);
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

    private void recreateFullTextIndex(JdbcTemplate jdbcTemplate) {
        dropIndexIfExists(jdbcTemplate, "template", "idx_template_fulltext");
        dropIndexIfExists(jdbcTemplate, "source_code", "idx_source_code_fulltext");

        jdbcTemplate.execute("ALTER TABLE template ADD FULLTEXT INDEX idx_template_fulltext (title, description)");
        jdbcTemplate.execute("ALTER TABLE source_code ADD FULLTEXT INDEX idx_source_code_fulltext (content, filename)");
    }

    private void dropIndexIfExists(JdbcTemplate jdbcTemplate, String tableName, String indexName) {
        String checkIndexQuery = "SELECT COUNT(*) FROM information_schema.statistics " +
                "WHERE table_schema = DATABASE() " +
                "AND table_name = ? " +
                "AND index_name = ?";

        int indexCount = jdbcTemplate.queryForObject(checkIndexQuery, Integer.class, tableName, indexName);

        if (indexCount > 0) {
            jdbcTemplate.execute("ALTER TABLE " + tableName + " DROP INDEX " + indexName);
        }
    }
}
