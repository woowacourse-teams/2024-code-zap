package codezap.template.repository;

import java.util.List;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.query.sqm.produce.function.StandardArgumentsValidators;
import org.hibernate.sql.ast.SqlAstNodeRenderingMode;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;

public class FullTextSearchMySQLDialect extends MySQLDialect {

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);

        SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();
        functionRegistry.register("match", ExactPhraseMatchFunction.INSTANCE);
    }

    public static class ExactPhraseMatchFunction extends NamedSqmFunctionDescriptor {

        public static final ExactPhraseMatchFunction INSTANCE = new ExactPhraseMatchFunction();

        public ExactPhraseMatchFunction() {
            super("MATCH", false, StandardArgumentsValidators.exactly(3), null);
        }

        @Override
        public void render(
                SqlAppender sqlAppender,
                List<? extends SqlAstNode> arguments,
                ReturnableType<?> returnType,
                SqlAstTranslator<?> translator
        ) {
            sqlAppender.appendSql("MATCH(");
            translator.render(arguments.get(0), SqlAstNodeRenderingMode.DEFAULT);
            sqlAppender.appendSql(", ");
            translator.render(arguments.get(1), SqlAstNodeRenderingMode.DEFAULT);
            sqlAppender.appendSql(") AGAINST (");
            translator.render(arguments.get(2), SqlAstNodeRenderingMode.DEFAULT);
            sqlAppender.appendSql(" IN NATURAL LANGUAGE MODE)");

        }
    }
}
