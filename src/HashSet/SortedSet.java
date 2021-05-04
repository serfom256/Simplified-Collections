package HashSet;

import Lists.AbstractList;
import Lists.LinkedList;

import java.util.Iterator;


public class SortedSet<E extends Comparable<E>> implements Iterable<E>, AbstractSet<E> {
    private static class TNode<V> {
        V element;
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
    private static final int defaultBalanceThreshold = 10;

    // currentPos variable used for search element in the Set(Binary Tree) by position
    private int currentPos = 0;

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
        for (E el : data) {
            root = insert(root, el);
        }
        reBalance();
    }

    /**
     * Search specified element by the position in the Set
     *
     * @param pos position of element
     * @return Element if element present in the Set, otherwise null
     * @throws ArrayIndexOutOfBoundsException if position out of Set bounds
     */
    public E get(int pos) {
        if (pos < 0 || pos >= size) {
            throw new ArrayIndexOutOfBoundsException("if position out of Set bounds");
        }
        currentPos = 0;
        return searchValue(root, pos);
    }

    /**
     * Search specified element in the Binary Tree
     *
     * @param root root of the Binary Tree
     * @param pos  position of element
     * @return Element if element present in the Binary Tree, otherwise null
     */
    private E searchValue(TNode<E> root, int pos) {
        E result = null;
        if (root != null) {
            result = searchValue(root.left, pos);
            if (pos == currentPos++) {
                return root.element;
            }
            if (result == null) {
                result = searchValue(root.right, pos);
            }
        }
        return result;
    }

    /**
     * Replace element in the Set if element present in the Set
     *
     * @param OldElement element to replace
     * @param newElement new element to replace old element
     */
    @Override
    public void update(E OldElement, E newElement) {
        update(OldElement, newElement, root);
    }

    /**
     * Replace element in the Set if element present in the Set
     *
     * @param OldElement element to replace
     * @param newElement new element to replace old element
     * @param root       root of Binary Tree
     */
    public void update(E OldElement, E newElement, TNode<E> root) {
        if (root != null) {
            if (root.element.equals(OldElement)) {
                root.element = newElement;
                return;
            }
            update(OldElement, newElement, root.left);
            update(OldElement, newElement, root.right);
        }
    }

    /**
     * Returns min element of Set
     *
     * @return min element if Set size greater then 0 otherwise null
     */
    public E getMin() {
        if (root == null) {
            return null;
        }
        while (root.left != null) {
            root = root.left;
        }
        return root.element;
    }

    /**
     * Returns max element of Set
     *
     * @return max element if Set size greater then 0 otherwise null
     */
    public E getMax() {
        if (root == null) {
            return null;
        }
        while (root.right != null) {
            root = root.right;
        }
        return root.element;
    }

    /**
     * Add element to the Set
     *
     * @param element element to append
     */
    @Override
    public void add(E element) {
        root = insert(root, element);
        if (++balanceCount > maxBalanceThreshold) {
            balanceCount = 0;
            reBalance();
        }
    }

    /**
     * Insert new element to the Set
     *
     * @param root    root of Binary Tree
     * @param element data to insert
     * @return Root after insertion
     */
    private TNode<E> insert(TNode<E> root, E element) {
        if (root == null) {
            size++;
            return new TNode<>(element);
        }
        if (element.compareTo(root.element) > 0) {
            root.right = insert(root.right, element);
        } else if (root.element.compareTo(element) > 0) {
            root.left = insert(root.left, element);
        }
        return root;
    }

    /**
     * Rebalanced tree after insertion element to the Binary Tree
     */
    private void reBalance() {
        AbstractList<TNode<E>> list = new LinkedList<>();
        getAllNodes(list, root);
        root = balance(list, 0, list.getLength() - 1);
    }

    /**
     * Rebalanced Binary Tree
     *
     * @param nodes list with nodes to rebuild tree
     * @param start start position of current node in the nodes list
     * @param end   end position of current node in the nodes list
     * @return Rebalanced Binary Tree root
     */
    private TNode<E> balance(AbstractList<TNode<E>> nodes, int start, int end) {
        if (start > end) {
            return null;
        }
        int mid = (start + end) / 2;
        TNode<E> node = nodes.get(mid);
        node.left = balance(nodes, start, mid - 1);
        node.right = balance(nodes, mid + 1, end);
        return node;
    }

    /**
     * Append all nodes of the root to the list
     *
     * @param lst  list to append Binary Tree nodes
     * @param root root of Binary Tree
     */
    private void getAllNodes(AbstractList<TNode<E>> lst, TNode<E> root) {
        if (root != null) {
            getAllNodes(lst, root.left);
            lst.pushLast(root);
            getAllNodes(lst, root.right);
        }
    }

    /**
     * Remove specified element from set if set contains element
     *
     * @param element element to remove
     * @return removed element if element present in the set otherwise null
     */
    //TODO implement remove method
    @Override
    public E remove(E element) {
        return null;
    }

    /**
     * Remove specified element from set if set contains element
     *
     * @param element element to remove
     * @return removed element if element present in the set otherwise null
     */
    @Override
    public boolean contains(E element) {
        if (root == null) {
            return false;
        }
        TNode<E> curr = root;
        while (!curr.element.equals(element)) {
            if (element.compareTo(curr.element) < 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
            if (curr == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clear current Set
     */
    @Override
    public void clear() {
        this.size = this.balanceCount = 0;
        this.root = null;
    }

    /**
     * Method which provide get size of the Set
     *
     * @return size of the Set
     */
    @Override
    public int getSize() {
        return size;
    }


    @Override
    public Iterator<E> iterator() {
        return new SelfIterator<E>();
    }

    private class SelfIterator<T> implements Iterator<E> {
        int position;

        public SelfIterator() {
            position = 0;
        }

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public E next() {
            return get(position++);
        }
    }

    /**
     * Utility method for print Set
     *
     * @param root   root of the set
     * @param result result is String to append elements
     * @return String with all elements of Set in the ascending order
     */
    private StringBuilder inorderRec(TNode<E> root, StringBuilder result) {
        if (root != null) {
            result = inorderRec(root.left, result);
            result.append(root.element).append(", ");
            result = inorderRec(root.right, result);
        }
        return result;
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder res = inorderRec(root, new StringBuilder());
        return "{" + res.substring(0, res.length() - 2) + "}";
    }
}
