package lists;

public interface AbstractSortedList<E> extends SimpleList<E> {

    E getMin();

    E getMax();

    boolean contains(E element);
}
