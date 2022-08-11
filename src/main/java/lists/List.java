package lists;

public interface List<E> extends SimpleList<E> {

    void insert(int position, E element);

    void sort();

    void replace(int start, int end, E data);

    void replace(int start, E data);

    void replace(int start, int end, Iterable<E> data);

    void replace(int start, Iterable<E> data);

    void update(int pos, E data);

    void delete(int start, int end);

    E get(int position);

    E getFirst();

    E getLast();
}
