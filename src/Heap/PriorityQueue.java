package Heap;

/**
 * This is simple PriorityQueue
 *
 * @param <T>
 */
public class PriorityQueue<T extends Comparable<? super T>> {
    Data<T>[] queue;
    int size;
    int capacity;
    int FIRST = 0;
    final static double LOAD_FACTOR = 0.8;
    final static int DEFAULT_CAPACITY = 20;

    private static class Data<T> {
        private T value;
        private int priority;

        public Data(T value, int priority) {
            this.priority = priority;
            this.value = value;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public void update(T el) {
            this.value = el;
        }
    }

    public PriorityQueue() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Initialize priorityQueue with capacity
     *
     * @param capacity size of the queue
     */
    @SuppressWarnings("unchecked")
    private void initQueue(int capacity) {
        queue = (Data<T>[]) new Data[capacity];
    }

    public PriorityQueue(int capacity) {
        if (capacity < 10) {
            throw new IllegalArgumentException("Capacity if priorityQueue must be more then 10");
        }
        this.capacity = capacity;
        initQueue(this.capacity);
        this.size = 0;
    }

    /**
     * Add element to the priorityQueue
     *
     * @param element element to add into the priorityQueue
     * @throws IllegalArgumentException if element is null
     */
    public void push(T element, int priority) {
        if (element == null) {
            throw new IllegalArgumentException("Element must be not null");
        }

        if (isOverFlow()) {
            grow();
        }
        queue[size] = new Data<>(element, priority);
        raiseUp(size++);
    }

    /**
     * If priorityQueue is fill more that 75%(LOAD_FACTOR) => then priorityQueue size increase by 50%
     */
    @SuppressWarnings("unchecked")
    private void grow() {
        capacity += capacity >> 1;
        Data<T>[] newQueue = (Data<T>[]) new Data[capacity];
        if (size >= 0) {
            System.arraycopy(queue, 0, newQueue, 0, size);
        }
        queue = newQueue;
    }

    /**
     * Return and remove first element of the priorityQueue
     *
     * @return first(max) element of the priorityQueue
     * @throws ArrayIndexOutOfBoundsException if priorityQueue is empty
     */
    public T poll() {
        if (size == 0) {
            throw new ArrayIndexOutOfBoundsException("Queue is empty");
        }
        T result = queue[FIRST].value;
        queue[FIRST] = queue[--size];
        queue[size] = null;
        sinkDown(FIRST);

        return result;
    }

    /**
     * Returns first element of the priorityQueue
     *
     * @return first element of thee priorityQueue if priorityQueue isn't empty else null
     */
    public T peek() {
        return size > 0 ? queue[FIRST].value : null;
    }

    /**
     * Sink element by the position in a queue
     *
     * @param pos position of element to sink
     */
    private void sinkDown(int pos) {
        Data<T> current = queue[pos];
        while (pos < size / 2) {
            int left = leftChild(pos), right = rightChild(pos);
            int maxChild;
            if (right < size && queue[left].priority < queue[right].priority) {
                maxChild = right;
            } else {
                maxChild = left;
            }
            if (current.priority > queue[maxChild].priority) {
                break;
            }
            queue[pos] = queue[maxChild];
            pos = maxChild;
        }
        queue[pos] = current;
    }

    /**
     * Raise element by the position in a queue
     *
     * @param pos position of element to raise
     */
    private void raiseUp(int pos) {
        int parent = parent(pos);
        if (queue[parent] == null || queue[pos] == null) {
            return;
        }
        Data<T> current = queue[pos];
        while (pos > 0 && queue[parent].priority < current.priority) {
            queue[pos] = queue[parent];
            pos = parent;
            parent = parent(pos);
            if (queue[parent] == null) break;
        }
        queue[pos] = current;
    }

    /**
     * Check if priorityQueue is overflow (if current size  > 75% of the queue)
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

    /**
     * Set priority to element in the priorityQueue
     *
     * @param element  the element for which you want to change priority in the queue
     * @param priority priority of element
     * @throws IllegalArgumentException if (element) is null
     */
    public void setPriority(T element, int priority) {
        if (element == null) {
            throw new IllegalArgumentException("Elements must be not null");
        }
        for (int i = 0; i < size; i++) {
            if (queue[i].value.equals(element)) {
                int lastPriority = queue[i].priority;
                queue[i].setPriority(priority);
                if (priority > lastPriority) {
                    raiseUp(i);
                } else {
                    sinkDown(i);
                }
                return;
            }
        }
    }

    /**
     * Replaces one item that equal [oldElement] in priorityQueue (if item present in queue)
     *
     * @param newElement the element which replaces old element if old element in priorityQueue
     * @param oldElement replacement element
     * @throws IllegalArgumentException if (newElement or oldElement) is null
     */
    public void update(T oldElement, T newElement) {
        if (newElement == null || oldElement == null) {
            throw new IllegalArgumentException("Elements must be not null");
        }
        for (int i = 0; i < size; i++) {
            if (queue[i].value.equals(oldElement)) {
                update(newElement, i);
                return;
            }
        }
    }


    /**
     * Replaces all items that equals [oldElement] item in priorityQueue (if item present in queue)
     *
     * @param newElement the element which replaces old element if old element in priorityQueue
     * @param oldElement replacement element
     * @throws IllegalArgumentException if (newElement or oldElement) is null
     */
    public void updateEquals(T oldElement, T newElement) {
        if (newElement == null || oldElement == null) {
            throw new IllegalArgumentException("Elements must be not null");
        }
        for (int i = 0; i < size; i++) {
            if (queue[i].value.equals(oldElement)) {
                update(newElement, i);
            }
        }
    }

    /**
     * Replaces one element that by position in priorityQueue (if element present in queue)
     *
     * @param element the element to replace
     * @param pos     position to replace
     */
    private void update(T element, int pos) {
        queue[pos].update(element);
    }

    /**
     * Removes element in the priorityQueue
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
            if (queue[i].value.equals(element)) {
                queue[i] = queue[--size];
                queue[size] = null;
                sinkDown(i);
                return;
            }
        }
    }

    /**
     * Returns the last position of this element on the priorityQueue
     *
     * @param element element in priorityQueue
     * @return position of element if element in the Queue else -1
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
        if (element.equals(queue[pos].value)) return true;
        return contains(element, leftChild(pos)) || contains(element, rightChild(pos));
    }

    /**
     * Returns count of exactly elements in the queue
     *
     * @param element element in priorityQueue
     * @return count of same elements which equals this (element)
     */
    public int countOf(T element) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (queue[i].value.equals(element)) {
                count++;
            }
        }
        return count;
    }

    /**
     * @return length of the queue
     */
    public int getSize() {
        return size;
    }

    /**
     * Clear queue
     */
    public void clear() {
        initQueue(capacity);
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder res = new StringBuilder("[");
        for (int i = 0; i < size - 1; i++) {
            res.append("{").append(queue[i].value).append(":").append(queue[i].priority).append("}, ");
        }
        if (queue[size - 1] == null) {
            return res.toString() + "]";
        }
        return res.toString() + "{" + queue[size - 1].value + ":" + queue[size - 1].priority + "}" + "]";
    }
}
