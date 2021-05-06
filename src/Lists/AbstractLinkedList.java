package Lists;

public interface AbstractLinkedList<E> extends AbstractList<E> {

    void pushLast(E data);

    void pushFirst(E data);

    E peekLast();

    E peekFirst();

    E popFirst();

    E popLast();
}
