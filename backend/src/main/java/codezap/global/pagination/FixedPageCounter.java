package codezap.global.pagination;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Component
public class FixedPageCounter {

    private static final int MAXIMUM_PAGE = 5;

    public int countNextFixedPage(
            JPAQueryFactory queryFactory,
            EntityPathBase<?> entityPath,
            Pageable pageable,
            BooleanExpression... conditions
    ) {
        int maximumElementsCount = pageable.getPageSize() * MAXIMUM_PAGE;
        long nextFixedElementCounts = queryFactory
                .selectFrom(entityPath)
                .where(conditions)
                .offset(pageable.getOffset())
                .limit(maximumElementsCount)
                .fetch()
                .size();

        return (int) Math.ceil((double) nextFixedElementCounts / pageable.getPageSize());
    }
}
