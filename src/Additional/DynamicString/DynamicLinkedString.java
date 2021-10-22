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
        if (node == null) node = head = last = new Node(s.charAt(posInWord++));
        for (int i = s.length() - 1; i >= posInWord; i--) {
            node = insertBefore(node, s.charAt(i));
        }
    }

    private void insertSequenceAfter(Node node, String s, int posInWord) {
        if (node == null) node = head = last = new Node(s.charAt(posInWord++));
        for (int i = posInWord; i < s.length(); i++) {
            node = insertAfter(node, s.charAt(i));
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
        if (pos < 0 || (pos >= size && head != null))
            throw new UnsupportedOperationException("Specified position out of String bounds");
        Node node = getNodeByPos(pos);
        if (node == null) addFirst(c);
        else insertBefore(node, c);
        return this;
    }

    private Node insertAfter(Node node, char toInsert) {
        Node newNode = new Node(toInsert);
        newNode.next = node.next;
        node.next = newNode;
        newNode.prev = node;
        if (newNode.next != null) newNode.next.prev = newNode;
        else last = newNode;
        size++;
        return newNode;
    }

    private Node insertBefore(Node node, char toInsert) {
        Node newNode = new Node(toInsert);
        newNode.prev = node.prev;
        node.prev = newNode;
        newNode.next = node;
        if (newNode.prev != null) newNode.prev.next = newNode;
        else head = newNode;
        size++;
        return newNode;
    }

    @Override
    public DynamicLinkedString insert(int pos, String s) {
        if (pos < 0 || (pos > size && head != null))
            throw new UnsupportedOperationException("Specified position out of String bounds");
        if (pos == 0) {
            insertSequenceBefore(head, s, 0);
            return this;
        }
        if (pos == size) {
            insertSequenceAfter(last, s, 0);
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

    private void deleteNode(Node node) {
        size--;
        Node prev = node.prev;
        Node next = node.next;
    }

    public DynamicLinkedString delete(int startPos, int endPos) {
        return this;
    }

    public DynamicLinkedString delete(int start) {
        return this;
    }

    public DynamicLinkedString deleteOnPosition(int pos) {
        if (pos < 0 || (pos >= size && head != null))
            throw new UnsupportedOperationException("Specified position out of String bounds");
        Node node = getNodeByPos(pos);
        if (node.prev == null) return removeFirst();
        if (node.next == null) return removeLast();
        deleteNode(node);
        return this;
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
        if (head == null) return this;
        size--;
        last = last.prev;
        if (last != null) last.next = null;
        else head = null;
        return this;
    }


    @Override
    public char getLast() {
        return last.val;
    }

    @Override
    public char getFirst() {
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
        for (int pos = 0; pos < size; pos++, curr = curr.prev) curr.val = result[pos];
        return this;
    }

    @Override
    public int count(char element) {
        return 0;
    }


    private char[] subStr(int start, int end) {
        Node first = head;
        for (int pos = 0; pos < start; pos++, first = first.next) ;
        int len = end - start;
        char[] lst = new char[len];
        for (int pos = start; pos < end; pos++, first = first.next) lst[pos] = first.val;
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

    public String subString(int start, int end) {
        return new String(subStr(start, end));
    }

    public String subString(int start) {
        return new String(subStr(start, size));
    }

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
        for (int pos = 0; pos < size; pos++, first = first.next)
            lst[pos] = first.val;
        return new String(lst);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicLinkedString that = (DynamicLinkedString) o;
        return size == that.size && Objects.equals(head, that.head) && Objects.equals(last, that.last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, last, size);
    }

    @Override
    public int getSize() {
        return size;
    }
}
