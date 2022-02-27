package tries;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.NullableArgumentException;
import additional.nodes.HashNode;
import additional.nodes.Pair;
import hashtables.HashTable;
import lists.AbstractList;
import lists.impl.ArrayList;
import sets.AbstractSet;
import sets.Set;

public class TrieMap {

    public enum Verbose {
        MAX,
        MIN
    }

    private final TNode root;
    private int size;
    private int pairsCount;

    private static class TNode {
        private Character element;
        private boolean isEnd;
        private boolean isKey;
        private boolean isVal;
        private TNode prev;
        private final HashTable<Character, TNode> nodes;
        private final Set<TNode> pairs;

        public TNode(Character element, TNode prev) {
            this.element = element;
            this.prev = prev;
            this.isKey = this.isVal = this.isEnd = false;
            this.nodes = new HashTable<>(4);
            this.pairs = new Set<>(4);
        }

        public TNode() {
            this.isKey = this.isVal = this.isEnd = false;
            this.nodes = new HashTable<>();
            this.pairs = new Set<>(4);
            this.prev = null;
        }
    }

    public TrieMap() {
        this.root = new TNode();
        this.root.nodes.setCapacity(32);
        this.size = 0;
    }

    /**
     * Appends the new key-values pair to the TrieMap of the specified key and the specified value
     *
     * @throws NullableArgumentException if the specified key or value is null
     * @throws IllegalArgumentException  if the length of the specified key is equals 0
     */
    public void add(String key, String value) {
        if (key == null || value == null) throw new NullableArgumentException();
        if (key.length() == 0) throw new IllegalArgumentException();
        TNode keyNode = putSequence(key);
        if (value.length() != 0) {
            TNode valueNode = putSequence(value);
            keyNode.pairs.add(valueNode);
            valueNode.pairs.add(keyNode);
            if (valueNode.isKey) valueNode.isVal = true;
            valueNode.isEnd = valueNode.isVal = true;
        }
        if (!keyNode.isKey) pairsCount++;
        keyNode.isEnd = keyNode.isKey = true;
    }

    /**
     * Creates branch from all characters of the specified sequence
     *
     * @return end node of branch
     */
    private TNode putSequence(String sequence) {
        TNode curr = root;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            TNode next = curr.nodes.get(c);
            if (next == null) {
                size++;
                next = new TNode(c, curr);
                curr.nodes.add(c, next);
            }
            curr = next;
        }
        return curr;
    }

    /**
     * Returns node by the specified value
     *
     * @return end node for the specified val if founded otherwise null
     */
    private TNode getNode(String val) {
        TNode curr = root;
        for (int i = 0; i < val.length(); i++) {
            curr = curr.nodes.get(val.charAt(i));
            if (curr == null) return null;
        }
        return curr;
    }

    /**
     * Removes the specified key and all associated with the specified key values
     *
     * @param key key to remove
     * @return true if removed otherwise false
     * @throws NullableArgumentException if the specified key is null
     */
    public Pair<String, AbstractSet<String>> deleteKey(String key) {
        if (key == null) throw new NullableArgumentException();
        Pair<String, AbstractSet<String>> result;
        TNode keyNode = getNode(key);
        if (keyNode == null || !keyNode.isKey) return new Pair<>();
        result = getPair(keyNode);
        deleteValuesFor(keyNode);
        pairsCount--;
        keyNode.isKey = false;
        if (isSelfLinked(keyNode)) deleteNode(keyNode);
        if (keyNode.nodes.getSize() == 0 && !keyNode.isVal) deleteNode(keyNode);
        else if (!keyNode.isVal) keyNode.isEnd = false;
        return result;
    }

    private boolean isSelfLinked(TNode node) {
        for (TNode n : node.pairs) {
            if (node == n) return true;
        }
        return false;
    }

    /**
     * Removes the specified value from the TrieMap
     *
     * @param value value to remove
     * @return true if removed otherwise false
     * @throws NullableArgumentException if the specified value is null
     */
    public boolean deleteValue(String value) {
        if (value == null) throw new NullableArgumentException();
        TNode valueNode = getNode(value);
        if (valueNode == null || !valueNode.isVal) return false;
        for (TNode keyNode : valueNode.pairs) {
            keyNode.pairs.delete(valueNode);
        }
        if (valueNode.nodes.getSize() == 0 && !valueNode.isKey) deleteNode(valueNode);
        else if (valueNode.nodes.getSize() == 0) valueNode.isEnd = false;
        valueNode.isVal = false;
        return true;
    }

    /**
     * Unlinks all value nodes from the specified node
     */
    private void deleteValuesFor(TNode keyNode) {
        for (TNode value : keyNode.pairs) {
            if (value.isKey) continue;
            if (value.pairs.getSize() <= 1) {
                if (value.nodes.getSize() == 0) deleteNode(value);
                else value.isVal = false;
            }
            keyNode.pairs.delete(value);
            value.pairs.delete(keyNode);

        }
    }

    /**
     * Removes all nodes from the specified node down to the next end node
     */
    private void deleteNode(TNode valueNode) {
        TNode node = valueNode;
        TNode prev = valueNode;
        int pos = 0;
        valueNode.isEnd = false;
        while (node != root && !node.isEnd && node.nodes.getSize() <= 1) {
            pos++;
            prev = node;
            node = node.prev;
        }
        prev.prev = null;
        node.nodes.delete(prev.element);
        size -= pos;
    }

    /**
     * Searches pair by the specified key
     *
     * @return key-values pair if founded otherwise empty pair
     * @throws NullableArgumentException if the specified key is null
     */
    public Pair<String, AbstractSet<String>> get(String key) {
        if (key == null) throw new NullableArgumentException();
        TNode curr = getNode(key);
        if (curr == null || !curr.isKey) return new Pair<>();
        return getPair(curr);
    }

    /**
     * Returns true if the TrieMap contains the specified key
     *
     * @throws NullableArgumentException if the specified key is null
     */
    public boolean containsKey(String key) {
        if (key == null) throw new NullableArgumentException();
        TNode keyNode = getNode(key);
        return keyNode != null && keyNode.isKey;
    }

    /**
     * Returns true if the TrieMap contains the specified value
     *
     * @throws NullableArgumentException if the specified value is null
     */
    public boolean containsValue(String value) {
        if (value == null) throw new NullableArgumentException();
        TNode valueNode = getNode(value);
        return valueNode != null && valueNode.isVal;
    }

    /**
     * Returns true if the TrieMap contains the specified string as key or value
     *
     * @throws NullableArgumentException if the specified value is null
     */
    public boolean contains(String data) {
        if (data == null) throw new NullableArgumentException();
        TNode valueNode = getNode(data);
        return valueNode != null && valueNode.isEnd;
    }

    /**
     * @param input    string apply to search
     * @param distance maximum number of typos in the searched string
     * @param verbose  range of founded results
     * @return list of founded pairs
     * @throws NullableArgumentException if the specified key is null
     * @throws IllegalArgumentException  if the specified input length less then specified distance
     */
    public AbstractList<Pair<String, AbstractSet<String>>> lookup(String input, int distance, Verbose verbose) {
        if (input == null) throw new NullableArgumentException();
        if (input.length() <= 1 || input.length() <= distance) {
            throw new IllegalArgumentException("Input length must be more then specified distance");
        }
        AbstractList<Pair<String, AbstractSet<String>>> founded = new ArrayList<>();
        AbstractSet<TNode> memo = new Set<>(4);
        TNode curr = root;
        int len = input.length();
        for (int i = 0; i < len; i++) {
            char c = input.charAt(i);
            TNode next = curr.nodes.get(c);
            if (next == null) {
                search(i, curr, input, distance, founded, memo, verbose);
                return founded;
            }
            if (i == len - 1 && next.isEnd && i + distance >= len) {
                memo.add(curr);
                collectAll(founded, curr);
            }
            curr = next;
        }
        if (!curr.isEnd) {
            search(len, curr, input, distance, founded, memo, verbose);
        } else if (!memo.contains(curr)) {
            collectAll(founded, curr);
        }
        return founded;
    }

    /**
     * @param pos      initial search position
     * @param curr     initial node
     * @param toSearch string apply to search
     * @param distance number of typos
     * @param founded  list for collecting founded results
     * @param memo     set for memoization
     * @param verbose  node traverse range  MIN - only for current node MAX - for all nodes from current down to root node
     */
    private void search(int pos, TNode curr, String toSearch, int distance, AbstractList<Pair<String, AbstractSet<String>>> founded, AbstractSet<TNode> memo, Verbose verbose) {
        for (int j = pos; j >= 0; j--) {
            fuzzyCompound(curr, toSearch, j, distance, founded, memo);
            if (verbose != Verbose.MAX && founded.getSize() != 0) break;
            curr = curr.prev;
        }
    }

    /**
     * Collects all data from the specified node with using fuzzy search considering the count of typos
     *
     * @param word    word of all collected words
     * @param pos     position in word from which fuzzy search starts
     * @param typos   maximum count of typos in searched word
     * @param founded list of founded pairs
     * @param memo    set for memoization
     */
    private void fuzzyCompound(TNode start, String word, int pos, int typos, AbstractList<Pair<String, AbstractSet<String>>> founded, AbstractSet<TNode> memo) {
        if (typos < 0) return;
        if (pos + typos >= word.length() && start.prev != null && !memo.contains(start.prev)) {
            memo.add(start.prev);
            collectAll(founded, start.prev);
        }
        for (Character k : start.nodes) {
            TNode v = start.nodes.get(k);
            if (pos < word.length() && k.equals(word.charAt(pos))) {
                fuzzyCompound(v, word, pos + 1, typos, founded, memo);
            } else {
                fuzzyCompound(v, word, pos + 1, typos - 1, founded, memo);
                fuzzyCompound(v, word, pos, typos - 1, founded, memo);
                fuzzyCompound(start, word, pos + 1, typos - 1, founded, memo);
            }
        }
    }

    /**
     * Collect all key-values pairs from the specified node
     *
     * @param set result
     */
    private void collectAll(AbstractList<Pair<String, AbstractSet<String>>> set, TNode node) {
        for (HashNode<Character, TNode> n : node.nodes.items) {
            if (n.getValue().isEnd) {
                Set<String> values = new Set<>(2);
                Pair<String, AbstractSet<String>> pair = new Pair<>();
                String v = getReversed(n.getValue());
                if (n.getValue().isKey) pair.setKey(v);
                else values.add(v);
                for (TNode val : n.getValue().pairs) {
                    if (val.isKey) pair.setKey(getReversed(val));
                    else values.add(getReversed(val));
                }
                pair.setValue(values);
                set.add(pair);
            }
        }
    }

    /**
     * Returns String which contains characters from the specified node to the TrieMap root
     */
    private String getReversed(TNode node) {
        AbstractDynamicString prefix = new DynamicLinkedString();
        while (node != root) {
            prefix.addFirst(node.element);
            node = node.prev;
        }
        return prefix.toString();
    }

    public int getPairsCount() {
        return this.pairsCount;
    }

    public int getSize() {
        return size;
    }


    public void clear() {
        this.size = this.pairsCount = 0;
        this.root.nodes.clear();
    }

    /**
     * Returns pair of the key and values which linked to the specified node
     */
    private Pair<String, AbstractSet<String>> getPair(TNode node) {
        AbstractSet<String> values = new Set<>();
        Pair<String, AbstractSet<String>> pair = new Pair<>(getReversed(node), values);
        for (TNode n : node.pairs) {
            if (n.isVal) values.add(getReversed(n));
        }
        return pair;
    }

    /**
     * Helps to print all entries from the TrieMap
     */
    private void toStringHelper(AbstractDynamicString res, TNode node) {
        for (HashNode<Character, TNode> c : node.nodes.items) {
            TNode curr = c.getValue();
            if (curr.isKey) {
                res.add(getPair(c.getValue()).toString()).add(", ");
            }
            toStringHelper(res, curr);
        }
    }

    @Override
    public String toString() {
        if (pairsCount == 0) return "[]";
        AbstractDynamicString s = new DynamicLinkedString('[');
        toStringHelper(s, root);
        return s.replace(s.getSize() - 2, ']').toString();
    }
}