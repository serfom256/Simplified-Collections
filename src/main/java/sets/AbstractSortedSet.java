package sets;

public interface AbstractSortedSet<E> extends AbstractSet<E>{

    E getMax();

    E getMin();

    E get(int pos);
}
