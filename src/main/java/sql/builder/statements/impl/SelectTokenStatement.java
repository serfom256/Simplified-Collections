package sql.builder.statements.impl;

import sql.builder.statements.AbstractStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.impl.DistinctToken;
import sql.builder.tokens.impl.FromToken;

public class SelectTokenStatement extends AbstractStatement {

    public SelectTokenStatement(AbstractToken prevToken, AbstractToken firstToken) {
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

    public FromTokenStatement from(final AbstractToken nestedQuery) {
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
