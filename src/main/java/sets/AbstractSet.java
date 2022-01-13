package sets;

public interface AbstractSet<E> extends Iterable<E> {

    void update(E OldElement, E newElement);

    void add(E element);

    void clear();

    boolean remove(E element);

    boolean contains(E element);

    int getSize();

    Object[] toObjectArray();


    AbstractSet<E> left(AbstractSet<E> set);

    AbstractSet<E> right(AbstractSet<E> set);

    AbstractSet<E> between(AbstractSet<E> set);

    AbstractSet<E> union(AbstractSet<E> set);

}
