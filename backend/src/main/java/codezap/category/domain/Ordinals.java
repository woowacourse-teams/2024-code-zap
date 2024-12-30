package codezap.category.domain;

import java.util.List;
import java.util.stream.IntStream;

public class Ordinals {

    private List<Integer> ordinals;

    public Ordinals(List<Integer> ordinals) {
        this.ordinals = ordinals;
    }

    public void validateOrdinals() {
        if (!isSequential()) {
            throw new IllegalArgumentException("순서가 잘못되었습니다.");
        }
    }

    private boolean isSequential() {
        return IntStream.range(0, ordinals.size())
                .allMatch(index -> ordinals.get(index) == index + 1);
    }
}
