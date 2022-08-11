package lists;

public interface LinkedList<E> extends List<E> {

    void addFirst(E element);

    void addLast(E element);

    E deleteFirst();

    E deleteLast();

}
