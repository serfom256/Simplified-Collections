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
        this.size = 0;
    }

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

    private TNode getNode(String val) {
        TNode curr = root;
        for (int i = 0; i < val.length(); i++) {
            curr = curr.nodes.get(val.charAt(i));
            if (curr == null) return null;
        }
        return curr;
    }

    public Pair<String, AbstractSet<String>> deleteKey(String key) {
        if (key == null) throw new NullableArgumentException();
        Pair<String, AbstractSet<String>> result;
        TNode keyNode = getNode(key);
        if (keyNode == null || !keyNode.isKey) return new Pair<>();
        result = getPairAsString(keyNode);
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

    public AbstractList<Pair<String, AbstractSet<String>>> get(String value) {
        TNode curr = root;
        for (int i = 0; i < value.length() && curr != null; i++) {
            char c = value.charAt(i);
            curr = curr.nodes.get(c);
        }
        AbstractList<Pair<String, AbstractSet<String>>> result = new ArrayList<>();
        if (curr == null || !curr.isEnd) return result;
        collectAll(result, curr, new Set<>());
        return result;
    }

    public boolean replaceKey(String oldKey, String newKey) {
        if (oldKey == null || newKey == null) throw new NullableArgumentException();
        TNode oldKeyNode = getNode(oldKey);
        if (oldKeyNode == null || !oldKeyNode.isKey) return false;
        TNode newKeyNode = putSequence(oldKey);
        newKeyNode.isKey = true;
        newKeyNode.pairs.addFrom(oldKeyNode.pairs);
        if (oldKeyNode.nodes.getSize() == 0) deleteNode(oldKeyNode);
        else oldKeyNode.isKey = false;
        return true;
    }

    public boolean containsKey(String key) {
        if (key == null) throw new NullableArgumentException();
        TNode keyNode = getNode(key);
        return keyNode != null && keyNode.isKey;
    }

    public boolean containsValue(String value) {
        if (value == null) throw new NullableArgumentException();
        TNode valueNode = getNode(value);
        return valueNode != null && valueNode.isVal;
    }

    public boolean contains(String data) {
        if (data == null) throw new NullableArgumentException();
        TNode valueNode = getNode(data);
        return valueNode != null && valueNode.isEnd;
    }

    public AbstractList<Pair<String, AbstractSet<String>>> lookup(String input, int distance, Verbose verbose) {
        if (input == null) throw new NullableArgumentException();
        if (input.length() <= 1 || input.length() <= distance) {
            throw new IllegalArgumentException("Prefix length must be more then specified distance");
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
                collectAll(founded, curr, memo);
            }
            curr = next;
        }
        if (!curr.isEnd) {
            search(len, curr, input, distance, founded, memo, verbose);
        } else {
            collectAll(founded, curr, memo);
        }
        return founded;
    }

    private void search(int pos, TNode curr, String toSearch, int distance, AbstractList<Pair<String, AbstractSet<String>>> founded, AbstractSet<TNode> memo, Verbose verbose) {
        for (int j = pos; j >= 0; j--) {
            fuzzyCompound(curr, toSearch, j, distance, founded, memo);
            if (verbose != Verbose.MAX && founded.getSize() != 0) break;
            curr = curr.prev;
        }
    }

    /**
     * Provides to collect all data from the specified node with using fuzzy search
     *
     * @param word    word of all collected words
     * @param pos     position in word from which search starts
     * @param typos   maximum count of typos in searched word
     * @param founded set which contains all founded words
     * @param memo    set for memoization
     */
    private void fuzzyCompound(TNode start, String word, int pos, int typos, AbstractList<Pair<String, AbstractSet<String>>> founded, AbstractSet<TNode> memo) {
        if (typos < 0) return;
        if (pos + typos >= word.length()) {
            collectAll(founded, start, memo);
            return;
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

    private void collectAll(AbstractList<Pair<String, AbstractSet<String>>> set, TNode node, AbstractSet<TNode> memo) {
        if (node == null || memo.contains(node)) return;
        memo.add(node);
        if (node.isEnd) collectAll(set, node.prev, memo);
        for (HashNode<Character, TNode> n : node.nodes.items) {
            if (n.getValue().isEnd) {
                Set<String> values = new Set<>(2);
                String key = getReversed(n.getValue());
                Pair<String, AbstractSet<String>> pair = new Pair<>(key, values);
                for (TNode value : n.getValue().pairs) {
                    values.add(getReversed(value));
                }
                set.add(pair);
            }
        }
    }

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

    private Pair<String, AbstractSet<String>> getPairAsString(TNode node) {
        AbstractSet<String> values = new Set<>();
        Pair<String, AbstractSet<String>> pair = new Pair<>(getReversed(node), values);
        for (TNode n : node.pairs) {
            if (n.isVal) values.add(getReversed(n));
        }
        return pair;
    }

    private void toStringHelper(AbstractDynamicString res, TNode node) {
        for (HashNode<Character, TNode> c : node.nodes.items) {
            TNode curr = c.getValue();
            if (curr.isKey) {
                res.add(getPairAsString(c.getValue()).toString()).add(", ");
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

    public static void main(String[] args) {

//        String search = "234a";
//        int cnt = 1000, typos = 3;
        TrieMap map = new TrieMap();
        map.add("1234", "value");
        map.add("4567", "value");
        map.add("qwe", "00000");
        map.add("qwe", "11111111111");
        map.add("test", "00000");
        map.add("abc909", "qwe");
        map.add("12345", "val");
        map.add("12345", "this is 12 value");
        map.add("urge", "ome-random-string");
        map.add("urge", "somerandomstring");

//        System.out.println(map);
//        System.out.println(map.deleteKey("1234"));
//        System.out.println(map.deleteKey("qwe"));
//        System.out.println(map.deleteKey("test"));
//        System.out.println(map.deleteKey("4567"));
//        System.out.println(map.deleteKey("12345"));
//        System.out.println(map.deleteKey("urge"));
//        System.out.println(map.deleteKey("abc909"));

        //    System.out.println(map);
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < cnt; i++) {
//            map.lookup(search, typos, Verbose.MIN);
//        }
//
//        System.out.println(System.currentTimeMillis() - start);
    }

}
