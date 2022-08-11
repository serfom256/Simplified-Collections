package lists;

public interface SimpleList<E> extends Iterable<E> {

    void add(E element);

    void addIfAbsent(E element);

    <T extends Iterable<E>> void addFrom(T data);

    void clear();

    boolean delete(E element);

    E deleteAtPosition(int position);

    E get(int position);

    int indexOf(E element);

    int lastIndexOf(E element);

    int getSize();

    int count(E element);

    Object[] toObjectArray();

    <T> E[] toArray(Class<T> type);

    List<E> slice(int start, int end);
}
