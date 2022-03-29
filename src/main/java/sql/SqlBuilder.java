package sql;


import sql.builder.PrettifyAbleQueryProvider;
import sql.builder.SqlDialect;

public class SqlBuilder {

    private final InitialQuery query;

    public SqlBuilder() {
        query = new InitialQuery();
    }

    public InitialQuery getInstance() {
        return query;
    }

    public InitialQuery create() {
        query.setPrettifyAbleQueryProvider(new DefaultPrettifyAbleQueryProvider());
        return query;
    }

    public void setDialect(SqlDialect dialect) {
        // todo implement sql dialect support
        query.setSqlDialect(null);
    }

    public void setPrettifyAbleQueryProvider(PrettifyAbleQueryProvider prettifyAbleQueryProvider) {
        query.setPrettifyAbleQueryProvider(prettifyAbleQueryProvider);
    }
}
