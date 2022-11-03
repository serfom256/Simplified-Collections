package additional.dynamicstring;

import additional.exceptions.IndexOutOfCollectionBoundsException;
import lists.List;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class DynamicLinkedString implements DynamicString {

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
        for (char c : lst) {
            add(c);
        }
    }

    public DynamicLinkedString(String str) {
        this();
        for (int i = 0; i < str.length(); i++) {
            add(str.charAt(i));
        }
    }

    public DynamicLinkedString(Character c) {
        this();
        addFirst(c);
    }

    public DynamicLinkedString(char c) {
        this();
        addFirst(c);
    }

    public DynamicLinkedString(DynamicString str) {
        this();
        for (char c : str) {
            add(c);
        }
    }

    /**
     * Appends specified String to the last of this dynamicLinkedString
     *
     * @param s String to add to the last
     */
    @Override
    public DynamicLinkedString add(DynamicString s) {
        insertSequenceAfter(last, s.toString(), 0);
        return this;
    }

    /**
     * Appends specified Integer to the last of this dynamicLinkedString
     *
     * @param i Integer to add to the last
     */
    @Override
    public DynamicLinkedString add(int i) {
        return add(String.valueOf(i));
    }

    /**
     * Appends Object as String value to the last of this dynamicLinkedString
     *
     * @param o String value of specified Object to add to the last
     */
    @Override
    public DynamicLinkedString add(Object o) {
        return add(String.valueOf(o));
    }

    /**
     * Appends specified String to the last of this dynamicLinkedString
     *
     * @param s String to add to the last
     */
    @Override
    public DynamicLinkedString add(String s) {
        insertSequenceAfter(last, s, 0);
        return this;
    }

    /**
     * Appends all characters from the specified position of the string to end of dynamicLinkedString
     */
    @Override
    public DynamicLinkedString add(String s, int pos) {
        insertSequenceAfter(last, s, pos);
        return this;
    }

    /**
     * Appends all characters from the specified position of the string to end of dynamicLinkedString
     */
    @Override
    public DynamicLinkedString add(DynamicString s, int pos) {
        insertSequenceAfter(last, s.toString(), pos);
        return this;
    }

    /**
     * Appends specified Character to the last of this dynamicLinkedString
     *
     * @param c Char to add to the last
     */
    @Override
    public DynamicLinkedString add(char c) {
        if (last == null) {
            head = last = new Node(c);
        } else {
            insertAfter(last, c);
        }
        size++;
        return this;
    }

    @Override
    public DynamicString add(char[] c) {
        return null;
    }

    /**
     * Inserts specified String s before specified node from specified position in String
     */
    private void insertSequenceBefore(Node node, String s, int posInWord) {
        if (s.length() == 0) return;
        if (node == null) {
            size++;
            node = head = last = new Node(s.charAt(posInWord--));
        }
        for (int i = posInWord; i >= 0; i--) {
            node = insertBefore(node, s.charAt(i));
            size++;
        }
    }

    /**
     * Inserts specified String s after specified node from specified position in String
     */
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

    /**
     * Inserts specified String s after specified node from specified position in String
     */
    @Override
    public DynamicLinkedString addFirst(DynamicString s) {
        for (char c : s) {
            addFirst(c);
        }
        return this;
    }

    /**
     * Inserts specified Object as String value to the first position of this dynamicLinkedString
     *
     * @param o Object as String value to insert
     */
    @Override
    public DynamicLinkedString addFirst(Object o) {
        return addFirst(String.valueOf(String.valueOf(o)));
    }

    /**
     * Inserts specified String to the first position of this dynamicLinkedString
     *
     * @param s String to insert
     */
    @Override
    public DynamicLinkedString addFirst(String s) {
        insertSequenceBefore(head, s, s.length() - 1);
        return this;
    }

    /**
     * Inserts specified Character to the first position of this dynamicLinkedString
     *
     * @param c char to insert
     */
    @Override
    public DynamicLinkedString addFirst(char c) {
        if (head == null) {
            head = last = new Node(c);
        } else {
            insertBefore(head, c);
        }
        size++;
        return this;
    }

    /**
     * Inserts specified Number to the first position of this dynamicLinkedString
     *
     * @param num to insert
     */
    @Override
    public DynamicLinkedString addFirst(int num) {
        return addFirst(String.valueOf(num));
    }

    /**
     * Appends unicode number as character
     *
     * @param code unicode character point
     */
    @Override
    public DynamicLinkedString addUnicodeChar(int code) {
        add((char) code);
        return this;
    }

    /**
     * Inserts specified char to the specified position
     *
     * @param pos position to insert char
     * @param c   char to insert
     */
    @Override
    public DynamicLinkedString insert(int pos, char c) {
        if (pos >= size) return add(c);
        if (pos <= 0) return addFirst(c);
        insertBefore(getNodeByPos(pos), c);
        size++;
        return this;
    }

    /**
     * Inserts specified String to the specified position
     *
     * @param pos position to insert specified String
     * @param s   String to insert
     */
    @Override
    public DynamicLinkedString insert(int pos, String s) {
        if (pos >= size) {
            insertSequenceAfter(last, s, 0);
            return this;
        }
        if (pos <= 0) {
            insertSequenceBefore(head, s, s.length() - 1);
            return this;
        }

        insertSequenceBefore(getNodeByPos(pos), s, s.length() - 1);
        return this;
    }

    /**
     * Inserts specified String to the specified position
     *
     * @param pos position to insert specified String
     * @param s   String to insert
     */
    @Override
    public DynamicLinkedString insert(int pos, DynamicString s) {
        return insert(pos, s.toString());
    }

    /**
     * Inserts specified char array to the specified position
     *
     * @param pos position to insert specified char array
     * @param s   char array to insert
     */
    @Override
    public DynamicLinkedString insert(int pos, char[] s) {
        return insert(pos, new String(s));
    }

    /**
     * Returns node from dynamicLinkedString by the specified position
     */
    private Node getNodeByPos(int pos) {
        int mid = size / 2;
        boolean h = pos >= mid;
        pos = pos >= mid ? size - pos - 1 : pos;
        Node curr;
        if (h) {
            for (curr = last; pos > 0; pos--) {
                curr = curr.prev;
            }
        } else {
            for (curr = head; pos > 0; pos--) {
                curr = curr.next;
            }
        }
        return curr;
    }

    /**
     * Links new node with specified character after the specified node
     */
    private Node insertAfter(Node node, char toInsert) {
        Node newNode = new Node(toInsert);
        newNode.next = node.next;
        node.next = newNode;
        newNode.prev = node;
        if (newNode.next != null) newNode.next.prev = newNode;
        else last = newNode;
        return newNode;
    }

    /**
     * Links new node with specified character before the specified node
     */
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
     * Provides to remove sequence in range from the specified start to the specified end
     *
     * @param start start position of removed range
     * @param end   end position of removed range
     * @throws IndexOutOfCollectionBoundsException if startPos > end or specified positions out of string range
     */
    @Override
    public DynamicLinkedString delete(int start, int end) {
        if (start < 0 || start >= end) {
            throw new IndexOutOfCollectionBoundsException();
        }
        if (end >= size) end = size;
        Node node = getNodeByPos(start);
        if (node == head) {
            end--;
        } else {
            node = node.prev;
        }
        while (start < end && node != null) {
            deleteAfter(node);
            end--;
            size--;
        }
        return start == 0 ? deleteFirst() : this;
    }

    /**
     * Provides to all characters from the specified position
     *
     * @throws IndexOutOfCollectionBoundsException if the specified start position out of string bounds
     */
    @Override
    public DynamicLinkedString delete(int start) {
        if (start < 0) {
            throw new IndexOutOfCollectionBoundsException();
        }
        if (start == 0) return clear();
        if (start >= size) start = size;
        Node node = getNodeByPos(start - 1);
        int len = size;
        for (int i = start; i < len; i++, size--) {
            deleteAfter(node);
        }
        return this;
    }

    /**
     * Provides to remove character on the specified position
     *
     * @throws IndexOutOfCollectionBoundsException if the specified position out of string bounds
     */
    @Override
    public DynamicLinkedString deleteAtPosition(int pos) {
        if (pos < 0 || pos > size) throw new IndexOutOfCollectionBoundsException();
        Node node = getNodeByPos(pos);
        if (node.prev == null) return deleteFirst();
        if (node.next == null) return deleteLast();
        if (deleteAfter(node.prev) != null) size--;
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
        if (toRemove == head) head = toRemove.next;
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
        if (toRemove == last) last = toRemove.prev;
        Node next = toRemove.next;
        toRemove.prev = null;
        if (next != null) next.prev = node;
        node.next = next;
        return node;
    }

    /**
     * Removes first character of String if string size more than 0
     */
    @Override
    public DynamicLinkedString deleteFirst() {
        if (head == null) return this;
        size--;
        head = head.next;
        if (head != null) head.prev = null;
        else last = null;
        return this;
    }

    /**
     * Removes last character of String if string size more than 0
     */
    @Override
    public DynamicLinkedString deleteLast() {
        if (last == null) return this;
        size--;
        last = last.prev;
        if (last != null) last.next = null;
        else head = null;
        return this;
    }

    /**
     * Replaces specified characters in range from start to end by the new specified string
     *
     * @param start start of range
     * @param end   end of range
     * @param s     string to replace
     * @throws IndexOutOfCollectionBoundsException if specified position is specified start or end position is illegal
     */
    @Override
    public DynamicLinkedString replace(int start, int end, String s) {
        if (start < 0 || start >= end || start >= size) {
            throw new IndexOutOfCollectionBoundsException();
        }
        if (end >= size) end = size;
        Node curr = getNodeByPos(start);
        int gap = end - start, pos;
        Node temp = curr;
        for (pos = 0; pos < s.length() && pos < gap; pos++, curr = curr.next) {
            curr.val = s.charAt(pos);
            temp = curr;
        }
        if (gap - pos > 0) {
            return delete(start + pos, end);
        } else if (pos < s.length()) {
            insertSequenceAfter(temp, s, pos);
        }
        return this;
    }

    /**
     * Replaces specified characters in range from start to end of dynamicLinkedSting by the new specified string
     *
     * @param start start of range
     * @param s     string to replace
     * @throws IndexOutOfCollectionBoundsException if specified position is specified start or end position is illegal
     */
    @Override
    public DynamicLinkedString replace(int start, String s) {
        return replace(start, size, s);
    }

    /**
     * Replaces specified characters in range from start to end by new specified string
     *
     * @param start start of range
     * @param end   end of range
     * @param s     string to replace
     * @throws IndexOutOfCollectionBoundsException if specified position is specified start or end position is illegal
     */
    @Override
    public DynamicLinkedString replace(int start, int end, DynamicLinkedString s) {
        return replace(start, end, s.toString());
    }

    /**
     * Replaces specified characters in range from start to end of dynamicLinkedSting by the new specified string
     *
     * @param start start of range
     * @param s     string to replace
     * @throws IndexOutOfCollectionBoundsException if specified position is specified start or end position is illegal
     */
    @Override
    public DynamicLinkedString replace(int start, DynamicLinkedString s) {
        return replace(start, size, s.toString());
    }

    /**
     * Replaces specified characters in range from start to end by the new specified char array
     *
     * @param start start of range
     * @param end   end of range
     * @param c     char array to replace
     * @throws IndexOutOfCollectionBoundsException if specified position is specified start or end position is illegal
     */
    @Override
    public DynamicLinkedString replace(int start, int end, char[] c) {
        return replace(start, end, String.valueOf(c));
    }

    /**
     * Replaces specified characters in range from start to end of dynamicLinkedSting by the new specified  char array
     *
     * @param start start of range
     * @param c     char array to replace
     * @throws IndexOutOfCollectionBoundsException if specified position is specified start or end position is illegal
     */
    @Override
    public DynamicLinkedString replace(int start, char[] c) {
        return replace(start, size, String.valueOf(c));
    }

    /**
     * Replaces specified characters in range from start to end by the new specified char
     *
     * @param start start of range
     * @param end   end of range
     * @param c     char to replace
     * @throws IndexOutOfCollectionBoundsException if specified position is specified start or end position is illegal
     */
    @Override
    public DynamicLinkedString replace(int start, int end, char c) {
        return replace(start, end, String.valueOf(c));
    }

    /**
     * Replaces specified characters in range from start to end of dynamicLinkedSting by the new specified  char
     *
     * @param start start of range
     * @param c     char to replace
     * @throws IndexOutOfCollectionBoundsException if specified position is specified start or end position is illegal
     */
    @Override
    public DynamicLinkedString replace(int start, char c) {
        return replace(start, size, String.valueOf(c));
    }

    /**
     * Splits current dynamicLinkedString by the specified delimiter
     */
    @Override
    public List<DynamicString> split(String delimiter) {
        //todo implement it!!!
        throw new IllegalStateException("Method not implemented yet!!!");
    }

    /**
     * Splits current dynamicLinkedString by the specified delimiter
     */
    @Override
    public List<DynamicString> split(DynamicString delimiter) {
        //todo implement it!!!
        throw new IllegalStateException("Method not implemented yet!!!");
    }

    /**
     * Updates value in the specified position by the specified character
     */
    @Override
    public DynamicLinkedString update(int pos, char c) {
        if (pos < 0 || pos >= size) {
            throw new IndexOutOfCollectionBoundsException();
        }
        getNodeByPos(pos).val = c;
        return this;
    }

    /**
     * Provides to get char by specified position
     *
     * @return char by position
     * @throws ArrayIndexOutOfBoundsException if specified position out of string bounds
     */
    @Override
    public char get(int pos) {
        if (pos < 0 || pos >= size) {
            throw new ArrayIndexOutOfBoundsException("Specified position out of string bounds");
        }
        return getNodeByPos(pos).val;
    }

    /**
     * Returns first character of String
     *
     * @throws UnsupportedOperationException if string is empty
     */
    @Override
    public char getFirst() {
        if (head == null) throw new UnsupportedOperationException("String is empty");
        return head.val;
    }

    /**
     * Returns last character of String
     *
     * @throws UnsupportedOperationException if string is empty
     */
    @Override
    public char getLast() {
        if (last == null) throw new UnsupportedOperationException("String is empty");
        return last.val;
    }

    @Override
    public char[] toCharArray() {
        char[] result = new char[size];
        Node curr = head;
        for (int pos = 0; pos < size; pos++, curr = curr.next) result[pos] = curr.val;
        return result;
    }

    /**
     * Reverses String
     */
    @Override
    public DynamicLinkedString reverse() {
        char[] result = new char[size];
        Node curr = head;
        for (int pos = 0; pos < size; pos++, curr = curr.next) result[pos] = curr.val;
        curr = head;
        for (int pos = size - 1; pos >= 0; pos--, curr = curr.next) curr.val = result[pos];
        return this;
    }

    /**
     * Returns count of specified character in String
     */
    @Override
    public int count(char c) {
        int count = 0;
        for (Node curr = head; curr != null; curr = curr.next) {
            if (curr.val == c) count++;
        }
        return count;
    }

    /**
     * Slices string in range from start to end
     */
    private char[] subStr(int start, int end) {
        if (start > end || start < 0) {
            throw new IndexOutOfCollectionBoundsException();
        }
        Node first = head;
        for (int pos = 0; pos < start; pos++) first = first.next;
        int len = end - start;
        char[] lst = new char[len];
        for (int pos = 0; pos < len; pos++, first = first.next) lst[pos] = first.val;
        return lst;
    }

    /**
     * Returns coped substring in range from start to end
     *
     * @param start start position of substring
     * @param end   end position of substring
     */
    @Override
    public DynamicLinkedString subSequence(int start, int end) {
        return new DynamicLinkedString(subStr(start, end));
    }


    /**
     * Returns coped substring in range from start to end of this dynamicLinkedString
     *
     * @param start start position of substring
     */
    @Override
    public DynamicLinkedString subSequence(int start) {
        return new DynamicLinkedString(subStr(start, size));
    }


    /**
     * Returns substring in range from start to end
     *
     * @param start start position of substring
     * @param end   end position of substring
     */
    @Override
    public String subString(int start, int end) {
        return new String(subStr(start, end));
    }

    /**
     * Returns substring in range from start to end of this dynamicLinkedString
     *
     * @param start start position of substring
     */
    @Override
    public String subString(int start) {
        return new String(subStr(start, size));
    }

    /**
     * Returns true if this dynamicLinkedString starts with specified string otherwise false
     */
    @Override
    public boolean startsWith(String s) {
        if (s.length() > size) return false;
        Node first = head;
        for (int i = 0; i < s.length(); i++, first = first.next) {
            if (first.val != s.charAt(i)) return false;
        }
        return true;
    }

    /**
     * Returns true if this dynamicLinkedString starts with specified char otherwise false
     */
    @Override
    public boolean startsWith(char c) {
        return head != null && head.val == c;
    }

    /**
     * Returns true if this dynamicLinkedString starts with specified string otherwise false
     */
    @Override
    public boolean startsWith(DynamicString s) {
        if (s.getSize() > size) return false;
        Node first = head;
        for (Character c : s) {
            if (first.val != c) return false;
            first = first.next;
        }
        return true;
    }

    /**
     * Returns true if this dynamicLinkedString ends with specified string otherwise false
     */
    @Override
    public boolean endsWith(String s) {
        if (s.length() > size) return false;
        Node first = last;
        int len = s.length() - 1;
        for (int i = 0; i < s.length(); i++, first = first.prev) {
            if (first.val != s.charAt(len - i)) return false;
        }
        return true;
    }

    /**
     * Returns true if this dynamicLinkedString ends with specified char otherwise false
     */
    @Override
    public boolean endsWith(char c) {
        return head != null && last.val == c;
    }

    /**
     * Returns true if this dynamicLinkedString ends with specified string otherwise false
     */
    @Override
    public boolean endsWith(DynamicString s) {
        if (s.getSize() > size) return false;
        Node first = getNodeByPos(size - s.getSize());
        for (Character c : s) {
            if (first.val != c) return false;
            first = first.next;
        }
        return true;
    }

    /**
     * Returns first occurrence of the specified char in this dynamicLinkedString
     *
     * @return first char position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int indexOf(char c) {
        return indexOf(c, 0);
    }

    /**
     * Returns first occurrence of the specified char in this dynamicLinkedString from the specified position
     *
     * @return first char position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int indexOf(char c, int pos) {
        if (size == 0) return -1;
        Node first = getNodeByPos(pos);
        for (int i = pos; first != null; first = first.next, i++) {
            if (first.val == c) return i;
        }
        return -1;
    }

    /**
     * Returns first occurrence of the specified char array in this dynamicLinkedString
     *
     * @return first char array position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int indexOf(char[] c) {
        return indexOf(c, 0);
    }

    /**
     * Returns first occurrence of the specified char array in this dynamicLinkedString from the specified position
     *
     * @return first char array position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int indexOf(char[] c, int pos) {
        if (c.length > size || c.length == 0) return -1;
        Node first = getNodeByPos(pos);
        int startPos = 0;
        for (int i = pos; first != null; first = first.next, i++) {
            if (first.val == c[startPos]) {
                if (++startPos == c.length) {
                    return i + 1 - c.length;
                }
            } else {
                startPos = 0;
            }
        }
        return -1;
    }

    /**
     * Returns first occurrence of the specified String in this dynamicLinkedString
     *
     * @return first String position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int indexOf(String s) {
        return indexOf(s, 0);
    }

    /**
     * Returns first occurrence of the specified String in this dynamicLinkedString from the specified position
     *
     * @return first String position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int indexOf(String s, int pos) {
        int strSize = s.length();
        if (strSize > size || strSize == 0) return -1;
        Node first = getNodeByPos(pos);
        int startPos = 0;
        for (int i = pos; first != null; first = first.next, i++) {
            if (first.val == s.charAt(startPos)) {
                if (++startPos == strSize) {
                    return i + 1 - strSize;
                }
            } else {
                startPos = 0;
            }
        }
        return -1;
    }

    /**
     * Returns first occurrence first index of the specified String in this dynamicLinkedString
     *
     * @return first String position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int indexOf(DynamicString s) {
        return indexOf(s, 0);
    }

    /**
     * Returns first occurrence first index of the specified String in this dynamicLinkedString from the specified position
     *
     * @return first String position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int indexOf(DynamicString s, int pos) {
        int strSize = s.getSize();
        if (strSize > size || strSize == 0) return -1;
        Iterator<Character> foreignIterator = s.iterator();
        Node first = getNodeByPos(pos);
        char fChar = foreignIterator.next();
        for (int i = pos; first != null; first = first.next, i++) {
            if (first.val == fChar) {
                if (!foreignIterator.hasNext()) {
                    return i + 1 - strSize;
                }
            } else {
                foreignIterator = s.iterator();
            }
            fChar = foreignIterator.next();
        }
        return -1;
    }

    /**
     * Returns last occurrence of the specified char  in this dynamicLinkedString
     *
     * @return last5 occurrence of the specified char in this dynamicLinkedString if founded otherwise -1
     */

    @Override
    public int lastIndexOf(char c) {
        Node end = last;
        for (int i = size - 1; end != null; end = end.prev, i--) {
            if (end.val == c) return i;
        }
        return -1;
    }

    /**
     * Returns last index of the specified char array in this dynamicLinkedString
     *
     * @return last char array position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int lastIndexOf(char[] c) {
        if (c.length > size || c.length == 0) return -1;
        Node end = last;
        int startPos = c.length - 1;
        for (int i = size - 1; end != null; end = end.prev, i--) {
            if (end.val == c[startPos]) {
                if (--startPos == -1) {
                    return i;
                }
            } else {
                startPos = c.length - 1;
            }
        }
        return -1;
    }

    /**
     * Returns last index of the specified String in this dynamicLinkedString
     *
     * @return last String position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int lastIndexOf(String s) {
        int strSize = s.length();
        if (strSize > size || strSize == 0) return -1;
        Node end = last;
        int startPos = s.length() - 1;
        for (int i = size - 1; end != null; end = end.prev, i--) {
            if (end.val == s.charAt(startPos)) {
                if (--startPos == -1) {
                    return i;
                }
            } else {
                startPos = s.length() - 1;
            }
        }
        return -1;
    }

    /**
     * Returns last index of the specified String in this dynamicLinkedString
     *
     * @return last String position in this dynamicLinkedString if founded otherwise -1
     */
    @Override
    public int lastIndexOf(DynamicString s) {
        return lastIndexOf(s.toCharArray());
    }

    @Override
    public DynamicLinkedString clear() {
        head = last = null;
        size = 0;
        return this;
    }

    @Override
    public DynamicLinkedString copy() {
        return new DynamicLinkedString(this);
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

    @Override
    public Iterator<Character> iterator() {
        return new SelfIterator();
    }

    class SelfIterator implements Iterator<Character> {
        Node current;

        public SelfIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Character next() {
            if (current == null) throw new NoSuchElementException();
            Character data = current.val;
            current = current.next;
            return data;
        }
    }
}
