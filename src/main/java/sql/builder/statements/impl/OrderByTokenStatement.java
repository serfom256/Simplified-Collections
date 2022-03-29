package sql.builder.statements.impl;

import sql.builder.statements.TerminationStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.impl.PagingToken;
import sql.builder.tokens.impl.LogicalOperationToken;

public class OrderByTokenStatement extends TerminationStatement {

    public OrderByTokenStatement(AbstractToken prevToken, AbstractToken firstToken) {
        super(prevToken, firstToken);
    }

    public PagingTokenStatement paging(final int limit, final int offset) {
        PagingToken nextToken = new PagingToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.paging(limit, offset);
    }

    public PagingTokenStatement paging(final int limit) {
        PagingToken nextToken = new PagingToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.paging(limit);
    }

    public OrderByTokenStatement and(final String e1, final String predicate, final String e2) {
        LogicalOperationToken<OrderByTokenStatement> nextToken = new LogicalOperationToken<>(getFirstToken());
        nextToken.setTokenStatement(new OrderByTokenStatement(nextToken, getFirstToken()));
        getPrevToken().setNextToken(nextToken);
        return nextToken.and(e1, predicate, e2);
    }

    public OrderByTokenStatement or(final String e1, final String predicate, final String e2) {
        LogicalOperationToken<OrderByTokenStatement> nextToken = new LogicalOperationToken<>(getFirstToken());
        nextToken.setTokenStatement(new OrderByTokenStatement(nextToken, getFirstToken()));
        getPrevToken().setNextToken(nextToken);
        return nextToken.or(e1, predicate, e2);
    }

}
