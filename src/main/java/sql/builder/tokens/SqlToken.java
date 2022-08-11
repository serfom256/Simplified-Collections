package sql.builder.tokens;

import additional.dynamicstring.DynamicString;
import sql.builder.PrettifyAbleQueryProvider;
import sql.builder.SqlDialect;

public abstract class SqlToken {

    private final SqlToken firstToken;

    private SqlToken nextToken;

    private PrettifyAbleQueryProvider prettifyAbleQueryProvider;

    private SqlDialect sqlDialect;

    private Keyword keyWord;

    protected SqlToken(SqlToken firstToken) {
        this.firstToken = firstToken;
    }

    public abstract DynamicString build();

    public SqlToken getFirstToken() {
        if (firstToken == null) throw new IllegalStateException("First query argument didn't specified");
        return firstToken;
    }

    public void setPrettifyAbleQueryProvider(final PrettifyAbleQueryProvider prettifyAbleQueryProvider) {
        this.prettifyAbleQueryProvider = prettifyAbleQueryProvider;
    }

    public PrettifyAbleQueryProvider getPrettifyAbleQueryProvider() {
        return prettifyAbleQueryProvider;
    }

    public SqlDialect getSqlDialect() {
        return sqlDialect;
    }

    public void setSqlDialect(final SqlDialect sqlDialect) {
        this.sqlDialect = sqlDialect;
    }

    protected SqlToken getNextToken() {
        return nextToken;
    }

    public Keyword getKeyWord() {
        return this.keyWord;
    }

    public void setKeyWord(Keyword keyWord) {
        this.keyWord = keyWord;
    }

    public void setNextToken(SqlToken nextToken) {
        this.nextToken = nextToken;
    }

}
