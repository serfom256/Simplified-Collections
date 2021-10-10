package Additional.DynamicString;

public interface AbstractDynamicString {

    AbstractDynamicString add(AbstractDynamicString s);

    AbstractDynamicString add(String s);

    AbstractDynamicString add(char element);

    AbstractDynamicString addFirst(char element);

    AbstractDynamicString insert(int pos, char c);

    AbstractDynamicString insert(int pos, String s);

    AbstractDynamicString insert(int pos, AbstractDynamicString s);

    AbstractDynamicString subString(int start, int end);

    AbstractDynamicString subString(int start);

    char getLast();

    char getFirst();

    AbstractDynamicString popFirst();

    AbstractDynamicString popLast();

    int count(char element);

    int getSize();

}
