package HashSet;

import Lists.AbstractList;
import Lists.LinkedList;

import java.util.Iterator;


public class SortedSet<E extends Comparable<E>> implements Iterable<E>, AbstractSet<E> {
    private static class TNode<V> {
        V element;
        int pos;
        TNode<V> left, right;

        public TNode(V element) {
            this.element = element;
            this.left = this.right = null;
        }
    }

    private TNode<E> root;
    private int size;
    private int balanceCount;
    private final int maxBalanceThreshold;
    private static final int defaultBalanceThreshold = 3;

    public SortedSet() {
        this(defaultBalanceThreshold);
    }

    public SortedSet(int threshold) {
        this.root = null;
        this.size = 0;
        this.balanceCount = 0;
        this.maxBalanceThreshold = threshold;
    }

    @SafeVarargs
    public final void addAll(E... data) {
        for (E obj : data) {
            add(obj);
        }
    }


    @Override
    public void update(E OldElement, E newElement) {
        
    }

    @Override
    public void add(E element) {

    }

    @Override
    public void clear() {
        this.size = 0;
        this.root = null;
    }

    @Override
    public E remove(E element) {
        return null;
    }

    @Override
    public boolean contains(E element) {
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    private class SelfIterator<T> implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }
    }

    @Override
    public String toString() {
        return "SortedSet{}";
    }
}
