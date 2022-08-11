package sql.builder.statements.impl;

import sql.builder.tokens.SqlToken;
import sql.builder.tokens.impl.HavingToken;
import sql.builder.tokens.impl.LogicalOperationToken;

public class GroupByTokenStatement extends HavingTokenStatement {

    public GroupByTokenStatement(SqlToken prevToken, SqlToken firstToken) {
        super(prevToken, firstToken);
    }

    public HavingTokenStatement having(final String e1, final String predicate, final String e2) {
        HavingToken nextToken = new HavingToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.having(e1, predicate, e2);
    }

    @Override
    public GroupByTokenStatement and(final String e1, final String predicate, final String e2) {
        LogicalOperationToken<GroupByTokenStatement> nextToken = new LogicalOperationToken<>(getFirstToken());
        nextToken.setTokenStatement(new GroupByTokenStatement(nextToken, getFirstToken()));
        getPrevToken().setNextToken(nextToken);
        return nextToken.and(e1, predicate, e2);
    }

    @Override
    public GroupByTokenStatement or(final String e1, final String predicate, final String e2) {
        LogicalOperationToken<GroupByTokenStatement> nextToken = new LogicalOperationToken<>(getFirstToken());
        nextToken.setTokenStatement(new GroupByTokenStatement(nextToken, getFirstToken()));
        getPrevToken().setNextToken(nextToken);
        return nextToken.or(e1, predicate, e2);
    }

}