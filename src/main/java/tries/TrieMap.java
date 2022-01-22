package tries;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.NullableArgumentException;
import additional.nodes.HashNode;
import hashtables.AbstractMap;
import hashtables.HashTable;
import lists.AbstractLinkedList;
import lists.impl.LinkedList;
import stack.AbstractStack;
import stack.LinkedStack;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TrieMap<K, V> implements AbstractMap<K, V> {

    private final TNode<K, V> root;
    public final Items items;
    private int size;

    private static class TNode<K, V> implements HashNode<K, V> {

        private Byte hashNum;
        private K key;
        private int hash;
        private AbstractLinkedList<V> values;
        private boolean isEnd;
        TNode<K, V> prev;
        private final HashTable<Byte, TNode<K, V>> nodes;

        public TNode(TNode<K, V> prev, Byte hashNum) {
            this.hashNum = hashNum;
            this.isEnd = false;
            this.prev = prev;
            this.values = null;
            this.nodes = new HashTable<>(4);
        }

        public TNode() {
            this.isEnd = false;
            this.nodes = new HashTable<>();
            this.prev = null;
            this.values = null;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return values.getFirst();
        }

        @Override
        public int getHash() {
            return hash;
        }
    }

    public TrieMap() {
        this.root = new TNode<>();
        this.size = 0;
        this.items = new Items();
    }

    /**
     * Compute hashCode by key
     *
     * @return computed hashCode for specified key
     */
    private int generateHash(K key) {
        return key.hashCode() ^ key.hashCode() >>> 16;
    }

    /**
     * Appends association of the specified key and the specified value to the TrieMap
     *
     * @throws NullableArgumentException if the specified key is null
     */
    @Override
    public void add(K key, V value) {
        if (key == null) throw new NullableArgumentException();
        int hash = generateHash(key);
        TNode<K, V> keyNode = putNode(root, hash);
        if (!keyNode.isEnd) size++;
        keyNode.isEnd = true;
        keyNode.key = key;
        keyNode.hash = hash;
        if (keyNode.values == null) keyNode.values = new LinkedList<>();
        keyNode.values.add(value);
    }

    /**
     * Removes specified value of the specified key
     *
     * @param value value associated with key to remove
     * @return removed value if value is present otherwise null
     * @throws NullableArgumentException if key is null
     */
    @Override
    public boolean deleteValue(K key, V value) {
        if (key == null) throw new NullableArgumentException();
        TNode<K, V> node = getTargetNode(key);
        if (node == null) return false;
        if (node.values.getSize() == 1) {
            delete(key);
            return true;
        }
        return node.values.delete(value);
    }

    /**
     * Removes specified key with all associated values
     *
     * @return first value of the specified key
     * @throws NullableArgumentException if the specified key is null
     */
    @Override
    public V delete(K key) {
        if (key == null) throw new NullableArgumentException();
        TNode<K, V> curr = root, lastEnd = root;
        int hash = generateHash(key), num = hash % 10;
        while (hash != 0) {
            byte c = (byte) (hash % 10);
            hash /= 10;
            curr = curr.nodes.get(c);
            if (curr == null) return null;
            if (curr.nodes.getSize() > 1 || (curr.isEnd && hash / 10 > 10)) {
                num = hash % 10;
                lastEnd = curr;
            }
        }
        if (curr.nodes.getSize() == 0) {
            TNode<K, V> toRemove = lastEnd.nodes.get((byte) num);
            if (toRemove != null) toRemove.prev = null;
            lastEnd.nodes.delete((byte) num);
        } else if (curr.isEnd) {
            curr.isEnd = false;
        } else {
            return null;
        }
        size--;
        return curr.values.getFirst();
    }

    /**
     * Creates branch of the specified hash numbers
     *
     * @param root root of the tries
     * @param hash hash of the inserted key
     * @return last inserted node
     */
    private TNode<K, V> putNode(TNode<K, V> root, int hash) {
        TNode<K, V> curr;
        while (hash != 0) {
            byte c = (byte) (hash % 10);
            hash /= 10;
            curr = root.nodes.get(c);
            if (curr == null) {
                curr = new TNode<>(root, c);
                root.nodes.add(c, curr);
            }
            root = curr;
        }
        return root;
    }

    /**
     * Returns end node of the specified key founded otherwise null
     */
    private TNode<K, V> getTargetNode(K key) {
        TNode<K, V> curr = root;
        int hash = generateHash(key);
        while (hash != 0) {
            byte c = (byte) (hash % 10);
            hash /= 10;
            curr = curr.nodes.get(c);
            if (curr == null) return null;
        }
        return curr;
    }

    /**
     * Returns first value of the specified key if key presents otherwise null
     *
     * @throws NullableArgumentException if the specified key is null
     */
    @Override
    public V get(K key) {
        if (key == null) throw new NullableArgumentException();
        TNode<K, V> node = getTargetNode(key);
        if (node == null) return null;
        return node.values.getFirst();
    }

    /**
     * Replaces oldKey by newKey with merging oldKey and newKey values
     *
     * @return true if replacement done otherwise false
     * @throws NullableArgumentException if oldKey or newKey is null
     */
    @Override
    public boolean replace(K oldKey, K newKey) {
        if (oldKey == null || newKey == null) throw new NullableArgumentException();
        TNode<K, V> node = getTargetNode(oldKey);
        if (node == null) return false;
        AbstractLinkedList<V> values = node.values;
        delete(oldKey);
        size++;
        TNode<K, V> newNode = putNode(root, generateHash(newKey));
        if (newNode.values == null) newNode.values = new LinkedList<>();
        newNode.values.addFrom(values);
        return true;
    }

    /**
     * Returns true if TrieMap contains specified key otherwise false
     *
     * @throws NullableArgumentException if the specified key is null
     */
    @Override
    public boolean containsKey(K key) {
        if (key == null) throw new NullableArgumentException();
        return getTargetNode(key) != null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public final class Items implements Iterable<HashNode<K, V>> {

        private final AbstractStack<Pair> prevNodes = new LinkedStack<>();
        private TNode<K, V> endNode = null;
        private int count = 0;

        private class Pair {
            private final TNode<K, V> node;
            private final Iterator<Byte> iterator;
            private final DynamicLinkedString prefix;

            public Pair(TNode<K, V> node, Iterator<Byte> iterator, DynamicLinkedString prefix) {
                this.node = node;
                this.iterator = iterator;
                this.prefix = prefix;
            }
        }

        @Override
        public Iterator<HashNode<K, V>> iterator() {
            return new NodesIterator();
        }

        private class NodesIterator implements Iterator<HashNode<K, V>> {

            NodesIterator() {
                TNode<K, V> rt = root;
                prevNodes.push(new Pair(rt, rt.nodes.iterator(), new DynamicLinkedString()));
            }

            private void prepareNextNode(Pair pair) {
                DynamicLinkedString prefix = pair.prefix;
                HashTable<Byte, TNode<K, V>> table = pair.node.nodes;
                Byte b = null;
                if (pair.iterator.hasNext()) {
                    b = pair.iterator.next();
                    prevNodes.push(pair);
                }
                if (b == null) {
                    prepareNextNode(prevNodes.poll());
                    return;
                }
                TNode<K, V> nextNode = table.get(b);
                Iterator<Byte> nextIterator = nextNode.nodes.iterator();
                Pair newPair = new Pair(nextNode, nextIterator, new DynamicLinkedString(prefix));
                newPair.prefix.add(b);
                if (table.get(b).isEnd) {
                    endNode = table.get(b);
                    prevNodes.push(newPair);
                    return;
                }
                prevNodes.push(newPair);
                prepareNextNode(prevNodes.poll());
            }

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public HashNode<K, V> next() {
                if (count++ >= size) throw new NoSuchElementException();
                prepareNextNode(prevNodes.poll());
                return endNode;
            }
        }
    }

    /**
     * Helps to print this trieMap
     */
    private AbstractDynamicString getAll(AbstractDynamicString result, AbstractDynamicString current, HashTable<Byte, TNode<K, V>> nodes) {
        for (Byte b : nodes) {
            TNode<K, V> node = nodes.get(b);
            current.add(node.hashNum);
            if (node.isEnd) result.add(node.key).add(':').add(node.values).add(", ");
            getAll(result, current, node.nodes);
            current.deleteLast();
        }
        return result;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.root.nodes.clear();
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        AbstractDynamicString res = getAll(new DynamicLinkedString("{"), new DynamicLinkedString(), root.nodes);
        return res.replace(res.getSize() - 2, '}').toString();
    }

}
