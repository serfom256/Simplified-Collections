package sql.builder.tokens.impl;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.statements.impl.JoinExtendedTokenStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.TerminationToken;

public class JoinExtendedToken extends TerminationToken {

    private final JoinExtendedTokenStatement joinExtendedTokenStatement;

    public JoinExtendedToken(AbstractToken firstToken) {
        super(firstToken);
        this.joinExtendedTokenStatement = new JoinExtendedTokenStatement(this, firstToken);
    }

    public JoinExtendedTokenStatement inner() {
        setKeyWord(Keyword.INNER);
        return joinExtendedTokenStatement;

    }

    public JoinExtendedTokenStatement outer() {
        setKeyWord(Keyword.OUTER);
        return joinExtendedTokenStatement;
    }

    public JoinExtendedTokenStatement right() {
        setKeyWord(Keyword.RIGHT);
        return joinExtendedTokenStatement;
    }

    public JoinExtendedTokenStatement left() {
        setKeyWord(Keyword.LEFT);
        return joinExtendedTokenStatement;
    }

    @Override
    public AbstractDynamicString build() {
        return new DynamicLinkedString(' ').add(buildNextOrStop());
    }

}
