package sql.builder.statements.impl;

import sql.builder.statements.TerminationStatement;
import sql.builder.tokens.SqlToken;
import sql.builder.tokens.impl.PagingToken;

public class PagingTokenStatement extends TerminationStatement {

    public PagingTokenStatement(SqlToken prevToken, SqlToken firstToken) {
        super(prevToken, firstToken);
    }

    public TerminationStatement paging(final int limit, final int offset){
        PagingToken nextToken = new PagingToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.paging(limit, offset);
    }

}
