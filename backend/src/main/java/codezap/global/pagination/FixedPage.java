package codezap.global.pagination;

import java.util.List;

public record FixedPage<T> (List<T> contents, int nextPages) {
}
