package Stack;

public class Stack<E> implements AbstractStack<E> {
    private static final int DEFAULT_CAPACITY = 20;
    private int capacity;
    private int size;

    private E[] data;

    public Stack() {
        this(DEFAULT_CAPACITY);
    }

    public Stack(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        init(capacity);
    }

    @SuppressWarnings("unchecked")
    private void init(int capacity) {
        data = (E[]) new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        E[] newData = (E[]) new Object[capacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    /**
     * Returns first element of stack
     *
     * @return firs element of stack
     * @throws UnsupportedOperationException if stack is empty
     */
    @Override
    public E peek() {
        if (size == 0) {
            throw new UnsupportedOperationException("stack is empty");
        }
        return data[size - 1];
    }

    /**
     * Removes and returns first element of stack
     *
     * @return firs element of stack
     * @throws UnsupportedOperationException if stack is empty
     */
    @Override
    public E poll() {
        if (size == 0) {
            throw new UnsupportedOperationException("Stack is empty");
        }
        E toRemove = data[--size];

        data[size] = null;
        if (size <= (capacity >> 1)) {
            int j = (capacity >> 1) + (capacity >> 2);
            capacity = j;
            resize(j);
        }
        return toRemove;
    }

    /**
     * Push element to the top of stack
     *
     * @param element element to append
     */
    @Override
    public void push(E element) {
        data[size] = element;
        if (++size >= capacity) {
            capacity += (capacity >> 1);
            resize(capacity);
        }
    }

    /**
     * Method provides to check if stack is empty
     *
     * @return true if stack empty otherwise false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns index of specified element in the stack
     *
     * @param element element to search
     * @return position of the specified element in the stack
     * if element in the stack otherwise -1
     */
    @Override
    public int indexOf(E element) {
        for (int i = size - 1; i >= 0; i--) {
            if (data[i].equals(element)) {
                return size - 1 - i;
            }
        }
        return -1;
    }

    /**
     * Clear current stack
     */
    @Override
    public void clear() {
        size = 0;
        init(capacity);
    }

    /**
     * @return size of the stack
     */
    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder res = new StringBuilder("[");
        for (int i = size - 1; i > 0; i--) {
            res.append(data[i]).append(", ");
        }
        return res.toString() + data[0] + "]";
    }
}
