package tries.tries;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.NullableArgumentException;
import sets.Set;
import sets.RBTSet;
import hashtables.HashTable;
import stack.Stack;
import stack.LinkedStack;
import tries.Trie;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleTrie implements Iterable<String>, Trie {

    protected TNode root;
    protected int size;
    protected int entriesCount;

    static class TNode {
        char element;
        boolean isEnd;
        TNode prev;
        HashTable<Character, TNode> nodes;

        public TNode(Character element) {
            this.element = element;
            this.isEnd = false;
            this.prev = null;
            this.nodes = new HashTable<>(4);
        }

        public TNode() {
            this.isEnd = false;
            this.prev = null;
            this.nodes = new HashTable<>();
        }
    }

    public SimpleTrie() {
        this.root = new TNode();
        this.size = 0;
    }

    /**
     * Appends specified sequence to the Trie if sequence not equals null
     *
     * @param sequence sequence to append
     */
    public void add(String sequence) {
        if (sequence == null || sequence.length() == 0) return;
        putSequence(sequence);
    }

    /**
     * Inserts sequence char by char to the root if sequence length not equals 0
     *
     * @param sequence sequence to insert
     */
    private void putSequence(String sequence) {
        TNode curr = root;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
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
    }

    public final void addAll(String... data) {
        for (String obj : data) add(obj);
    }

    /**
     * @return true if specified sequence present in trie as a prefix
     * @throws NullableArgumentException if specified sequence is null
     */
    public boolean containsPrefix(String sequence) {
        if (sequence == null) throw new NullableArgumentException();
        TNode curr = root;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            curr = curr.nodes.get(c);
            if (curr == null) return false;
        }
        return true;
    }

    /**
     * @return true if specified sequence present in trie as a prefix by the first prefix match
     * @throws NullableArgumentException if specified sequence is null
     */
    public boolean contains(String sequence) {
        if (sequence == null) throw new NullableArgumentException();
        TNode curr = root;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            curr = curr.nodes.get(c);
            if (curr == null) return false;
            if (curr.isEnd) return true;
        }
        return false;
    }

    /**
     * Returns true if specified sequence was added to trie otherwise false
     *
     * @throws NullableArgumentException if specified sequence is null
     */
    public boolean presents(String sequence) {
        if (sequence == null) throw new NullableArgumentException();
        TNode curr = root;
        TNode last = null;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            last = curr.nodes.get(c);
            if (last == null) return false;
            curr = last;
        }
        return last != null && last.isEnd;
    }

    /**
     * Returns one founded sequence as a String if sequence founded
     *
     * @param prefix to search sequences with specified prefix
     * @return sequences as a String if found otherwise empty String
     * @throws NullableArgumentException if the specified prefix is null
     */
    public String getByPrefix(String prefix) {
        String[] res = getByPrefix(prefix, 1);
        return res.length == 0 ? "" : res[0];
    }

    /**
     * Returns founded sequences as a String array if sequences founded
     *
     * @param prefix to search sequences with specified prefix
     * @return sequences as a String array if sequences with specified prefix founded
     * otherwise empty String array
     * @throws IllegalArgumentException  if count < 0
     * @throws NullableArgumentException if the specified prefix is null
     */
    public String[] getByPrefix(String prefix, int count) {
        if (prefix == null) throw new NullableArgumentException();
        if (count <= 0) throw new IllegalArgumentException("Count must be more than 0");
        if (prefix.length() == 0) return new String[0];
        Set<String> list = new RBTSet<>();
        TNode curr = root;
        DynamicString result = new DynamicLinkedString();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!curr.nodes.containsKey(c)) return new String[0];
            result.add(c);
            if (i == prefix.length() - 1 && curr.nodes.get(c).isEnd) {
                count--;
                list.add(result.toString());
            }
            curr = curr.nodes.get(c);
        }
        collect(list, result, curr.nodes, count);
        Object[] instance = list.toObjectArray();
        return Arrays.copyOf(instance, instance.length, String[].class);
    }

    /**
     * Provides to collect all branches of the specified node to list
     *
     * @param result list to collect all founded sequences
     * @param prefix prefix of all founded sequences
     * @param start  start position from which collect all branches
     * @param count  number of sequences to collect
     *               Not taken into account if specified count of sequences not found in the specified branch
     */
    int collect(Set<String> result, DynamicString prefix, HashTable<Character, TNode> start, int count) {
        if (start == null || count == 0) return count;
        for (Character c : start) {
            TNode node = start.get(c);
            prefix.add(node.element);
            if (node.isEnd) {
                count--;
                result.add(prefix.toString());
            }
            count = collect(result, prefix, node.nodes, count);
            if (count == 0) return 0;
            prefix.deleteLast();
        }
        return count;
    }

    /**
     * Removes all data associated with the specified sequence
     *
     * @param sequence sequence to remove
     * @return true if removed otherwise false
     * @throws NullableArgumentException if the specified sequence is null
     */
    public boolean removeHard(String sequence) {
        if (sequence == null) throw new NullableArgumentException();
        if (sequence.length() == 0) return false;
        return removeSequenceHard(sequence);
    }

    private boolean removeSequenceHard(String sequence) {
        TNode lastNode = getLastNodeAfterEnd(sequence);
        if (lastNode == null) return false;
        lastNode.prev.nodes.delete(lastNode.element);
        reduceCountOfElements(lastNode);
        if (lastNode.prev == null) root.nodes.delete(sequence.charAt(0));
        else lastNode.prev.nodes.delete(lastNode.element);
        return true;
    }

    /**
     * Tries to remove only associated with specified sequence branch
     * Removes the branch and returns true if branch hasn't successors otherwise false
     *
     * @param sequence sequence to remove
     * @return true if removed otherwise false
     * @throws NullableArgumentException if the specified sequence is null
     */
    public boolean removeWeak(String sequence) {
        if (sequence == null) throw new NullableArgumentException();
        if (sequence.length() == 0) return false;
        return removeSequenceWeak(sequence);
    }

    private boolean removeSequenceWeak(String sequence) {
        TNode lastNode = getLastNodeAfterEnd(sequence);
        if (lastNode == null || hasSucceedingBranches(lastNode)) return false;
        lastNode.prev.nodes.delete(lastNode.element);
        reduceCountOfElements(lastNode);
        if (lastNode.prev == null) root.nodes.delete(sequence.charAt(0));
        else lastNode.prev.nodes.delete(lastNode.element);
        return true;
    }

    /**
     * Removes only specified sequence from branch (Recommended for use)
     *
     * @param sequence sequence to remove
     * @return true if removed otherwise false
     * @throws NullableArgumentException if the specified sequence is null
     */
    public boolean delete(String sequence) {
        if (sequence == null) throw new NullableArgumentException();
        if (sequence.length() == 0) return false;
        TNode curr = root, lastNode = root;
        int lastCharPos = 0, len = sequence.length() - 1;
        for (int i = 0; i <= len; i++) {
            Character c = sequence.charAt(i);
            curr = curr.nodes.get(c);
            if (curr == null) return false;
            if (curr.nodes.getSize() > 1 || (curr.isEnd && i != len)) {
                lastCharPos = i + 1;
                lastNode = curr;
            }
        }
        if (curr.nodes.getSize() == 0) {
            size -= len + 1 - lastCharPos;
            lastNode.nodes.delete(sequence.charAt(lastCharPos));
        } else if (curr.isEnd) {
            curr.isEnd = false;
        } else {
            return false;
        }
        entriesCount--;
        return true;
    }

    /**
     * Returns true if the branches of the specified node is empty otherwise false.
     */
    private boolean hasSucceedingBranches(TNode node) {
        HashTable<Character, TNode> table = node.nodes;
        while (table.getSize() != 0) {
            if (table.getSize() > 1 || node.isEnd) return true;
            for (Character c : table) {
                node = node.nodes.get(c);
                table = node.nodes;
            }
        }
        return false;
    }

    /**
     * Provides to get last node which equals end of the specified sequence
     *
     * @return node if founded otherwise null
     */
    private TNode getLastNodeAfterEnd(String sequence) {
        TNode curr = root, lastNode = root;
        int lastCharPos = 0;
        int len = sequence.length() - 1;
        for (int i = 0; i <= len; i++) {
            Character c = sequence.charAt(i);
            curr = curr.nodes.get(c);
            if (curr == null) return null;
            if (curr.nodes.getSize() > 1 || (curr.isEnd && i != len)) {
                lastCharPos = i + 1;
                lastNode = curr;
            }
        }
        return lastNode.nodes.get(sequence.charAt(lastCharPos));
    }

    /**
     * Provides to remove all elements on the specified node branches
     */
    private void reduceCountOfElements(TNode node) {
        if (node == null) return;
        if (node.isEnd) entriesCount--;
        size--;
        for (Character c : node.nodes) {
            reduceCountOfElements(node.nodes.get(c));
        }
    }

    /**
     * Clear trie
     */
    public void clear() {
        this.size = this.entriesCount = 0;
        this.root = new TNode();
    }

    /**
     * Returns count all characters in the trie
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Returns count of inserted sequences
     */
    @Override
    public int getEntriesCount() {
        return entriesCount;
    }

    /**
     * Helps to collect all entries from the trie
     */
    private DynamicString getAll(DynamicString result, DynamicString current, HashTable<Character, TNode> nodes) {
        for (Character c : nodes) {
            TNode node = nodes.get(c);
            current.add(node.element);
            if (node.isEnd) result.add(current).add(", ");
            getAll(result, current, node.nodes);
            current.deleteLast();
        }
        return result;
    }

    @Override
    public String toString() {
        if (entriesCount == 0) return "[]";
        DynamicString res = getAll(new DynamicLinkedString("["), new DynamicLinkedString(), root.nodes);
        return res.replace(res.getSize() - 2, ']').toString();
    }


    @Override
    public Iterator<String> iterator() {
        return new SelfIterator();
    }

    private class SelfIterator implements Iterator<String> {
        private int position = 0;
        DynamicLinkedString tempString = new DynamicLinkedString();
        Stack<Pair> prevNodes = new LinkedStack<>();


        public SelfIterator() {
            TNode rt = root;
            prevNodes.push(new Pair(rt, rt.nodes.iterator(), new DynamicLinkedString()));
        }

        class Pair {
            private final TNode node;
            private final Iterator<Character> iterator;
            private final DynamicLinkedString prefix;

            public Pair(TNode node, Iterator<Character> iterator, DynamicLinkedString prefix) {
                this.node = node;
                this.iterator = iterator;
                this.prefix = prefix;
            }
        }

        private void prepareNextNode(Pair pair) {
            DynamicLinkedString prefix = pair.prefix;
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
            Pair newPair = new Pair(nextNode, nextIterator, new DynamicLinkedString(prefix));
            newPair.prefix.add(nextChar);
            if (table.get(nextChar).isEnd) {
                tempString = newPair.prefix;
                prevNodes.push(newPair);
                return;
            }
            prevNodes.push(newPair);
            prepareNextNode(prevNodes.poll());
        }

        @Override
        public boolean hasNext() {
            return position < entriesCount;
        }

        @Override
        public String next() {
            if (++position > entriesCount) throw new NoSuchElementException();
            prepareNextNode(prevNodes.poll());
            return tempString.toString();
        }
    }
}
