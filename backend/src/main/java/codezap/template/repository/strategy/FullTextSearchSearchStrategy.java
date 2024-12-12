package codezap.template.repository.strategy;

import static codezap.template.domain.QSourceCode.sourceCode;
import static codezap.template.domain.QTemplate.template;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;

@Component
public class FullTextSearchSearchStrategy implements SearchStrategy {

    private static final String MATCH_FUNCTION = "function('fulltext_match', {0}, {1}, {2})";
    private static final int FULL_TEXT_FIELD_FIRST_ORDER = 0;
    private static final int FULL_TEXT_FIELD_SECOND_ORDER = 1;
    private static final int FULL_TEXT_KEYWORD_ORDER = 2;
    private static final int NO_MATCHED_SCORE = 0;

    @Override
    public BooleanExpression matchedKeyword(String trimmedKeyword) {
        String parsedKeyword = parseKeyword(trimmedKeyword);
        NumberExpression<Double> titleScore = getMatchedAccuracy(template.title, template.description, parsedKeyword);
        NumberExpression<Double> sourceCodeScore = getMatchedAccuracy(sourceCode.filename, sourceCode.content, parsedKeyword);
        return titleScore.gt(NO_MATCHED_SCORE).or(
                template.id.in(JPAExpressions
                        .select(sourceCode.template.id)
                        .from(sourceCode)
                        .where(sourceCodeScore.gt(NO_MATCHED_SCORE))
                )
        );
    }

    private String parseKeyword(String trimmedKeyword) {
        String[] parsedKeywords = trimmedKeyword.split(" ");
        return Arrays.stream(parsedKeywords)
                .map(keyword -> keyword.replaceAll("[^a-zA-Z0-9가-힣]", ""))
                .filter(keyword -> !keyword.isEmpty())
                .map(keyword -> "+" + keyword)
                .collect(Collectors.joining(" "));
    }

    private NumberExpression<Double> getMatchedAccuracy(Object... args) {
        return Expressions.numberTemplate(Double.class,
                MATCH_FUNCTION,
                args[FULL_TEXT_FIELD_FIRST_ORDER],
                args[FULL_TEXT_FIELD_SECOND_ORDER],
                args[FULL_TEXT_KEYWORD_ORDER]
        );
    }
}
