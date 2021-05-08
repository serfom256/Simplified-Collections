package Lists.impl;

import Lists.AbstractLinkedList;

import java.util.Arrays;
import java.util.Iterator;

public class LinkedList<E> implements AbstractLinkedList<E> {
    private static class Node<T> {
        T val;
        Node<T> next;

        Node(T val) {
            this.val = val;
        }
    }

    private Node<E> head;
    private Node<E> last;
    private int length;

    public LinkedList() {
        this.head = null;
        this.last = null;
        this.length = 0;
    }

    @Override
    @SafeVarargs
    public final void addAll(E... data) {
        for (E obj : data) {
            pushLast(obj);
        }
    }

    /**
     * Add specified element to end of the list
     * @param element the element to add
     */
    @Override
    public void add(E element) {
        pushLast(element);
    }

    /**
     * Add element to end of the list
     *
     * @param element the element to add
     */
    @Override
    public void pushLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null) {
            head = newNode;
            last = head;
        } else {
            last.next = newNode;
            last = last.next;
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
        head = newNode;
        length++;
    }


    /**
     * Method that return last element of list if list isn't empty, else null
     *
     * @return last element of the list
     */
    @Override
    public E peekLast() {
        if (length == 1) {
            return head.val;
        }
        return length != 0 ? last.val : null;
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
     * Remove first element
     *
     * @return first element of list if list isn't empty else return null
     */
    @Override
    public E popFirst() {
        if (head != null) {
            E value = head.val;
            head = head.next;
            length--;
            return value;
        } else {
            throw new ArrayIndexOutOfBoundsException("List is empty");
        }
    }

    /**
     * Remove last element
     *
     * @return last element of list if list isn't empty else return null
     */
    @Override
    public E popLast() {
        if (head == null) {
            throw new ArrayIndexOutOfBoundsException("List is empty");
        }
        E toRemove;
        if (head.next == null) {
            toRemove = head.val;
            head = null;
            last = null;
        } else {
            Node<E> first = head;
            while (first.next.next != null) {
                first = first.next;
            }
            toRemove = first.next.val;
            last = first;
            first.next = null;
        }
        length--;
        return toRemove;
    }

    /**
     * Remove element by position
     *
     * @param pos position of element
     * @return removed element from position
     * @throws ArrayIndexOutOfBoundsException if position out of the list bounds
     */
    @Override
    public E pop(int pos) {
        if (pos < 0 || pos >= length) {
            throw new ArrayIndexOutOfBoundsException("Index out of the list bounds");
        }
        if (pos == 0) return popFirst();
        if (pos == length - 1) return popLast();

        Node<E> first = head;
        int position = 0;
        while (position + 1 < pos) {
            first = first.next;
            position++;
        }
        E toRemove = first.next.val;
        first.next = first.next.next;
        length--;
        return toRemove;
    }

    /**
     * Method that provides to remove the element of the list
     *
     * @param data element to remove of list
     * @return removed element
     */
    @Override
    public E remove(E data) {
        Node<E> curr = head;
        for (int i = 0; curr != null; curr = curr.next, i++) {
            if (curr.val.equals(data)) {
                return pop(i);
            }
        }
        return null;
    }


    /**
     * Insert data at the specified position
     *
     * @param data data to add to the position
     * @param pos  this is position to insert the data
     */
    @Override
    public void insert(E data, int pos) {
        if (pos <= 0) {
            pushFirst(data);
            return;
        }
        if (pos >= length) {
            pushLast(data);
            return;
        }

        int position = 0;
        Node<E> first = head, prev = null;
        Node<E> newNode = new Node<>(data);
        while (pos != position) {
            prev = first;
            position++;
            first = first.next;
        }
        prev.next = newNode;
        newNode.next = first;
        length++;
    }


    /**
     * Method which provides to get first index of specified element in the current list
     *
     * @param element some element in the list
     * @return index of element in list, if list doesn't contains the element return value will be -1
     */
    @Override
    public int indexOf(E element) {
        Node<E> curr = head;
        for (int i = 0; curr != null; curr = curr.next, i++) {
            if (curr.val.equals(element)) {
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
        Node<E> curr = head;
        int last = -1;
        for (int i = 0; curr != null; curr = curr.next, i++) {
            if (curr.val.equals(element)) {
                last = i;
            }
        }
        return last;
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
     * Sort list in ascending order
     */
    @SuppressWarnings("unchecked")
    public void sort() {
        Object[] temp = toObjectArray();
        Arrays.sort(temp);
        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            first.val = (E) temp[pos];
        }
    }

    public Object[] toObjectArray() {
        Object[] array = new Object[length];
        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            array[pos] = first.val;
        }
        return array;
    }

    @Override
    public int getLength() {
        return this.length;
    }

    @Override
    public void clear() {
        this.head = null;
        this.last = null;
        this.length = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new SelfIterator<E>();
    }

    class SelfIterator<T> implements Iterator<E> {
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

