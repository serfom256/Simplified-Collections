package sql.builder.tokens.impl;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;
import sql.builder.statements.impl.OrderByTokenStatement;
import sql.builder.tokens.SqlToken;
import sql.builder.tokens.TerminationToken;


public class OrderByToken extends TerminationToken {

    private final DynamicString columns;
    private final OrderByTokenStatement orderByTokenStatement;

    public OrderByToken(SqlToken firstToken) {
        super(firstToken);
        this.orderByTokenStatement = new OrderByTokenStatement(this, firstToken);
        this.columns = new DynamicLinkedString();
        setKeyWord(Keyword.ORDERBY);
    }

    public OrderByTokenStatement orderBy(final String... columns) {
        for (String col : columns) {
            if (col.equals(Keyword.ASC.toString()) || col.equals(Keyword.DESC.toString())) {
                this.columns.replace(this.columns.getSize() - 2, ' ').add(col).add(", ");
            } else {
                this.columns.add(SqlUtil.formatVariable(col)).add(", ");
            }
        }
        this.columns.replace(this.columns.getSize() - 2, ' ');
        return orderByTokenStatement;
    }

    @Override
    public DynamicString build() {
        DynamicString query = new DynamicLinkedString(getKeyWord().getName()).add(' ');
        return query.add(columns).add(buildNextOrStop());
    }

}
