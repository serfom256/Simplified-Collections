package sql.builder.utils;

import sql.builder.tokens.Keyword;

import static sql.builder.tokens.Keyword.AS;
import static sql.builder.tokens.Keyword.SELECT;

public class SqlUtil {

    private static String bracketsFormattingPattern = "`%s`";

    private static String predicateFormattingPattern = "%s%s%s";

    private static String aggregationFormattingPattern = "%s(`%s`)";

    private static String dateFormattingPattern = "%s(%s)";

    private static String aliasFormattingPattern = "%s %s `%s`";

    private static String joinFormattingPattern = "%s %s ON %s";

    private static String nestedQueryFormattingPattern = "(%s)";

    private static String tableAndSchemaFormattingPattern = "%s.%s";

    private static final KeyWordsSet kwSet = new KeyWordsSet();

    public static KeyWordsSet getKeywordSet() {
        return kwSet;
    }

    private SqlUtil() {
    }

    public static void setBracketsFormattingPattern(String bracketsFormattingPattern) {
        SqlUtil.bracketsFormattingPattern = bracketsFormattingPattern;
    }

    public static void setPredicateFormattingPattern(String predicateFormattingPattern) {
        SqlUtil.predicateFormattingPattern = predicateFormattingPattern;
    }

    public static void setAggregationFormattingPattern(String aggregationFormattingPattern) {
        SqlUtil.aggregationFormattingPattern = aggregationFormattingPattern;
    }

    public static void setDateFormattingPattern(String dateFormattingPattern) {
        SqlUtil.dateFormattingPattern = dateFormattingPattern;
    }

    public static void setNestedQueryFormattingPattern(String nestedQueryFormattingPattern) {
        SqlUtil.nestedQueryFormattingPattern = nestedQueryFormattingPattern;
    }

    public static void setAliasFormattingPattern(String aliasFormattingPattern) {
        SqlUtil.aliasFormattingPattern = aliasFormattingPattern;
    }

    public static void setJoinFormattingPattern(String joinFormattingPattern) {
        SqlUtil.joinFormattingPattern = joinFormattingPattern;
    }

    public static void setTableAndSchemaFormattingPattern(String tableAndSchemaFormattingPattern) {
        SqlUtil.tableAndSchemaFormattingPattern = tableAndSchemaFormattingPattern;
    }

    public static String formatVariable(final String value) {
        return normalize(value);
    }

    public static String formatAggregationToken(final Keyword tokenName, final String value) {
        return String.format(aggregationFormattingPattern, tokenName.getName(), value);
    }

    public static String formatAlias(final Keyword tokenName, final String variable, final String alias) {
        final String NVariable = normalize(variable);
        final String NAlias = normalize(alias);
        if (NVariable.equals(variable) || NAlias.equals(alias)) {
            return NVariable + " " + tokenName.getName() + " " + NAlias;
        }
        return String.format(aliasFormattingPattern, variable, tokenName.getName(), alias);
    }

    public static String formatDate(final Keyword kw, final String date) {
        return String.format(dateFormattingPattern, kw, date);
    }

    public static String formatTable(final String schema, final String table) {
        if (schema == null) return table;
        return String.format(tableAndSchemaFormattingPattern, schema, table);
    }

    public static String formatJoin(final Keyword joinToken, final String schema, final String table, final String on) {
        final String st = formatTable(schema, table);
        return String.format(joinFormattingPattern, joinToken.getName(), st, on);
    }

    public static String normalizeExpression(final String expression, final String tableName) {
        if (!tableName.contains(AS.getName())) {
            return expression.replace(".", ".`").replace("=", "`=") + '`';
        }
        return "`" + expression.replace(".", "`.`").replace("=", "`=`") + '`';
    }

    public static String formatPredicate(final String e1, final String predicate, final String e2) {
        final boolean b1 = kwSet.presents(e1);
        final boolean b2 = kwSet.presents(e2);
        String normalizedExp1 = b1 && e1.startsWith(Keyword.DATE.getName()) ? String.format(nestedQueryFormattingPattern, e1) : e1;
        String normalizedExp2 = b2 && e1.startsWith(Keyword.DATE.getName()) ? String.format(nestedQueryFormattingPattern, e2) : e2;
        if (b1 || b2) {
            if (b1) normalizedExp1 = normalize(normalizedExp1);
            if (b2) normalizedExp2 = normalize(normalizedExp2);

            return normalizedExp1 + " " + predicate + " " + normalizedExp2;
        }
        return String.format(predicateFormattingPattern, normalize(e1), predicate, normalize(e2));
    }

    private static String normalize(String variable) {
        if (variable.startsWith(SELECT.getName())) return String.format(nestedQueryFormattingPattern, variable);
        if (isNumeric(variable) || kwSet.presents(variable)) return variable;
        if (variable.indexOf("'") == 0 && variable.lastIndexOf("'") == variable.length() - 1) return variable;
        if (variable.indexOf('.') != -1) variable = variable.replace(".", "`.`");
        return String.format(bracketsFormattingPattern, variable);
    }

    private static boolean isNumeric(String variable) {
        return variable.matches("-?\\d+(\\.\\d+)?");
    }

}
