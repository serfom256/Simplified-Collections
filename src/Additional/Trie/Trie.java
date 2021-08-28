package Additional.Trie;

import HashTables.HashTable;
import Lists.AbstractList;
import Lists.impl.ArrayList;

import java.util.Arrays;

public class Trie {

    private final HashTable<Character, TNode> root;
    private int size;
    private int entriesCount;

    private static class TNode {
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
    }

    public Trie() {
        this.root = new HashTable<>();
        this.size = 0;
    }

    public void put(char[] sequence) {
        if (sequence == null) return;
        putSequence(sequence);
    }

    private void putSequence(char[] sequence) {
        if (sequence.length == 0) return;
        HashTable<Character, TNode> curr = root;
        TNode currNode = null, prevNode;
        for (Character c : sequence) {
            if (!curr.containsKey(c)) {
                size++;
                curr.add(c, new TNode(c));
            }
            prevNode = currNode;
            currNode = curr.get(c);
            currNode.prev = prevNode;
            curr = currNode.nodes;
        }
        if (!currNode.isEnd) {
            entriesCount++;
            currNode.isEnd = true;
        }
    }

    /**
     * Returns true if specified sequence present in trie
     */
    public boolean presents(char[] sequence) {
        if (sequence == null) throw new IllegalArgumentException("Value to find must be not null");
        HashTable<Character, TNode> curr = root;
        for (Character c : sequence) {
            if (!curr.containsKey(c)) return false;
            curr = curr.get(c).nodes;
        }
        return true;
    }

    /**
     * Returns true if specified sequence was added to trie otherwise false
     */
    public boolean contains(char[] sequence) {
        if (sequence == null) throw new IllegalArgumentException("Value to find must be not null");
        if (sequence.length == 0) return true;
        HashTable<Character, TNode> curr = root;
        TNode last = null;
        for (Character c : sequence) {
            if (!curr.containsKey(c)) return false;
            last = curr.get(c);
            curr = last.nodes;
        }
        return last.isEnd;
    }

    public String getByPrefix(char[] prefix) {
        String[] res = getByPrefix(prefix, 1);
        return res.length == 0 ? "" : res[0];
    }

    public String[] getByPrefix(char[] prefix, int count) {
        if (count <= 0) throw new IllegalArgumentException("Count must be more then 0");
        if (prefix.length == 0) return new String[0];
        AbstractList<String> list = new ArrayList<>();
        HashTable<Character, TNode> curr = root;
        StringBuilder result = new StringBuilder();
        for (int c = 0; c < prefix.length; c++) {
            if (!curr.containsKey(prefix[c])) return new String[0];
            result.append(prefix[c]);
            if (c == prefix.length - 1 && curr.get(prefix[c]).isEnd) {
                count--;
                list.add(result.toString());
            }
            curr = curr.get(prefix[c]).nodes;
        }
        collect(list, result, curr, count);
        Object[] instance = list.toObjectArray();
        return Arrays.copyOf(instance, instance.length, String[].class);
    }

    private int collect(AbstractList<String> result, StringBuilder prefix, HashTable<Character, TNode> start, int count) {
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

    public boolean removeHard(char[] sequence) {
        if (sequence.length == 0) return false;
        return removeSequenceHard(sequence);
    }

    private boolean removeSequenceHard(char[] sequence) {
        TNode lastNode = getLastNodeAfterEnd(sequence);
        if (lastNode == null) return false;
        reduceCountOfElements(lastNode);
        if (lastNode.prev == null) root.remove(sequence[0]);
        else lastNode.prev.nodes.remove(lastNode.element);
        return true;
    }

    public boolean removeWeak(char[] sequence) {
        if (sequence.length == 0) return false;
        return removeSequenceWeak(sequence);
    }

    public boolean remove(char[] sequence) {
        if (sequence.length == 0) return false;
        TNode node = getLastNodeAfterEnd(sequence);
        if (node == null || !node.isEnd) return false;
        if (node.nodes.getSize() != 0) {
            node.isEnd = false;
            entriesCount--;
        } else {
            reduceCountOfElements(node);
            if (node.prev == null) root.remove(sequence[0]);
            else node.prev.nodes.remove(node.element);
        }
        return true;
    }

    private boolean removeSequenceWeak(char[] sequence) {
        TNode lastNode = getLastNodeAfterEnd(sequence);
        if (lastNode == null || hasSucceedingBranches(lastNode)) return false;
        reduceCountOfElements(lastNode);
        if (lastNode.prev == null) root.remove(sequence[0]);
        else lastNode.prev.nodes.remove(lastNode.element);
        return true;
    }

    private boolean hasSucceedingBranches(TNode node) {
        HashTable<Character, TNode> table = node.nodes;
        while(table.getSize() != 0) {
            if(table.getSize() > 1 || node.isEnd) return true;
            for (Character c : table) {
                node = node.nodes.get(c);
                table = node.nodes;
            }
        }
        return false;
    }

    private TNode getLastNodeAfterEnd(char[] sequence) {
        HashTable<Character, TNode> curr = root;
        TNode lastEnd = null;
        boolean ifPrevIsEnd = false;
        for (int i = 0; i < sequence.length; i++) {
            Character c = sequence[i];
            if (!curr.containsKey(c)) return null;
            TNode node = curr.get(c);
            if ((node.isEnd || node.nodes.getSize() > 1) && i != sequence.length - 1) ifPrevIsEnd = true;
            else if (ifPrevIsEnd) lastEnd = node;
            curr = curr.get(c).nodes;
        }
        if (lastEnd == null) lastEnd = root.get(sequence[0]);
        return lastEnd;
    }

    private void reduceCountOfElements(TNode node) {
        if (node == null) return;
        if (node.isEnd) entriesCount--;
        size--;
        for (Character c : node.nodes) {
            reduceCountOfElements(node.nodes.get(c));
        }
    }

    public void clear() {
        this.size = this.entriesCount = 0;
        this.root.clear();
    }

    public int getSize() {
        return size;
    }

    public int getEntriesCount() {
        return entriesCount;
    }

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
        StringBuilder res = getAll(new StringBuilder("["), new StringBuilder(), root);
        return res.delete(res.length() - 2, res.length()).append(']').toString();
    }

}

