package sql.builder.tokens.impl;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;
import sql.builder.statements.AbstractStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.TerminationToken;


public class LogicalOperationToken<T extends AbstractStatement> extends TerminationToken {

    private final AbstractDynamicString predicates;
    private T tokenStatement;

    public LogicalOperationToken(AbstractToken firstToken) {
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
    public AbstractDynamicString build() {
        if (tokenStatement == null) throw new IllegalStateException("TokenStatement didn't specified");
        AbstractDynamicString query = new DynamicLinkedString(getKeyWord().getName()).add(' ');
        return query.add(predicates).add(' ').add(buildNextOrStop());
    }

}

