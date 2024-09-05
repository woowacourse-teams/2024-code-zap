package codezap.auth.encryption;

@FunctionalInterface
public interface SaltGenerator {
    String generate();
}
