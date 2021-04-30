package tests.HashTableTests;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public class HashTable<K, V> implements Iterable<K>{

    Node<K, V>[] Table;

    private final static int DEFAULT_CAPACITY = 32;
    private final double LOAD_FACTOR = 0.75;
    private int CAPACITY;
    private int size;

    private static class Node<K, V> {
        final K key;
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
        initTable(this.CAPACITY);
        this.size = 0;

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

    private int generateHash(K key) {
        if (key == null) return 0;
        return key.hashCode() ^ key.hashCode() >>> 16;
    }

    private int getPosByHash(K key) {
        int hash = generateHash(key);
        return (CAPACITY - 1) & hash;
    }

    /**
     * Method which provide associate specified key with specified value in the hashTable
     *
     * @param key key of bucket in the hashTable
     * @param value value by key in the bucket
     * @throws IllegalArgumentException if key is null
     */
    public void add(K key, V value) {
        if (key == null){
            throw new IllegalArgumentException("Key must be not null");
        }
        if ((CAPACITY / (++size)) >= (int) LOAD_FACTOR) {
            resizeTable();
        }
        final int position = getPosByHash(key);
        addToBucket(position, new Node<>(key, value));

    }

    private void addToBucket(int pos, Node<K, V> node) {
        if (Table[pos] != null) {
            node.next = Table[pos];
        }
        Table[pos] = node;
    }

    /**
     * Method which provide get size of the hashTable
     * @return size of the hashTable
     */
    public int getSize() {
        return size;
    }

    /**
     * Method which provide get count of keys in the hashTable
     * @return count of keys in the hashTable
     */
    public int getKeysCount() {
        return (int) Arrays.stream(Table).filter(Objects::nonNull).count();
    }

    public void clear() {
        initTable(CAPACITY);
        this.size = 0;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super K> action) {

    }

    @Override
    public Spliterator<K> spliterator() {
        return null;
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