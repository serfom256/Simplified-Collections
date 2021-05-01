package tests.HashTableTests;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class HashTable<K, V> implements Iterable<K> {

    Node<K, V>[] Table;

    private final static int DEFAULT_CAPACITY = 32;
    private final double LOAD_FACTOR = 0.75;
    private int CAPACITY;
    private int size;

    private static class Node<K, V> {
        final K key;
        int length;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }
    }

    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    public HashTable(int capacity) {
        this.CAPACITY = capacity;
        this.size = 0;
        initTable(this.CAPACITY);

    }

    @SuppressWarnings("unchecked")
    private void initTable(int capacity) {
        Table = (Node<K, V>[]) new Node[capacity];
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void resizeTable() {
        int newCapacity = (CAPACITY + CAPACITY >> 1);
        Node[] newTab = new Node[newCapacity];
        System.arraycopy(Table, 0, newTab, 0, CAPACITY);
        CAPACITY = newCapacity;
        Table = (Node<K, V>[]) newTab;
    }

    /**
     * Compute hashCode by key
     *
     * @return computed hashCode for specified key
     */
    private int generateHash(K key) {
        if (key == null) return 0;
        return key.hashCode() ^ key.hashCode() >>> 16;
    }

    /**
     * Returns position in the HashTable by hashCode of the specified key
     *
     * @return position in the HashTable by key
     */
    private int getPosByHash(K key) {
        int hash = generateHash(key);
        return (CAPACITY - 1) & hash;
    }

    /**
     * Append and associate specified key with specified value in the HashTable
     *
     * @param key   key to associate with specified value
     * @param value value to associate with specified key
     * @throws IllegalArgumentException if key is null
     */
    public void add(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        if ((CAPACITY / (++size)) >= (int) LOAD_FACTOR) {
            resizeTable();
        }
        addToBucket(getPosByHash(key), new Node<>(key, value));
    }

    /**
     * Add Specified node to the Table[pos] position
     *
     * @param pos  position in the HashTable to insert the bucket(node)
     * @param node bucket for insertion
     */
    private void addToBucket(int pos, Node<K, V> node) {
        if (Table[pos] != null) {
            node.next = Table[pos];
            node.length++;
        }
        node.length++;
        Table[pos] = node;
    }

    /**
     * Remove specified value in the bucket
     *
     * @param bucket bucket from which to remove the value
     * @param value  value to remove
     * @return removed value specified node the bucket
     */
    private V removeValue(Node<K, V> bucket, V value) {
        Node<K, V> prev = null;
        Node<K, V> replaceNode = bucket;
        while (bucket.next != null && !bucket.value.equals(value)) {
            prev = bucket;
            bucket = bucket.next;
        }
        V toRemove = null;
        if (bucket.value.equals(value)) {
            toRemove = bucket.value;
            K key = bucket.key;

            if (prev == null) {
                replaceNode = replaceNode.next;
            } else if (bucket.next == null) {
                prev.next = null;
            } else {
                prev.next = bucket.next;
            }

            if (replaceNode != null) {
                replaceNode.length--;
                Table[getPosByHash(bucket.key)] = replaceNode;
            } else {
                remove(key);
            }
        }
        return toRemove;
    }

    /**
     * Remove specified value by key in the HashTable
     *
     * @param key   for bucket from which to remove the value
     * @param value value to remove
     * @return removed value by specified key in the HashTable
     */
    public V removeValue(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        int pos = getPosByHash(key);
        if (Table[pos] == null) {
            return null;
        }
        return removeValue(Table[pos], value);
    }


    /**
     * Remove specified key with all associated values
     *
     * @param key key from  HashTable to remove
     * @return value from removed key
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        V toRemove = Table[getPosByHash(key)].value;
        Table[getPosByHash(key)] = null;
        return toRemove;
    }

    /**
     * Update value by the specified key
     *
     * @param key   key which associated value updates
     * @param value value to update
     * @return removed value if HashTable contains value by specified key
     */
    public V update(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        int pos = getPosByHash(key);
        V removedVal = null;
        if (Table[pos] != null) {
            removedVal = Table[pos].value;
            Table[pos].value = value;
        }
        return removedVal;
    }


    /**
     * Returns value by key in the hashTable
     *
     * @param key key of value in the hashTable
     * @return value by the specified key if key not equals null otherwise null
     * @throws IllegalArgumentException if key is null
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        int pos = getPosByHash(key);
        return Table[pos] != null ? Table[pos].value : null;
    }

    /**
     * Return if HashTable contains specified value
     *
     * @param value value of the all values in the HashTable
     * @return true if value found else false
     */
    public boolean containsValue(V value) {
        for (int i = 0; i < CAPACITY; i++) {
            if (Table[i] != null) {
                for (Node<K, V> current = Table[i]; current != null; current = current.next) {
                    if (current.value.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Return first key by the specified value in the HashTable
     *
     * @param value value
     * @return key which associated specified value in the HashTable if not found returns  null
     */
    public K getKeyByValue(V value) {
        for (int i = 0; i < CAPACITY; i++) {
            if (Table[i] != null) {
                for (Node<K, V> current = Table[i]; current != null; current = current.next) {
                    if (current.value.equals(value)) {
                        return current.key;
                    }
                }
            }
        }
        return null;
    }


    /**
     * Returns true if HashTable contains key
     *
     * @param key key of the values in the HashTable
     * @return true if HashTable contains specified key, otherwise false
     * @throws IllegalArgumentException if key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        return Table[getPosByHash(key)] != null;
    }

    /**
     * Returns values which associated specified key in the HashTable
     *
     * @param key key of the values in the HashTable
     * @return array of values by the specified key if HashTable contains specified key, otherwise null
     * @throws IllegalArgumentException if key is null
     */
    public V[] getValues(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        if (Table[getPosByHash(key)] == null) {
            return null;
        }
        return getValuesPos(getPosByHash(key));
    }

    /**
     * Returns values by key from specified position in the HashTable
     *
     * @param pos position to get values
     * @return array of values by position
     */
    @SuppressWarnings("unchecked")
    public V[] getValuesPos(int pos) {
        V[] values = (V[]) new Object[Table[pos].length];
        int i = 0;
        for (Node<K, V> current = Table[pos]; current != null; current = current.next) {
            values[i++] = current.value;
        }
        return values;
    }

    /**
     * Method which provide get size of the hashTable
     *
     * @return size of the hashTable
     */
    public int getSize() {
        return size;
    }

    /**
     * Method which provide get count of keys in the hashTable
     *
     * @return count of keys in the hashTable
     */
    public int getKeysCount() {
        return (int) Arrays.stream(Table).filter(Objects::nonNull).count();
    }

    /**
     * Clear all HashTable
     */
    public void clear() {
        for (int i = 0; i < CAPACITY; i++) {
            Table[i] = null;
        }
        this.size = 0;
    }

    @Override
    public Iterator<K> iterator() {
        return new SelfIterator<K>();
    }

    private class SelfIterator<T> implements Iterator<K> {
        private int pos;

        SelfIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            for (int i = pos; i < CAPACITY; i++) {
                if (Table[i] != null) {
                    pos = i;
                    return true;
                }
            }
            return false;
        }

        @Override
        public K next() {
            return Table[pos++].key;
        }
    }


    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder res = new StringBuilder("[");
        for (int i = 0; i < Table.length - 1; i++) {
            if (Table[i] != null) {
                res.append("{").append(Table[i].key).append(":");
                Node<K, V> current = Table[i];
                while (current.next != null) {
                    res.append(current.value).append(",");
                    current = current.next;
                }
                res.append(current.value).append("}, ");
            }
        }
        return res.replace(res.length() - 2, res.length() - 1, "]").toString();
    }
}