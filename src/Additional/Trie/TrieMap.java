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
            this.size = this.entriesCount = 1;
        }

        public TNode() {
            this.isEnd = false;
            this.prev = null;
            this.nodes = new HashTable<>();
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

}
