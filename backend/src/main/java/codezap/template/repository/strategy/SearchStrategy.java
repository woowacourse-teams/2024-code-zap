package codezap.template.repository.strategy;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface SearchStrategy {

    BooleanExpression matchedKeyword(String trimmedKeyword);
}
