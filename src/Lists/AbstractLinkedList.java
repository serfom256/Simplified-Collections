package Lists;

public interface AbstractLinkedList<E> extends AbstractList<E> {

    void pushLast(E element);

    void pushFirst(E element);

    E peekLast();

    E peekFirst();

    E popFirst();

    E popLast();

    int count(E element);
}
