package Lists;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayList<E> implements AbstractList<E>, Iterable<E> {

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
        init(capacity);
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
    @SafeVarargs
    public final void addAll(E... data) {
        for (E element : data) {
            add(element);
        }
    }

    /**
     * Add specified element to the list
     *
     * @param element element to append
     */
    @Override
    public void add(E element) {
        data[size] = element;
        if (++size >= capacity) {
            capacity += (capacity >> 1);
            resize(capacity);
        }
    }

    public void print() {
        System.out.println(Arrays.toString(data));
    }

    /**
     * Insert element at the specified position
     *
     * @param element element to add to the position
     * @param pos     this is position to insert the element
     */
    @Override
    public void insert(E element, int pos) {
        if (pos < 0 || pos >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of the list bounds");
        }
        if (++size >= capacity) {
            capacity += (capacity >> 1);
            resize(capacity);
        }
        System.arraycopy(data, pos, data, pos + 1, size - pos);
        data[pos] = element;
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

    /**
     * Clear current list
     */
    @Override
    public void clear() {
        init(capacity);
        size = 0;
    }

    /**
     * Remove element by position
     *
     * @param pos position of element to remove
     * @return removed element from position
     * @throws ArrayIndexOutOfBoundsException if specified index out of list bounds
     */
    @Override
    public E pop(int pos) {
        if (pos < 0 || pos >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of the list bounds");
        }
        E toRemove = data[pos];
        System.arraycopy(data, pos + 1, data, pos, size - pos - 1);
        data[--size] = null;
        if (size <= (capacity >> 1)) {
            int j = (capacity >> 1) + (capacity >> 2);
            capacity = j;
            resize(j);
        }
        return toRemove;
    }

    /**
     * Removes specified element form the list
     *
     * @param element element to remove
     * @return removed element if element present in the list otherwise null
     */
    @Override
    public E remove(E element) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(element)) {
                return pop(i);
            }
        }
        return null;
    }

    /**
     * Replace element in the list if element present in the list
     *
     * @param position position of element to replace
     * @param element  element to replace
     * @throws ArrayIndexOutOfBoundsException if specified index out of list bounds
     */
    public void update(int position, E element) {
        if (position < 0 || position >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of the list bounds");
        }
        data[position] = element;
    }

    /**
     * Removes from the list all of the elements in specified range between start and end
     *
     * @param start start of range
     * @param end   end of range
     * @throws IllegalArgumentException if start < 0 or end >= list size
     *                                  or if start index larger then end index
     */
    //FIXME not working, to fix
    public void removeRange(int start, int end) {
        if (start < 0 && end >= size && start < end) {
            throw new IllegalArgumentException("Invalid method parameters");
        }
        int gap = end - start;
        System.arraycopy(data, end, data, start, gap);
        size -= gap;
        if (size <= (capacity >> 1)) {
            int j = (capacity >> 1) + (capacity >> 2);
            capacity = j;
            resize(j);
        }
    }

    /**
     * Returns slice from the list all of the elements in specified range between start and end
     *
     * @param start start of range
     * @param end   end of range
     * @throws IllegalArgumentException if start < 0 or end >= list size
     *                                  or if start index larger then end index
     */
    public AbstractList<E> subList(int start, int end) {
        if (start < 0 && end >= size && start < end) {
            throw new IllegalArgumentException("Invalid method parameters");
        }
        AbstractList<E> sublist = new ArrayList<>();
        for (int i = start; i < end; i++) {
            sublist.add(data[i]);
        }
        return sublist;
    }

    /**
     * Method that provides to get the element from list by position
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
     * Method which provides to get first index of specified element in the current list
     *
     * @param element some element in the list
     * @return index of element in list, if list doesn't contains the element return value will be -1
     */
    @Override
    public int indexOf(E element) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Method which provides to get last index of specified element in the current list
     *
     * @param element some element in the list
     * @return index of element in list, if list doesn't contains the element return value will be -1
     */
    @Override
    public int lastIndexOf(E element) {
        for (int i = size - 1; i >= 0; i--) {
            if (data[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return length of current list
     */
    @Override
    public int getLength() {
        return size;
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
            return data[pos++];
        }

    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder res = new StringBuilder("[");
        for (int i = 0; i < size - 1; i++) {
            res.append(data[i]).append(", ");
        }
        return res.toString() + data[size - 1] + "]";
    }
}

