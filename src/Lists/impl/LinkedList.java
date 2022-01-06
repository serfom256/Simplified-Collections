package Lists.impl;

import Additional.DynamicString.AbstractDynamicString;
import Additional.DynamicString.DynamicLinkedString;
import Lists.AbstractLinkedList;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
        this.head = this.last = null;
        this.length = 0;
    }

    @SafeVarargs
    public final void addAll(E... data) {
        for (E obj : data) {
            addLast(obj);
        }
    }

    /**
     * Add all data from Iterable objects in the end of list
     *
     * @param data it is all iterable object of elements to add
     */
    @Override
    public <T extends Iterable<E>> void add(T data) {
        for (E obj : data) {
            addLast(obj);
        }
    }

    /**
     * Add specified element to end of the list
     *
     * @param element the element to add
     */
    @Override
    public void add(E element) {
        addLast(element);
    }

    /**
     * Add data to the beginning of current list
     *
     * @param element the element to add
     */
    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null) {
            head = last = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        length++;
    }

    /**
     * Add element to end of the list
     *
     * @param element the element to add
     */
    @Override
    public void addLast(E element) {
        if (last == null) {
            head = last = new Node<>(element);
        } else {
            insertAfter(last, element);
        }
        length++;
    }

    /**
     * Insert data at the specified position
     *
     * @param data data to add to the position
     * @param pos  this is position to insert the data
     */
    @Override
    public void insert(int pos, E data) {
        if (pos >= length) {
            addLast(data);
            return;
        }
        if (pos <= 0) {
            addFirst(data);
            return;
        }
        insertAfter(getNode(pos - 1), data);
        length++;
    }

    /**
     * Links new node with specified element after the specified node
     */
    private Node<E> insertAfter(Node<E> node, E toInsert) {
        Node<E> newNode = new Node<>(toInsert);
        newNode.next = node.next;
        node.next = newNode;
        if (newNode.next == null) last = newNode;
        return newNode;
    }

    /**
     * Removes node after the specified node if the specified node is not equals null
     *
     * @return node if node after the specified node removed otherwise null
     */
    private Node<E> deleteAfter(Node<E> node) {
        if (node == null) return null;
        Node<E> toRemove = node.next;
        if (toRemove == last) last = node;
        node.next = toRemove.next;
        return node;
    }

    /**
     * Provides to remove the element of the list
     *
     * @param data element to remove of list
     * @return removed element
     */
    @Override
    public E delete(E data) {
        if (head == null) return null;
        if (head.val.equals(data)) return deleteFirst();
        for (Node<E> curr = head, prev = null; curr != null; prev = curr, curr = curr.next) {
            if (curr.val.equals(data)) {
                deleteAfter(prev);
                length--;
                return data;
            }
        }
        return null;
    }

    /**
     * Remove element by position
     *
     * @param pos position of element
     * @return removed element from position
     * @throws ArrayIndexOutOfBoundsException if position out of the list bounds
     */
    @Override
    public E deleteAtPosition(int pos) {
        if (pos < 0 || pos >= length) {
            throw new ArrayIndexOutOfBoundsException("Index out of the list bounds");
        }
        if (pos == 0) return deleteFirst();
        if (pos == length - 1) return deleteLast();
        Node<E> node = getNode(pos - 1);
        if (deleteAfter(node) != null) length--;
        return node.val;
    }

    /**
     * Removes first element
     *
     * @return first element of list if list isn't empty else return null
     * @throws ArrayIndexOutOfBoundsException if list is empty
     */
    @Override
    public E deleteFirst() {
        if (head == null) {
            throw new ArrayIndexOutOfBoundsException("List is empty");
        }
        E value = head.val;
        head = head.next;
        if (head == null) last = null;
        length--;
        return value;
    }

    /**
     * Removes last element
     *
     * @return last element of list if list isn't empty else return null
     * @throws ArrayIndexOutOfBoundsException if list is empty
     */
    @Override
    public E deleteLast() {
        if (last == null) {
            throw new ArrayIndexOutOfBoundsException("List is empty");
        }
        E toRemove = last.val;
        if (head.next == null) {
            head = last = null;
        } else {
            Node<E> preLast = getNode(length - 2);
            preLast.next = null;
            last = preLast;
        }
        length--;
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
        if (start < 0 || end > length || start >= end) {
            throw new IllegalArgumentException("Invalid method parameters");
        }
        if (start == 0) {
            while (start++ < end) {
                deleteFirst();
            }
            return;
        }
        Node<E> node = getNode(start - 1);
        while (start < end && node != null) {
            node = deleteAfter(node);
            end--;
            length--;
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
        delete(start, length);
        addLast(data);
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
        Node<E> node = getNode(start);
        for (E element : data) {
            node = insertAfter(node, element);
            node = node.next;
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
        delete(start, length);
        for (E element : data) {
            addLast(element);
        }
    }

    /**
     * Replace element in the list if element present in the list
     *
     * @param pos  position of element to replace
     * @param data element to replace
     * @throws ArrayIndexOutOfBoundsException if specified index out of list bounds
     */
    @Override
    public void update(int pos, E data) {
        if (pos < 0 || pos >= length) {
            throw new ArrayIndexOutOfBoundsException("Specified position out of list bounds");
        }
        getNode(pos).val = data;
    }

    /**
     * Returns first element of list if list isn't empty, else null
     *
     * @return fist element of the list
     */
    @Override
    public E getFirst() {
        return head != null ? head.val : null;
    }

    /**
     * Returns last element of list if list isn't empty, else null
     *
     * @return last element of the list
     */
    @Override
    public E getLast() {
        return length != 0 ? last.val : null;
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
        if (position < 0 || position >= length) {
            throw new ArrayIndexOutOfBoundsException("Position out of list bounds");
        }
        Node<E> first = head;
        while (position-- > 0) {
            first = first.next;
        }
        return first.val;
    }

    /**
     * Returns node by the specified position
     */
    private Node<E> getNode(int position) {
        if (position < 0 || position >= length) {
            throw new ArrayIndexOutOfBoundsException("Position out of list bounds");
        }
        Node<E> first = head;
        while (position-- > 0) {
            first = first.next;
        }
        return first;
    }

    /**
     * Returns slice from the list in specified range between the specified start and the specified end
     *
     * @param start start of range
     * @param end   end of range
     * @throws IllegalArgumentException if start < 0 or end > list size
     *                                  or if start index larger then end index
     */
    @Override
    public LinkedList<E> slice(int start, int end) {
        if (start < 0 || end > length || start >= end) {
            throw new IllegalArgumentException("Invalid method parameters");
        }
        Node<E> curr = getNode(start);
        LinkedList<E> result = new LinkedList<>();
        for (int i = start; i < end; curr = curr.next, i++) {
            result.add(curr.val);
        }
        return result;
    }

    /**
     * Provides to get first index of specified element in the current list
     *
     * @param element some element in the list
     * @return index of element in list, if list doesn't contains the element return value will be -1
     */
    @Override
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
     * Provides to get last index of specified element in the current list
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
     * Sort list in ascending order
     */
    @Override
    @SuppressWarnings("unchecked")
    public void sort() {
        Object[] temp = toObjectArray();
        Arrays.sort(temp);
        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            first.val = (E) temp[pos];
        }
    }

    @Override
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
     * Returns all data from current list as array of objects
     */
    @Override
    public Object[] toObjectArray() {
        Object[] array = new Object[length];
        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            array[pos] = first.val;
        }
        return array;
    }

    /**
     * Returns count of elements which equals the specified element in this list
     */
    @Override
    public int count(E element) {
        int count = 0;
        for (Node<E> curr = head; curr != null; curr = curr.next) {
            if (curr.val.equals(element)) {
                count++;
            }
        }
        return count;
    }

    /**
     * @return length of current list
     */
    @Override
    public int getSize() {
        return this.length;
    }

    @Override
    public void clear() {
        this.head = this.last = null;
        this.length = 0;
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
            if (current == null) throw new NoSuchElementException();
            E data = current.val;
            current = current.next;
            return data;
        }
    }

    @Override
    public String toString() {
        if (head == null) return "[]";
        Node<E> first = head;
        AbstractDynamicString lst = new DynamicLinkedString("[");
        while (first.next != null) {
            lst.add(first.val).add(", ");
            first = first.next;
        }
        return lst.add(first.val).add("]").toString();
    }
}

