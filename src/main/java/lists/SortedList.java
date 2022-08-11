package lists;

public interface SortedList<E> extends SimpleList<E> {

    E getMin();

    E getMax();

    boolean contains(E element);
}
