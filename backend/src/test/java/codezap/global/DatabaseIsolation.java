package codezap.global;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.test.context.TestExecutionListeners;

@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners(value = DatabaseCleaner.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public @interface DatabaseIsolation {
}
