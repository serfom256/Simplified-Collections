package Stack;

import Lists.AbstractLinkedList;
import Lists.LinkedList;

public class LinkedStack<E> implements AbstractStack<E> {
    private final AbstractLinkedList<E> linkedList;

    public LinkedStack() {
        linkedList = new LinkedList<>();
    }

    /**
     * Returns first element of stack
     *
     * @return firs element of stack
     * @throws UnsupportedOperationException if stack is empty
     */
    @Override
    public E peek() {
        if (linkedList.getLength() == 0) {
            throw new UnsupportedOperationException("stack is empty");
        }
        return linkedList.peekFirst();
    }

    /**
     * Removes and returns first element of stack
     *
     * @return firs element of stack
     * @throws UnsupportedOperationException if stack is empty
     */
    @Override
    public E poll() {
        if (linkedList.getLength() == 0) {
            throw new UnsupportedOperationException("stack is empty");
        }
        return linkedList.popFirst();
    }

    /**
     * Push element to the top of stack
     *
     * @param element element to append
     */
    @Override
    public void push(E element) {
        linkedList.pushFirst(element);
    }

    /**
     * Clear current stack
     */
    @Override
    public void clear() {
        linkedList.clear();
    }

    /**
     * Method provides to check if stack is empty
     *
     * @return true if stack empty otherwise false
     */
    @Override
    public boolean isEmpty() {
        return linkedList.getLength() == 0;
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
        return linkedList.indexOf(element);
    }

    /**
     * @return size of the stack
     */
    @Override
    public int getSize() {
        return linkedList.getLength();
    }

    @Override
    public String toString() {
        int stackLen = linkedList.getLength();
        if (stackLen == 0) return "[]";
        StringBuilder res = new StringBuilder("[");
        for (int i = 0; i < linkedList.getLength() - 1; i++) {
            res.append(linkedList.get(i)).append(", ");
        }
        return res.toString() + linkedList.get(stackLen - 1) + "]";
    }
}
