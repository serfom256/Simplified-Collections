package HashSet;

import HashTables.HashTable;

public class Set<E> {
    private final HashTable<E, Object> hashTable;
    private final Object VALUE = new Object();

    public Set() {
        hashTable = new HashTable<>();
    }

    public Set(int capacity) {
        hashTable = new HashTable<>(capacity);
    }

    public void add(E element) {
        hashTable.add(element, VALUE);
    }

    public void remove(E element) {
        hashTable.remove(element);
    }

    public boolean contains(E element) {
        return hashTable.containsKey(element);
    }

    public E update(E OldElement, E newElement) {
        return hashTable.replace(OldElement, newElement);
    }

    /**
     * Method which provide get size of the Set
     *
     * @return size of the Set
     */
    public int getSize() {
        return hashTable.getSize();
    }


    /**
     * Clear current Set
     */
    public void clear() {
        hashTable.clear();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("{");
        for (E e : hashTable) {
            res.append(e).append(", ");
        }
        return res.substring(0, res.length() - 2) + "}";
    }
}