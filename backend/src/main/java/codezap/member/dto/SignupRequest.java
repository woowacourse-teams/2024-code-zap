package codezap.member.dto;

public record SignupRequest(
        String email,
        String password,
        String username
) {
}
