package codezap.member.dto.response;

public record LoginResponse(
        long memberId,
        String username
) {
}
