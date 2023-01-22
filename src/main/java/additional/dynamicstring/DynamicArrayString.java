package additional.dynamicstring;

import additional.exceptions.IndexOutOfCollectionBoundsException;
import lists.List;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

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
        add(str);
    }

    public DynamicArrayString(Character c) {
        this();
        add(c);
    }

    public DynamicArrayString(char c) {
        this();
        add(c);
    }

    public DynamicArrayString(DynamicString str) {
        this();
        add(str);
    }

    private void calculateCapacity() {
        capacity = (size << 1) - (size >> 1);
        if (capacity < DEFAULT_CAPACITY) {
            capacity = DEFAULT_CAPACITY;
        }
        resize(capacity);
    }

    @Override
    public String toString() {
        if (size == 0) return "";
        return String.valueOf(toCharArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicArrayString that = (DynamicArrayString) o;
        return capacity == that.capacity && size == that.size && Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(capacity, size);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    private void resize(int capacity) {
        char[] newData = new char[capacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    private void init() {
        data = new char[DEFAULT_CAPACITY];
        size = 0;
    }

    private char[] insertArrayAtPosition(char[] arr1, char[] arr2, int insertPos) {
        char[] result = new char[arr1.length + arr2.length];
        System.arraycopy(arr2, 0, result, 0, insertPos);
        System.arraycopy(arr1, 0, result, insertPos, arr1.length);
        System.arraycopy(arr2, insertPos, result, insertPos + arr1.length, arr2.length - insertPos);
        return result;
    }

    private int dataPosChecker(int pos) {
        if (data == null) init();
        if (pos >= size) pos = size;
        if (pos <= 0) pos = 0;
        return pos;
    }

    @Override
    public DynamicString add(char element) {
        if (data == null) init();
        data[size++] = element;
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString add(DynamicString s) {
        if (data == null) init();
        data = insertArrayAtPosition(s.toCharArray(), data, size);
        size += s.getSize();
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString add(Object o) {
        return add(o.toString());
    }

    @Override
    public DynamicString add(char[] c) {
        if (data == null) init();
        data = insertArrayAtPosition(c, data, size);
        size += c.length;
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString add(String s) {
        if (data == null) init();
        data = insertArrayAtPosition(s.toCharArray(), data, size);
        size += s.length();
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString add(int num) {
        if (data == null) init();
        data[++size] = (char) num;
        return this;
    }

    @Override
    public DynamicString addFirst(DynamicString s) {
        if (data == null) init();
        char[] dataBackUp = data.clone();
        data = s.toCharArray();
        data = insertArrayAtPosition(dataBackUp, data, s.getSize());
        size += s.getSize();
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString addFirst(Object o) {
        return addFirst(o.toString());
    }

    @Override
    public DynamicString addFirst(String s) {
        if (data == null) init();
        char[] dataBackUp = data.clone();
        data = s.toCharArray();
        data = insertArrayAtPosition(dataBackUp, data, s.length());
        size += s.length();
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString addFirst(char c) {
        if (data == null) init();
        char[] dataBackUp = data.clone();
        data[0] = c;
        data = insertArrayAtPosition(dataBackUp, data, 1);
        size++;
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString addFirst(int num) {
        char[] dataBackUp = data;
        data[0] = (char) num;
        data = insertArrayAtPosition(dataBackUp, data, 1);
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString addUnicodeChar(int code) {
        data[++size] = (char) code;
        calculateCapacity();
        return this;
    }


    @Override
    public DynamicString insert(int pos, char c) {
        pos = dataPosChecker(pos);
        char[] rightPart = Arrays.copyOfRange(data, pos, size++);
        data[pos] = c;
        data = insertArrayAtPosition(rightPart, data, ++pos);
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString insert(int pos, String s) {
        pos = dataPosChecker(pos);
        char[] rightPart = Arrays.copyOfRange(data, pos, size);
        data = insertArrayAtPosition(s.toCharArray(), data, pos);
        data = insertArrayAtPosition(rightPart, data, pos + s.length());
        size += s.length();
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString insert(int pos, DynamicString s) {
        pos = dataPosChecker(pos);
        char[] rightPart = Arrays.copyOfRange(data, pos, size);
        data = insertArrayAtPosition(s.toCharArray(), data, pos);
        data = insertArrayAtPosition(rightPart, data, pos + s.getSize());
        size += s.getSize();
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString subSequence(int start, int end) {
        if (start >= 0 && end < this.size + 1) {
            return new DynamicArrayString(Arrays.copyOfRange(data, start, end));
        }
        throw new IndexOutOfCollectionBoundsException();
    }

    @Override
    public DynamicString subSequence(int start) {
        return new DynamicArrayString(Arrays.copyOfRange(data, start, size));
    }

    @Override
    public DynamicString reverse() {
        int start = 0;
        int end = size - 1;
        while (end > start) {
            char dataSave = data[start];
            data[start++] = data[end];
            data[end--] = dataSave;
        }
        return this;
    }

    @Override
    public DynamicString insert(int pos, char[] s) {
        pos = dataPosChecker(pos);
        char[] savesChar = Arrays.copyOfRange(data, pos, size);
        System.arraycopy(s, 0, data, pos, s.length);
        data = insertArrayAtPosition(savesChar, data, pos + s.length);
        size += s.length;
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString delete(int start, int end) {
        char[] rightPart = Arrays.copyOfRange(data, end, size);
        data = Arrays.copyOfRange(data, 0, start);
        data = insertArrayAtPosition(rightPart, data, data.length);
        size = size - (end - start);
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString delete(int start) {
        return delete(start, size);
    }

    @Override
    public DynamicString deleteAtPosition(int pos) {
        if (size == 0) return this;
        char[] rightPart = Arrays.copyOfRange(data, pos + 1, size--);
        data = Arrays.copyOfRange(data, 0, pos);
        data = insertArrayAtPosition(rightPart, data, pos);
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString deleteFirst() {
        if (size == 0) return this;
        data = Arrays.copyOfRange(data, 1, size--);
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString deleteLast() {
        if (size == 0) return this;
        data = Arrays.copyOfRange(data, 0, --size);
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString replace(int start, int end, String s) {
        if (start < 0 || start >= end || start >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (end > size) end = size;
        char[] leftPart = Arrays.copyOfRange(data, 0, start);
        char[] rightPart = Arrays.copyOfRange(data, end, size);
        data = s.toCharArray();
        data = insertArrayAtPosition(leftPart, data, 0);
        data = insertArrayAtPosition(rightPart, data, data.length);
        size = data.length;
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString replace(int start, String s) {
        if (start < 0 || start >= size) {
            throw new IndexOutOfBoundsException();
        }
        char[] leftPart = Arrays.copyOfRange(data, 0, start);
        data = s.toCharArray();
        data = insertArrayAtPosition(leftPart, data, 0);
        size = data.length;
        calculateCapacity();
        return this;
    }

    @Override
    public DynamicString replace(int start, int end, DynamicLinkedString s) {
        return null;
        // TODO: 1/20/2023 implement method
    }

    @Override
    public DynamicString replace(int start, DynamicLinkedString s) {
        return null;
        // TODO: 1/20/2023 implement method
    }

    @Override
    public DynamicString replace(int start, int end, char[] c) {
        return null;
        // TODO: 1/20/2023 implement method
    }

    @Override
    public DynamicString replace(int start, char[] c) {
        return null;
        // TODO: 1/20/2023 implement method
    }

    @Override
    public DynamicString replace(int start, int end, char c) {
        return null;
        // TODO: 1/20/2023 implement method
    }

    @Override
    public DynamicString replace(int start, char c) {
        return null;
        // TODO: 1/20/2023 implement method
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
        data[pos] = c;
        return this;
    }

    @Override
    public DynamicString clear() {
        init();
        return this;
    }

    @Override
    public DynamicString copy() {
        return this;
    }

    @Override
    public String subString(int start, int end) {
        return String.valueOf(Arrays.copyOfRange(data, start, end));
    }

    @Override
    public String subString(int start) {
        if (start >= size) return "";
        return String.valueOf(Arrays.copyOfRange(data, start, size));
    }

    @Override
    public boolean startsWith(DynamicString s) {
        int strSize = s.getSize() - 1;
        if (strSize > size) return false;
        for (int i = 0; i < size && i <= strSize; i++) {
            if (data[i] == s.get(i) && i == strSize) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean startsWith(String s) {
        int strSize = s.length() - 1;
        if (strSize > size || size == 0) return false;
        for (int i = 0; i < size && i <= strSize; i++) {
            if (data[i] == s.charAt(i) && i == strSize) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean startsWith(char c) {
        if (size == 0) return false;
        return c == data[0];
    }

    @Override
    public boolean endsWith(DynamicString s) {
        int strSize = s.getSize();
        if (strSize > size || strSize == 0) return false;
        int startPos = strSize - 1;
        for (int i = size - 1; i >= (size - strSize - 1); i--) {
            if (data[i] == s.get(startPos)) {
                if (--startPos == -1) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean endsWith(String s) {
        if (size == 0 || s.length() == 0) return false;
        int startPos = s.length() - 1;
        for (int i = size - 1; i >= (size - s.length()); i--) {
            if (data[i] == s.charAt(startPos)) {
                if (--startPos == -1) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean endsWith(char c) {
        return data != null && this.getLast() == c;
    }

    @Override
    public int indexOf(char c) {
        if (size == 0) return -1;
        for (int i = 0; i < size; i++) {
            if (data[i] == c) return i;
        }
        return -1;
    }

    @Override
    public int indexOf(char[] c) {
        if (c.length > size || c.length == 0) return -1;
        int startPos = 0;
        for (int i = 0; i < size; i++) {
            if (data[i] == c[startPos]) {
                if (++startPos == c.length) {
                    return i + 1 - c.length;
                }
            } else {
                startPos = 0;
            }
        }
        return -1;
    }

    @Override
    public int indexOf(String s) {
        int strSize = s.length();
        if (strSize > size || strSize == 0) return -1;
        int startPos = 0;
        for (int i = 0; i < size; i++) {
            if (data[i] == s.charAt(startPos)) {
                if (++startPos == strSize) {
                    return i + 1 - strSize;
                }
            } else {
                startPos = 0;
            }
        }
        return -1;
    }

    @Override
    public int indexOf(DynamicString s) {
        int strSize = s.getSize();
        int startPos = 0;
        if (strSize == 0 || strSize > size) return -1;
        for (int i = 0; i < size; i++) {
            if (data[i] == s.get(startPos)) {
                if (++startPos == strSize) {
                    return i + 1 - strSize;
                }
            } else {
                startPos = 0;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(char c) {
        if (size == 0) return -1;
        for (int i = size; i > 0; i--) {
            if (data[i] == c) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(char[] c) {
        if (size == 0 || c.length > size || c.length == 0) return -1;
        int startPos = c.length - 1;
        for (int i = size; i > -1; i--) {
            if (data[i] == c[startPos]) {
                if (--startPos == -1) return i;
            } else {
                startPos = c.length - 1;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(String s) {
        if (size == 0 || s.length() > size || s.length() == 0) return -1;
        int startPos = s.length() - 1;
        for (int i = size; i > -1; i--) {
            if (data[i] == s.charAt(startPos)) {
                if (--startPos == -1) return i;
            } else {
                startPos = s.length() - 1;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(DynamicString s) {
        if (size == 0 || s.getSize() > size || s.getSize() == 0) return -1;
        int startPos = s.getSize() - 1;
        for (int i = size; i > -1; i--) {
            if (data[i] == s.get(startPos)) {
                if (--startPos == -1) return i;
            } else {
                startPos = s.getSize() - 1;
            }
        }
        return -1;
    }

    @Override
    public int indexOf(char c, int pos) {
        for (int i = pos; i < size; i++) {
            if (data[i] == c) return i;
        }
        return -1;
    }

    @Override
    public int indexOf(char[] c, int pos) {
        if (c.length > size || c.length == 0 || (size - pos) < c.length) return -1;
        int startPos = 0;
        for (int i = pos; i < size; i++) {
            if (data[i] == c[startPos]) {
                if (++startPos == c.length) {
                    return i + 1 - c.length;
                }
            } else {
                startPos = 0;
            }
        }
        return -1;
    }

    @Override
    public int indexOf(String s, int pos) {
        int strSize = s.length();
        if (strSize > size || strSize == 0) return -1;
        int startPos = 0;
        for (int i = pos; i < size; i++) {
            if (data[i] == s.charAt(startPos)) {
                if (++startPos == strSize) {
                    return i + 1 - strSize;
                }
            } else {
                startPos = 0;
            }
        }
        return -1;
    }

    @Override
    public int indexOf(DynamicString s, int pos) {
        int strSize = s.getSize();
        if (strSize > size || strSize == 0) return -1;
        int startPos = 0;
        for (int i = pos; i < size; i++) {
            if (data[i] == s.get(startPos)) {
                if (++startPos == strSize) {
                    return i + 1 - strSize;
                }
            } else {
                startPos = 0;
            }
        }
        return -1;
    }

    @Override
    public char getLast() {
        return data[size - 1];
    }

    @Override
    public char get(int pos) {
        return data[pos];
    }

    @Override
    public char getFirst() {
        return data[0];
    }

    @Override
    public int count(char c) {
        int charCount = 0;
        for (int p = 0; p < size; p++) {
            if (data[p] == c) charCount++;
        }
        return charCount;
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
