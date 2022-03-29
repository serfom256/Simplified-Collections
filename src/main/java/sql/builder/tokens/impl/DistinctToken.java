package sql.builder.tokens.impl;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.statements.impl.DistinctTokenStatement;
import sql.builder.tokens.AbstractToken;

public class DistinctToken extends AbstractToken {

    private final DistinctTokenStatement distinctTokenStatement;

    public DistinctToken(AbstractToken firstToken) {
        super(firstToken);
        this.distinctTokenStatement = new DistinctTokenStatement(this, firstToken);
        setKeyWord(Keyword.DISTINCT);
    }

    public DistinctTokenStatement distinct() {
        return distinctTokenStatement;
    }

    @Override
    public AbstractDynamicString build() {
        return new DynamicLinkedString().add(getNextToken().build());
    }
}