package codezap.global.swagger.error;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        getApiErrorResponses(handlerMethod).ifPresent(responses ->
                addApiErrorResponses(responses, operation.getResponses())
        );

        return operation;
    }

    private Optional<ApiErrorResponse[]> getApiErrorResponses(HandlerMethod handlerMethod) {
        if (handlerMethod.hasMethodAnnotation(ApiErrorResponses.class)) {
            return Optional.ofNullable(handlerMethod.getMethodAnnotation(ApiErrorResponses.class))
                    .map(ApiErrorResponses::value);
        }
        return Optional.ofNullable(handlerMethod.getMethodAnnotation(ApiErrorResponse.class))
                .map(response -> new ApiErrorResponse[]{response});
    }

    private void addApiErrorResponses(ApiErrorResponse[] apiErrorResponses, ApiResponses responses) {
        Arrays.stream(apiErrorResponses)
                .map(this::makeFailResponse)
                .forEach(apiResponse -> responses.addApiResponse(apiResponse.getDescription(), apiResponse));
    }

    private ApiResponse makeFailResponse(ApiErrorResponse apiErrorResponse) {
        return new ApiResponse()
                .description(getDescriptionByStatus(apiErrorResponse.status()))
                .content(new Content().addMediaType("application/json", makeMediaType(apiErrorResponse)));
    }

    private String getDescriptionByStatus(HttpStatusCode httpStatusCode) {
        String description = httpStatusCode.value() + " - ";
        if (httpStatusCode.is4xxClientError()) {
            return description + "클라이언트 오류";
        }
        if (httpStatusCode.is5xxServerError()) {
            return description + "서버 오류";
        }
        return description + "문서화에 오류가 발생했습니다. 서버팀에게 문의해주세요 😭";
    }

    private MediaType makeMediaType(ApiErrorResponse apiErrorResponse) {
        MediaType mediaType = new MediaType();
        Arrays.stream(apiErrorResponse.errorCases()).forEach(
                errorCase -> mediaType.addExamples(errorCase.description(), makeExample(apiErrorResponse, errorCase)));
        return mediaType;
    }

    private Example makeExample(ApiErrorResponse apiErrorResponse, ErrorCase errorCase) {
        return new Example()
                .summary(errorCase.description())
                .value(ProblemDetailSchema.of(apiErrorResponse, errorCase.exampleMessage()));
    }
}
