package tries.tries;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.NullableArgumentException;
import additional.nodes.HashNode;
import additional.nodes.Pair;
import hashtables.HashTable;
import lists.List;
import lists.impl.ArrayList;
import sets.Set;
import sets.HashedSet;
import stack.Stack;
import stack.LinkedStack;
import tries.TrieMap;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleTrieMap implements TrieMap<String, String>, Iterable<Pair<String, List<String>>> {

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
        private final List<TNode> pairs;

        public TNode(Character element, TNode prev) {
            this.element = element;
            this.prev = prev;
            this.isKey = this.isVal = this.isEnd = false;
            this.nodes = new HashTable<>(4);
            this.pairs = new ArrayList<>(4);
        }

        public TNode() {
            this.isKey = this.isVal = this.isEnd = false;
            this.nodes = new HashTable<>();
            this.pairs = new ArrayList<>(4);
            this.prev = null;
        }
    }

    public SimpleTrieMap() {
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
            keyNode.pairs.addIfAbsent(valueNode);
            valueNode.pairs.addIfAbsent(keyNode);
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
    public Pair<String, List<String>> deleteKey(String key) {
        if (key == null) throw new NullableArgumentException();
        TNode keyNode = getNode(key);
        if (keyNode == null || !keyNode.isKey) return new Pair<>();
        Pair<String, List<String>> result = getPair(keyNode);
        deleteValuesFor(keyNode);
        pairsCount--;
        keyNode.isKey = false;
        if (isSelfLinked(keyNode) || keyNode.nodes.getSize() == 0 && !keyNode.isVal) deleteNode(keyNode);
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
        int pos = 0;
        while (pos < keyNode.pairs.getSize()) {
            TNode value = keyNode.pairs.get(pos);
            if (value.isKey) {
                pos++;
                continue;
            }
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
    public Pair<String, List<String>> get(String key) {
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
    public boolean contains(String key) {
        if (key == null) throw new NullableArgumentException();
        TNode valueNode = getNode(key);
        return valueNode != null && valueNode.isEnd;
    }

    /**
     * @param input    string apply to search
     * @param distance maximum number of typos in the searched string
     * @param verbose  range of founded results
     * @return list of founded pairs
     * @throws NullableArgumentException if the specified key is null
     * @throws IllegalArgumentException  if the specified input length less than specified distance
     */
    public List<Pair<String, List<String>>> lookup(String input, int distance, Verbose verbose) {
        if (input == null) throw new NullableArgumentException();
        if (input.length() <= 1 || input.length() <= distance) {
            throw new IllegalArgumentException("Input length must be more than specified distance");
        }
        List<Pair<String, List<String>>> founded = new ArrayList<>();
        TNode curr = root;
        int len = input.length();
        for (int i = 0; i < len; i++) {
            char c = input.charAt(i);
            TNode next = curr.nodes.get(c);
            if (next == null) {
                search(i, curr, input, distance, founded, verbose);
                return founded;
            }
            curr = next;
        }
        search(len, curr, input, distance, founded, verbose);
        return founded;
    }

    /**
     * @param pos      initial search position
     * @param curr     initial node
     * @param toSearch string apply to search
     * @param distance number of typos
     * @param founded  list for collecting founded results
     * @param verbose  node traverse range  MIN - only for current node MAX - for all nodes from current down to root node
     */
    private void search(int pos, TNode curr, String toSearch, int distance, List<Pair<String, List<String>>> founded, Verbose verbose) {
        Set<TNode> memo = new HashedSet<>();
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
     */
    private void fuzzyCompound(TNode start, String word, int pos, int typos, List<Pair<String, List<String>>> founded, Set<TNode> memo) {
        if (typos < 0) return;
        if (pos + typos >= word.length() && start.prev != null) {
            collectForNode(founded, start, memo);
        }
        for (HashNode<Character, TNode> pair : start.nodes.items) {
            TNode v = pair.getValue();
            if (pos < word.length() && pair.getKey().equals(word.charAt(pos))) {
                fuzzyCompound(v, word, pos + 1, typos, founded, memo);
            } else {
                fuzzyCompound(v, word, pos + 1, typos - 1, founded, memo);
                fuzzyCompound(v, word, pos, typos - 1, founded, memo);
                fuzzyCompound(start, word, pos + 1, typos - 1, founded, memo);
            }
        }
    }

    /**
     * Collect key-value pair from the specified node
     *
     * @param set result
     */
    private void collectForNode(List<Pair<String, List<String>>> set, TNode node, Set<TNode> memo) {
        if (!node.isEnd || memo.contains(node)) return;
        memo.add(node);
        List<String> values = new ArrayList<>(2);
        Pair<String, List<String>> pair = new Pair<>();
        String v = getReversed(node);
        if (node.isKey) pair.setKey(v);
        else values.add(v);
        for (TNode val : node.pairs) {
            if (val.isKey) pair.setKey(getReversed(val));
            if (val.isVal) values.add(getReversed(val));
        }
        pair.setValue(values);
        set.add(pair);
    }

    /**
     * Returns String which contains characters from the specified node to the TrieMap root
     */
    private String getReversed(TNode node) {
        DynamicString prefix = new DynamicLinkedString();
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
    private Pair<String, List<String>> getPair(TNode node) {
        List<String> values = new ArrayList<>();
        Pair<String, List<String>> pair = new Pair<>(getReversed(node), values);
        for (TNode n : node.pairs) {
            if (n.isVal) values.add(getReversed(n));
        }
        return pair;
    }

    @Override
    public Iterator<Pair<String, List<String>>> iterator() {
        return new SelfIterator();
    }

    private class SelfIterator implements Iterator<Pair<String, List<String>>> {
        private int position = 0;
        Stack<IteratorPair> prevNodes = new LinkedStack<>();


        public SelfIterator() {
            TNode rt = root;
            prevNodes.push(new IteratorPair(rt, rt.nodes.iterator()));
        }

        class IteratorPair {
            private final TNode node;
            private final Iterator<Character> iterator;
            private Pair<String, List<String>> pair;

            public IteratorPair(TNode node, Iterator<Character> iterator) {
                this.node = node;
                this.iterator = iterator;
                this.pair = null;
            }

            public Pair<String, List<String>> getPair() {
                return pair;
            }

            void setPair(Pair<String, List<String>> pair) {
                this.pair = pair;
            }
        }

        private void prepareNextNode(IteratorPair pair) {
            HashTable<Character, TNode> table = pair.node.nodes;
            Character nextChar = null;
            if (pair.iterator.hasNext()) {
                nextChar = pair.iterator.next();
                prevNodes.push(pair);
            }
            if (nextChar == null) {
                prepareNextNode(prevNodes.poll());
                return;
            }
            TNode nextNode = table.get(nextChar);
            Iterator<Character> nextIterator = nextNode.nodes.iterator();
            IteratorPair newPair = new IteratorPair(nextNode, nextIterator);
            if (table.get(nextChar).isKey) {
                newPair.setPair(getPair(nextNode));
                prevNodes.push(newPair);
                return;
            }
            prevNodes.push(newPair);
            prepareNextNode(prevNodes.poll());
        }

        @Override
        public boolean hasNext() {
            return position < pairsCount;
        }

        @Override
        public Pair<String, List<String>> next() {
            if (++position > pairsCount) throw new NoSuchElementException();
            prepareNextNode(prevNodes.poll());
            return prevNodes.peek().getPair();
        }
    }

    /**
     * Helps to print all entries from the TrieMap
     */
    private void toStringHelper(DynamicString res, TNode node) {
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
        DynamicString s = new DynamicLinkedString('[');
        toStringHelper(s, root);
        return s.replace(s.getSize() - 2, ']').toString();
    }
}
