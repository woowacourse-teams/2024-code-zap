package codezap.global.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.global.querydsl.QueryDSLConfig;
import codezap.global.rds.DataSourceConfig;
import codezap.template.domain.QTemplate;

@DataJpaTest
@Import({DataSourceConfig.class, QueryDSLConfig.class, FixedPageCounter.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:search.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FixedPageCounterTest {

    @Autowired
    private EntityManager em;

    private JPAQueryFactory queryFactory;
    private FixedPageCounter fixedPageCounter;

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(em);
        fixedPageCounter = new FixedPageCounter();
    }

    @Test
    @DisplayName("페이지가 7개까지 있어도, 최대 5페이지까지만 카운팅")
    void countNextFixedPage_WithPageSize10() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        QTemplate template = QTemplate.template;

        int nextFixedPage = fixedPageCounter.countNextFixedPage(
                queryFactory,
                template,
                pageRequest
        );

        assertThat(nextFixedPage).isEqualTo(5);
    }
}
