package codezap.category.repository;

import static codezap.category.domain.QCategory.category;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Modifying(clearAutomatically = true)
    public void shiftOrdinal(Member member, Long ordinal) {
        jpaQueryFactory.update(category)
                .set(category.ordinal, category.ordinal.subtract(1))
                .where(category.member.eq(member)
                        .and(category.ordinal.gt(ordinal)))
                .execute();
    }

    public boolean existsDuplicateOrdinalsByMember(Member member) {
        return jpaQueryFactory
                .select(category.ordinal.count())
                .from(category)
                .where(category.member.eq(member))
                .groupBy(category.ordinal)
                .having(category.ordinal.count().gt(1))
                .fetchFirst() != null;
    }
}
