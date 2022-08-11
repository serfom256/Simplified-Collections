package sql.builder.tokens.impl;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;
import sql.builder.statements.Statement;
import sql.builder.tokens.SqlToken;
import sql.builder.tokens.TerminationToken;


public class LogicalOperationToken<T extends Statement> extends TerminationToken {

    private final DynamicString predicates;
    private T tokenStatement;

    public LogicalOperationToken(SqlToken firstToken) {
        super(firstToken);
        this.predicates = new DynamicLinkedString();
    }

    public void setTokenStatement(T tokenStatement) {
        this.tokenStatement = tokenStatement;
    }

    public T and(final String e1, final String predicate, final String e2) {
        this.predicates.add(SqlUtil.formatPredicate(e1, predicate, e2));
        setKeyWord(Keyword.AND);
        return tokenStatement;
    }

    public T or(final String e1, final String predicate, final String e2) {
        this.predicates.add(SqlUtil.formatPredicate(e1, predicate, e2));
        setKeyWord(Keyword.OR);
        return tokenStatement;
    }

    @Override
    public DynamicString build() {
        if (tokenStatement == null) throw new IllegalStateException("TokenStatement didn't specified");
        DynamicString query = new DynamicLinkedString(getKeyWord().getName()).add(' ');
        return query.add(predicates).add(' ').add(buildNextOrStop());
    }

}

