package sql.builder.tokens.impl;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;
import sql.builder.statements.impl.HavingTokenStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.TerminationToken;

public class HavingToken extends TerminationToken {

    private final AbstractDynamicString predicate;
    private final HavingTokenStatement havingTokenStatement;

    public HavingToken(AbstractToken firstToken) {
        super(firstToken);
        this.havingTokenStatement = new HavingTokenStatement(this, firstToken);
        this.predicate = new DynamicLinkedString();
        setKeyWord(Keyword.HAVING);
    }

    public HavingTokenStatement having(final String e1, final String predicate, final String e2) {
        this.predicate.add(SqlUtil.formatPredicate(e1, predicate, e2));
        return havingTokenStatement;
    }

    @Override
    public AbstractDynamicString build() {
        AbstractDynamicString query = new DynamicLinkedString(getKeyWord().getName()).add(' ');
        return query.add(predicate).add(' ').add(buildNextOrStop());
    }

}
