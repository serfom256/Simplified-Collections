package sets;

public interface AbstractSet<E> extends Iterable<E> {

    void add(E element);

    void clear();

    boolean delete(E element);

    boolean contains(E element);

    int getSize();

    Object[] toObjectArray();


    AbstractSet<E> left(AbstractSet<E> set);

    AbstractSet<E> right(AbstractSet<E> set);

    AbstractSet<E> between(AbstractSet<E> set);

    AbstractSet<E> union(AbstractSet<E> set);

}
