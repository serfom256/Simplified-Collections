package Additional.DynamicString;

public interface AbstractDynamicString {

    AbstractDynamicString add(AbstractDynamicString s);

    AbstractDynamicString add(Object o);

    AbstractDynamicString add(String s);

    AbstractDynamicString add(char element);

    AbstractDynamicString addFirst(char element);

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

    char getLast();

    char getFirst();

    int count(char element);

    int getSize();

    char[] toCharArray();


}
