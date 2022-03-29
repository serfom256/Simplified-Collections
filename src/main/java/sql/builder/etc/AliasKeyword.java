package sql.builder.etc;

import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;

public class AliasKeyword {

    private AliasKeyword() {
    }

    public static String as(final String variable, final String alias) {
        return SqlUtil.formatAlias(Keyword.AS, variable, alias);
    }

}
