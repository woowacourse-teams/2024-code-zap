package codezap.auth.encryption;

public interface PasswordEncryptor {
    String encrypt(String plainPassword, String salt);
}
