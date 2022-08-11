package sql.builder.statements;

import additional.dynamicstring.DynamicString;
import sql.builder.tokens.SqlToken;

public abstract class TerminationStatement extends Statement {

    protected TerminationStatement(SqlToken prevToken, SqlToken firstToken) {
        super(prevToken, firstToken);
    }

    public DynamicString build() {
        return getFirstToken().build();
    }

    public String prettify() {
        DynamicString sqlQuery = getFirstToken().build();
        return getFirstToken().getPrettifyAbleQueryProvider().prettify(sqlQuery);
    }

}
