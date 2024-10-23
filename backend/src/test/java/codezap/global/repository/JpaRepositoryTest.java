package codezap.global.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import codezap.global.DatabaseIsolation;
import codezap.global.auditing.JpaAuditingConfiguration;
import codezap.global.rds.DataSourceConfig;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@DatabaseIsolation
@Import({JpaAuditingConfiguration.class, DataSourceConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public @interface JpaRepositoryTest {
}
