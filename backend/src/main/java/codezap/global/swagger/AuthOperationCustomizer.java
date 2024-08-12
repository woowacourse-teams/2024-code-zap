package codezap.global.swagger;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * 인증 관련 요청 및 응답 규격을 커스터마이징하는 OperationCustomizer
 * <p>
 * - 인증 토큰이 필요한 API에 대해 인증 토큰 추출 시 발생할 수 있는 에러 응답을 추가합니다.
 * <p>
 * - 인증 객체는 토큰으로부터 서버에서 추출하므로 요청 파라미터에서 문서를 제거합니다.
 */
@Component
public class AuthOperationCustomizer implements OperationCustomizer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Operation customize(final Operation operation, final HandlerMethod handlerMethod) {
        if (hasSecurityRequirement(handlerMethod)) {
//            generateAuthErrorResponse(operation);
            hideAuthInfoParameter(operation);
        }
        return operation;
    }

    /**
     * @SecurityRequirement 어노테이션이 부착된 메서드나 클래스에 적용됩니다.
     */
    private boolean hasSecurityRequirement(HandlerMethod handlerMethod) {
        return handlerMethod.hasMethodAnnotation(SecurityRequirement.class)
                || handlerMethod.getBeanType().isAnnotationPresent(SecurityRequirement.class);
    }

    /**
     * 인증 토큰이 필요한 API에 대해 인증 토큰 추출 시 발생할 수 있는 에러 응답을 추가합니다.
     */
    private ApiResponses generateAuthErrorResponse(Operation operation) {
        ApiResponses apiResponses = operation.getResponses();

        Example noTokenCookieExample = new Example()
                .externalValue("Authorization 쿠키 없음")
                .description("쿠키는 있지만 Authorization 대한 담은 쿠키가 없는 경우")
                .value(getExampleJsonString(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                        "쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.")));

        Example noCookiesExample = new Example()
                .externalValue("모든 쿠키 없음")
                .description("쿠키 자체가 null인 경우")
                .value(getExampleJsonString(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                        "쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.")));

        MediaType mediaType = new MediaType().schema(new Schema<>().$ref("#/components/schemas/Error"));
        mediaType.addExamples("Authorization 쿠키 없음", noTokenCookieExample);
        mediaType.addExamples("모든 쿠키 없음", noCookiesExample);

        ApiResponse unauthorizedResponse = new ApiResponse()
                .description("회원 인증 실패")
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        mediaType));
        apiResponses.addApiResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()), unauthorizedResponse);

        return apiResponses;
    }

    private String getExampleJsonString(ProblemDetail problemDetail) {
        try {
            return objectMapper.writeValueAsString(problemDetail);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("ProblemDetail 객체를 파싱할 수 없습니다. 서버에 문의해주세요.", e);
        }
    }

    /**
     * 인증 객체는 토큰으로부터 서버에서 추출하므로 요청 파라미터에서 문서를 제거합니다.
     */
    private void hideAuthInfoParameter(Operation operation) {
        if (operation.getParameters() != null) {
            operation.getParameters().removeIf(parameter -> parameter.getName().equals("memberDto"));
        }
    }
}
