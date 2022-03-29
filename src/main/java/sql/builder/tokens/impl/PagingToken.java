package sql.builder.tokens.impl;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.statements.impl.PagingTokenStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.TerminationToken;


public class PagingToken extends TerminationToken {

    private final AbstractDynamicString paging;
    private final PagingTokenStatement pagingTokenStatement;

    public PagingToken(AbstractToken firstToken) {
        super(firstToken);
        this.pagingTokenStatement = new PagingTokenStatement(this, firstToken);
        this.paging = new DynamicLinkedString();
    }

    public PagingTokenStatement paging(final int limit, final int offset) {
        this.paging.add(Keyword.LIMIT).add(' ').add(limit).add(Keyword.OFFSET).add(' ').add(offset);
        return pagingTokenStatement;
    }

    public PagingTokenStatement paging(final int limit) {
        this.paging.add(Keyword.LIMIT).add(' ').add(limit);
        return pagingTokenStatement;
    }

    @Override
    public AbstractDynamicString build() {
        return new DynamicLinkedString().add(paging).add(buildNextOrStop());
    }

}
