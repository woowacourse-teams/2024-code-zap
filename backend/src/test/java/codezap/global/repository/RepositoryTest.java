package codezap.global.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;

import codezap.global.DatabaseIsolation;
import codezap.global.auditing.JpaAuditingConfiguration;
import codezap.global.querydsl.QueryDSLConfig;
import codezap.global.rds.DataSourceConfig;
import codezap.template.repository.TemplateSearchExpressionProvider;
import codezap.template.repository.strategy.FullTextSearchSearchStrategy;
import codezap.template.repository.strategy.LikeSearchStrategy;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class), useDefaultFilters = false)
@DatabaseIsolation
@Import({JpaAuditingConfiguration.class,
        DataSourceConfig.class,
        QueryDSLConfig.class,
        TemplateSearchExpressionProvider.class,
        LikeSearchStrategy.class,
        FullTextSearchSearchStrategy.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public @interface RepositoryTest {
}
