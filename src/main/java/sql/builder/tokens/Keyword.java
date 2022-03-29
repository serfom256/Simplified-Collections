package sql.builder.tokens;

public enum Keyword {

    WHERE("WHERE"), FROM("FROM"), SELECT("SELECT"), DISTINCT("DISTINCT"),
    INNER("INNER JOIN"), OUTER("OUTER JOIN"), LEFT("LEFT JOIN"), RIGHT("RIGHT JOIN"),
    SUM("SUM"), MIN("MIN"), MAX("MAX"), AVG("AVG"), COUNT("COUNT"),
    AND("AND"), OR("OR"), AS("AS"), ASC("ASC"), DESC("DESC"),
    GROUPBY("GROUP BY"), HAVING("HAVING"), ORDERBY("ORDER BY"), LIMIT("LIMIT"), OFFSET("OFFSET"),
    DATE("DATE"), DATETIME("DATETIME"), TIMESTAMP("TIMESTAMP");

    private final String name;

    Keyword(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

}
