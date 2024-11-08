package codezap.template.repository;

import static org.hibernate.type.StandardBasicTypes.BOOLEAN;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

public class FullTextFunctionContributor implements FunctionContributor {

    private static final String FUNCTION_NAME = "fulltext_match";
    private static final String MATCH_AGAINST_FUNCTION = "match(?1, ?2) against(?3)";

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions
                .getFunctionRegistry()
                .registerPattern(FUNCTION_NAME, MATCH_AGAINST_FUNCTION,
                        functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(BOOLEAN));
    }
}
