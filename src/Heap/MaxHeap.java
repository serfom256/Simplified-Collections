package Heap;

/**
 * This is simple MAX binary Heap
 *
 * @param <T>
 */
public class MaxHeap<T extends Comparable<? super T>> {
    Data<T>[] heap;
    int size;
    int capacity;
    int FIRST = 0;
    final static double LOAD_FACTOR = 0.8;
    final static int DEFAULT_CAPACITY = 20;

    private static class Data<T> {
        private T value;

        public Data(T value) {
            this.value = value;
        }

        public void update(T el) {
            this.value = el;
        }
    }

    public MaxHeap() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Initialize heap with capacity
     *
     * @param capacity size of the heap
     */
    @SuppressWarnings("unchecked")
    private void initHeap(int capacity) {
        heap = (Data<T>[]) new Data[capacity];
    }

    public MaxHeap(int capacity) {
        if (capacity < 10) {
            throw new IllegalArgumentException("Capacity if heap must be more then 10");
        }
        this.capacity = capacity;
        initHeap(this.capacity);
        this.size = 0;
    }

    @SafeVarargs
    public final void addAll(T... data) {
        for (T el : data) {
            push(el);
        }
    }


    /**
     * Add element to the heap
     *
     * @param element element to add into the heap
     * @throws IllegalArgumentException if element is null
     */
    public void push(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must be not null");
        }

        if (isOverFlow()) {
            grow();
        }
        heap[size] = new Data<>(element);
        raiseUp(size++);
    }

    /**
     * If heap is fill more that 75%(LOAD_FACTOR) => then heap size increase by 50%
     */
    @SuppressWarnings("unchecked")
    private void grow() {
        capacity += capacity >> 1;
        Data<T>[] newHeap = (Data<T>[]) new Data[capacity];
        if (size >= 0) {
            System.arraycopy(heap, 0, newHeap, 0, size);
        }
        heap = newHeap;
    }

    /**
     * Return and remove first element of the heap
     *
     * @return first(max) element of the heap
     * @throws ArrayIndexOutOfBoundsException if heap is empty
     */
    public T poll() {
        if (size == 0) {
            throw new ArrayIndexOutOfBoundsException("Heap is empty");
        }
        T result = heap[FIRST].value;
        heap[FIRST] = heap[--size];
        sinkDown(FIRST);
        heap[size] = null;
        return result;
    }

    /**
     * Returns first element of the heap
     *
     * @return first element of thee heap if heap isn't empty else null
     */
    public T peek() {
        return size > 0 ? heap[FIRST].value : null;
    }

    /**
     * Sink element by the position in a heap
     *
     * @param pos position of element to sink
     */
    private void sinkDown(int pos) {
        if (pos < size) {
            Data<T> left = heap[leftChild(pos)], right = heap[rightChild(pos)];
            if (right != null && left != null) {
                if (heap[pos].value.compareTo(left.value) < 0 || heap[pos].value.compareTo(right.value) < 0) {
                    if (left.value.compareTo(right.value) > 0) {
                        swap(pos, leftChild(pos));
                        sinkDown(leftChild(pos));
                    } else {
                        swap(pos, rightChild(pos));
                        sinkDown(rightChild(pos));
                    }
                }
            }
        }
    }

    /**
     * Raise element by the position in a heap
     *
     * @param pos position of element to raise
     */
    private void raiseUp(int pos) {
        int parent = parent(pos);
        if (heap[parent] == null || heap[pos] == null) {
            return;
        }
        Data<T> current = heap[pos];
        while (pos > 0 && heap[parent].value.compareTo(current.value) < 0) {
            heap[pos] = heap[parent];
            pos = parent;
            parent = parent(pos);
            if (heap[parent] == null) break;
        }
        heap[pos] = current;
    }

    /**
     * Check if heap is overflow (if current size  > 75% of the heap)
     *
     * @return boolean if overflow else false
     */
    private boolean isOverFlow() {
        return capacity * LOAD_FACTOR <= size;
    }

    private int parent(int pos) {
        return (pos - 1) / 2;
    }

    private int leftChild(int pos) {
        return (2 * pos) + 1;
    }

    private int rightChild(int pos) {
        return (2 * pos) + 2;
    }

    private void swap(int pos1, int pos2) {
        Data<T> tmp = heap[pos1];
        heap[pos1] = heap[pos2];
        heap[pos2] = tmp;
    }

    /**
     * Replaces one item that equal [oldElement] in heap (if item present in heap)
     *
     * @param newElement the element which replaces old element if old element in heap
     * @param oldElement replacement element
     * @throws IllegalArgumentException if (newElement or oldElement) is null
     */
    public void update(T oldElement, T newElement) {
        if (newElement == null || oldElement == null) {
            throw new IllegalArgumentException("Elements must be not null");
        }
        for (int i = 0; i < size; i++) {
            if (heap[i].value.equals(oldElement)) {
                update(newElement, i);
                return;
            }
        }
    }

    /**
     * Replaces all items that equals [oldElement] item in heap (if item present in heap)
     *
     * @param newElement the element which replaces old element if old element in heap
     * @param oldElement replacement element
     * @throws IllegalArgumentException if (newElement or oldElement) is null
     */
    public void updateEquals(T oldElement, T newElement) {
        if (newElement == null || oldElement == null) {
            throw new IllegalArgumentException("Elements must be not null");
        }
        for (int i = 0; i < size; i++) {
            if (heap[i].value.equals(oldElement)) {
                update(newElement, i);
            }
        }
    }

    /**
     * Replaces one element that by position in heap (if element present in heap)
     *
     * @param element the element to replace
     * @param pos     position to replace
     */
    private void update(T element, int pos) {
        T oldVal = heap[pos].value;
        heap[pos].update(element);
        if (oldVal.compareTo(element) < 0) {
            sinkDown(pos);
        } else {
            raiseUp(pos);
        }
    }

    /**
     * Removes element in the heap
     *
     * @param element element to remove
     * @throws IllegalArgumentException if element is null
     */
    public void remove(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must be not null");
        }
        if (size == 0) {
            return;
        }
        for (int i = 0; i < size; i++) {
            if (heap[i].value.equals(element)) {
                heap[i] = heap[--size];
                heap[size] = null;
                sinkDown(i);
                return;
            }
        }
    }

    /**
     * Returns the last position of this element on the heap
     *
     * @param element element in heap
     * @return position of element if element in the heap else -1
     * @throws IllegalArgumentException if element is null
     */
    public boolean contains(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must be not null");
        }
        return contains(element, 0);
    }

    private boolean contains(T element, int pos) {
        if (pos >= size) return false;
        if (element.equals(heap[pos].value)) return true;
        return contains(element, leftChild(pos)) || contains(element, rightChild(pos));
    }

    /**
     * @return length of the heap
     */
    public int getLength() {
        return size;
    }

    /**
     * Clear heap
     */
    public void clear() {
        initHeap(capacity);
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder res = new StringBuilder("[");
        for (int i = 0; i < size - 1; i++) {
            res.append(heap[i].value).append(", ");
        }
        return res.toString() + (heap[size - 1] == null ? "" : heap[size - 1].value) + "]";
    }
}
