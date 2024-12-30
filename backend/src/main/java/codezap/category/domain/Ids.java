package codezap.category.domain;

import java.util.HashSet;
import java.util.List;

public class Ids {

    private List<Long> ids;

    public Ids(List<Long> ids) {
        this.ids = ids;
    }

    public void validateIds() {
        if (hasDuplicates()) {
            throw new IllegalArgumentException("id가 중복되었습니다.");
        }
    }

    private boolean hasDuplicates() {
        return ids.size() != new HashSet<>(ids).size();
    }
}
