package HashSet;

import HashTables.HashTable;
import Lists.AbstractList;
import Lists.impl.ArrayList;

import java.util.Iterator;

public class Set<E> implements Iterable<E>, AbstractSet<E> {

    private final HashTable<E, Object> hashTable;
    private final Object VALUE = new Object();

    public Set() {
        hashTable = new HashTable<>();
    }

    public Set(int capacity) {
        hashTable = new HashTable<>(capacity);
    }

    @SafeVarargs
    public final void addAll(E... data) {
        for (E obj : data) {
            add(obj);
        }
    }

    /**
     * Add element to the Set
     *
     * @param element element to append
     * @throws IllegalArgumentException if element is null
     */
    @Override
    public void add(E element) {
        hashTable.add(element, VALUE);
    }

    /**
     * Remove specified element from set if set contains element
     *
     * @param element element to remove
     * @return removed element if element present in the set otherwise null
     */
    @Override
    public E remove(E element) {
        if (hashTable.remove(element) != null) {
            return element;
        }
        return null;
    }

    /**
     * Return true if Set contains specified element
     *
     * @param element test element present in the Set
     * @return true if element present in the set otherwise false
     * @throws IllegalArgumentException if element is null
     */
    @Override
    public boolean contains(E element) {
        return hashTable.containsKey(element);
    }

    @Override
    public Object[] toObjectArray() {
        AbstractList<E> result = new ArrayList<>(hashTable.getSize() + 1);
        for (E key : hashTable) result.add(key);
        return result.toObjectArray();
    }

    /**
     * Replace element in the Set if element present in the Set
     *
     * @param OldElement element to replace
     * @param newElement new element to replace old element
     * @throws IllegalArgumentException if (OldElement or newElement) is null
     */
    @Override
    public void update(E OldElement, E newElement) {
        hashTable.replace(OldElement, newElement);
    }

    /**
     * Method which provide get size of the Set
     *
     * @return size of the Set
     */
    @Override
    public int getSize() {
        return hashTable.getSize();
    }

    /**
     * Clear current Set
     */
    @Override
    public void clear() {
        hashTable.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return hashTable.iterator();
    }

    @Override
    public String toString() {
        if (hashTable.getSize() == 0) return "{}";
        StringBuilder res = new StringBuilder("{");
        for (E e : hashTable) {
            res.append(e).append(", ");
        }
        return res.substring(0, res.length() - 2) + "}";
    }
}