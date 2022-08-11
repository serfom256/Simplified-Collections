package sets;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.NullableArgumentException;
import hashtables.HashTable;
import lists.List;
import lists.impl.ArrayList;

import java.util.Iterator;

public class HashedSet<E> implements Iterable<E>, Set<E> {

    private final HashTable<E, Object> hashTable;
    private static final Object VALUE = new Object();

    public HashedSet() {
        hashTable = new HashTable<>();
    }

    public HashedSet(int capacity) {
        hashTable = new HashTable<>(capacity);
    }

    /**
     * @throws NullableArgumentException if one of specified  arguments is null
     */
    @SafeVarargs
    public final void addAll(E... data) {
        for (E obj : data) {
            add(obj);
        }
    }

    /**
     * @throws NullableArgumentException if one of specified  arguments is null
     */
    @Override
    public <T extends Iterable<E>> void addFrom(T iterable) {
        for (E e : iterable) add(e);
    }

    /**
     * Add element to the Set
     *
     * @param element element to append
     * @throws NullableArgumentException if the specified element is null
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
     * @throws NullableArgumentException if the specified element is null
     */
    @Override
    public boolean delete(E element) {
        return hashTable.delete(element) != null;
    }

    /**
     * Return true if Set contains specified element
     *
     * @param element test element present in the Set
     * @return true if element present in the set otherwise false
     * @throws NullableArgumentException if the specified element is null
     */
    @Override
    public boolean contains(E element) {
        return hashTable.containsKey(element);
    }

    @Override
    public Object[] toObjectArray() {
        List<E> result = new ArrayList<>(hashTable.getSize() + 1);
        for (E key : hashTable) result.add(key);
        return result.toObjectArray();
    }

    /**
     * Returns set of elements from the specified set which isn't presents in the specified set
     * {1, 2, 3}.left({3, 4, 5, 6}) => {1, 2}
     *
     * @throws NullableArgumentException if the specified set is null
     */
    @Override
    public HashedSet<E> left(Set<E> set) {
        if (set == null) throw new NullableArgumentException();
        HashedSet<E> left = new HashedSet<>(hashTable.getCapacity());
        for (E element : set) {
            if (!this.contains(element)) left.add(element);
        }
        return left;
    }

    /**
     * Returns set of elements from the specified set which isn't presents in this set
     * {1, 2, 3}.right({3, 4, 5, 6}) => {4, 5, 6}
     *
     * @throws NullableArgumentException if the specified set is null
     */
    @Override
    public HashedSet<E> right(Set<E> set) {
        if (set == null) throw new NullableArgumentException();
        HashedSet<E> right = new HashedSet<>(hashTable.getCapacity());
        for (E element : this) {
            if (!set.contains(element)) right.add(element);
        }
        return right;
    }

    /**
     * Returns set of crossing elements from this set and specified set
     * {1, 2, 3, 4}.between({1, 3, 4, 5, 6}) => {1, 3, 4}
     *
     * @throws NullableArgumentException if the specified set is null
     */
    @Override
    public HashedSet<E> between(Set<E> set) {
        if (set == null) throw new NullableArgumentException();
        HashedSet<E> mid = new HashedSet<>();
        for (E element : set) {
            if (this.contains(element)) mid.add(element);
        }
        return mid;
    }

    /**
     * Returns union of this set and specified set
     * {1, 2, 3, 4}.union({4, 5, 6}) => {1, 2, 3, 4, 5, 6}
     *
     * @return union of this and specified set
     * @throws NullableArgumentException if the specified set is null
     */
    @Override
    public HashedSet<E> union(Set<E> set) {
        if (set == null) throw new NullableArgumentException();
        HashedSet<E> union = new HashedSet<>((int) ((hashTable.getSize() + set.getSize()) * 1.4));
        Iterator<E> foreignIterator = set.iterator();
        Iterator<E> selfIterator = this.iterator();
        while (foreignIterator.hasNext() && selfIterator.hasNext()) {
            union.add(foreignIterator.next());
            union.add(selfIterator.next());
        }
        while (foreignIterator.hasNext()) {
            union.add(foreignIterator.next());
        }
        while (selfIterator.hasNext()) {
            union.add(selfIterator.next());
        }
        return union;
    }

    @Override
    public int getSize() {
        return hashTable.getSize();
    }

    public int getCapacity() {
        return hashTable.getCapacity();
    }

    public void setCapacity(int cap) {
        hashTable.setCapacity(cap);
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
        DynamicString res = new DynamicLinkedString("{");
        for (E e : hashTable) {
            res.add(e).add(", ");
        }
        return res.subSequence(0, res.getSize() - 2).add('}').toString();
    }
}