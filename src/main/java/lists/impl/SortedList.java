package lists.impl;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.IndexOutOfCollectionBoundsException;
import additional.exceptions.NullableArgumentException;
import lists.AbstractList;
import lists.AbstractSortedList;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * SortedList based on Skip List
 */
public class SortedList<E extends Comparable<E>> implements AbstractSortedList<E> {

    private int size;
    private int height;
    private Node<E> top, head;
    private final Random random;
    private final E borderValue;
    private static final long SEED = 16;
    public static final int DEFAULT_HEIGHT = 3;
    private static final int MAX_HEIGHT = Byte.MAX_VALUE >> 2;
    private static final int MIN_HEIGHT = MAX_HEIGHT >> 4;


    private static class Node<E> {
        E value;
        byte level;
        Node<E> next, prev, down, up;

        public Node(E value, Node<E> up, Node<E> down, byte level) {
            this.next = this.prev = null;
            this.up = up;
            this.down = down;
            this.value = value;
            this.level = level;
        }
    }

    public SortedList() {
        this(DEFAULT_HEIGHT);
    }

    public SortedList(int height) {
        this(height, SEED);
    }

    public SortedList(int height, long seed) {
        this.height = height;
        this.random = new Random(seed);
        this.borderValue = null;
        this.top = null;
        this.size = 0;
    }

    @SafeVarargs
    public final void addAll(E... data) {
        for (E obj : data) {
            add(obj);
        }
    }

    /**
     * Appends all elements from Iterable collection to the list in the ascending order
     *
     * @param data elements to append
     * @throws NullableArgumentException if one element of the iterable is null
     */
    @Override
    public <T extends Iterable<E>> void addFrom(T data) {
        for (E obj : data) {
            add(obj);
        }
    }

    /**
     * Appends specified element to the list in the ascending order
     *
     * @param element the element to append
     * @throws NullableArgumentException if the specified element is null
     */
    public void add(E element) {
        if (element == null) throw new NullableArgumentException();
        if (top == null) initHeadNodes();
        byte level = getRandomLevel();
        if (level == height) addTopLayer();
        size++;
        Node<E> curr = top, prev = null;
        while (curr != null) {
            if (curr.next == null || curr.next.value.compareTo(element) > 0) {
                if (level >= curr.level) {
                    Node<E> newNode = new Node<>(element, prev, null, curr.level);
                    if (prev != null) prev.down = newNode;
                    insertNodeAfter(curr, newNode);
                    prev = newNode;
                }
                curr = curr.down;
            } else {
                curr = curr.next;
            }
        }
    }

    /**
     * Creates Left border of the list
     */
    private void initHeadNodes() {
        if (height > MAX_HEIGHT || height < MIN_HEIGHT) {
            throw new IllegalArgumentException("Height of the Skip List must be more then 2 and less then 33");
        }
        for (byte i = 0; i < height; i++) {
            Node<E> newNode = new Node<>(borderValue, null, top, i);
            if (head == null) head = newNode;
            if (top != null) top.up = newNode;
            top = newNode;
        }
    }

    /**
     * Removes element by position
     *
     * @param position position of element
     * @return removed element from position
     * @throws IndexOutOfCollectionBoundsException if specified position out of list bounds
     */
    @Override
    public E deleteAtPosition(int position) {
        if (position < 0 && position >= size) {
            throw new IndexOutOfCollectionBoundsException();
        }
        Node<E> toRemove = getNodeByPosition(position);
        deleteNode(toRemove);
        size--;
        return toRemove.value;
    }

    /**
     * Provides to get the element from list by position
     *
     * @param position position of element
     * @return element from the specified position
     * @throws IndexOutOfCollectionBoundsException if specified position out of list bounds
     */
    @Override
    public E get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfCollectionBoundsException();
        }
        return getNodeByPosition(position).value;
    }

    /**
     * Returns element with minimum value of the list
     */
    @Override
    public E getMin() {
        return head != null ? head.next.value : null;
    }

    /**
     * Returns element with maximum value of the list
     */
    @Override
    public E getMax() {
        Node<E> current = head;
        while (current.next != null || current.down != null) {
            if (current.next == null) {
                current = current.down;
            } else {
                current = current.next;
            }
        }
        return current.value;
    }

    /**
     * Removes the element of the list
     *
     * @param element element to remove of list
     * @return removed element
     * @throws NullableArgumentException if the specified element is null
     */
    @Override
    public boolean delete(E element) {
        if (element == null) throw new NullableArgumentException();
        Node<E> toRemove = findNodeByValue(element);
        if (toRemove == null) return false;
        Node<E> next = toRemove.next;
        deleteNode(toRemove);

        if (next == null) {
            while (top != null && top.next == null) {
                removeTopLayer();
            }
        } else if (next.down != null && (next.level > (height >> 1 ^ height >> 2) && random.nextDouble() < 0.4)) { // balancing top layers
            Node<E> newNode = restoreIntegrity(next, true);
            if (newNode != null && newNode.next == top.next.next && newNode.prev.prev == null) {
                removeTopLayer();
            }
        } else if (top != null && top.down != null) {
            while (top.down.next.next == top.next.next) {
                removeTopLayer();
            }
        }
        size--;
        return true;
    }

    /**
     * Rebalances Skip list top layers after element deletion
     *
     * @param layer  layer to rebalance
     * @param isSupp true if called from another method otherwise false
     */
    private Node<E> restoreIntegrity(Node<E> layer, boolean isSupp) {
        Node<E> balanceNode;
        if ((layer.next == null || isSupp) && layer.down != null) {
            balanceNode = restoreIntegrity(layer.down, false);
            if (balanceNode == null) return null;
            Node<E> newNode = new Node<>(balanceNode.value, null, balanceNode, layer.level);
            balanceNode.up = newNode;
            insertInLayer(layer, newNode);
            return newNode;
        }
        return getRandomNode(layer);
    }

    /**
     * Inserts specified node to the specified layer
     */
    private void insertInLayer(Node<E> layer, Node<E> toInsert) {
        while (layer.next != null && toInsert.value.compareTo(layer.next.value) >= 0) {
            layer = layer.next;
        }
        insertNodeAfter(layer, toInsert);
    }

    /**
     * Returns random node from the specified level
     */
    private Node<E> getRandomNode(Node<E> level) {
        Node<E> node = level.next, prev = node;
        while (node != null && node.up == null && random.nextDouble() < 0.7) {
            prev = node;
            node = node.next;
        }
        while (prev != null && prev.up != null) {
            prev = prev.next;
        }
        return prev;
    }

    private void deleteNode(Node<E> node) {
        while (node != null) {
            Node<E> next = node.next, prev = node.prev;
            prev.next = next;
            if (next != null) next.prev = prev;
            node.up = null;
            node.next = node.prev = null;
            node = node.down;
        }
    }

    private void insertNodeAfter(Node<E> node, Node<E> toInsert) {
        Node<E> next = node.next;
        toInsert.next = node.next;
        if (next != null) next.prev = toInsert;
        toInsert.prev = node;
        node.next = toInsert;
    }

    /**
     * Generates level with random height
     */
    private byte getRandomLevel() {
        byte level = 0;
        while (level < height && random.nextDouble() < 0.5) {
            level++;
        }
        return level;
    }

    private void addTopLayer() {
        top = new Node<>(borderValue, null, top, (byte) height++);
    }

    /**
     * Removes all nodes on the layer of the list
     */
    private void removeTopLayer() {
        Node<E> curr = top;
        top = top.down;
        while (curr != null) { // remove links
            curr.down = curr.prev = null;
            curr = curr.next;
        }
        height--;
    }

    /**
     * Returns node by the specified position
     */
    private Node<E> getNodeByPosition(int pos) {
        Node<E> curr = head.next;
        while (--pos >= 0) {
            curr = curr.next;
        }
        return curr;
    }

    /**
     * Returns true if list contains specified element
     *
     * @param element test element present in the Set
     * @return true if element presents otherwise false
     * @throws NullableArgumentException if the specified element is null
     */
    public boolean contains(E element) {
        if (element == null) throw new NullableArgumentException();
        return findNodeByValue(element) != null;
    }

    private Node<E> findNodeByValue(E key) {
        Node<E> node = top;
        while (node != null) {
            if (node.value != borderValue && node.value.equals(key)) return node;
            else if (node.next != null && node.next.value.compareTo(key) <= 0) node = node.next;
            else node = node.down;
        }
        return null;
    }

    /**
     * Returns count of elements which equals the specified element in this list
     *
     * @throws NullableArgumentException if the specified element is null
     */
    @Override
    public int count(E element) {
        if (element == null) throw new NullableArgumentException();
        int count = 0;
        if (head == null) return 0;
        for (Node<E> first = head.next; first != null; first = first.next) {
            if (first.value.equals(element)) {
                count++;
            }
        }
        return count;
    }


    /**
     * Returns first occurrence position of specified element in the list
     *
     * @return first position of the specified element if specified element found otherwise -1
     * @throws NullableArgumentException if the specified element is null
     */
    @Override
    public int indexOf(E element) {
        if (element == null) throw new NullableArgumentException();
        int pos = 0;
        if (head == null) return 0;
        for (Node<E> first = head.next; first != null; first = first.next, pos++) {
            if (first.value.equals(element)) {
                return pos;
            }
        }
        return -1;
    }

    /**
     * Returns last occurrence position of specified element in the list
     *
     * @return last position of the specified element if specified element found otherwise -1
     * @throws NullableArgumentException if the specified element is null
     */
    @Override
    public int lastIndexOf(E element) {
        if (element == null) throw new NullableArgumentException();
        int pos = 0;
        int result = -1;
        for (Node<E> first = head.next; first != null; first = first.next, pos++) {
            if (first.value.equals(element)) {
                result = pos;
            }
        }
        return result;
    }

    /**
     * Returns slice from the list in specified range between the specified start and the specified end
     *
     * @param start start of range
     * @param end   end of range
     * @throws IndexOutOfCollectionBoundsException if start < 0 or end > list size
     *                                             or if start index larger then end index
     */
    @Override
    public AbstractList<E> slice(int start, int end) {
        if (start < 0 || end > size || start >= end) {
            throw new IndexOutOfCollectionBoundsException();
        }
        Node<E> curr = getNodeByPosition(start);
        DoubleLinkedList<E> result = new DoubleLinkedList<>();
        for (int i = start; i < end; curr = curr.next, i++) {
            result.add(curr.value);
        }
        return result;
    }

    /**
     * Returns all data from current list as the specified type array
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> E[] toArray(Class<T> type) {
        E[] temp = (E[]) Array.newInstance(type, size);
        if (head == null) return temp;
        int pos = 0;
        for (Node<E> first = head.next; first != null; first = first.next, pos++) {
            temp[pos] = first.value;
        }
        return temp;
    }

    /**
     * Returns all data from current list as array of objects
     */
    @Override
    public Object[] toObjectArray() {
        if (head == null) return new Object[0];
        Object[] array = new Object[size];
        int pos = 0;
        for (Node<E> first = head.next; first != null; first = first.next, pos++) {
            array[pos] = first.value;
        }
        return array;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void clear() {
        this.top = this.head = null;
        this.size = 0;
        this.height = DEFAULT_HEIGHT;
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
            E data = current.value;
            current = current.next;
            return data;
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        Node<E> node = head;
        AbstractDynamicString result = new DynamicLinkedString("[");
        node = node.next;
        while (node.next != null) {
            result.add(node.value).add(", ");
            node = node.next;
        }
        return result.add(node.value).add("]").toString();
    }
}
