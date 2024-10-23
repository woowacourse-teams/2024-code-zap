package codezap.template.repository;

import static codezap.template.domain.QTemplate.template;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.template.domain.Template;
import codezap.template.domain.Visibility;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class TemplateQueryDSLRepository {

    private final TemplateSearchExpressionProvider expressionProvider;
    private final JPAQueryFactory queryFactory;

    public Page<Template> findTemplates(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Visibility visibility,
            Pageable pageable
    ) {
        List<Template> content = getTemplates(memberId, keyword, categoryId, tagIds, visibility, pageable);
        long nextItemsCount = countNextTemplates(memberId, keyword, categoryId, tagIds, visibility);
        return new PageImpl<>(content, pageable, nextItemsCount);
    }

    private long countNextTemplates(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Visibility visibility
    ) {
        return queryFactory
                .select(template.count())
                .from(template)
                .where(
                        expressionProvider.memberEquals(memberId),
                        expressionProvider.filterCategory(categoryId),
                        expressionProvider.hasAnyTags(tagIds),
                        expressionProvider.filterVisibility(visibility),
                        expressionProvider.matchesKeyword(keyword)
                )
                .fetchOne();
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
                .selectFrom(template)
                .leftJoin(template.category).fetchJoin()
                .leftJoin(template.member).fetchJoin()
                .where(
                        expressionProvider.memberEquals(memberId),
                        expressionProvider.filterCategory(categoryId),
                        expressionProvider.hasAnyTags(tagIds),
                        expressionProvider.filterVisibility(visibility),
                        expressionProvider.matchesKeyword(keyword)
                )
                .orderBy(TemplateOrderSpecifierUtils.getOrderSpecifier(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}

