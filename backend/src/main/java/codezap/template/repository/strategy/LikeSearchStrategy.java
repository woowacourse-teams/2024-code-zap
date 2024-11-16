package codezap.template.repository.strategy;

import static codezap.template.domain.QSourceCode.sourceCode;
import static codezap.template.domain.QTemplate.template;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

@Component
public class LikeSearchStrategy implements SearchStrategy {

    private static final String WILD_CARD = "%";

    @Override
    public BooleanExpression matchedKeyword(String trimmedKeyword) {
        String likeSearchedKeyword = WILD_CARD + trimmedKeyword + WILD_CARD;

        return createTitleMatch(likeSearchedKeyword)
                .or(createDescriptionMatch(likeSearchedKeyword))
                .or(createSourceCodeMatch(likeSearchedKeyword));
    }

    private BooleanExpression createTitleMatch(String keyword) {
        return template.title.likeIgnoreCase(keyword);
    }

    private BooleanExpression createDescriptionMatch(String keyword) {
        return template.description.likeIgnoreCase(keyword);
    }

    private BooleanExpression createSourceCodeMatch(String keyword) {
        return template.id.in(
                JPAExpressions
                        .select(sourceCode.template.id)
                        .from(sourceCode)
                        .where(sourceCode.content.likeIgnoreCase(keyword)
                                .or(sourceCode.filename.likeIgnoreCase(keyword)))
        );
    }
}
