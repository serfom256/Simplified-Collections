package tries.tries;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.NullableArgumentException;
import additional.nodes.Pair;
import hashtables.HashTable;
import stack.AbstractStack;
import stack.LinkedStack;
import tries.AbstractTrieMap;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class VTrieMap<V> implements AbstractTrieMap<String, V>, Iterable<Pair<String, V>> {

    private final TNode<V> root;
    private int size;
    private int pairsCount;

    private static class TNode<V> {
        private Character element;
        private boolean isEnd;
        private TNode<V> prev;
        private final HashTable<Character, TNode<V>> nodes;
        private V value;

        public TNode(Character element, TNode<V> prev) {
            this.element = element;
            this.prev = prev;
            this.nodes = new HashTable<>(4);
            this.value = null;
        }

        public TNode() {
            this.nodes = new HashTable<>();
            this.prev = null;
            this.value = null;
        }
    }

    public VTrieMap() {
        this.root = new TNode<>();
        this.root.nodes.setCapacity(32);
        this.size = 0;
    }

    public void add(String key, V value, boolean otherWrite) {
        if (key == null) throw new NullableArgumentException();
        if (key.length() == 0) throw new IllegalArgumentException();
        TNode<V> keyNode = putSequence(key);
        if (!keyNode.isEnd) {
            pairsCount++;
            keyNode.value = value;
        } else if (otherWrite) {
            keyNode.value = value;
        }
        keyNode.isEnd = true;
    }

    /**
     * Creates branch from all characters of the specified sequence
     *
     * @return end node of branch
     */
    private TNode<V> putSequence(String sequence) {
        TNode<V> curr = root;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            TNode<V> next = curr.nodes.get(c);
            if (next == null) {
                size++;
                next = new TNode<>(c, curr);
                curr.nodes.add(c, next);
            }
            curr = next;
        }
        return curr;
    }

    private TNode<V> getNode(String val) {
        TNode<V> curr = root;
        for (int i = 0; i < val.length(); i++) {
            curr = curr.nodes.get(val.charAt(i));
            if (curr == null) return null;
        }
        return curr;
    }


    private boolean containsValueRecursive(TNode<V> node, V value) {
        for (Character n : node.nodes) {
            TNode<V> curr = node.nodes.get(n);
            if (curr.isEnd && curr.value == value || containsValueRecursive(curr, value)) return true;
        }
        return false;
    }

    /**
     * Removes all nodes from the specified node down to the next end node
     */
    private void deleteNode(TNode<V> valueNode) {
        TNode<V> node = valueNode;
        TNode<V> prev = valueNode;
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
     * Returns pair of the key and values which linked to the specified node
     */
    private Pair<String, V> getPair(TNode<V> node) {
        return new Pair<>(getReversed(node), node.value);
    }

    /**
     * Returns String which contains characters from the specified node to the TrieMap root
     */
    private String getReversed(TNode<V> node) {
        AbstractDynamicString prefix = new DynamicLinkedString();
        while (node != root) {
            prefix.addFirst(node.element);
            node = node.prev;
        }
        return prefix.toString();
    }

    /**
     * @throws NullableArgumentException if the specified key is null
     */
    public Pair<String, V> deleteKey(String key) {
        if (key == null) throw new NullableArgumentException();
        TNode<V> keyNode = getNode(key);
        if (keyNode == null || !keyNode.isEnd) return new Pair<>();
        Pair<String, V> result = getPair(keyNode);
        deleteNode(keyNode);
        return result;
    }

    /**
     * Returns true if the TrieMap contains the specified string as key or value
     *
     * @throws NullableArgumentException if the specified value is null
     */
    @Override
    public boolean contains(String key) {
        if (key == null) throw new NullableArgumentException();
        TNode<V> valueNode = getNode(key);
        return valueNode != null && valueNode.isEnd;
    }

    public int getSize() {
        return size;
    }

    public int getPairsCount() {
        return pairsCount;
    }

    @Override
    public Iterator<Pair<String, V>> iterator() {
        return new SelfIterator();
    }

    private class SelfIterator implements Iterator<Pair<String, V>> {
        private int position = 0;
        AbstractStack<SelfIterator.IteratorPair> prevNodes = new LinkedStack<>();


        public SelfIterator() {
            TNode<V> rt = root;
            prevNodes.push(new SelfIterator.IteratorPair(rt, rt.nodes.iterator()));
        }

        class IteratorPair {
            private final TNode<V> node;
            private final Iterator<Character> iterator;
            private Pair<String, V> pair;

            public IteratorPair(TNode<V> node, Iterator<Character> iterator) {
                this.node = node;
                this.iterator = iterator;
                this.pair = null;
            }

            public Pair<String, V> getPair() {
                return pair;
            }

            void setPair(Pair<String, V> pair) {
                this.pair = pair;
            }
        }

        private void prepareNextNode(SelfIterator.IteratorPair pair) {
            HashTable<Character, TNode<V>> table = pair.node.nodes;
            Character nextChar = null;
            if (pair.iterator.hasNext()) {
                nextChar = pair.iterator.next();
                prevNodes.push(pair);
            }
            if (nextChar == null) {
                prepareNextNode(prevNodes.poll());
                return;
            }
            TNode<V> nextNode = table.get(nextChar);
            Iterator<Character> nextIterator = nextNode.nodes.iterator();
            SelfIterator.IteratorPair newPair = new SelfIterator.IteratorPair(nextNode, nextIterator);
            if (table.get(nextChar).isEnd) {
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
        public Pair<String, V> next() {
            if (++position > pairsCount) throw new NoSuchElementException();
            prepareNextNode(prevNodes.poll());
            return prevNodes.peek().getPair();
        }
    }
}
