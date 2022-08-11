package sql.builder;

import additional.dynamicstring.DynamicString;

public interface PrettifyAbleQueryProvider {

    String prettify(final DynamicString sqlQuery);

}
