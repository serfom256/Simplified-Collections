package Lists;

public interface AbstractList<E extends Comparable<E>> {
    void pushLast(E data);

    void pushFirst(E data);

    void pushAll(E... data);

    void insert(E data, int position);

    void sort();

    void clear();

    E peekLast();

    E peekFirst();

    E popFirst();

    E popLast();

    E pop(int position);

    E remove(E data);

    E get(int position);

    int indexOf(E data);

    int lastIndexOf(E data);

    int getLength();


}
