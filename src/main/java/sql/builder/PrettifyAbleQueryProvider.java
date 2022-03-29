package sql.builder;

import additional.dynamicstring.AbstractDynamicString;

public interface PrettifyAbleQueryProvider {

    String prettify(final AbstractDynamicString sqlQuery);

}
