package Additional.Trie;

import HashSet.AbstractSet;
import HashSet.SortedSet;
import HashTables.HashTable;

import java.util.Arrays;

public class Trie {

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
            this.nodes = new HashTable<>(35);
        }

        public TNode() {
            this.isEnd = false;
            this.prev = null;
            this.nodes = new HashTable<>(35);
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
        if (sequence == null) return;
        putSequence(sequence);
    }

    /**
     * Inserts sequence char by char to the root if sequence length not equals 0
     *
     * @param sequence sequence to insert
     */
    private void putSequence(String sequence) {
        if (sequence.length() == 0) return;
        TNode curr = root;
        TNode currNode = root, prevNode;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            if (!curr.nodes.containsKey(c)) {
                size++;
                curr.nodes.add(c, new TNode(c));
            }
            prevNode = currNode;
            currNode = curr.nodes.get(c);
            currNode.prev = prevNode;
            curr = currNode;
        }
        if (!currNode.isEnd) {
            entriesCount++;
            currNode.isEnd = true;
        }
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
            if (!curr.nodes.containsKey(c)) return false;
            curr = curr.nodes.get(c);
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
            if (!curr.nodes.containsKey(c)) return false;
            last = curr.nodes.get(c);
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
        AbstractSet<String> list = new SortedSet<>(count + 1);
        TNode curr = root;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!curr.nodes.containsKey(c)) return new String[0];
            result.append(c);
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
    int collect(AbstractSet<String> result, StringBuilder prefix, HashTable<Character, TNode> start, int count) {
        if (start == null || count == 0) return count;
        for (Character c : start) {
            TNode node = start.get(c);
            prefix.append(node.element);
            if (node.isEnd) {
                count--;
                result.add(prefix.toString());
            }
            count = collect(result, prefix, node.nodes, count);
            if (count == 0) return 0;
            prefix.deleteCharAt(prefix.length() - 1);
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
        if (sequence.length() == 0) return false;
        return removeSequenceHard(sequence);
    }

    private boolean removeSequenceHard(String sequence) {
        TNode lastNode = getLastNodeAfterEnd(sequence);
        if (lastNode == null) return false;
        reduceCountOfElements(lastNode);
        if (lastNode.prev == null) root.nodes.remove(sequence.charAt(0));
        else lastNode.prev.nodes.remove(lastNode.element);
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
        if (sequence.length() == 0) return false;
        return removeSequenceWeak(sequence);
    }

    /**
     * Removes only specified sequence from branch (Recommended for use)
     *
     * @param sequence sequence to remove
     * @return true if removed otherwise false
     */
    // TODO optimize it
    public boolean remove(String sequence) {
        if (sequence.length() == 0) return false;
        TNode curr = root, lastNode = root;
        Character lastChar = sequence.charAt(0);
        for (int i = 0; i < sequence.length(); i++) {
            Character c = sequence.charAt(i);
            if (!curr.nodes.containsKey(c)) return false;
            curr = curr.nodes.get(c);
            if (curr.nodes.getSize() > 1 || (curr.isEnd && i != sequence.length() - 1)) {
                lastChar = curr.nodes.get(sequence.charAt(i + 1)).element;
                lastNode = curr;
            }
        }
        if (curr.nodes.getSize() == 0) {
            reduceCountOfElements(lastNode.nodes.get(lastChar));
            lastNode.nodes.remove(lastChar);
        } else if (curr.isEnd) {
            curr.isEnd = false;
            entriesCount--;
        } else {
            return false;
        }
        return true;
    }

    private boolean removeSequenceWeak(String sequence) {
        TNode lastNode = getLastNodeAfterEnd(sequence);
        if (lastNode == null || hasSucceedingBranches(lastNode)) return false;
        reduceCountOfElements(lastNode);
        if (lastNode.prev == null) root.nodes.remove(sequence.charAt(0));
        else lastNode.prev.nodes.remove(lastNode.element);
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
     * Provides to get last node of the specified sequence end
     *
     * @return node if founded otherwise null
     */
    private TNode getLastNodeAfterEnd(String sequence) {
        TNode curr = root;
        TNode lastEnd = null;
        boolean ifPrevIsEnd = false;
        for (int i = 0; i < sequence.length(); i++) {
            Character c = sequence.charAt(i);
            if (!curr.nodes.containsKey(c)) return null;
            TNode node = curr.nodes.get(c);
            if ((node.isEnd || node.nodes.getSize() > 1) && i != sequence.length() - 1) ifPrevIsEnd = true;
            else if (ifPrevIsEnd) lastEnd = node;
            curr = curr.nodes.get(c);
        }
        if (lastEnd == null) lastEnd = root.nodes.get(sequence.charAt(0));
        return lastEnd;
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
     * Method which helps to collect all entries from the trie
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

}

