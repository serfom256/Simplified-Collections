package sql.builder.statements.impl;

import sql.builder.statements.TerminationStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.impl.JoinExtendedToken;

public class JoinTokenStatement extends TerminationStatement {

    public JoinTokenStatement(AbstractToken prevToken, AbstractToken firstToken) {
        super(prevToken, firstToken);
    }

    public JoinExtendedTokenStatement inner() {
        JoinExtendedToken nextToken = new JoinExtendedToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.inner();
    }

    public JoinExtendedTokenStatement outer() {
        JoinExtendedToken nextToken = new JoinExtendedToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.outer();
    }

    public JoinExtendedTokenStatement right() {
        JoinExtendedToken nextToken = new JoinExtendedToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.right();
    }

    public JoinExtendedTokenStatement left() {
        JoinExtendedToken nextToken = new JoinExtendedToken(getFirstToken());
        getPrevToken().setNextToken(nextToken);
        return nextToken.left();
    }

}
