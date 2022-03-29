package sql.builder.etc;

import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;


public class Aggregators {

    private Aggregators() {
    }

    public static String count(final String field) {
        return SqlUtil.formatAggregationToken(Keyword.COUNT, field);
    }

    public static String sum(final String field) {
        return SqlUtil.formatAggregationToken(Keyword.SUM, field);
    }

    public static String min(final String field) {
        return SqlUtil.formatAggregationToken(Keyword.MIN, field);
    }

    public static String max(final String field) {
        return SqlUtil.formatAggregationToken(Keyword.MAX, field);
    }

    public static String avg(final String field) {
        return SqlUtil.formatAggregationToken(Keyword.AVG, field);
    }

}
