package HashSet;

import Lists.AbstractList;
import Lists.impl.ArrayList;

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
     * @throws IllegalArgumentException if (OldElement or newElement) is null
     */
    @Override
    public void update(E OldElement, E newElement) {
        if (OldElement == null || newElement == null) {
            throw new IllegalArgumentException("(OldElement or newElement) must be not null");
        }
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
     * @throws IllegalArgumentException if element is null
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must be not null");
        }
        size++;
        if (root == null) {
            root = new TNode<>(element);
            return;
        }
        TNode<E> curr = root;
        while (curr.left != null || curr.right != null) {

            if (curr.element.compareTo(element) > 0 && curr.left != null) curr = curr.left;
            else if (curr.element.compareTo(element) < 0 && curr.right != null) curr = curr.right;
            else break;
        }
        if (curr.element.equals(element)) {
            size--;
            return;
        }
        TNode<E> newNode = new TNode<>(element);
        if (curr.element.compareTo(element) < 0) {
            newNode.right = curr.right;
            curr.right = newNode;
        } else {
            newNode.left = curr.left;
            curr.left = newNode;
        }
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
        AbstractList<TNode<E>> list = new ArrayList<>();
        getAllNodes(list, root);
        root = balance(list, 0, list.getSize() - 1);
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
            lst.add(root);
            getAllNodes(lst, root.right);
        }
    }

    /**
     * Removes specified element from set if set contains element
     *
     * @param element element to remove
     * @return removed element if element present in the set otherwise null
     * @throws IllegalArgumentException if element is null
     */
    @Override
    public E remove(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must be not null");
        }
        if (contains(element)) {
            root = deleteNode(root, element);
            size--;
            if (++balanceCount > maxBalanceThreshold) {
                balanceCount = 0;
                reBalance();
            }
            return element;
        }
        return null;
    }

    /**
     * Removes element from Binary Tree
     *
     * @param root    root of Binary Tree
     * @param element element to remove
     * @return root of Binary Tree with removed element
     */
    private TNode<E> deleteNode(TNode<E> root, E element) {
        if (root == null) return null;

        if (element.compareTo(root.element) > 0) { // if element to remove greater then current element => right
            root.right = deleteNode(root.right, element);
        } else if (element.compareTo(root.element) < 0) { // if element to remove lesser then current element => left
            root.left = deleteNode(root.left, element);
        } else { // if element to remove found => remove element from Binary Tree
            if (root.left == null || root.right == null) { // if current root is leaf => replace node
                return root.right == null ? root.left : root.right;
            }
            root.element = getSuccessor(root); // if element isn't leaf => find successor and replace this node
            root.right = deleteNode(root.right, root.element); // remove successor node
        }
        return root;
    }

    /**
     * Returns successor of current node
     *
     * @param node node to get successor
     * @return successor of specified node
     */
    private E getSuccessor(TNode<E> node) {
        if (node.right != null) {
            node = node.right;
            while (node.left != null) {
                node = node.left;
            }
        } else {
            node = node.left;
            while (node.right != null) {
                node = node.right;
            }
        }
        return node.element;
    }

    /**
     * Remove specified element from set if set contains element
     *
     * @param element element to remove
     * @return removed element if element present in the set otherwise null
     */
    @Override
    public boolean contains(E element) {
        if (root == null || element == null) {
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
     * Append all values of the root to the list
     *
     * @param lst  list to append values
     * @param root root of Binary Tree
     */
    private void collectAllNodes(AbstractList<E> lst, TNode<E> root) {
        if (root != null) {
            collectAllNodes(lst, root.left);
            lst.add(root.element);
            collectAllNodes(lst, root.right);
        }
    }

    @Override
    public Object[] toObjectArray() {
      AbstractList<E> result = new ArrayList<>(size+1);
        collectAllNodes(result, root);
      return result.toObjectArray();
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
        return new SelfIterator();
    }

private class SelfIterator implements Iterator<E> {
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
    private StringBuilder traversePrint(TNode<E> root, StringBuilder result) {
        if (root != null) {
            result = traversePrint(root.left, result);
            result.append(root.element).append(", ");
            result = traversePrint(root.right, result);
        }
        return result;
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder res = traversePrint(root, new StringBuilder());
        return "{" + res.substring(0, res.length() - 2) + "}";
    }
}
