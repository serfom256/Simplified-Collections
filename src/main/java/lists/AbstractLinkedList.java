package lists;

public interface AbstractLinkedList<E> extends AbstractList<E> {

    void addFirst(E element);

    void addLast(E element);

    E deleteFirst();

    E deleteLast();

}
