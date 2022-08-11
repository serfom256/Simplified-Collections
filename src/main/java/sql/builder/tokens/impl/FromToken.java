package sql.builder.tokens.impl;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.tokens.Keyword;
import sql.builder.statements.impl.FromTokenStatement;
import sql.builder.tokens.SqlToken;

public class FromToken extends SqlToken {

    private String table;
    private String schema;
    private String nestedQuery;

    private final FromTokenStatement fromTokenStatement;

    public FromToken(SqlToken firstToken) {
        super(firstToken);
        this.fromTokenStatement = new FromTokenStatement(this, firstToken);
        setKeyWord(Keyword.FROM);
    }

    public FromTokenStatement from(final String table) {
        this.table = table;
        return fromTokenStatement;
    }

    public FromTokenStatement from(final String schema, final String table) {
        this.table = table;
        this.schema = schema;
        return fromTokenStatement;
    }

    public FromTokenStatement from(final SqlToken nestedQuery) {
        FromToken nextToken = new FromToken(this);
        this.nestedQuery = nestedQuery.build().toString();
        setNextToken(nextToken);
        return fromTokenStatement;
    }

    @Override
    public DynamicString build() {
        DynamicString query = new DynamicLinkedString(getKeyWord().getName()).add(' ');
        if (nestedQuery != null) {
            return query.add('(').add(nestedQuery).deleteLast().add(')').add(' ').add(getNextToken().build());
        }
        if (schema != null) query.add(schema).add('.');
        return query.add(table).add(' ').add(getNextToken().build());
    }

}
