package sql.builder.tokens.impl;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.statements.impl.PagingTokenStatement;
import sql.builder.tokens.SqlToken;
import sql.builder.tokens.TerminationToken;


public class PagingToken extends TerminationToken {

    private final DynamicString paging;
    private final PagingTokenStatement pagingTokenStatement;

    public PagingToken(SqlToken firstToken) {
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
    public DynamicString build() {
        return new DynamicLinkedString().add(paging).add(buildNextOrStop());
    }

}
