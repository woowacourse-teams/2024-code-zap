package codezap.global.swagger.error;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

/**
 * HTTP API 에러 응답을 문서화하기 위한 어노테이션입니다.
 * RFC 7807 "Problem Details for HTTP APIs" 규격을 따르는 {@link ProblemDetailSchema}를 생성하는 데 사용됩니다.
 *
 * @see ProblemDetailSchema
 * @see ErrorCase
 */

@Target(value = METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorResponse {

    String type() default "about:blank";

    HttpStatus status();

    String instance();

    ErrorCase[] failCases();
}

