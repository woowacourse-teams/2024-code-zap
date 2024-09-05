package codezap.secure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RandomSaltGeneratorTest {

    SaltGenerator saltGenerator = new RandomSaltGenerator();

    @Test
    @DisplayName("1,000,000번의 난수 생성에서 난수 충돌이 발생하지 않는다.")
    @Disabled
    void test() {
        Set<String> salts = new HashSet<>();
        for (int i = 0; i < 1_000_000; i++) {
            salts.add(saltGenerator.getSalt());
        }

        assertThat(salts).hasSize(1_000_000);
    }
}
