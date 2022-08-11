package sql.builder.statements.impl;

import sql.builder.tokens.SqlToken;

import sql.builder.tokens.impl.GroupByToken;
import sql.builder.tokens.impl.LogicalOperationToken;


public class WhereTokenStatement extends GroupByTokenStatement {

    public WhereTokenStatement(SqlToken prevToken, SqlToken firstToken) {
        super(prevToken, firstToken);
    }

    public GroupByTokenStatement groupBy(final String... columns) {
        GroupByToken nextToken = new GroupByToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.groupBy(columns);
    }

    @Override
    public WhereTokenStatement and(final String e1, final String predicate, final String e2) {
        LogicalOperationToken<WhereTokenStatement> nextToken = new LogicalOperationToken<>(getFirstToken());
        nextToken.setTokenStatement(new WhereTokenStatement(nextToken, getFirstToken()));
        getPrevToken().setNextToken(nextToken);
        return nextToken.and(e1, predicate, e2);
    }

    @Override
    public WhereTokenStatement or(final String e1, final String predicate, final String e2) {
        LogicalOperationToken<WhereTokenStatement> nextToken = new LogicalOperationToken<>(getFirstToken());
        nextToken.setTokenStatement(new WhereTokenStatement(nextToken, getFirstToken()));
        getPrevToken().setNextToken(nextToken);
        return nextToken.or(e1, predicate, e2);
    }

}
