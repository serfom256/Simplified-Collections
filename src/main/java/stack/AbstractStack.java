package stack;

public interface AbstractStack<E> {
    E peek();

    E poll();

    void push(E element);

    void clear();

    boolean isEmpty();

    int indexOf(E element);

    int getSize();
}
