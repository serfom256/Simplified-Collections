package HashSet;

import java.util.Iterator;

public class SortedSet<E extends Comparable<E>> implements Iterable<E>, AbstractSet {
    private static int size;

    public SortedSet() {
        this.size = 0;
    }

    @SafeVarargs
    public final void addAll(E... data) {
        for (E obj : data) {
            add(obj);
        }
    }

    @Override
    public void update(Object OldElement, Object newElement) {

    }

    @Override
    public void add(Object element) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Object remove(Object element) {
        return null;
    }

    @Override
    public boolean contains(Object element) {
        return false;
    }

    @Override
    public int getSize() {
        return 0;
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
