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
            ApiErrorResponse apiErrorResponse = Objects.requireNonNull(handlerMethod.getMethodAnnotation(ApiErrorResponse.class));
            ApiResponses responses = operation.getResponses();
            String statusCode = String.valueOf(apiErrorResponse.status().value());
            responses.addApiResponse(statusCode, makeFailResponse(apiErrorResponse));
        }
        return operation;
    }

    private ApiResponse makeFailResponse(ApiErrorResponse apiErrorResponse) {
        ApiResponse apiResponse = new ApiResponse().description(apiErrorResponse.status().name());
        return apiResponse.content(new Content().addMediaType("application/json", makeMediaType(apiErrorResponse)));
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
