package codezap.template.repository;

import static codezap.template.domain.QTemplate.template;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

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
        int nextFixedPage = countNextFixedPage(pageable, memberId, keyword, categoryId, tagIds, visibility);
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
        return queryFactory
                .selectFrom(QTemplate.template)
                .leftJoin(QTemplate.template.category).fetchJoin()
                .leftJoin(QTemplate.template.member).fetchJoin()
                .where(matchesKeyword(memberId, keyword, categoryId, tagIds, visibility))
                .orderBy(TemplateOrderSpecifierUtils.getOrderSpecifier(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private int countNextFixedPage(
            Pageable pageable,
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Visibility visibility
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
}

