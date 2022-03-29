package additional.dynamicstring;

import lists.AbstractList;

public interface AbstractDynamicString extends Iterable<Character> {

    AbstractDynamicString add(AbstractDynamicString s);

    AbstractDynamicString add(Object o);

    AbstractDynamicString add(String s);

    AbstractDynamicString add(AbstractDynamicString s, int pos);

    AbstractDynamicString add(String s, int pos);

    AbstractDynamicString add(char c);

    AbstractDynamicString add(int num);

    AbstractDynamicString addFirst(AbstractDynamicString s);

    AbstractDynamicString addFirst(Object o);

    AbstractDynamicString addFirst(String s);

    AbstractDynamicString addFirst(char c);

    AbstractDynamicString addFirst(int num);

    AbstractDynamicString addUnicodeChar(int code);

    AbstractDynamicString insert(int pos, char c);

    AbstractDynamicString insert(int pos, String s);

    AbstractDynamicString insert(int pos, AbstractDynamicString s);

    AbstractDynamicString subSequence(int start, int end);

    AbstractDynamicString subSequence(int start);

    AbstractDynamicString reverse();

    AbstractDynamicString insert(int pos, char[] s);

    AbstractDynamicString delete(int start, int end);

    AbstractDynamicString delete(int start);

    AbstractDynamicString deleteAtPosition(int pos);

    AbstractDynamicString deleteFirst();

    AbstractDynamicString deleteLast();

    AbstractDynamicString replace(int start, int end, String s);

    AbstractDynamicString replace(int start, String s);

    AbstractDynamicString replace(int start, int end, DynamicLinkedString s);

    AbstractDynamicString replace(int start, DynamicLinkedString s);

    AbstractDynamicString replace(int start, int end, char[] c);

    AbstractDynamicString replace(int start, char[] c);

    AbstractDynamicString replace(int start, int end, char c);

    AbstractDynamicString replace(int start, char c);

    AbstractList<AbstractDynamicString> split(String delimiter);

    AbstractList<AbstractDynamicString> split(AbstractDynamicString delimiter);

    AbstractDynamicString update(int pos, char c);

    AbstractDynamicString clear();

    AbstractDynamicString copy();

    String subString(int start, int end);

    String subString(int start);

    boolean startsWith(AbstractDynamicString s);

    boolean startsWith(String s);

    boolean startsWith(char c);

    boolean endsWith(AbstractDynamicString s);

    boolean endsWith(String s);

    boolean endsWith(char c);

    int indexOf(char c);

    int indexOf(char[] c);

    int indexOf(String s);

    int indexOf(AbstractDynamicString s);

    int lastIndexOf(char c);

    int lastIndexOf(char[] c);

    int lastIndexOf(String s);

    int lastIndexOf(AbstractDynamicString s);

    int indexOf(char c, int pos);

    int indexOf(char[] c, int pos);

    int indexOf(String s, int pos);

    int indexOf(AbstractDynamicString s, int pos);

    char getLast();

    char get(int pos);

    char getFirst();

    int count(char c);

    int getSize();

    char[] toCharArray();
}
