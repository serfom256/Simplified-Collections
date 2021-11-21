package Additional.DynamicString;

public interface AbstractDynamicString extends Iterable<Character> {

    AbstractDynamicString add(AbstractDynamicString s);

    AbstractDynamicString add(Object o);

    AbstractDynamicString add(String s);

    AbstractDynamicString add(char c);

    AbstractDynamicString add(int num);

    AbstractDynamicString addFirst(AbstractDynamicString s);

    AbstractDynamicString addFirst(Object o);

    AbstractDynamicString addFirst(String s);

    AbstractDynamicString addFirst(char c);

    AbstractDynamicString addFirst(int num);

    AbstractDynamicString insert(int pos, char c);

    AbstractDynamicString insert(int pos, String s);

    AbstractDynamicString insert(int pos, AbstractDynamicString s);

    AbstractDynamicString subSequence(int start, int end);

    AbstractDynamicString subSequence(int start);

    AbstractDynamicString reverse();

    AbstractDynamicString insert(int pos, char[] s);

    AbstractDynamicString delete(int startPos, int endPos);

    AbstractDynamicString delete(int start);

    AbstractDynamicString deleteAtPosition(int pos);

    AbstractDynamicString removeFirst();

    AbstractDynamicString removeLast();

    AbstractDynamicString replace(int start, int end, String s);

    AbstractDynamicString replace(int start, String s);

    AbstractDynamicString replace(int start, int end, DynamicLinkedString s);

    AbstractDynamicString replace(int start, DynamicLinkedString s);

    AbstractDynamicString replace(int start, int end, char[] c);

    AbstractDynamicString replace(int start, char[] c);

    AbstractDynamicString replace(int start, int end, char c);

    AbstractDynamicString replace(int start, char c);

    AbstractDynamicString clear();

    String subString(int start, int end);

    String subString(int start);

    boolean startsWith(String s);

    boolean startsWith(char c);

    boolean endsWith(String s);

    boolean endsWith(char c);

    boolean startsWith(AbstractDynamicString s);

    boolean endsWith(AbstractDynamicString s);

    int indexOf(char c);

    int indexOf(char[] c);

    int indexOf(String s);

    int indexOf(AbstractDynamicString s);

    char getLast();

    char get(int pos);

    char getFirst();

    int count(char c);

    int getSize();

    char[] toCharArray();
}
