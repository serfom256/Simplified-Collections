package sql.builder.statements.impl;

import sql.builder.statements.Statement;
import sql.builder.tokens.SqlToken;
import sql.builder.tokens.impl.DistinctToken;
import sql.builder.tokens.impl.FromToken;

public class SelectTokenStatement extends Statement {

    public SelectTokenStatement(SqlToken prevToken, SqlToken firstToken) {
        super(prevToken, firstToken);
    }

    public FromTokenStatement from(final String table) {
        FromToken nextToken = new FromToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.from(table);
    }

    public FromTokenStatement from(final String schema, final String table) {
        FromToken nextToken = new FromToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.from(schema, table);
    }

    public FromTokenStatement from(final SqlToken nestedQuery) {
        FromToken nextToken = new FromToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.from(nestedQuery);
    }

    public DistinctTokenStatement distinct() {
        DistinctToken nextToken = new DistinctToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.distinct();
    }

}
