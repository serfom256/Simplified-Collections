package sql.builder.tokens.impl;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import sql.builder.statements.impl.SelectTokenStatement;
import sql.builder.tokens.AbstractToken;
import sql.builder.tokens.Keyword;
import sql.builder.utils.SqlUtil;

public class SelectToken extends AbstractToken {

    private final AbstractDynamicString args;

    private final SelectTokenStatement selectTokenStatement;

    public SelectToken(AbstractToken firstToken) {
        super(firstToken);
        this.args = new DynamicLinkedString();
        this.selectTokenStatement = new SelectTokenStatement(this, firstToken);
        setKeyWord(Keyword.SELECT);
    }

    public SelectTokenStatement select() {
        args.add(" * ");
        return selectTokenStatement;
    }

    public SelectTokenStatement select(final String... args) {
        for (String arg : args) {
            if (SqlUtil.getKeywordSet().presents(arg)) {
                this.args.add(arg).add(", ");
            } else {
                this.args.add(SqlUtil.formatVariable(arg)).add(", ");
            }
        }
        this.args.replace(this.args.getSize() - 2, ' ');
        return selectTokenStatement;
    }

    @Override
    public AbstractDynamicString build() {
        AbstractDynamicString query = new DynamicLinkedString(getKeyWord().getName());
        if (getNextToken().getKeyWord() == Keyword.DISTINCT) {
            query.add(' ').add(getNextToken().getKeyWord());
        }
        return query.add(' ').add(args).add(getNextToken().build());
    }

}
