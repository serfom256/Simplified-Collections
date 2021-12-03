package HashSet;

import Additional.DynamicString.AbstractDynamicString;
import Additional.DynamicString.DynamicLinkedString;
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
        if (hashTable.delete(element) != null) {
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
     * @param oldElement element to replace
     * @param newElement new element to replace old element
     * @throws IllegalArgumentException if (OldElement or newElement) is null
     */
    @Override
    public void update(E oldElement, E newElement) {
        hashTable.replace(oldElement, newElement);
    }

    @Override
    public Set<E> left(AbstractSet<E> set) {
        Set<E> left = new Set<E>((int) (set.getSize() * 1.2));
        for (E element : set) {
            if (!this.contains(element)) left.add(element);
        }
        return left;
    }

    @Override
    public Set<E> right(AbstractSet<E> set) {
        Set<E> right = new Set<E>(hashTable.getCapacity());
        for (E element : this) {
            if (!set.contains(element)) right.add(element);
        }
        return right;
    }

    @Override
    public Set<E> between(AbstractSet<E> set) {
        Set<E> mid = new Set<E>(64);
        for (E element : set) {
            if (this.contains(element)) mid.add(element);
        }
        return mid;
    }

    @Override
    public Set<E> union(AbstractSet<E> set) {
        Set<E> union = new Set<E>((int) ((hashTable.getSize() + set.getSize()) * 1.4));
        Iterator<E> foreignIterator = set.iterator();
        Iterator<E> thisIterator = this.iterator();
        while (foreignIterator.hasNext() && iterator().hasNext()) {
            union.add(foreignIterator.next());
            union.add(thisIterator.next());
        }
        while (foreignIterator.hasNext()) {
            union.add(foreignIterator.next());
        }
        while (thisIterator.hasNext()) {
            union.add(thisIterator.next());
        }
        return union;
    }

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
        AbstractDynamicString res = new DynamicLinkedString("{");
        for (E e : hashTable) {
            res.add(e).add(", ");
        }
        return res.subSequence(0, res.getSize() - 2).add('}').toString();
    }
}