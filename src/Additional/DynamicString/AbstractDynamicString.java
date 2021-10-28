package Additional.DynamicString;

public interface AbstractDynamicString {

    AbstractDynamicString add(AbstractDynamicString s);

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

    AbstractDynamicString clear();

    char getLast();

    char getFirst();

    int count(char element);

    int getSize();

    char[] toCharArray();




}
