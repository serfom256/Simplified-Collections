package Lists;

public interface AbstractLinkedList<E> extends AbstractList<E> {


    void addFirst(E element);

    void addLast(E element);

    E removeFirst();

    E removeLast();

}
