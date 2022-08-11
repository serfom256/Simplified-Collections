package sql.builder.statements.impl;

import sql.builder.tokens.SqlToken;
import sql.builder.tokens.impl.JoinToken;
import sql.builder.tokens.impl.WhereToken;

public class FromTokenStatement extends WhereTokenStatement {

    public FromTokenStatement(SqlToken prevToken, SqlToken firstToken) {
        super(prevToken, firstToken);
    }

    public WhereTokenStatement where(final String e1, final String predicate, final String e2) {
        WhereToken nextToken = new WhereToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.where(e1, predicate, e2);
    }

    public JoinTokenStatement join(final String table, final String on) {
        JoinToken nextToken = new JoinToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.join(table, on);
    }

    public JoinTokenStatement join(final String schema, final String table, final String on) {
        JoinToken nextToken = new JoinToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.join(schema, table, on);
    }

}
