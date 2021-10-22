package Additional.Trie;

import HashSet.Set;
import HashTables.HashTable;

public class TrieMap {

    TNode root;
    int size;
    int entriesCount;

    static class TNode {
        Character element;
        boolean isEnd;
        TNode prev;
        Set<TNode> pairs;
        HashTable<Character, TNode> nodes;
        int size;
        int entriesCount;

        public TNode(Character element) {
            this.element = element;
            this.isEnd = false;
            this.prev = null;
            this.nodes = new HashTable<>();
            this.pairs = new Set<>(4);
            this.size = this.entriesCount = 1;
        }

        public TNode() {
            this.isEnd = false;
            this.prev = null;
            this.nodes = new HashTable<>();
            this.pairs = new Set<>(4);
            this.size = this.entriesCount = 0;
        }

        void removePair(TNode toRemove) {
            pairs.remove(toRemove);
        }

        void remove(Character c) {
            TNode toRemove = nodes.get(c);
            toRemove.prev = null;
            entriesCount -= toRemove.entriesCount;
            size -= toRemove.size;
            nodes.remove(c);
        }

        TNode getSubTrie(Character c) {
            return nodes.get(c);
        }
    }

    public TrieMap() {
        this.root = new TNode();
        this.size = 0;
    }

    public void put(String key, String value) {
        if (key == null || value == null) throw new IllegalArgumentException("Key or Value must be not null");
        TNode curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            TNode next = curr.nodes.get(c);
            if (next == null) {
                size++;
                next = new TNode(c);
                curr.nodes.add(c, next);
            }
            next.prev = curr;
            curr = next;
        }
        if(!curr.isEnd)entriesCount++;
        curr.isEnd = true;
    }

}
