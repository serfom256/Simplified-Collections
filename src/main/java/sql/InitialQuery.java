package sql;

import additional.dynamicstring.AbstractDynamicString;
import sql.builder.statements.impl.SelectTokenStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.impl.SelectToken;


public class InitialQuery extends AbstractToken {

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
    public AbstractDynamicString build() {
        return getNextToken().build();
    }

}
