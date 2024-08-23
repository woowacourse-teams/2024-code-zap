package codezap.global.swagger.error;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 에러 응답의 개별 케이스를 문서화하기 위한 어노테이션입니다.
 * {@link ApiErrorResponse} 어노테이션 내에서 사용되어 다양한 에러 시나리오를 설명합니다.
 *
 * @see ApiErrorResponse
 */

@Target(value = METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorCase {

    String description();

    String exampleMessage();
}

