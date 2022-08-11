package sets;

public interface Set<E> extends Iterable<E> {

    <T extends Iterable<E>> void addFrom(T iterable);

    void add(E element);

    void clear();

    boolean delete(E element);

    boolean contains(E element);

    int getSize();

    Object[] toObjectArray();


    Set<E> left(Set<E> set);

    Set<E> right(Set<E> set);

    Set<E> between(Set<E> set);

    Set<E> union(Set<E> set);

}
