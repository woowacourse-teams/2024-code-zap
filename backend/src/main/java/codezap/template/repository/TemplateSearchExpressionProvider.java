package codezap.template.repository;

import static codezap.template.domain.QSourceCode.sourceCode;
import static codezap.template.domain.QTemplate.template;
import static codezap.template.domain.QTemplateTag.templateTag;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;

import codezap.template.domain.Visibility;

@Component
public class TemplateSearchExpressionProvider {

    private static final String MATCH_FUNCTION = "function('fulltext_match', {0}, {1}, {2})";
    private static final int MINIMUM_KEYWORD_LENGTH = 3;
    private static final int FULL_TEXT_FIELD_FIRST_ORDER = 0;
    private static final int FULL_TEXT_FIELD_SECOND_ORDER = 1;
    private static final int FULL_TEXT_KEYWORD_ORDER = 2;
    private static final int NO_MATCHED_SCORE = 0;
    private static final String WILD_CARD = "%";

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
            return matchedKeywordUsingLike(trimmedKeyword);
        }
        return matchedKeywordUsingFullTextIndex(trimmedKeyword);
    }

    public BooleanExpression matchedKeywordUsingLike(String trimmedKeyword) {
        String likeSearchedKeyword = WILD_CARD + trimmedKeyword + WILD_CARD;
        BooleanExpression titleMatch = template.title.likeIgnoreCase(likeSearchedKeyword);
        BooleanExpression descriptionMatch = template.description.likeIgnoreCase(likeSearchedKeyword);
        BooleanExpression sourceCodeMatch = template.id.in(
                JPAExpressions
                        .select(sourceCode.template.id)
                        .from(sourceCode)
                        .where(sourceCode.content.likeIgnoreCase(likeSearchedKeyword)
                                .or(sourceCode.filename.likeIgnoreCase(likeSearchedKeyword)))
        );
        return titleMatch.or(descriptionMatch).or(sourceCodeMatch);
    }


    public BooleanExpression matchedKeywordUsingFullTextIndex(String trimmedKeyword) {
        NumberExpression<Double> titleScore = getMatchedAccuracy(template.title, template.description, trimmedKeyword);
        NumberExpression<Double> sourceCodeScore = getMatchedAccuracy(sourceCode.filename, sourceCode.content, trimmedKeyword);
        return titleScore.gt(NO_MATCHED_SCORE).or(
                template.id.in(JPAExpressions
                        .select(sourceCode.template.id)
                        .from(sourceCode)
                        .where(sourceCodeScore.gt(NO_MATCHED_SCORE))
                )
        );
    }

    public NumberExpression<Double> getMatchedAccuracy(Object... args) {
        return Expressions.numberTemplate(Double.class,
                MATCH_FUNCTION,
                args[FULL_TEXT_FIELD_FIRST_ORDER],
                args[FULL_TEXT_FIELD_SECOND_ORDER],
                args[FULL_TEXT_KEYWORD_ORDER]
        );
    }
}
