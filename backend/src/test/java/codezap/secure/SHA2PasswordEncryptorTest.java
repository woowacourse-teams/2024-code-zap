package codezap.secure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SHA2PasswordEncryptorTest {

    private final PasswordEncryptor passwordEncryptor = new SHA2PasswordEncryptor();

    @Test
    @DisplayName("성공 : 동일한 평문으로 여러번 암호화를 해도 똑같은 암호가 나온다.")
    void encryptTest() {
        //given
        String password = "password";
        String salt = "salt";

        //when
        String firstCipherText = passwordEncryptor.encrypt(password, salt);
        String secondCipherText = passwordEncryptor.encrypt(password, salt);

        //then
        assertThat(firstCipherText).isEqualTo(secondCipherText);
    }

    @Test
    @DisplayName("성공 : 동일한 평문, 다른 salt 값을 사용하면 다른 암호문이 나온다.")
    void encryptTestWithDifferentSalt() {
        //given
        String password = "password";

        //when
        String firstCipherText = passwordEncryptor.encrypt(password, "salt");
        String secondCipherText = passwordEncryptor.encrypt(password, "otherSalt");

        //then
        assertThat(firstCipherText).isNotEqualTo(secondCipherText);
    }

    @Test
    @DisplayName("성공 : 동일한 salt 값, 다른 평문을 사용하면 다른 암호문이 나온다.")
    void encryptTestWithDifferentPassword() {
        //given
        String salt = "salt";

        //when
        String firstCipherText = passwordEncryptor.encrypt("password1", salt);
        String secondCipherText = passwordEncryptor.encrypt("password2", salt);

        //then
        assertThat(firstCipherText).isNotEqualTo(secondCipherText);
    }
}
