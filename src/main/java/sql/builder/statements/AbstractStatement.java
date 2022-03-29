package sql.builder.statements;

import sql.builder.tokens.AbstractToken;

public abstract class AbstractStatement {

    private final AbstractToken prevToken;
    private final AbstractToken firstToken;

    protected AbstractStatement(AbstractToken prevToken, AbstractToken firstToken) {
        this.prevToken = prevToken;
        this.firstToken = firstToken;
    }

    public AbstractToken getPrevToken() {
        return prevToken;
    }

    public AbstractToken getFirstToken() {
        return firstToken;
    }

}
