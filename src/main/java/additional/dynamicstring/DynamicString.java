package additional.dynamicstring;

import lists.List;

public interface DynamicString extends Iterable<Character> {

    DynamicString add(DynamicString s);

    DynamicString add(Object o);

    DynamicString add(String s);

    DynamicString add(DynamicString s, int pos);

    DynamicString add(String s, int pos);

    DynamicString add(char c);

    DynamicString add(int num);

    DynamicString addFirst(DynamicString s);

    DynamicString addFirst(Object o);

    DynamicString addFirst(String s);

    DynamicString addFirst(char c);

    DynamicString addFirst(int num);

    DynamicString addUnicodeChar(int code);

    DynamicString insert(int pos, char c);

    DynamicString insert(int pos, String s);

    DynamicString insert(int pos, DynamicString s);

    DynamicString subSequence(int start, int end);

    DynamicString subSequence(int start);

    DynamicString reverse();

    DynamicString insert(int pos, char[] s);

    DynamicString delete(int start, int end);

    DynamicString delete(int start);

    DynamicString deleteAtPosition(int pos);

    DynamicString deleteFirst();

    DynamicString deleteLast();

    DynamicString replace(int start, int end, String s);

    DynamicString replace(int start, String s);

    DynamicString replace(int start, int end, DynamicLinkedString s);

    DynamicString replace(int start, DynamicLinkedString s);

    DynamicString replace(int start, int end, char[] c);

    DynamicString replace(int start, char[] c);

    DynamicString replace(int start, int end, char c);

    DynamicString replace(int start, char c);

    List<DynamicString> split(String delimiter);

    List<DynamicString> split(DynamicString delimiter);

    DynamicString update(int pos, char c);

    DynamicString clear();

    DynamicString copy();

    String subString(int start, int end);

    String subString(int start);

    boolean startsWith(DynamicString s);

    boolean startsWith(String s);

    boolean startsWith(char c);

    boolean endsWith(DynamicString s);

    boolean endsWith(String s);

    boolean endsWith(char c);

    int indexOf(char c);

    int indexOf(char[] c);

    int indexOf(String s);

    int indexOf(DynamicString s);

    int lastIndexOf(char c);

    int lastIndexOf(char[] c);

    int lastIndexOf(String s);

    int lastIndexOf(DynamicString s);

    int indexOf(char c, int pos);

    int indexOf(char[] c, int pos);

    int indexOf(String s, int pos);

    int indexOf(DynamicString s, int pos);

    char getLast();

    char get(int pos);

    char getFirst();

    int count(char c);

    int getSize();

    char[] toCharArray();
}
