package sql.builder.tokens.impl;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;
import sql.builder.statements.impl.HavingTokenStatement;
import sql.builder.tokens.SqlToken;
import sql.builder.tokens.TerminationToken;

public class HavingToken extends TerminationToken {

    private final DynamicString predicate;
    private final HavingTokenStatement havingTokenStatement;

    public HavingToken(SqlToken firstToken) {
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
    public DynamicString build() {
        DynamicString query = new DynamicLinkedString(getKeyWord().getName()).add(' ');
        return query.add(predicate).add(' ').add(buildNextOrStop());
    }

}
