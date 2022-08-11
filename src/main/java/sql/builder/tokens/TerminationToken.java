package sql.builder.tokens;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;

public abstract class TerminationToken extends SqlToken {

    protected TerminationToken(SqlToken firstToken) {
        super(firstToken);
    }

    public DynamicString buildNextOrStop() {
        return getNextToken() == null ? new DynamicLinkedString() : getNextToken().build();
    }

}
