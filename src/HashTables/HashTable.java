package HashTables;


import java.util.Iterator;

public class HashTable<K, V> implements Iterable<K> {

    Node<K, V>[] Table;

    private final static int DEFAULT_CAPACITY = 32;
    private final double LOAD_FACTOR = 0.8;
    private int CAPACITY;
    private int size;

    private static class Node<K, V> {
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

    /**
     * Resize Table if current Table full more then LOAD_FACTOR %
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void resizeTable() {
        int newCapacity = CAPACITY + (CAPACITY >> 1);
        Node[] newTab = new Node[newCapacity];
        for (int i = 0; i < CAPACITY; i++) {
            if (Table[i] != null) {
                for (Node<K, V> current = Table[i]; current != null; current = current.next) {
                    addToBucket(getPosByKey(current.key, newCapacity), new Node<>(current.key, current.value, current.hash), newTab);
                }
            }
        }
        Table = (Node<K, V>[]) newTab;
        CAPACITY = newCapacity;
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
     */
    private int getPosByKey(K key, int capacity) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        int pos = generateHash(key) % capacity;
        return pos < 0 ? -pos : pos;
    }

    /**
     * Append and associate specified key with specified value in the HashTable
     *
     * @param key   key to associate with specified value
     * @param value value to associate with specified key
     * @throws IllegalArgumentException if key is null
     */
    public void add(K key, V value) {
        if ((++size) >= CAPACITY * LOAD_FACTOR) {
            resizeTable();
        }
        addToBucket(getPosByKey(key, CAPACITY), new Node<>(key, value, generateHash(key)), Table);
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
     * Remove specified value which associated with the key
     *
     * @param key   key which value remove
     * @param value value associated with key
     * @return removed value if value is present, else null
     */
    public V removeValue(K key, V value) {
        int pos = getPosByKey(key, CAPACITY);
        if (Table[pos] == null) {
            return null;
        }
        int keyHash;
        if (Table[pos].hash == (keyHash = generateHash(key)) && Table[pos].value.equals(value)) {
            Table[pos] = Table[pos].next;
            size--;
            return value;
        }
        for (Node<K, V> current = Table[pos]; current.next != null; current = current.next) {
            if (current.next.value.equals(value) && current.next.hash == keyHash) {// if node with specified value found
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
     * Remove bucket with specified hash in the HashTable
     *
     * @param pos  position of bucket from which remove item by hash
     * @param hash of removed bucket
     * @return removed bucket with specified hash
     */
    private V removeByHash(int pos, int hash) {
        V value = Table[pos].value;
        int start = 0;
        if (Table[pos].hash == hash) {
            Table[pos] = Table[pos].next;
            size--;
            return value;
        }
        for (Node<K, V> current = Table[pos]; current.next != null; current = current.next, start++) {
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
     * Remove key with associated value
     *
     * @param key key for associated value from which to remove
     * @return removed key and associated value by specified key in the HashTable
     * @throws IllegalArgumentException if key is null
     */
    public V remove(K key) {
        int pos = getPosByKey(key, CAPACITY);
        if (Table[pos] == null) {
            return null;
        }
        return removeByHash(pos, generateHash(key));
    }

    /**
     * Update value by the specified key
     *
     * @param key   key which associated value updates
     * @param value value to update
     * @return updated value if HashTable contains value by specified key
     * @throws IllegalArgumentException if key is null
     */
    public V updateValue(K key, V value) {
        int keyHash = generateHash(key);
        V oldVal = null;
        for (Node<K, V> current = Table[getPosByKey(key, CAPACITY)]; current != null; current = current.next) {
            if (current.hash == keyHash) {
                oldVal = current.value;
                current.value = value;
                break;
            }
        }
        return oldVal;
    }

    /**
     * Replace old item with specified key to new item with specified
     *
     * @param oldKey old key of item to replace
     * @param newKey new key to replace oldKey
     * @return replaced key if updated is successful otherwise null
     * @throws IllegalArgumentException if (oldKey or newKey) is null
     */
    public K replace(K oldKey, K newKey) {
        int pos;
        if (Table[pos = getPosByKey(oldKey, CAPACITY)] == null || !containsKey(oldKey)) {
            return null;
        }
        addToBucket(
                getPosByKey(newKey, CAPACITY),
                new Node<>(newKey, removeByHash(pos, generateHash(oldKey)), generateHash(newKey)),
                Table);
        size++;
        return oldKey;
    }

    /**
     * Returns value by key in the hashTable
     *
     * @param key key of value in the hashTable
     * @return value by the specified key if key not equals null otherwise null
     * @throws IllegalArgumentException if key is null
     */
    public V get(K key) {
        int pos = getPosByKey(key, CAPACITY);
        int keyHash = generateHash(key);
        if (Table[pos] != null) {
            for (Node<K, V> current = Table[pos]; current != null; current = current.next) {
                if (current.hash == keyHash) {
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
        int keyHash = generateHash(key);
        for (Node<K, V> current = Table[getPosByKey(key, CAPACITY)]; current != null; current = current.next) {
            if (current.hash == keyHash) {
                return true;
            }
        }
        return false;
    }

    //TODO implement method keySet and add Entry class which represent bucket and do more tests
//    public K[] keySet() {
//        AbstractList<K> result = new ArrayList<>(size>>1);
//        for (Node<K, V> bucket : Table){
//            if(bucket != null){
//                Node<K,V> start = bucket;
//                while (bucket != null){
//                    result.add(bucket.key);
//                    bucket = bucket.next;
//                }
//            }
//        }
//    }

    /**
     * Method which provide get size of the HashTable
     *
     * @return size of the hashTable
     */
    public int getSize() {
        return size;
    }

    /**
     * Method which provide get capacity of the HashTable
     *
     * @return capacity of the hashTable
     */
    public int getCapacity() {
        return CAPACITY;
    }

    /**
     * Clear current HashTable
     */
    public void clear() {
        for (int i = 0; i < CAPACITY; i++) {
            Table[i] = null;
        }
        this.size = 0;
    }

    @Override
    public Iterator<K> iterator() {
        return new SelfIterator();
    }

    private class SelfIterator implements Iterator<K> {
        private int pos;
        private Node<K, V> current = null;

        SelfIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            for (int i = pos; i < CAPACITY; i++) {
                if (Table[i] != null) {
                    pos = i;
                    if (current == null) {
                        current = Table[pos];
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public K next() {
            K key = current.key;
            if ((current = current.next) == null) {
                pos++;
            }
            return key;
        }
    }

    /**
     * Return string of all bucket keys and values which associate with keys
     *
     * @param node node to convert to String
     * @return string of all node keys and node values
     */
    private String toStringBucket(Node<K, V> node) {
        StringBuilder res = new StringBuilder("[" + node.key + ":" + node.value);
        Node<K, V> prev = node;
        for (Node<K, V> current = node.next; current != null; current = current.next) {
            if (!(prev.hash == current.hash)) {
                res.append("], [").append(current.key).append(":").append(current.value);
            }
            prev = current;
        }
        return res + "], ";
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }
        StringBuilder res = new StringBuilder("{");
        for (int i = 0; i < CAPACITY; i++) {
            if (Table[i] != null) {
                res.append(toStringBucket(Table[i]));
            }
        }
        return res.replace(res.length() - 2, res.length() - 1, "}").toString();
    }
}