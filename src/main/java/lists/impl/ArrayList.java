package lists.impl;

import additional.DynamicString.AbstractDynamicString;
import additional.DynamicString.DynamicLinkedString;
import lists.AbstractList;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements AbstractList<E> {

    private static final int DEFAULT_CAPACITY = 20;
    private int capacity;
    private int size;
    private E[] data;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayList(int capacity) {
        this.capacity = capacity;
        this.size = 0;
    }

    @SafeVarargs
    public final void addAll(E... data) {
        for (E element : data) {
            add(element);
        }
    }

    /**
     * Add all data from Iterable objects in the end of list
     *
     * @param data it is all iterable object of elements to add
     */
    @Override
    public <T extends Iterable<E>> void addFrom(T data) {
        for (E obj : data) {
            add(obj);
        }
    }

    /**
     * Add specified element to the list
     *
     * @param element element to append
     */
    @Override
    public void add(E element) {
        if (data == null) init(capacity);
        data[size] = element;
        if (++size >= capacity) {
            capacity += (capacity >> 1);
            resize(capacity);
        }
    }

    /**
     * Insert element at the specified position
     *
     * @param element element to add to the position
     * @param pos     this is position to insert the element
     */
    @Override
    public void insert(int pos, E element) {
        if (pos < 0 || pos > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (++size >= capacity) {
            capacity += (capacity >> 1);
            resize(capacity);
        }
        System.arraycopy(data, pos, data, pos + 1, size - pos);
        data[pos] = element;
    }

    /**
     * Removes specified element form the list
     *
     * @param element element to remove
     * @return removed element if element present in the list otherwise null
     */
    @Override
    public boolean delete(E element) {
        if (element != null) {
            for (int i = 0; i < size; i++) {
                if (data[i].equals(element)) {
                    deleteAtPosition(i);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (data[i] == null) {
                    deleteAtPosition(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes element by position
     *
     * @param pos position of element to remove
     * @return removed element from position
     * @throws ArrayIndexOutOfBoundsException if specified index out of list bounds
     */
    @Override
    public E deleteAtPosition(int pos) {
        if (pos < 0 || pos >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        E toRemove = data[pos];
        System.arraycopy(data, pos + 1, data, pos, size - pos - 1);
        data[--size] = null;
        if (size < (capacity >> 1)) {
            capacity = (capacity >> 1) + (capacity >> 2);
            resize(capacity);
        }
        return toRemove;
    }

    /**
     * Removes from the list all of the elements in specified range between start and end
     *
     * @param start start of range
     * @param end   end of range
     * @throws IllegalArgumentException if start < 0 or end > list size
     *                                  or if start index larger then end index
     */
    @Override
    public void delete(int start, int end) {
        if (start < 0 || end > size || start >= end) {
            throw new IllegalArgumentException("Invalid method parameters");
        }
        int gap = end - start;
        for (int i = start; (i + gap) < size; i++) {
            data[i] = data[i + gap];
            data[i + gap] = null;
        }
        size -= gap;
        if (size < (capacity >> 1)) {
            capacity = (capacity >> 1);
            resize(capacity);
        }
    }

    /**
     * Replaces all values in range from the specified start to the specified end with specified value
     *
     * @param start start of range
     * @param end   end of range
     * @param data  value for replacement
     * @throws IllegalArgumentException if start < 0 or end > list size
     *                                  or if start index larger then end index
     */
    @Override
    public void replace(int start, int end, E data) {
        delete(start, end);
        insert(start, data);
    }

    /**
     * Replaces all values in range from the specified start to the size of current list with specified value
     *
     * @param start start of range
     * @param data  value for replacement
     * @throws IllegalArgumentException if start < 0 or start > list size
     *                                  or if start index larger then end index
     */
    @Override
    public void replace(int start, E data) {
        delete(start, size);
        insert(start, data);
    }

    /**
     * Replaces all values in range from the specified start to the specified end with specified elements
     *
     * @param start start of range
     * @param end   end of range
     * @param data  values for replacement
     * @throws IllegalArgumentException if start < 0 or end > list size
     *                                  or if start index larger then end index
     */
    @Override
    public void replace(int start, int end, Iterable<E> data) {
        delete(start, end);
        for (E element : data) {
            insert(start++, element);
        }
    }

    /**
     * Replaces all values in range from the specified start to the size of current list with specified elements
     *
     * @param start start of range
     * @param data  values for replacement
     * @throws IllegalArgumentException if start < 0 or start > list size
     *                                  or if start index larger then end index
     */
    @Override
    public void replace(int start, Iterable<E> data) {
        delete(start, size);
        for (E element : data) {
            insert(start++, element);
        }
    }

    /**
     * Replace element in the list if element present in the list
     *
     * @param position position of element to replace
     * @param element  element to replace
     * @throws ArrayIndexOutOfBoundsException if specified index out of list bounds
     */
    @Override
    public void update(int position, E element) {
        if (position < 0 || position >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of the list bounds");
        }
        data[position] = element;
    }

    /**
     * Returns first element of list if list isn't empty otherwise null
     */
    @Override
    public E getFirst() {
        return size != 0 ? data[0] : null;
    }

    /**
     * Returns last element of list if list isn't empty otherwise null
     */
    @Override
    public E getLast() {
        return size != 0 ? data[size - 1] : null;
    }

    /**
     * Provides to get the element from list by position
     *
     * @param position position of element
     * @return element from the specified position
     * @throws ArrayIndexOutOfBoundsException if position out of list bounds
     */
    @Override
    public E get(int position) {
        if (position < 0 || position >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of the list bounds");
        }
        return data[position];
    }

    /**
     * Returns slice from the list all of the elements in specified range between start and end
     *
     * @param start start of range
     * @param end   end of range
     * @throws IllegalArgumentException if start < 0 or end > list size
     *                                  or if start index larger then end index
     */
    @Override
    public AbstractList<E> slice(int start, int end) {
        if (start < 0 || end > size || start >= end) {
            throw new IllegalArgumentException("Invalid method parameters");
        }
        AbstractList<E> sublist = new ArrayList<>();
        for (int i = start; i < end; i++) {
            sublist.add(data[i]);
        }
        return sublist;
    }

    /**
     * Returns first occurrence position of specified element in the list
     *
     * @return first position of the specified element if specified element found otherwise -1
     */
    @Override
    public int indexOf(E element) {
        if (element != null) {
            for (int i = 0; i < size; i++) {
                if (data[i].equals(element)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (data[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Returns last occurrence position of specified element in the list
     *
     * @return last position of the specified element if specified element found otherwise -1
     */
    @Override
    public int lastIndexOf(E element) {
        if (element != null) {
            for (int i = size - 1; i >= 0; i--) {
                if (data[i].equals(element)) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (data[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Sort list in ascending order
     */
    @Override
    @SuppressWarnings("unchecked")
    public void sort() {
        if (size > 1) {
            E[] temp = (E[]) new Object[size];
            System.arraycopy(data, 0, temp, 0, size);
            Arrays.sort(temp);
            System.arraycopy(temp, 0, data, 0, size);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> E[] toArray(Class<T> type) {
        E[] temp = (E[]) Array.newInstance(type, size);
        if (size >= 0) System.arraycopy(data, 0, temp, 0, size);
        return temp;
    }

    /**
     * Returns all data from current list as array of objects
     */
    @Override
    public Object[] toObjectArray() {
        Object[] array = new Object[size];
        if (size > 0) System.arraycopy(data, 0, array, 0, size);
        return array;
    }

    /**
     * Returns count of elements which equals the specified element in this list
     */
    @Override
    public int count(E element) {
        int count = 0;
        if (element != null) {
            for (int i = 0; i < size; i++) {
                if (data[i].equals(element)) {
                    count++;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (data[i] == null) {
                    count++;
                }
            }

        }
        return count;
    }

    @Override
    public int getSize() {
        return size;
    }

    /**
     * Clears current list
     */
    @Override
    public void clear() {
        init(capacity);
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void init(int capacity) {
        data = (E[]) new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        E[] newData = (E[]) new Object[capacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    @Override
    public Iterator<E> iterator() {
        return new SelfIterator();
    }


    private class SelfIterator implements Iterator<E> {
        int pos;

        public SelfIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            for (int i = pos; i < size; i++) {
                if (data[i] != null) {
                    pos = i;
                    return true;
                }
            }
            return false;
        }

        @Override
        public E next() {
            if (pos == size) throw new NoSuchElementException();
            return data[pos++];
        }

    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        AbstractDynamicString res = new DynamicLinkedString("[");
        for (int i = 0; i < size - 1; i++) {
            res.add(data[i]).add(", ");
        }
        return res.add(data[size - 1]).add("]").toString();
    }
}

