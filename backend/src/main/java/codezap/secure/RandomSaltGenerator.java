package codezap.secure;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class RandomSaltGenerator implements SaltGenerator {

    @Override
    public String getSalt() {
        SecureRandom byteGenerator = new SecureRandom();
        byte[] saltByte = new byte[32];
        byteGenerator.nextBytes(saltByte);
        return Base64.getEncoder().encodeToString(saltByte);
    }
}
