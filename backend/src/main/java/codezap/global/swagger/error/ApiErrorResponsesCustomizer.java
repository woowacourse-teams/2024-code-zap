package codezap.global.swagger.error;

import java.util.Arrays;
import java.util.Objects;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Component
public class ApiErrorResponsesCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        if (handlerMethod.hasMethodAnnotation(ApiErrorResponse.class)) {
            ApiErrorResponse apiErrorResponse = Objects.requireNonNull(
                    handlerMethod.getMethodAnnotation(ApiErrorResponse.class));
            ApiResponses responses = operation.getResponses();
            String statusCode = String.valueOf(apiErrorResponse.status().value());
            responses.addApiResponse(statusCode, makeFailResponse(apiErrorResponse));
        }
        return operation;
    }

    private ApiResponse makeFailResponse(ApiErrorResponse apiErrorResponse) {
        ApiResponse apiResponse = new ApiResponse().description(getDescriptionByStatus(apiErrorResponse.status()));
        return apiResponse.content(new Content().addMediaType("application/json", makeMediaType(apiErrorResponse)));
    }

    private String getDescriptionByStatus(HttpStatusCode httpStatusCode) {
        if (httpStatusCode.is4xxClientError()) {
            return "클라이언트 오류";
        }
        if (httpStatusCode.is5xxServerError()) {
            return "서버 오류";
        }
        return "문서화에 오류가 발생했습니다. 서버팀에게 문의해주세요 😭";
    }

    private MediaType makeMediaType(ApiErrorResponse apiErrorResponse) {
        ErrorCase[] errorCases = apiErrorResponse.errorCases();

        MediaType mediaType = new MediaType();
        Arrays.stream(errorCases).forEach(
                errorCase -> mediaType.addExamples(errorCase.description(), makeExample(apiErrorResponse, errorCase)));
        return mediaType;
    }

    private Example makeExample(ApiErrorResponse apiErrorResponse, ErrorCase failResponse) {
        Example example = new Example().summary(failResponse.description());
        return example.value(ProblemDetailSchema.of(apiErrorResponse, failResponse.exampleMessage()));
    }
}
