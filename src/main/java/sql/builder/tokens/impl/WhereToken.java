package sql.builder.tokens.impl;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;
import sql.builder.statements.impl.WhereTokenStatement;
import sql.builder.tokens.SqlToken;
import sql.builder.tokens.TerminationToken;

public class WhereToken extends TerminationToken {

    private final DynamicString predicate;
    private final WhereTokenStatement whereTokenStatement;

    public WhereToken(SqlToken firstToken) {
        super(firstToken);
        this.whereTokenStatement = new WhereTokenStatement(this, firstToken);
        this.predicate = new DynamicLinkedString();
        setKeyWord(Keyword.WHERE);
    }

    public WhereTokenStatement where(final String e1, final String predicate, final String e2) {
        this.predicate.add(SqlUtil.formatPredicate(e1, predicate, e2));
        return whereTokenStatement;
    }

    @Override
    public DynamicString build() {
        DynamicString query = new DynamicLinkedString(getKeyWord().getName()).add(' ');
        return query.add(predicate).add(' ').add(buildNextOrStop());
    }

}
