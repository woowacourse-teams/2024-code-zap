package codezap.category.domain;

import java.util.HashSet;
import java.util.List;

public class Names {

    private List<String> names;

    public Names(List<String> names) {
        this.names = names;
    }

    public void validateNames() {
        if (hasDuplicates()) {
            throw new IllegalArgumentException("카테고리명이 중복되었습니다.");
        }
    }

    private boolean hasDuplicates() {
        return names.size() != new HashSet<>(names).size();
    }
}
