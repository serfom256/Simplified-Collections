package Tries;

import Additional.DynamicString.AbstractDynamicString;
import Additional.DynamicString.DynamicLinkedString;
import HashSet.AbstractSet;
import HashSet.Set;
import HashSet.SortedSet;
import HashTables.HashTable;

public class TrieMap {

    private final TNode root;
    private int size;
    private int entriesCount;

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
            nodes.delete(c);
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
        TNode keyNode = putNode(key);
        TNode valueNode = putNode(value);
        keyNode.pairs.add(valueNode);
        valueNode.pairs.add(keyNode);
    }

    public AbstractSet<String> get(String key) {
        TNode curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            curr = curr.nodes.get(c);
            if (curr == null) break;
        }
        return getPairs(curr);
    }

    private AbstractSet<String> getPairs(TNode node) {
        AbstractSet<String> result = new SortedSet<>();
        if (node == null) return result;
        AbstractDynamicString prefix = new DynamicLinkedString();
        for (TNode c : node.pairs) {
            while (c.prev != null) {
                prefix.add(c.element);
                c = c.prev;
            }
            result.add(prefix.reverse().toString());
            prefix.clear();
        }
        return result;
    }

    private TNode putNode(String value) {
        TNode curr = root;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            TNode next = curr.nodes.get(c);
            if (next == null) {
                size++;
                next = new TNode(c);
                curr.nodes.add(c, next);
            }
            next.prev = curr;
            curr = next;
        }
        if (!curr.isEnd) entriesCount++;
        curr.isEnd = true;
        return curr;
    }

}
