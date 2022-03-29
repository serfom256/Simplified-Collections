package sql.builder.tokens.impl;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.utils.SqlUtil;
import sql.builder.statements.impl.JoinTokenStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.TerminationToken;

import static sql.builder.tokens.Keyword.INNER;

public class JoinToken extends TerminationToken {

    private final JoinTokenStatement joinTokenStatement;

    private String table;
    private String schema;
    private String expression;

    public JoinToken(AbstractToken firstToken) {
        super(firstToken);
        this.joinTokenStatement = new JoinTokenStatement(this, firstToken);
        setKeyWord(INNER);
    }


    public JoinTokenStatement join(final String schema, final String table, final String on) {
        this.schema = schema;
        this.table = table;
        this.expression = on;
        return joinTokenStatement;
    }

    public JoinTokenStatement join(final String table, final String on) {
        this.table = table;
        this.expression = on;
        return joinTokenStatement;
    }

//    public JoinTokenStatement join(final String table, final String on, final String alias) {
//        this.table = table;
//        this.expression = on;
//        this.alias = alias;
//        return joinTokenFactory;
//    }

    @Override
    public AbstractDynamicString build() {
        AbstractDynamicString query = new DynamicLinkedString();
        String exp = SqlUtil.normalizeExpression(expression, table);
        if (getNextToken() == null) {
            query.add(SqlUtil.formatJoin(getKeyWord(), schema, table, exp));
        } else {
            query.add(SqlUtil.formatJoin(getNextToken().getKeyWord(), schema, table, exp));
        }
        return query.add(buildNextOrStop());
    }

}
