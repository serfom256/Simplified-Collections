package additional.dynamicstring;

import lists.List;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

public class DynamicArrayString implements DynamicString {
    private static final int DEFAULT_CAPACITY = 20;
    private int capacity;
    private int size;
    private char[] data;


    public DynamicArrayString() {
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
    }


    public DynamicArrayString(char[] lst) {
        this();
        for (char c : lst) {
            add(c);
        }
    }

    public DynamicArrayString(String str) {
        this();
        for (int i = 0; i < str.length(); i++) {
            add(str.charAt(i));
        }
    }

    public DynamicArrayString(Character c) {
        this();
        addFirst(c);
    }

    public DynamicArrayString(char c) {
        this();
        addFirst(c);
    }

    public DynamicArrayString(DynamicString str) {
        this();
        add(str);
    }
    private void calculateCapacity(){
        while (size >= capacity) {
            capacity = (capacity << 1) - (capacity >> 1);
        }
        resize(capacity);
    }
    @Override
    public String toString() {
        return String.valueOf(toCharArray());
    }

    public static void main(String[] args) {
        DynamicString test = new DynamicArrayString().add("example");
        DynamicString temp = new DynamicArrayString().add("hello").add('$').add(new char[]{'a', 'b', 'c', 's'}).add(test, 3);
        System.out.println(temp.getSize());
        System.out.println(temp);
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        char[] newData = new char[capacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    @SuppressWarnings("unchecked")
    private void init(int capacity) {
        data = new char[capacity];
    }


    @Override
    public DynamicString add(char element) {
        if (data == null) init(capacity);
        data[size++] = element;
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString add(DynamicString s) {
        if (data == null) init(capacity);
        data = insertArrayAtPosition(s.toCharArray(), data, size);
        size += s.getSize();
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString add(Object o) {
        add(o.toString());
        return this;
    }

    @Override
    public DynamicString add(char[] c) {
        if (data == null) init(capacity);
        data = insertArrayAtPosition(c, data, size);
        size += c.length;
        while (size >= capacity) {
            capacity = (capacity << 1) - (capacity >> 1);
        }
        resize(capacity);
        return this;
    }

    @Override
    public DynamicString add(String s) {
        if (data == null) init(capacity);
        data = insertArrayAtPosition(s.toCharArray(), data, size);
        size += s.length();
        calculateCapacity();
        return this;
    }


    private static char[] insertArrayAtPosition(char[] arr1, char[] arr2, int insertPos) {
        char[] result = new char[arr1.length + arr2.length];
        System.arraycopy(arr2, 0, result, 0, insertPos);
        System.arraycopy(arr1, 0, result, insertPos, arr1.length);
        System.arraycopy(arr2, insertPos, result, insertPos + arr1.length, arr2.length - insertPos);
        return result;
    }
    // я не знаю как добавить один массв к другому...
    static char[] concatWithArrayCopy(char[] array1, char[] array2) {
        char[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    @Override
    public DynamicString add(DynamicString s, int pos) {
        char[] savesChar = Arrays.copyOfRange(data, pos, size);
        System.arraycopy(s.toCharArray(), 0, data, pos, s.getSize());
        data = insertArrayAtPosition( savesChar, data, pos + s.getSize());
        size += s.getSize();
        calculateCapacity();
        return this;
    }


    @Override
    public DynamicString add(String s, int pos) {
        return null;
    }

    @Override
    public DynamicString add(int num) {
        return null;
    }

    @Override
    public DynamicString addFirst(DynamicString s) {
        return null;
    }

    @Override
    public DynamicString addFirst(Object o) {
        return null;
    }

    @Override
    public DynamicString addFirst(String s) {
        return null;
    }

    @Override
    public DynamicString addFirst(char c) {
        return null;
    }

    @Override
    public DynamicString addFirst(int num) {
        return null;
    }

    @Override
    public DynamicString addUnicodeChar(int code) {
        return null;
    }

    @Override
    public DynamicString insert(int pos, char c) {
        return null;
    }

    @Override
    public DynamicString insert(int pos, String s) {
        return null;
    }

    @Override
    public DynamicString insert(int pos, DynamicString s) {
        return null;
    }

    @Override
    public DynamicString subSequence(int start, int end) {
        return null;
    }

    @Override
    public DynamicString subSequence(int start) {
        return null;
    }

    @Override
    public DynamicString reverse() {
        return null;
    }

    @Override
    public DynamicString insert(int pos, char[] s) {
        return null;
    }

    @Override
    public DynamicString delete(int start, int end) {
        return null;
    }

    @Override
    public DynamicString delete(int start) {
        return null;
    }

    @Override
    public DynamicString deleteAtPosition(int pos) {
        return null;
    }

    @Override
    public DynamicString deleteFirst() {
        return null;
    }

    @Override
    public DynamicString deleteLast() {
        return null;
    }

    @Override
    public DynamicString replace(int start, int end, String s) {
        return null;
    }

    @Override
    public DynamicString replace(int start, String s) {
        return null;
    }

    @Override
    public DynamicString replace(int start, int end, DynamicLinkedString s) {
        return null;
    }

    @Override
    public DynamicString replace(int start, DynamicLinkedString s) {
        return null;
    }

    @Override
    public DynamicString replace(int start, int end, char[] c) {
        return null;
    }

    @Override
    public DynamicString replace(int start, char[] c) {
        return null;
    }

    @Override
    public DynamicString replace(int start, int end, char c) {
        return null;
    }

    @Override
    public DynamicString replace(int start, char c) {
        return null;
    }

    @Override
    public List<DynamicString> split(String delimiter) {
        return null;
    }

    @Override
    public List<DynamicString> split(DynamicString delimiter) {
        return null;
    }

    @Override
    public DynamicString update(int pos, char c) {
        return null;
    }

    @Override
    public DynamicString clear() {
        return null;
    }

    @Override
    public DynamicString copy() {
        return null;
    }

    @Override
    public String subString(int start, int end) {
        return null;
    }

    @Override
    public String subString(int start) {
        return null;
    }

    @Override
    public boolean startsWith(DynamicString s) {
        return false;
    }

    @Override
    public boolean startsWith(String s) {
        return false;
    }

    @Override
    public boolean startsWith(char c) {
        return false;
    }

    @Override
    public boolean endsWith(DynamicString s) {
        return false;
    }

    @Override
    public boolean endsWith(String s) {
        return false;
    }

    @Override
    public boolean endsWith(char c) {
        return false;
    }

    @Override
    public int indexOf(char c) {
        return 0;
    }

    @Override
    public int indexOf(char[] c) {
        return 0;
    }

    @Override
    public int indexOf(String s) {
        return 0;
    }

    @Override
    public int indexOf(DynamicString s) {
        return 0;
    }

    @Override
    public int lastIndexOf(char c) {
        return 0;
    }

    @Override
    public int lastIndexOf(char[] c) {
        return 0;
    }

    @Override
    public int lastIndexOf(String s) {
        return 0;
    }

    @Override
    public int lastIndexOf(DynamicString s) {
        return 0;
    }

    @Override
    public int indexOf(char c, int pos) {
        return 0;
    }

    @Override
    public int indexOf(char[] c, int pos) {
        return 0;
    }

    @Override
    public int indexOf(String s, int pos) {
        return 0;
    }

    @Override
    public int indexOf(DynamicString s, int pos) {
        return 0;
    }

    @Override
    public char getLast() {
        return 0;
    }

    @Override
    public char get(int pos) {
        return 0;
    }

    @Override
    public char getFirst() {
        return 0;
    }

    @Override
    public int count(char c) {
        return 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public char[] toCharArray() {

        return Arrays.copyOfRange(data, 0, size);
    }

    @Override
    public Iterator<Character> iterator() {
        return null;
    }


}
