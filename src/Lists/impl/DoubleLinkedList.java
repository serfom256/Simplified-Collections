package Lists.impl;

import Lists.AbstractLinkedList;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;


public class DoubleLinkedList<E> implements AbstractLinkedList<E>, Iterable<E> {

    private static class Node<T> {
        T val;
        Node<T> next;
        Node<T> prev;

        Node(T val) {
            this.val = val;
        }
    }

    private Node<E> head;
    private Node<E> last;
    private int length;

    public DoubleLinkedList() {
        this.head = null;
        this.last = null;
        this.length = 0;
    }

    public DoubleLinkedList(Iterable<E> object) {
        this.head = null;
        this.last = null;
        this.length = 0;
        addFrom(object);
    }

    /**
     * Add element to end of the list
     *
     * @param element the element to add
     */
    @Override
    public void pushLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (last == null) {
            head = newNode;
            last = head;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        length++;
    }

    /**
     * Add data to the beginning of current list
     *
     * @param element the element to add
     */
    @Override
    public void pushFirst(E element) {
        Node<E> newNode = new Node<>(element);
        newNode.next = head;
        if (head != null) {
            head.prev = newNode;
        } else {
            last = newNode;
        }
        head = newNode;
        length++;
    }

    /**
     * Add specified element to end of the list
     *
     * @param element the element to add
     */
    public void add(E element) {
        pushLast(element);
    }


    @Override
    @SafeVarargs
    public final void addAll(E... data) {
        for (E obj : data) {
            pushLast(obj);
        }
    }

    /**
     * Add all data from Iterable objects in the end of list
     *
     * @param data it is all iterable object of elements to add
     */
    public <T extends Iterable<E>> void addFrom(T data) {
        for (E obj : data) {
            pushLast(obj);
        }
    }

    /**
     * Insert data at the specified position
     *
     * @param data data to add to the position
     * @param pos  this is position to insert the data
     */
    @Override
    public void insert(E data, int pos) {
        if (pos <= 0 || head == null) {
            pushFirst(data);
            return;
        }
        if (pos >= length) {
            pushLast(data);
            return;
        }
        Node<E> newNode = new Node<>(data);
        Node<E> current = head;
        length++;
        int currentPos = 0;
        while (currentPos < pos && current.next != null) {
            current = current.next;
            currentPos++;
        }
        newNode.prev = current.prev;
        current.prev = newNode;
        newNode.next = current;
        newNode.prev.next = newNode;
    }

    /**
     * Remove all duplicates from the list
     */
    //ATTENTION THIS METHOD VERY SLOW
    public void removeDuplicates() {
        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            if (count(first.val) > 1) {
                remove(first.val);
            }
        }
    }

    /**
     * Method that provides to remove the element of the list
     *
     * @param data element to remove of list
     * @return removed element
     */
    @Override
    public E remove(E data) {
        if (head != null && head.val.equals(data)) {
            return popFirst();
        }
        Node<E> first = head;
        while (first != null) {
            if (first.val.equals(data)) {
                if (first.next == null) {
                    return popLast();
                }
                length--;
                first.prev.next = first.next;
                first.next.prev = first.prev;
                return first.val;
            }
            first = first.next;
        }
        return null;
    }

    /**
     * Remove all elements which equals specified element in the list
     *
     * @param element  elements which equals will be removed
     */
    //ATTENTION THIS METHOD VERY SLOW
    public void removeAllLike(E element) {
        removeAllLike(element, 0);
    }

    /**
     * Remove all elements which equals specified element in the list
     *
     * @param element  elements which equals will be removed
     * @param startPos start position from which remove elements
     */
    //ATTENTION THIS METHOD VERY SLOW
    public void removeAllLike(E element, int startPos) {
        if (startPos < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        Node<E> node = head;
        boolean isStartFrom0 = startPos == 0;
        while (startPos > 0 && node != null) {
            node = node.next;
            startPos--;
        }

        if (node == null) {
            return;
        }
        if (isStartFrom0) {
            while (node.next != null && element.equals(node.val) && node.prev == null) {
                head = node.next;
                head.prev = null;
                node = head;
                length--;
            }
            node = node.next;
            if (node == null) {
                last = null;
                return;
            }
        }
        while (node.next != null) {
            if (element.equals(node.val)) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                length--;
            }
            node = node.next;
        }
        if (element.equals(node.val)) {
            node.prev.next = null;
            last = node.prev;
            length--;
        }
    }

    /**
     * Remove element by position
     *
     * @param position position of element
     * @return removed element from position
     * @throws ArrayIndexOutOfBoundsException if position out of list bounds
     */
    @Override
    public E pop(int position) {
        if (position < 0 && position >= length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (position == 0) return popFirst();
        if (position == length - 1) return popLast();

        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            if (pos == position) {
                E temp = first.val;
                first.prev.next = first.next;
                first.next.prev = first.prev;
                length--;
                return temp;
            }
        }
        return null;
    }

    /**
     * Remove last element
     *
     * @return last element of list if list isn't empty else return null
     * @throws ArrayIndexOutOfBoundsException if list is empty
     */
    @Override
    public E popLast() {

        if (head == null) {
            throw new ArrayIndexOutOfBoundsException("List is empty");
        }
        E toRemove = last.val;
        length--;
        last = last.prev;
        if (last != null) last.next = null;
        else head = null;
        return toRemove;
    }

    /**
     * Remove first element
     *
     * @return first element of list if list isn't empty else return null
     * @throws ArrayIndexOutOfBoundsException if list is empty
     */
    @Override
    public E popFirst() {
        if (head == null) {
            throw new ArrayIndexOutOfBoundsException("List is empty");
        }
        length--;
        E toRemove = head.val;
        head = head.next;
        if (head != null) head.prev = null;
        else last = null;
        return toRemove;
    }

    /**
     * Method that return first element of list if list isn't empty, else null
     *
     * @return fist element of the list
     */
    @Override
    public E peekFirst() {
        return head != null ? head.val : null;
    }

    /**
     * Method that return last element of list if list isn't empty, else null
     *
     * @return last element of the list
     */
    @Override
    public E peekLast() {
        return last != null ? last.val : null;
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
        if (position < 0 || position >= length) {
            throw new ArrayIndexOutOfBoundsException("Position out of list bounds");
        }
        Node<E> first = head;
        for (; position > 0; position--) {
            first = first.next;
        }
        return first.val;
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
    public DoubleLinkedList<E> slice(int start, int end) {
        if (start < 0 || end > length || start >= end) {
            throw new IllegalArgumentException("Invalid method parameters");
        }
        Node<E> curr = head;
        for (int i = start; i > 0; i--) {
            curr = curr.next;
        }
        DoubleLinkedList<E> result = new DoubleLinkedList<>();
        for (int i = start; i < end; curr = curr.next, i++) {
            result.add(curr.val);
        }
        return result;
    }

    /**
     * Set bounds by length to this list
     *
     * @param start start of bounds
     * @param end   end of bounds
     * @throws IllegalArgumentException if start < 0 or end > list size
     *                                  or if start index larger then end index
     */
    public void setLength(int start, int end) {
        if (start < 0 || end > length || start >= end) {
            throw new IllegalArgumentException("Invalid method arguments");
        }
        length = end - start;
        Node<E> curr = head;
        for (int i = start; i > 0; i--) {
            curr = curr.next;
        }
        head = curr;
        for (int i = 0; i < length - 1; i++) {
            curr = curr.next;
        }
        if (curr.next != null) {
            curr.next.prev = null;
            curr.next = null;
        }
        last = curr;
    }

    /**
     * Slices list after position
     *
     * @param start initial position to slicing
     * @return slice of current list from specified position to list length
     * @throws IllegalArgumentException if start < 0
     */
    public DoubleLinkedList<E> sliceFrom(int start) {
        return slice(start, length);
    }

    /**
     * Slices list before position
     *
     * @param end end of slice from 0
     * @return slice of current list from 0 to specified position
     * @throws IllegalArgumentException if end > list size
     */
    public DoubleLinkedList<E> sliceBefore(int end) {
        return slice(0, end);
    }

    /**
     * Sort list in in ascending order
     */
    @SuppressWarnings("unchecked")
    public void sort() {
        Object[] temp = toArray(Object.class);
        Arrays.sort(temp);
        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            first.val = (E) temp[pos];
        }
    }

    @SuppressWarnings("unchecked")
    public <T> E[] toArray(Class<T> type) {
        E[] temp = (E[]) Array.newInstance(type, length);
        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            temp[pos] = first.val;
        }
        return temp;
    }

    /**
     * Returns count of element in this list
     *
     * @param element some element
     * @return count of current elements int this list
     */
    @Override
    public int count(E element) {
        int count = 0;
        for (Node<E> first = head; first != null; first = first.next) {
            if (first.val.equals(element)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Method which provides to get first index of specified element in the current list
     *
     * @param element some element in the list
     * @return index of element in list, if list doesn't contains the element return value will be -1
     */
    public int indexOf(E element) {
        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            if (first.val.equals(element)) {
                return pos;
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
        int pos = length - 1;
        for (Node<E> first = last; first != null; first = first.prev, pos--) {
            if (first.val.equals(element)) {
                return pos;
            }
        }
        return -1;
    }


    /**
     * @return length of current list
     */
    public int getLength() {
        return length;
    }

    /**
     * method that clears all the elements in the current list
     */
    public void clear() {
        head = null;
        last = null;
        length = 0;
    }

    /**
     * Reverse linkedList
     */
    public void reverse() {
        Node<E> temp = null;
        Node<E> current = head;
        while (current != null) {
            temp = current.prev;
            current.prev = current.next;
            current.next = temp;
            current = current.prev;
        }
        if (temp != null) {
            head = temp.prev;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new SelfIterator();
    }

    class SelfIterator implements Iterator<E> {
        Node<E> current;

        public SelfIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            E data = current.val;
            current = current.next;
            return data;
        }
    }

    @Override
    protected void finalize() {
        clear();
    }

    @Override
    public String toString() {
        if (head == null) {
            return "[]";
        }
        Node<E> first = head;
        StringBuilder lst = new StringBuilder("[");
        while (first.next != null) {
            lst.append(first.val).append(", ");
            first = first.next;

        }
        return lst.toString() + first.val + "]";
    }
}

