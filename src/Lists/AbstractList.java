package Lists;

public interface AbstractList<E> extends Iterable<E> {

    void addAll(E... element);

    void add(E element);

    void insert(E element, int position);

    void sort();

    void clear();

    E pop(int position);

    E remove(E element);

    E get(int position);

    Object[] toObjectArray();

    int indexOf(E element);

    int lastIndexOf(E element);

    int getSize();

    AbstractList<E> slice(int start, int end);

}
