package sql.builder.tokens.impl;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;
import sql.builder.statements.impl.GroupByTokenStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.TerminationToken;


public class GroupByToken extends TerminationToken {

    private final AbstractDynamicString columns;
    private final GroupByTokenStatement groupByTokenStatement;

    public GroupByToken(AbstractToken firstToken) {
        super(firstToken);
        this.columns = new DynamicLinkedString();
        this.groupByTokenStatement = new GroupByTokenStatement(this, firstToken);
        setKeyWord(Keyword.GROUPBY);
    }

    public GroupByTokenStatement groupBy(final String... columns) {
        for (String arg : columns) {
            this.columns.add(SqlUtil.formatVariable(arg)).add(", ");
        }
        this.columns.replace(this.columns.getSize() - 2, ' ');
        return groupByTokenStatement;
    }

    @Override
    public AbstractDynamicString build() {
        AbstractDynamicString query = new DynamicLinkedString(getKeyWord().getName()).add(' ');
        return query.add(columns).add(buildNextOrStop());
    }

}
