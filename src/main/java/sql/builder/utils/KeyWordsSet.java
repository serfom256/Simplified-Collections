package sql.builder.utils;

import sql.builder.tokens.Keyword;
import tries.tries.SimpleTrie;

public class KeyWordsSet {

    private final SimpleTrie trie;

    public KeyWordsSet() {
        trie = new SimpleTrie();
        for (Keyword kw : Keyword.values()) {
            trie.add(kw.getName());
        }
    }

    public boolean presents(final String keyword) {
        return trie.contains(keyword);
    }

}
