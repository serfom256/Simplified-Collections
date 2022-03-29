package sql.builder.statements;

import additional.dynamicstring.AbstractDynamicString;
import sql.builder.tokens.AbstractToken;

public abstract class TerminationStatement extends AbstractStatement {

    protected TerminationStatement(AbstractToken prevToken, AbstractToken firstToken) {
        super(prevToken, firstToken);
    }

    public AbstractDynamicString build() {
        return getFirstToken().build();
    }

    public String prettify() {
        AbstractDynamicString sqlQuery = getFirstToken().build();
        return getFirstToken().getPrettifyAbleQueryProvider().prettify(sqlQuery);
    }

}
