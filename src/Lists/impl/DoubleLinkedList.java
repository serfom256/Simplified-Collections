package Lists.impl;

import Lists.AbstractLinkedList;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


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

    // type it is special variable, that provides cast current list<E> to Array(E[])
    private final Class<E> type;

    public DoubleLinkedList(Class<E> type) {
        this.head = null;
        this.last = null;
        this.length = 0;
        this.type = type;
    }

    public DoubleLinkedList(Iterable<E> object, Class<E> type) {
        this.head = null;
        this.last = null;
        this.length = 0;
        this.type = type;
        pushFrom(object);

    }


    //=============================METHODS PROVIDES ADD SOMETHING TO LIST=============================================//


    /**
     * Add data to the end of current list
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
     * @param data the element to add
     */
    @Override
    public void pushFirst(E data) {
        Node<E> newNode = new Node<>(data);
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
     * Add element to the end of current list
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
    public <T extends Iterable<E>> void pushFrom(T data) {
        for (E obj : data) {
            pushLast(obj);
        }
    }


    /**
     * Add all data from Iterable objects in the head of list
     *
     * @param toFirst if equals true then all data append to head of list
     * @param data    it is all iterable object of elements to add
     */
    public <T extends Iterable<E>> void pushFrom(T data, boolean toFirst) {
        if (toFirst) {
            for (E obj : data) {
                pushFirst(obj);
            }
        } else
            pushFrom(data);
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


    //=============================METHODS PROVIDES REMOVE SOMETHING FROM LIST========================================//
    //ATTENTION THIS METHOD VERY SLOW
    public void removeDuplicates() {
        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            if (count(first.val) > 1) {
//                removeAllLike(first.val, pos+1);
                remove(first.val);
            }
        }
    }

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
                first.prev.next = first.next;
                first.next.prev = first.prev;
                return first.val;
            }
            first = first.next;
        }
        return null;
    }

    //ATTENTION THIS METHOD VERY SLOW
    public void removeAllLike(E element) {
        removeAllLike(element, 0);
    }

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
     */
    @Override
    public E popLast() {
        if ((last == null) || (head == null)) {
            return null;
        }
        length--;
        if (head.next == null) {
            head = null;
            return null;
        }
        E data = last.val;
        last = last.prev;
        last.next = null;
        return data;
    }

    /**
     * Remove first element
     *
     * @return first element of list if list isn't empty else return null
     */
    @Override
    public E popFirst() {
        if (head == null) {
            return null;
        }
        length--;
        E data = head.val;
        if (head.next == null) {
            head = last = null;
            return null;
        }
        head = head.next;
        head.prev = null;
        if (length == 1) {
            last = head;
        }
        return data;
    }

    //=============================METHODS PROVIDES GET SOMETHING FROM LIST========================================//
    //FIXME

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
     * Method that return first element of list if list isn't empty, else null
     *
     * @return fist element of the list
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
    //=============================REGEXP METHODS=====================================================================//

    private DoubleLinkedList(Node<E> head, Class<E> type) {
        this.head = head;
        this.type = type;
        this.length = 0;

    }

    public DoubleLinkedList<E> slice(int begin, int end) {
        DoubleLinkedList<E> result = new DoubleLinkedList<>(head, type);
        result.setLength(begin, end);
        return result;

    }

    /**
     * Set bounds by length to this list
     *
     * @param begin start position of bounds
     * @param end   end position of bounds
     */
    public void setLength(int begin, int end) {
        if (begin >= end || begin < 0) {
            throw new IllegalArgumentException("Begin index should be less then end index");
        }
        int len = end - begin;
        Node<E> first = head;
        while (begin > 0 && first != null) {
            first = first.next;
            begin--;
        }
        Node<E> start = first;
        end = len;
        while (first != null && end > 1) {
            first = first.next;
            end--;
        }
        if (first != null) {
            if (first.next != null) {
                first.next.prev = null;
            }
            first.next = null;
        }
        length = len;
        head = start;
    }

    /**
     * Slice list after position
     *
     * @param begin initial position to slicing
     * @return this list, cut after the position
     */
    public DoubleLinkedList<E> sliceFrom(int begin) {
        //set begin into the range of list
        // FIXME
        begin = begin < 0 ? length + begin : begin;
        if (begin >= length || begin < 0) {
            return new DoubleLinkedList<>(type);
        }
        //========================================
        DoubleLinkedList<E> result = new DoubleLinkedList<>(head, type);
        result.setLength(begin, length);
        return result;
    }

    /**
     * Slice list before position
     *
     * @param end slicing from 0 to end position
     * @return this list, cut before the position
     */
    public DoubleLinkedList<E> sliceBefore(int end) {
//        if (end <= 0) {
//            return new DoubleLinkedList<>(type);
//        }
        end = end < 0 ? length + end : end;
        DoubleLinkedList<E> result = new DoubleLinkedList<>(head, type);
        result.setLength(0, end);
        return result;
    }
    //=============================ADDITIONAL METHODS FOR THE CONVENIENCE WORK========================================//

    /**
     * Sort list in in ascending order
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


    public <T extends List<E>> T toList(T list) {
        for (Node<E> first = head; first != null; first = first.next) {
            list.add(first.val);
        }
        return list;
    }

    public <T extends Set<E>> T toSet(T set) {
        for (Node<E> first = head; first != null; first = first.next) {
            set.add(first.val);
        }
        return set;
    }

    public Object[] toObjectArray() {
        Object[] array = new Object[length];
        int pos = 0;
        for (Node<E> first = head; first != null; first = first.next, pos++) {
            array[pos] = first.val;
        }
        return array;
    }


    @SuppressWarnings("unchecked")
    public <T> E[] toArray() {
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
    public int count(E element) {
        int count = 0;
        for (Node<E> first = head; first != null; first = first.next) {
            if (first.val == element) {
                count++;
            }
        }
        return count;
    }

    /**
     * Method which provides to get first index of some element in the current list
     *
     * @param element some element in the list
     * @return index of element in list, if list isn't contains the element return value will be -1
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
     * Method which provides to get last index of some element in the current list
     *
     * @param element some element in the list
     * @return index of element in list, if list isn't contains the element return value will be -1
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

    //================================OVERRIDE AND UTILITY METHODS===================================================//

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


    public String print() {
        if (head == null) {
            return "[]";
        }
        Node<E> first = head;
        StringBuilder lst = new StringBuilder("<-(");
        while (first.next != null) {
            if (first.prev != null) {
                lst.append("<-(");
            }
            lst.append(first.val);
            if (first.next != null) {
                lst.append(")->");
            }
            first = first.next;

        }
        if (lst.toString().length() == 3) {
            return "<-(" + first.val + ")->";
        }
        return lst.toString() + "<-(" + first.val + ")->";
    }

}

