package codezap.template.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.global.pagination.FixedPage;
import codezap.global.pagination.FixedPageCounter;
import codezap.likes.domain.QLikes;
import codezap.template.domain.QTemplate;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TemplateQueryDSLRepository {

    private final JPAQueryFactory queryFactory;
    private final TemplateSearchExpressionProvider expressionProvider;
    private final FixedPageCounter fixedPageCounter;

    public FixedPage<Template> findTemplates(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Visibility visibility,
            Pageable pageable
    ) {
        List<Template> content = getTemplates(memberId, keyword, categoryId, tagIds, visibility, pageable);
        int nextFixedPage = countMaxPageOfTemplates(memberId, keyword, categoryId, tagIds, visibility, pageable);
        return new FixedPage<>(content, nextFixedPage);
    }

    private List<Template> getTemplates(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Visibility visibility,
            Pageable pageable
    ) {
        return queryFactory.selectFrom(QTemplate.template)
                .leftJoin(QTemplate.template.category).fetchJoin()
                .leftJoin(QTemplate.template.member).fetchJoin()
                .where(matchesKeyword(memberId, keyword, categoryId, tagIds, visibility))
                .orderBy(TemplateOrderSpecifierUtils.getOrderSpecifier(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private int countMaxPageOfTemplates(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Visibility visibility,
            Pageable pageable
    ) {
        return fixedPageCounter.countNextFixedPage(
                queryFactory,
                QTemplate.template,
                pageable,
                matchesKeyword(memberId, keyword, categoryId, tagIds, visibility)
        );
    }

    private BooleanExpression[] matchesKeyword(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Visibility visibility
    ) {
        return new BooleanExpression[]{
                expressionProvider.filterMember(memberId),
                expressionProvider.filterCategory(categoryId),
                expressionProvider.filterVisibility(visibility),
                expressionProvider.hasAnyTags(tagIds),
                expressionProvider.matchesKeyword(keyword)
        };
    }

    public FixedPage<Template> findAllLikedByMemberId(Long memberId, Pageable pageable) {
        List<Template> content = queryFactory.select(QLikes.likes.template)
                .from(QLikes.likes)
                .where(isLikedTemplateByMember(memberId))
                .orderBy(TemplateOrderSpecifierUtils.getOrderSpecifier(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new FixedPage<>(content, countMaxPageOfLikeTemplates(memberId, pageable));
    }

    private int countMaxPageOfLikeTemplates(Long memberId, Pageable pageable) {
        return fixedPageCounter.countNextFixedPage(
                queryFactory,
                QLikes.likes,
                pageable,
                isLikedTemplateByMember(memberId)
        );
    }

    private static BooleanExpression isLikedTemplateByMember(Long memberId) {
        BooleanExpression isLikedByMemberId = QLikes.likes.member.id.eq(memberId);
        BooleanExpression isLikedTemplateByMemberId = QLikes.likes.template.member.id.eq(memberId);
        BooleanExpression isTemplatePublic = QLikes.likes.template.visibility.eq(Visibility.PUBLIC);

        return isLikedByMemberId.and(isLikedTemplateByMemberId.or(isTemplatePublic));
    }
}

