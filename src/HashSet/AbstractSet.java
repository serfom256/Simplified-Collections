package HashSet;

public interface AbstractSet<E> {

    void update(E OldElement, E newElement);

    void add(E element);

    void clear();

    E remove(E element);

    boolean contains(E element);

    int getSize();

    Object[] toObjectArray();


    // TODO add builder methods like (union, exclusion, between, merge)


}
