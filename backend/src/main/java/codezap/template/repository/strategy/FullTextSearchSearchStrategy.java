package codezap.template.repository.strategy;//package codezap.template.repository.strategy;

import static codezap.template.domain.QTemplate.template;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;

import codezap.template.domain.QSourceCode;
import codezap.template.domain.QTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FullTextSearchSearchStrategy implements SearchStrategy {

    private static final String MATCH_FUNCTION = "function('fulltext_match', {0}, {1}, {2})";
    private static final int FULL_TEXT_FIELD_FIRST_ORDER = 0;
    private static final int FULL_TEXT_FIELD_SECOND_ORDER = 1;
    private static final int FULL_TEXT_KEYWORD_ORDER = 2;
    private static final int NO_MATCHED_SCORE = 0;
    private static final Pattern INVALID_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9가-힣]");

    @Override
    public BooleanExpression matchedKeyword(String trimmedKeyword) {
        String parsedKeyword = parseKeyword(trimmedKeyword);

        NumberExpression<Double> titleScore = getMatchedAccuracy(template.title, template.description, parsedKeyword);

        QTemplate templateAlias = QTemplate.template;
        QSourceCode sourceCodeAlias = new QSourceCode("sourceCodeAlias");

        return titleScore.gt(NO_MATCHED_SCORE).or(
                template.id.in(JPAExpressions
                                .select(templateAlias.id)
                                .from(templateAlias)
                                .join(templateAlias.sourceCodes, sourceCodeAlias)
                                .where(getMatchedAccuracy(sourceCodeAlias.filename, sourceCodeAlias.content,
                                        parsedKeyword).gt(NO_MATCHED_SCORE))
                )
        );
    }

    private String parseKeyword(String trimmedKeyword) {
        return Arrays.stream(trimmedKeyword.split(" "))
                .map(keyword -> INVALID_CHAR_PATTERN.matcher(keyword).replaceAll(""))
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
