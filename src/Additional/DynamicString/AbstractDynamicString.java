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

    char getLast();

    char getFirst();

    AbstractDynamicString removeFirst();

    AbstractDynamicString removeLast();

    int count(char element);

    int getSize();

    char[] toCharArray();


}
