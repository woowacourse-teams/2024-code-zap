package codezap.template.dto.request;

public record CreateTemplateResponse(
        String email,
        String nickname,
        String password) {
}
