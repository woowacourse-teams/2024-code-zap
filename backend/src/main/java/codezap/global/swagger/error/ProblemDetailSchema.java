package codezap.global.swagger.error;

/**
 * ApiErrorResponse 어노테이션과 상세 설명을 바탕으로 ProblemDetailSchema 객체를 생성합니다.
 * 이 메서드는 RFC 7807 "Problem Details for HTTP APIs" 규격을 준수하는 오류 응답 스키마를 생성합니다.
 *
 * <br>
 * <br>
 * 객체를 생성하지 않을 경우, 개별 예외 케이스의 응답 예시를 문서화할 수 없습니다.
 *
 * @param detail 문제에 대한 상세 설명 (예외 메시지를 작성하시면 좋을 것 같습니다.)
 */

public record ProblemDetailSchema(
        String type,
        String title,
        int status,
        String detail,
        String instance
) {
    public static ProblemDetailSchema of(ApiErrorResponse apiErrorResponse, String detail) {
        return new ProblemDetailSchema(
                apiErrorResponse.type(),
                apiErrorResponse.status().name(),
                apiErrorResponse.status().value(),
                detail,
                apiErrorResponse.instance()
        );
    }
}
