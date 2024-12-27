package codezap.global.swagger.error;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

@Component
public class ApiErrorResponsesCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        var apiErrorResponses = getApiErrorResponses(handlerMethod);
        var customResponses = makeApiResponses(apiErrorResponses);
        var apiResponses = operation.getResponses();
        apiResponses.putAll(customResponses);
        return operation;
    }

    private List<ApiErrorResponse> getApiErrorResponses(HandlerMethod handlerMethod) {
        if (handlerMethod.hasMethodAnnotation(ApiErrorResponses.class)) {
            var responses = Objects.requireNonNull(handlerMethod.getMethodAnnotation(ApiErrorResponses.class)).value();
            return Arrays.stream(responses).toList();
        }
        if (handlerMethod.hasMethodAnnotation(ApiErrorResponse.class)) {
            var response = handlerMethod.getMethodAnnotation(ApiErrorResponse.class);
            return List.of(Objects.requireNonNull(response));
        }
        return List.of();
    }

    private Map<String, ApiResponse> makeApiResponses(List<ApiErrorResponse> apiErrorResponses) {
        return apiErrorResponses.stream()
                .map(this::makeFailResponse)
                .collect(Collectors.toMap(ApiResponse::getDescription, response -> response));
    }

    private ApiResponse makeFailResponse(ApiErrorResponse apiErrorResponse) {
        return new ApiResponse()
                .description(getDescriptionByStatus(apiErrorResponse.status()))
                .content(new Content().addMediaType("application/json", makeMediaType(apiErrorResponse)));
    }

    private String getDescriptionByStatus(HttpStatusCode httpStatusCode) {
        var description = httpStatusCode.value() + " - ";
        if (httpStatusCode.is4xxClientError()) {
            return description + "클라이언트 오류";
        }
        if (httpStatusCode.is5xxServerError()) {
            return description + "서버 오류";
        }
        return description + "문서화에 오류가 발생했습니다. 서버팀에게 문의해주세요 😭";
    }

    private MediaType makeMediaType(ApiErrorResponse apiErrorResponse) {
        var mediaType = new MediaType();
        var errorCases = apiErrorResponse.errorCases();
        for (var errorCase : errorCases) {
            var example = makeExample(apiErrorResponse, errorCase);
            mediaType.addExamples(errorCase.description(), example);
        }
        return mediaType;
    }

    private Example makeExample(ApiErrorResponse apiErrorResponse, ErrorCase errorCase) {
        return new Example()
                .summary(errorCase.description())
                .value(ProblemDetailSchema.of(apiErrorResponse, errorCase.exampleMessage()));
    }
}
