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
        createIfNotExistFullTextIndex(jdbcTemplate);
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

    private void createIfNotExistFullTextIndex(JdbcTemplate jdbcTemplate) {
        if (!indexExists(jdbcTemplate, "template", "idx_template_fulltext")) {
            jdbcTemplate.execute("ALTER TABLE template ADD FULLTEXT INDEX idx_template_fulltext (title, description)");
        }
        if (!indexExists(jdbcTemplate, "source_code", "idx_source_code_fulltext")) {
            jdbcTemplate.execute("ALTER TABLE source_code ADD FULLTEXT INDEX idx_source_code_fulltext (content, filename)");
        }
    }

    private boolean indexExists(JdbcTemplate jdbcTemplate, String tableName, String indexName) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND INDEX_NAME = ?",
                Integer.class,
                tableName,
                indexName
        );
        return count != null && count > 0;
    }
}
