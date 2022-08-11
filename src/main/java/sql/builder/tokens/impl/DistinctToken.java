package sql.builder.tokens.impl;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.statements.impl.DistinctTokenStatement;
import sql.builder.tokens.SqlToken;

public class DistinctToken extends SqlToken {

    private final DistinctTokenStatement distinctTokenStatement;

    public DistinctToken(SqlToken firstToken) {
        super(firstToken);
        this.distinctTokenStatement = new DistinctTokenStatement(this, firstToken);
        setKeyWord(Keyword.DISTINCT);
    }

    public DistinctTokenStatement distinct() {
        return distinctTokenStatement;
    }

    @Override
    public DynamicString build() {
        return new DynamicLinkedString().add(getNextToken().build());
    }
}