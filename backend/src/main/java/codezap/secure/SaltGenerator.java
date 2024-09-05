package codezap.secure;

@FunctionalInterface
public interface SaltGenerator {
    String generate();
}
