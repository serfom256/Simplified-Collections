package hashtables;


import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.NullableArgumentException;
import additional.nodes.HashNode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class HashTable<K, V> implements Iterable<K>, Map<K, V> {

    private Node<K, V>[] table;

    public static final int DEFAULT_CAPACITY = 32;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final int MIN_CAPACITY = 1 << 1;
    private static final double LOAD_FACTOR = 0.8;
    private int capacity;
    private int size;
    public final Items items;

    private static class Node<K, V> implements HashNode<K, V> {
        final K key;
        final int hash;
        V value;
        Node<K, V> next;

        public Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            next = null;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public int getHash() {
            return hash;
        }
    }

    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    public HashTable(int capacity) {
        this.capacity = DEFAULT_CAPACITY;
        this.capacity = getPowerOfTwoCap(capacity);
        this.size = 0;
        items = new Items();
    }

    @SuppressWarnings("unchecked")
    private void initTable(int capacity) {
        table = new Node[capacity];
    }

    /**
     * Resize Table if current Table full more than LOAD_FACTOR %
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void resizeTable() {
        if (capacity == MAX_CAPACITY) return;
        int newCapacity = getPowerOfTwoCap(capacity << 1);
        Node[] newTab = new Node[newCapacity];
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (Node<K, V> current = table[i]; current != null; current = current.next) {
                    addToBucket(getPosByKey(current.key, newCapacity), new Node<>(current.key, current.value, current.hash), newTab);
                }
            }
        }
        table = newTab;
        capacity = newCapacity;
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
     * Returns position in the HashTable by hashCode of the specified key
     *
     * @return position in the HashTable by key
     * @throws NullableArgumentException if the specified key is null
     */
    private int getPosByKey(K key, int capacity) {
        if (key == null) {
            throw new NullableArgumentException("Specified key must be not null");
        }
        int pos = generateHash(key) % capacity;
        return pos < 0 ? -pos : pos;
    }

    /**
     * Appends and associate specified key with specified value in the HashTable
     * If the current table size equals or higher than threshold param(table capacity * table load factor) table will be resized
     *
     * @param key   key to associate with specified value
     * @param value value to associate with specified key
     * @throws NullableArgumentException if the specified key is null
     */
    @Override
    public void add(K key, V value) {
        if (key == null) throw new NullableArgumentException("Specified key must be not null");
        if (size + 1 < 0) throw new NullableArgumentException("Out of the HashTable memory");
        if (table == null) initTable(capacity);
        if ((++size) >= capacity * LOAD_FACTOR) {
            resizeTable();
        }
        addToBucket(getPosByKey(key, capacity), new Node<>(key, value, generateHash(key)), table);
    }

    /**
     * Add Specified node to the Table[pos] position
     *
     * @param pos  position in the HashTable to insert the bucket(node)
     * @param node bucket for insertion
     */
    private void addToBucket(int pos, Node<K, V> node, Node<K, V>[] table) {
        if (table[pos] != null) {
            for (Node<K, V> current = table[pos]; current != null; current = current.next) {
                if (node.hash == current.hash && node.key.equals(current.key)) {
                    if (node.value.equals(current.value)) {
                        size--;
                        return;
                    }
                    break;
                }
            }
            node.next = table[pos];
        }
        table[pos] = node;
    }

    /**
     * Removes specified value which associated with the key
     * Also removes the specified key if the specified key has only one value
     *
     * @param value value associated with key to remove
     * @return removed value if value is present otherwise null
     * @throws NullableArgumentException if the specified key is null
     */
    @Override
    public boolean deleteValue(K key, V value) {
        int pos = getPosByKey(key, capacity);
        if (table == null || table[pos] == null) return false;
        int keyHash;
        if (table[pos].hash == (keyHash = generateHash(key)) && table[pos].value.equals(value)) {
            table[pos] = table[pos].next;
            size--;
            return true;
        }
        for (Node<K, V> current = table[pos]; current.next != null; current = current.next) {
            if (current.next.hash == keyHash && current.next.value.equals(value)) {// if node with specified value found
                if (current.next.next == null) { // if removed item is last in the bucket
                    current.next = null;
                } else {
                    current.next = current.next.next; //if removed item isn't last in the bucket
                }
                size--;
                return true;
            }
        }
        return false;
    }

    /**
     * Remove bucket with specified hash in the HashTable
     *
     * @param pos position of bucket from which remove item by hash
     * @param key key to remove
     * @return removed bucket with specified hash
     */
    private V removeKey(int pos, K key) {
        final int hash = generateHash(key);
        V value = table[pos].value;
        if (table[pos].hash == hash && table[pos].key.equals(key)) {
            table[pos] = table[pos].next;
            size--;
            return value;
        }
        for (Node<K, V> current = table[pos]; current.next != null; current = current.next) {
            if (current.next.hash == hash) {
                value = current.next.value;
                if (current.next.next == null) { // if remove item is last in the bucket
                    current.next = null;
                } else {
                    current.next = current.next.next; //if remove item isn't last in the bucket
                }
                size--;
                return value;
            }
        }
        return null;
    }

    /**
     * Removes key with associated value
     *
     * @param key key for associated value from which to remove
     * @return value of the specified key
     * @throws NullableArgumentException if the specified key is null
     */
    @Override
    public V delete(K key) {
        int pos = getPosByKey(key, capacity);
        if (table == null || table[pos] == null) {
            return null;
        }
        return removeKey(pos, key);
    }

    /**
     * Updates value by the specified key
     *
     * @param key   key which associated value updates
     * @param value value to update
     * @return updated value if HashTable contains value by specified key
     * @throws NullableArgumentException if the specified key is null
     */
    public V updateValue(K key, V value) {
        if (key == null) throw new NullableArgumentException("Specified key must be not null");
        if (table == null) return null;
        int keyHash = generateHash(key);
        V oldVal = null;
        for (Node<K, V> current = table[getPosByKey(key, capacity)]; current != null; current = current.next) {
            if (current.hash == keyHash && key.equals(current.key)) {
                oldVal = current.value;
                current.value = value;
                break;
            }
        }
        return oldVal;
    }

    /**
     * Replaces oldKey by newKey with saving merging oldKey and newKey values
     *
     * @param oldKey old key of item to replace
     * @param newKey new key to replace oldKey
     * @return true if replacement done otherwise false
     * @throws NullableArgumentException if (oldKey or newKey) is null
     */
    @Override
    public boolean replace(K oldKey, K newKey) {
        int pos = getPosByKey(oldKey, capacity);
        if (table == null || table[pos] == null || !containsKey(oldKey)) {
            return false;
        }
        addToBucket(
                getPosByKey(newKey, capacity),
                new Node<>(newKey, removeKey(pos, oldKey), generateHash(newKey)),
                table);
        size++;
        return true;
    }

    /**
     * Returns value by key in the HashTable
     *
     * @param key key of value in the HashTable
     * @return value by the specified key if key not equals null otherwise null
     * @throws NullableArgumentException if the specified key is null
     */
    @Override
    public V get(K key) {
        if (table == null) return null;
        int pos = getPosByKey(key, capacity);
        int keyHash = generateHash(key);
        if (table[pos] != null) {
            for (Node<K, V> current = table[pos]; current != null; current = current.next) {
                if (current.hash == keyHash && key.equals(current.key)) {
                    return current.value;
                }
            }
        }
        return null;
    }

    /**
     * Return if HashTable contains specified value
     *
     * @param value value of the all values in the HashTable
     * @return true if value founded else false
     */
    public boolean containsValue(V value) {
        if (value == null) return getNVKey() != null;
        if (table == null) return false;
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (Node<K, V> current = table[i]; current != null; current = current.next) {
                    if (current.value.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns first key with nullable value
     */
    private K getNVKey() {
        if (table == null) return null;
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (Node<K, V> current = table[i]; current != null; current = current.next) {
                    if (current.value == null) {
                        return current.key;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Return first key by the specified value in the HashTable
     *
     * @param value value
     * @return key which associated specified value in the HashTable if not found returns null
     */
    public K getKeyByValue(V value) {
        if (table == null) return null;
        if (value == null) return getNVKey();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (Node<K, V> current = table[i]; current != null; current = current.next) {
                    if (current.value.equals(value)) {
                        return current.key;
                    }
                }
            }
        }
        return null;
    }

    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>(capacity);
        if (table == null) return keySet();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (Node<K, V> current = table[i]; current != null; current = current.next) {
                    keySet.add(current.getKey());
                }
            }
        }
        return keySet;
    }

    public Set<V> valueSet() {
        Set<V> valueSet = new HashSet<>(capacity);
        if (table == null) return valueSet();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (Node<K, V> current = table[i]; current != null; current = current.next) {
                    valueSet.add(current.getValue());
                }
            }
        }
        return valueSet;
    }

    /**
     * Returns true if HashTable contains key
     *
     * @param key key of the values in the HashTable
     * @return true if HashTable contains specified key, otherwise false
     * @throws NullableArgumentException if the specified key is null
     */
    @Override
    public boolean containsKey(K key) {
        if (table == null || key == null) return false;
        int keyHash = generateHash(key);
        for (Node<K, V> current = table[getPosByKey(key, capacity)]; current != null; current = current.next) {
            if (current.hash == keyHash && key.equals(current.key)) {
                return true;
            }
        }
        return false;
    }

    public final class Items implements Iterable<HashNode<K, V>> {
        @Override
        public Iterator<HashNode<K, V>> iterator() {
            return new NodesIterator();
        }

        private class NodesIterator implements Iterator<HashNode<K, V>> {
            private int pos;
            private Node<K, V> current;

            NodesIterator() {
                pos = 0;
                current = null;
            }

            @Override
            public boolean hasNext() {
                if (table == null) return false;
                for (int i = pos; i < capacity; i++, pos++) {
                    if (table[i] != null) return true;
                }
                return false;
            }

            @Override
            public HashNode<K, V> next() {
                for (int i = pos; i < capacity; i++) {
                    if (table[i] != null) {
                        pos = i;
                        if (current == null) current = table[pos];
                        HashNode<K, V> hashNode = current;
                        current = current.next;
                        if (current == null) pos++;
                        return hashNode;
                    }
                }
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Provides get size of the HashTable
     *
     * @return size of the HashTable
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Provides get capacity of the HashTable
     *
     * @return capacity of the HashTable
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Clear current HashTable
     */
    public void clear() {
        if (table == null) return;
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
        this.size = 0;
    }

    /**
     * Updates table capacity
     */
    public HashTable<K, V> setCapacity(int cap) {
        this.capacity = getPowerOfTwoCap(cap);
        return this;
    }

    private int getPowerOfTwoCap(int cap) {
        if (cap == capacity || cap < MIN_CAPACITY || cap <= size) return capacity;
        int powerOfTwoCap = capacity;
        if (cap < capacity) {
            while (cap < powerOfTwoCap) {
                powerOfTwoCap >>= 1;
            }
            if (powerOfTwoCap < cap) powerOfTwoCap <<= 1;
        } else {
            while (cap > powerOfTwoCap && powerOfTwoCap > 0) {
                powerOfTwoCap <<= 1;
            }
            if (powerOfTwoCap < 0) powerOfTwoCap = MAX_CAPACITY;
        }
        return powerOfTwoCap;
    }

    @Override
    public Iterator<K> iterator() {
        return new SelfIterator();
    }

    private class SelfIterator implements Iterator<K> {
        private int pos;
        private Node<K, V> current;

        SelfIterator() {
            pos = 0;
            current = null;
        }

        @Override
        public boolean hasNext() {
            if (table == null) return false;
            for (int i = pos; i < capacity; i++, pos++) {
                if (table[i] != null) return true;
            }
            return false;
        }

        @Override
        public K next() {
            for (int i = pos; i < capacity; i++) {
                if (table[i] != null) {
                    pos = i;
                    if (current == null) current = table[pos];
                    K key = current.key;
                    current = current.next;
                    if (current == null) pos++;
                    return key;
                }
            }
            throw new NoSuchElementException();
        }
    }

    /**
     * Return string of all bucket keys and values which associate with keys
     *
     * @param node node to convert to String
     * @return string of all node keys and node values
     */
    private String bucketToString(Node<K, V> node) {
        DynamicString res = new DynamicLinkedString("[").add(node.key).add(":").add(node.value);
        Node<K, V> prev = node;
        for (Node<K, V> current = node.next; current != null; current = current.next) {
            if (prev.hash != current.hash) {
                res.add("], [").add(current.key).add(":").add(current.value);
            }
            prev = current;
        }
        return res.add("], ").toString();
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }
        DynamicString res = new DynamicLinkedString("{");
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                res.add(bucketToString(table[i]));
            }
        }
        return res.replace(res.getSize() - 2, "}").toString();
    }
}
