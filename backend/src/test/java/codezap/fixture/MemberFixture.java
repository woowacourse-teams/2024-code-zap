package codezap.fixture;

import codezap.auth.encryption.PasswordEncryptor;
import codezap.auth.encryption.SHA2PasswordEncryptor;
import codezap.member.domain.Member;

public class MemberFixture {

    private static final PasswordEncryptor passwordEncryptor = new SHA2PasswordEncryptor();

    public static Member getFirstMember() {
        String encrypted = passwordEncryptor.encrypt(getFixturePlainPassword(), "salt");
        return new Member(
                1L,
                "몰리",
                encrypted,
                "salt"
        );
    }

    public static Member getSecondMember() {
        String encrypted = passwordEncryptor.encrypt(getFixturePlainPassword(), "salt");
        return new Member(
                2L,
                "몰리2",
                encrypted,
                "salt"
        );
    }

    public static Member createFixture(String name) {
        String encrypted = passwordEncryptor.encrypt(getFixturePlainPassword(), "salt");
        return new Member(
                name,
                encrypted,
                "salt"
        );
    }

    public static String getFixturePlainPassword() {
        return "password1234";
    }
}
