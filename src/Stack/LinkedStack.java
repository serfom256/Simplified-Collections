package Stack;

import Additional.DynamicString.AbstractDynamicString;
import Additional.DynamicString.DynamicLinkedString;
import Lists.AbstractLinkedList;
import Lists.impl.LinkedList;

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
        if (linkedList.getSize() == 0) {
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
        if (linkedList.getSize() == 0) {
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
     * Provides to check if stack is empty
     *
     * @return true if stack empty otherwise false
     */
    @Override
    public boolean isEmpty() {
        return linkedList.getSize() == 0;
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
        return linkedList.getSize();
    }

    @Override
    public String toString() {
        int stackLength = linkedList.getSize();
        if (stackLength == 0) return "[]";
        AbstractDynamicString res = new DynamicLinkedString("[");
        for (int i = 0; i < stackLength - 1; i++) {
            res.add(linkedList.get(i)).add(", ");
        }
        return res.add(linkedList.get(stackLength - 1)) + "]";
    }
}
