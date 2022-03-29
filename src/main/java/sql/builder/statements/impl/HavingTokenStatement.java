package sql.builder.statements.impl;

import sql.builder.tokens.AbstractToken;

import sql.builder.tokens.impl.LogicalOperationToken;
import sql.builder.tokens.impl.OrderByToken;

public class HavingTokenStatement extends OrderByTokenStatement {

    public HavingTokenStatement(AbstractToken prevToken, AbstractToken firstToken) {
        super(prevToken, firstToken);
    }

    public OrderByTokenStatement orderBy(final String... columns) {
        OrderByToken nextToken = new OrderByToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.orderBy(columns);
    }

    @Override
    public HavingTokenStatement and(final String e1, final String predicate, final String e2) {
        LogicalOperationToken<HavingTokenStatement> nextToken = new LogicalOperationToken<>(getFirstToken());
        nextToken.setTokenStatement(new HavingTokenStatement(nextToken, getFirstToken()));
        getPrevToken().setNextToken(nextToken);
        return nextToken.and(e1, predicate, e2);
    }

    @Override
    public HavingTokenStatement or(final String e1, final String predicate, final String e2) {
        LogicalOperationToken<HavingTokenStatement> nextToken = new LogicalOperationToken<>(getFirstToken());
        nextToken.setTokenStatement(new HavingTokenStatement(getPrevToken(), getFirstToken()));
        getPrevToken().setNextToken(nextToken);
        return nextToken.or(e1, predicate, e2);
    }

}
