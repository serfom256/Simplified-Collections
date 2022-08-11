package sql.builder.statements.impl;

import sql.builder.tokens.SqlToken;
import sql.builder.tokens.impl.WhereToken;

public class JoinExtendedTokenStatement extends WhereTokenStatement {

    public JoinExtendedTokenStatement(SqlToken prevToken, SqlToken firstToken) {
        super(prevToken, firstToken);
    }

    public WhereTokenStatement where(final String e1, final String predicate, final String e2) {
        WhereToken nextToken = new WhereToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.where(e1, predicate, e2);
    }

}
