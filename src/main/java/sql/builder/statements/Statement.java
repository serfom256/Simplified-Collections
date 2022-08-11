package sql.builder.statements;

import sql.builder.tokens.SqlToken;

public abstract class Statement {

    private final SqlToken prevToken;
    private final SqlToken firstToken;

    protected Statement(SqlToken prevToken, SqlToken firstToken) {
        this.prevToken = prevToken;
        this.firstToken = firstToken;
    }

    public SqlToken getPrevToken() {
        return prevToken;
    }

    public SqlToken getFirstToken() {
        return firstToken;
    }

}
