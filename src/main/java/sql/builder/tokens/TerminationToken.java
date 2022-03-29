package sql.builder.tokens;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;

public abstract class TerminationToken extends AbstractToken {

    protected TerminationToken(AbstractToken firstToken) {
        super(firstToken);
    }

    public AbstractDynamicString buildNextOrStop() {
        return getNextToken() == null ? new DynamicLinkedString() : getNextToken().build();
    }

}
