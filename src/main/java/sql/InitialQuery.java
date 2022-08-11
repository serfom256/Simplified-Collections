package sql;

import additional.dynamicstring.DynamicString;
import sql.builder.statements.impl.SelectTokenStatement;
import sql.builder.tokens.SqlToken;
import sql.builder.tokens.impl.SelectToken;


public class InitialQuery extends SqlToken {

    protected InitialQuery() {
        super(null);
    }

    public SelectTokenStatement select() {
        SelectToken nextToken = new SelectToken(this);
        setNextToken(nextToken);
        return nextToken.select();
    }

    public SelectTokenStatement select(final String... args) {
        SelectToken nextToken = new SelectToken(this);
        setNextToken(nextToken);
        return nextToken.select(args);
    }

    @Override
    public DynamicString build() {
        return getNextToken().build();
    }

}
