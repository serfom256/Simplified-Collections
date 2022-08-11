package sets;

public interface SortedSet<E> extends Set<E> {

    E getMax();

    E getMin();

    E get(int pos);
}
