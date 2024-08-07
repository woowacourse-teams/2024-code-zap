package codezap.member.dto;

public record LoginResponse(
        long memberId,
        String username
) {
}
