package sql.builder.tokens;

import additional.dynamicstring.AbstractDynamicString;
import sql.builder.PrettifyAbleQueryProvider;
import sql.builder.SqlDialect;

public abstract class AbstractToken {

    private final AbstractToken firstToken;

    private AbstractToken nextToken;

    private PrettifyAbleQueryProvider prettifyAbleQueryProvider;

    private SqlDialect sqlDialect;

    private Keyword keyWord;

    protected AbstractToken(AbstractToken firstToken) {
        this.firstToken = firstToken;
    }

    public abstract AbstractDynamicString build();

    public AbstractToken getFirstToken() {
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

    protected AbstractToken getNextToken() {
        return nextToken;
    }

    public Keyword getKeyWord() {
        return this.keyWord;
    }

    public void setKeyWord(Keyword keyWord) {
        this.keyWord = keyWord;
    }

    public void setNextToken(AbstractToken nextToken) {
        this.nextToken = nextToken;
    }

}
