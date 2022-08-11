package stack;

public interface Stack<E> {
    E peek();

    E poll();

    void push(E element);

    void clear();

    boolean isEmpty();

    int indexOf(E element);

    int getSize();
}
