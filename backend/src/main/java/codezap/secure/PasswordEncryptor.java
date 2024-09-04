package codezap.secure;

public interface PasswordEncryptor {
    String encrypt(String plainPassword, String salt);
}
