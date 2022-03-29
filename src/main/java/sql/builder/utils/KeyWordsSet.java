package sql.builder.utils;

import sql.builder.tokens.Keyword;
import tries.Trie;

public class KeyWordsSet {

    private final Trie trie;

    public KeyWordsSet() {
        trie = new Trie();
        for (Keyword kw : Keyword.values()) {
            trie.add(kw.getName());
        }
    }

    public boolean presents(final String keyword) {
        return trie.contains(keyword);
    }

}
