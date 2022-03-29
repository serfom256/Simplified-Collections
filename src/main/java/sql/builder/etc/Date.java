package sql.builder.etc;

import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;

public class Date {

    private Date() {
    }

    public static String date(final String date) {
        return SqlUtil.formatDate(matchDate(date), date);
    }

    public static String date(final String date, final Keyword type) {
        return SqlUtil.formatDate(type, date);
    }

    private static Keyword matchDate(final String date) {
        if (date.length() == 19) return Keyword.DATETIME;
        return Keyword.DATE;
    }

}
