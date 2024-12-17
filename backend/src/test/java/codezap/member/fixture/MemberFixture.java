package codezap.member.fixture;

import codezap.auth.encryption.PasswordEncryptor;
import codezap.auth.encryption.SHA2PasswordEncryptor;
import codezap.member.domain.Member;
import codezap.member.domain.Role;

public class MemberFixture {

    private static final PasswordEncryptor passwordEncryptor = new SHA2PasswordEncryptor();

    public static Member memberFixture() {
        String encrypted = passwordEncryptor.encrypt(getFixturePlainPassword(), "salt");
        return new Member(
                1L,
                "몰리",
                encrypted,
                "salt",
                Role.MEMBER
        );
    }

    public static String getFixturePlainPassword() {
        return "password1234";
    }

    public static Member createFixture(String name) {
        return new Member(
                name,
                "password1234",
                "salt"
        );
    }
}
