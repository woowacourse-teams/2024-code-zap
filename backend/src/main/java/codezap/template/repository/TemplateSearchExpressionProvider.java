package codezap.template.repository;

import static codezap.template.domain.QTemplate.template;
import static codezap.template.domain.QTemplateTag.templateTag;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

import codezap.template.domain.Visibility;
import codezap.template.repository.strategy.FullTextSearchSearchStrategy;
import codezap.template.repository.strategy.LikeSearchStrategy;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TemplateSearchExpressionProvider {

    private final LikeSearchStrategy likeSearchStrategy;
    private final FullTextSearchSearchStrategy fullTextSearchStrategy;

    private static final int MINIMUM_KEYWORD_LENGTH = 3;

    public BooleanExpression filterMember(Long memberId) {
        return Optional.ofNullable(memberId)
                .map(template.member.id::eq)
                .orElse(null);
    }

    public BooleanExpression filterCategory(Long categoryId) {
        return Optional.ofNullable(categoryId)
                .map(template.category.id::eq)
                .orElse(null);
    }

    public BooleanExpression filterVisibility(Visibility visibility) {
        return Optional.ofNullable(visibility)
                .map(template.visibility::eq)
                .orElse(null);
    }

    public BooleanExpression hasAnyTags(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return null;
        }

        return template.id.in(
                JPAExpressions
                        .select(templateTag.template.id)
                        .from(templateTag)
                        .where(templateTag.tag.id.in(tagIds))
        );
    }

    public BooleanExpression matchesKeyword(String keyword) {
        return Optional.ofNullable(keyword)
                .filter(k -> !k.isBlank())
                .map(String::trim)
                .map(this::createKeywordMatchExpression)
                .orElse(null);
    }

    private BooleanExpression createKeywordMatchExpression(String trimmedKeyword) {
        if (trimmedKeyword.length() < MINIMUM_KEYWORD_LENGTH) {
            return likeSearchStrategy.matchedKeyword(trimmedKeyword);
        }

        return fullTextSearchStrategy.matchedKeyword(trimmedKeyword);
    }
}
