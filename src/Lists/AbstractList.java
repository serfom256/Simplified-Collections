package Lists;

public interface AbstractList<E> extends Iterable<E> {

    void add(E element);

    <T extends Iterable<E>> void add(T data);

    void insert(int position, E element);

    void sort();

    void clear();

    void replace(int start, int end, E data);

    void replace(int start, E data);

    void replace(int start, int end, Iterable<E> data);

    void replace(int start, Iterable<E> data);

    void update(int pos, E data);

    void delete(int start, int end);

    E deleteAtPosition(int position);

    E delete(E element);

    E get(int position);

    E getFirst();

    E getLast();

    Object[] toObjectArray();

    <T> E[] toArray(Class<T> type);

    int indexOf(E element);

    int lastIndexOf(E element);

    int getSize();

    int count(E element);

    AbstractList<E> slice(int start, int end);


}
