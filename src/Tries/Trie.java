package Tries;

import Additional.DynamicString.AbstractDynamicString;
import Additional.DynamicString.DynamicLinkedString;
import Sets.AbstractSet;
import Sets.SortedSetIP;
import HashTables.HashTable;
import Stack.AbstractStack;
import Stack.LinkedStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Trie implements Iterable<String> {

    TNode root;
    int size;
    int entriesCount;

    static class TNode {
        Character element;
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

    public Trie() {
        this.root = new TNode();
        this.size = 0;
    }

    /**
     * Append specified sequence to the Trie if sequence not equals null
     *
     * @param sequence sequence to append
     */
    public void put(String sequence) {
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
        for (String obj : data) put(obj);
    }

    /**
     * @return true if specified sequence present in trie as a prefix
     * @throws IllegalArgumentException if specified sequence is null
     */
    public boolean presents(String sequence) {
        if (sequence == null) throw new IllegalArgumentException("Value to find must be not null");
        TNode curr = root;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            curr = curr.nodes.get(c);
            if (curr == null) return false;
        }
        return true;
    }

    /**
     * Returns true if specified sequence was added to trie otherwise false
     *
     * @throws IllegalArgumentException if specified sequence is null
     */
    public boolean contains(String sequence) {
        if (sequence == null) throw new IllegalArgumentException("Value to find must be not null");
        if (sequence.length() == 0) return false;
        TNode curr = root;
        TNode last = null;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            last = curr.nodes.get(c);
            if (last == null) return false;
            curr = last;
        }
        return last.isEnd;
    }

    /**
     * Returns one founded sequence as a String if sequence founded
     *
     * @param prefix to search sequences with specified prefix
     * @return sequences as a String if found otherwise empty String
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
     */
    public String[] getByPrefix(String prefix, int count) {
        if (count <= 0) throw new IllegalArgumentException("Count must be more then 0");
        if (prefix.length() == 0) return new String[0];
        AbstractSet<String> list = new SortedSetIP<>();
        TNode curr = root;
        AbstractDynamicString result = new DynamicLinkedString();
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
    int collect(AbstractSet<String> result, AbstractDynamicString prefix, HashTable<Character, TNode> start, int count) {
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
     */
    public boolean removeHard(String sequence) {
        if (sequence == null || sequence.length() == 0) return false;
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
     * Removes only associated with specified sequence branch
     * if the branch contains only the specified sequence
     *
     * @param sequence sequence to remove
     * @return true if removed otherwise false
     */
    public boolean removeWeak(String sequence) {
        if (sequence == null || sequence.length() == 0) return false;
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
     */
    public boolean remove(String sequence) {
        if (sequence == null || sequence.length() == 0) return false;
        TNode curr = root, lastNode = root;
        int lastCharPos = 0;
        int len = sequence.length() - 1;
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
    public int getSize() {
        return size;
    }

    /**
     * Returns count of inserted sequences
     */
    public int getEntriesCount() {
        return entriesCount;
    }

    /**
     * Helps to collect all entries from the trie
     */
    private StringBuilder getAll(StringBuilder result, StringBuilder current, HashTable<Character, TNode> nodes) {
        for (Character c : nodes) {
            TNode node = nodes.get(c);
            current.append(node.element);
            if (node.isEnd) result.append(current).append(", ");
            getAll(result, current, node.nodes);
            current.deleteCharAt(current.length() - 1);
        }
        return result;
    }

    @Override
    public String toString() {
        if (entriesCount == 0) return "[]";
        StringBuilder res = getAll(new StringBuilder("["), new StringBuilder(), root.nodes);
        return res.delete(res.length() - 2, res.length()).append(']').toString();
    }


    @Override
    public Iterator<String> iterator() {
        return new SelfIterator();
    }

    private class SelfIterator implements Iterator<String> {
        DynamicLinkedString lastString;
        DynamicLinkedString tempString = new DynamicLinkedString();
        AbstractStack<Pair> prevNodes = new LinkedStack<>();


        public SelfIterator() {
            prevNodes.push(new Pair(root, root.nodes.iterator()));
        }

        private TNode getNext(HashTable<Character, TNode> table, Iterator<Character> iterator) {
            if (iterator.hasNext()) return table.get(iterator.next());
            return null;
        }

        class Pair {
            private final TNode table;
            private final Iterator<Character> iterator;

            public Pair(TNode node, Iterator<Character> iterator) {
                this.table = node;
                this.iterator = iterator;
            }
        }

        //FIXME fix foreach
        private boolean getNextNode(DynamicLinkedString prefix) {
            if (prevNodes.getSize() == 0) return false;
            Pair last = prevNodes.peek();
            if (last == null) return false;
            HashTable<Character, TNode> table = last.table.nodes;
            Iterator<Character> iterator = last.iterator;
            TNode newNode = getNext(table, iterator);
            Character newChar;
            if (newNode == null) {
                Pair prev = prevNodes.peek();
                while (!prev.iterator.hasNext() && prevNodes.getSize() > 0) {
                    prev = prevNodes.poll();
                    if (prev == null) return false;
                    if (prevNodes.getSize() == 0 || prev.iterator.hasNext()) break;
                    prefix.deleteLast();
                }
                newNode = prev.table;
                iterator = prev.iterator;
                if (!iterator.hasNext()) return false;
                newChar = iterator.next();
                table = newNode.nodes;
                if (prevNodes.getSize() == 0) {
                    prevNodes.push(new Pair(newNode, iterator));
                    prefix.deleteLast();
                } else if (iterator.hasNext()) {
                    prevNodes.push(new Pair(newNode, iterator));
                }

            } else {
                newChar = newNode.element;
            }
            prefix.add(newChar);
            newNode = table.get(newChar);
            prevNodes.push(new Pair(newNode, newNode.nodes.iterator()));
            if (newNode.isEnd) {
                lastString = new DynamicLinkedString(prefix);
                return true;
            }
            return getNextNode(prefix);
        }


        @Override
        public boolean hasNext() {
            return getNextNode(tempString);
        }

        @Override
        public String next() {
            if(lastString == null) throw new NoSuchElementException();
            String result = lastString.toString();
            lastString = null;
            return result;
        }
    }
}