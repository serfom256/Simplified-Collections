package Additional.DynamicString;

import java.util.Objects;

public class DynamicLinkedString implements AbstractDynamicString {

    private static class Node {
        char val;
        Node next;
        Node prev;

        Node(char val) {
            this.val = val;
        }
    }

    private Node head;
    private Node last;
    private int size;

    public DynamicLinkedString() {
        this.head = null;
        this.last = null;
        this.size = 0;
    }

    public DynamicLinkedString(char[] lst) {
        this();
        for (char c : lst) add(c);
    }

    public DynamicLinkedString(String str) {
        this();
        for (int i = 0; i < str.length(); i++) add(str.charAt(i));
    }

    public DynamicLinkedString(AbstractDynamicString str) {
        this();
        String s = str.toString();
        for (int i = 0; i < s.length(); i++) add(s.charAt(i));
    }

    @Override
    public DynamicLinkedString add(AbstractDynamicString s) {
        insertSequenceAfter(last, s.toString(), 0);
        return this;
    }

    @Override
    public AbstractDynamicString add(Object o) {
        return add(String.valueOf(o));
    }

    @Override
    public DynamicLinkedString add(String s) {
        insertSequenceAfter(last, s, 0);
        return this;
    }

    @Override
    public DynamicLinkedString add(char element) {
        Node newNode = new Node(element);
        if (last == null) {
            head = newNode;
            last = head;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        size++;
        return this;
    }


    private void insertSequenceBefore(Node node, String s, int posInWord) {
        if (s.length() == 0) return;
        if (node == null) {
            size++;
            node = head = last = new Node(s.charAt(posInWord++));
        }
        for (int i = s.length() - 1; i >= posInWord; i--) {
            node = insertBefore(node, s.charAt(i));
            size++;
        }
    }

    private void insertSequenceAfter(Node node, String s, int posInWord) {
        if (s.length() == 0) return;
        if (node == null) {
            size++;
            node = head = last = new Node(s.charAt(posInWord++));
        }
        for (int i = posInWord; i < s.length(); i++) {
            node = insertAfter(node, s.charAt(i));
            size++;
        }
    }


    @Override
    public DynamicLinkedString addFirst(char element) {
        Node newNode = new Node(element);
        newNode.next = head;
        if (head != null) head.prev = newNode;
        else last = newNode;
        head = newNode;
        size++;
        return this;
    }

    @Override
    public DynamicLinkedString insert(int pos, char c) {
        if (pos >= size) return add(c);
        if (pos <= 0) return addFirst(c);
        Node node = getNodeByPos(pos);
        insertBefore(node, c);
        size++;
        return this;
    }


    @Override
    public DynamicLinkedString insert(int pos, String s) {
        if (pos >= size) {
            insertSequenceAfter(last, s, 0);
            return this;
        }
        if (pos <= 0) {
            insertSequenceBefore(head, s, 0);
            return this;
        }

        Node node = getNodeByPos(pos);
        insertSequenceBefore(node, s, 0);
        return this;
    }

    @Override
    public DynamicLinkedString insert(int pos, AbstractDynamicString s) {
        return insert(pos, s.toString());
    }

    @Override
    public DynamicLinkedString insert(int pos, char[] s) {
        return insert(pos, new String(s));
    }

    private Node getNodeByPos(int pos) {
        Node current = head;
        int currentPos = 0;
        while (currentPos < pos && current.next != null) {
            current = current.next;
            currentPos++;
        }
        return current;
    }

    private Node insertAfter(Node node, char toInsert) {
        Node newNode = new Node(toInsert);
        newNode.next = node.next;
        node.next = newNode;
        newNode.prev = node;
        if (newNode.next != null) newNode.next.prev = newNode;
        else last = newNode;
        return newNode;
    }

    private Node insertBefore(Node node, char toInsert) {
        Node newNode = new Node(toInsert);
        newNode.prev = node.prev;
        node.prev = newNode;
        newNode.next = node;
        if (newNode.prev != null) newNode.prev.next = newNode;
        else head = newNode;
        return newNode;
    }

    /**
     * Provides to remove range ...
     *
     * @param startPos start position of removed range
     * @param endPos   end position of removed range
     * @throws IllegalArgumentException if startPos > endPos or specified positions out of string range
     */
    @Override
    public DynamicLinkedString delete(int startPos, int endPos) {
        if (startPos >= endPos || startPos < 0) {
            throw new IllegalArgumentException("Specified position is invalid");
        }
        if (endPos >= size) endPos = size;
        Node node = getNodeByPos(startPos);
        if (node == head) endPos--;
        while (startPos < endPos && node != null) {
            node = deleteAfter(node);
            endPos--;
            size--;
        }
        return startPos == 0 ? removeFirst() : this;
    }

    /**
     * Provides to all characters from the specified position
     *
     * @throws IllegalArgumentException if the specified start position out of string bounds
     */
    @Override
    public DynamicLinkedString delete(int start) {
        if (start < 0) {
            throw new IllegalArgumentException("Specified position out of String bounds");
        }
        if (start == 0) return clear();
        if (start >= size) start = size;
        Node node = getNodeByPos(start);
        int len = size;
        for (int i = start; i < len && node != null; i++, size--) {
            node = deleteAfter(node);
        }
        return this;
    }

    /**
     * Provides to remove character on the specified position
     *
     * @throws IllegalArgumentException if the specified position out of string bounds
     */
    @Override
    public DynamicLinkedString deleteAtPosition(int pos) {
        if (pos < 0 || pos > size) throw new IllegalArgumentException("Specified position out of String bounds");
        Node node = getNodeByPos(pos - 1);
        if (node.prev == null) return removeFirst();
        if (node.next == null) return removeLast();
        if (deleteAfter(node) != null) size--;
        return this;
    }

    /**
     * Removes node before the specified node if the specified node is not equals null
     *
     * @return node if node before the specified node removed otherwise null
     */
    private Node deleteBefore(Node node) {
        Node toRemove = node.prev;
        if (toRemove == null) return null;
        Node prev = toRemove.prev;
        if (prev != null) prev.next = node;
        node.prev = prev;
        return node;
    }

    /**
     * Removes node after the specified node if the specified node is not equals null
     *
     * @return node if node after the specified node removed otherwise null
     */
    private Node deleteAfter(Node node) {
        Node toRemove = node.next;
        if (toRemove == null) return null;
        Node next = toRemove.next;
        if (next != null) next.prev = node;
        node.next = next;
        return node;
    }

    @Override
    public DynamicLinkedString removeFirst() {
        if (head == null) return this;
        size--;
        head = head.next;
        if (head != null) head.prev = null;
        else last = null;
        return this;
    }

    @Override
    public DynamicLinkedString removeLast() {
        if (last == null) return this;
        size--;
        last = last.prev;
        if (last != null) last.next = null;
        else head = null;
        return this;
    }

    // TODO write comments for methods
    @Override
    public DynamicLinkedString replace(int start, int end, String s) {
        if (start < 0 || start >= end || start >= size) {
            throw new IllegalArgumentException("Specified position is illegal to replace");
        }
        if (end >= size) end = size;
        Node temp = getNodeByPos(start).next, curr;
        for (curr = temp; curr != null && start < end; start++, curr = curr.next) {
            if (curr.prev == head) {
                removeFirst();
            } else {
                curr = deleteBefore(curr);
                size--;
            }
            if (curr == null) break;
        }
        if (curr == null && start < end && size == 0) {
            removeFirst();
            insertSequenceAfter(head, s, 0);
        } else if (curr == null && start < end) {
            removeLast();
            insertSequenceAfter(last, s, 0);
        } else {
            insertSequenceBefore(curr == null ? head : curr.prev, s, 0);
        }
        return this;
    }

    @Override
    public AbstractDynamicString replace(int start, String s) {
        return replace(start, size, s);
    }

    @Override
    public AbstractDynamicString replace(int start, int end, DynamicLinkedString s) {
        return replace(start, end, s.toString());
    }

    @Override
    public AbstractDynamicString replace(int start, DynamicLinkedString s) {
        return replace(start, size, s.toString());
    }

    @Override
    public AbstractDynamicString replace(int start, int end, char[] c) {
        return replace(start, end, new String(c));
    }

    @Override
    public AbstractDynamicString replace(int start, char[] c) {
        return replace(start, size, new String(c));
    }

    @Override
    public AbstractDynamicString replace(int start, int end, char c) {
        return replace(start, end, String.valueOf(c));
    }

    @Override
    public AbstractDynamicString replace(int start, char c) {
        return replace(start, size, String.valueOf(c));
    }


    @Override
    public char getLast() {
        if (last == null) throw new UnsupportedOperationException("String is empty");
        return last.val;
    }

    @Override
    public char getFirst() {
        if (head == null) throw new UnsupportedOperationException("String is empty");
        return head.val;
    }

    @Override
    public char[] toCharArray() {
        char[] result = new char[size];
        Node curr = head;
        for (int pos = 0; pos < size; pos++, curr = curr.next) result[pos] = curr.val;
        return result;
    }

    @Override
    public DynamicLinkedString reverse() {
        char[] result = new char[size];
        Node curr = head;
        for (int pos = 0; pos < size; pos++, curr = curr.next) result[pos] = curr.val;
        curr = head;
        for (int pos = size - 1; pos >= 0; pos--, curr = curr.next) curr.val = result[pos];
        return this;
    }

    @Override
    public int count(char element) {
        int count = 0;
        for (Node curr = head; curr != null; curr = curr.next) {
            if (curr.val == element) count++;
        }
        return count;
    }


    private char[] subStr(int start, int end) {
        Node first = head;
        for (int pos = 0; pos < start; pos++) first = first.next;
        int len = end - start;
        char[] lst = new char[len];
        for (int pos = 0; pos < len; pos++, first = first.next) lst[pos] = first.val;
        return lst;
    }

    @Override
    public DynamicLinkedString subSequence(int start, int end) {
        return new DynamicLinkedString(subStr(start, end));
    }

    @Override
    public DynamicLinkedString subSequence(int start) {
        return new DynamicLinkedString(subStr(start, size));
    }

    @Override
    public String subString(int start, int end) {
        return new String(subStr(start, end));
    }

    @Override
    public String subString(int start) {
        return new String(subStr(start, size));
    }

    @Override
    public DynamicLinkedString clear() {
        head = last = null;
        size = 0;
        return this;
    }

    @Override
    public String toString() {
        if (head == null) return "";
        Node first = head;
        char[] lst = new char[size];
        for (int pos = 0; pos < size; pos++, first = first.next) lst[pos] = first.val;
        return new String(lst);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicLinkedString obj = (DynamicLinkedString) o;
        return size == obj.size && Objects.equals(this.toString(), obj.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.toString());
    }

    @Override
    public int getSize() {
        return size;
    }
}
