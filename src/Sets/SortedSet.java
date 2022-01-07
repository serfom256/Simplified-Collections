package Sets;

import Additional.DynamicString.AbstractDynamicString;
import Additional.DynamicString.DynamicLinkedString;
import Additional.Nodes.TreeNode;
import Lists.AbstractList;
import Lists.impl.ArrayList;
import Stack.AbstractStack;
import Stack.LinkedStack;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class SortedSet<E extends Comparable<E>> implements AbstractSortedSet<E> {

    private static class TNode<V> implements TreeNode<V> {
        private V element;
        private TNode<V> left, right;

        public TNode(V element) {
            this.element = element;
            this.left = this.right = null;
        }

        @Override
        public TNode<V> getLeft() {
            return left;
        }

        @Override
        public TNode<V> getRight() {
            return right;
        }

        @Override
        public V getElement() {
            return element;
        }
    }

    private static class Wrapper<E> {
        E value;

        public Wrapper() {
            this.value = null;
        }
    }

    private TNode<E> root;
    private int size;
    private int balanceCount;
    private final int maxBalanceThreshold;
    private static final int DEFAULT_BALANCE_THRESHOLD = 1024;

    public SortedSet() {
        this(DEFAULT_BALANCE_THRESHOLD);
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
    @Override
    public E get(int pos) {
        if (pos < 0 || pos >= size) throw new ArrayIndexOutOfBoundsException("if position out of Set bounds");
        Wrapper<E> obj = new Wrapper<>();
        searchValue(root, 0, pos, obj);
        return obj.value;
    }

    /**
     * Provides to get element from the tree by the position
     *
     * @param currPos     initial position of first node should be 0
     * @param pos         position for searched element
     * @param searchedVal value to search in the wrapper
     */
    private int searchValue(TNode<E> root, int currPos, int pos, Wrapper<E> searchedVal) {
        if (root == null || searchedVal.value != null) return currPos;
        currPos = searchValue(root.left, currPos, pos, searchedVal);
        if (currPos == pos && searchedVal.value == null) {
            searchedVal.value = root.element;
            return currPos;
        }
        currPos = searchValue(root.right, currPos + 1, pos, searchedVal);
        return currPos;
    }


    /**
     * Replace element in the Set if element present in the Set
     *
     * @param oldElement element to replace
     * @param newElement new element to replace old element
     * @throws IllegalArgumentException if (oldElement or newElement) is null
     */
    @Override
    public void update(E oldElement, E newElement) {
        if (oldElement == null || newElement == null) {
            throw new IllegalArgumentException("(oldElement or newElement) must be not null");
        }
        TNode<E> node = getNode(oldElement);
        if (node == null || !contains(oldElement)) return;
        root = insert(deleteNode(root, oldElement), newElement);
        size--;
    }

    /**
     * Returns min element of Set
     *
     * @return min element if Set size greater then 0 otherwise null
     */
    @Override
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
    @Override
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
            throw new IllegalArgumentException("Inserted element must be not null");
        }
        if (root == null) {
            root = new TNode<>(element);
            size++;
            return;
        }
        TNode<E> curr = root;
        while (curr.left != null || curr.right != null) {
            if (curr.element.compareTo(element) > 0 && curr.left != null) curr = curr.left;
            else if (curr.element.compareTo(element) < 0 && curr.right != null) curr = curr.right;
            else break;
        }
        if (curr.element.equals(element)) return;
        size++;
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
        AbstractList<TNode<E>> list = new ArrayList<>(size + 1);
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
    public boolean remove(E element) {
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
            return true;
        }
        return false;
    }

    /**
     * Returns node by value if node founded otherwise null
     */
    private TNode<E> getNode(E value) {
        TNode<E> curr = root;
        while (curr != null && !curr.element.equals(value)) {
            if (value.compareTo(curr.element) < 0) curr = curr.left;
            else curr = curr.right;
        }
        return curr;
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
     * Returns successor of the current node
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
        if (root == null || element == null) return false;
        return getNode(element) != null;
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
        AbstractList<E> result = new ArrayList<>(size + 1);
        collectAllNodes(result, root);
        return result.toObjectArray();
    }

    /**
     * Returns set of elements from the specified set which isn't presents in the specified set
     * {1, 2, 3}.left({3, 4, 5, 6}) => {1, 2}
     */
    @Override
    public AbstractSet<E> left(AbstractSet<E> set) {
        SortedSet<E> left = new SortedSet<>();
        for (E element : set) {
            if (!this.contains(element)) left.add(element);
        }
        return left;
    }

    /**
     * Returns set of elements from the specified set which isn't presents in this set
     * {1, 2, 3}.right({3, 4, 5, 6}) => {4, 5, 6}
     */
    @Override
    public AbstractSet<E> right(AbstractSet<E> set) {
        SortedSet<E> right = new SortedSet<>();
        for (E element : this) {
            if (!set.contains(element)) right.add(element);
        }
        return right;
    }

    /**
     * Returns set with crossing elements from this set and specified set
     * {1, 2, 3, 4}.between({1, 3, 4, 5, 6}) => {1, 3, 4}
     */
    @Override
    public AbstractSet<E> between(AbstractSet<E> set) {
        SortedSet<E> mid = new SortedSet<>();
        for (E element : set) {
            if (this.contains(element)) mid.add(element);
        }
        return mid;
    }

    /**
     * Returns union of this set and specified set
     * {1, 2, 3, 4}.union({4, 5, 6}) => {1, 2, 3, 4, 5, 6}
     *
     * @return union of this and specified set
     */
    @Override
    public AbstractSet<E> union(AbstractSet<E> set) {
        SortedSet<E> union = new SortedSet<>();
        Iterator<E> foreignIterator = set.iterator();
        Iterator<E> selfIterator = this.iterator();
        while (foreignIterator.hasNext() && selfIterator.hasNext()) {
            union.add(foreignIterator.next());
            union.add(selfIterator.next());
        }
        while (foreignIterator.hasNext()) {
            union.add(foreignIterator.next());
        }
        while (selfIterator.hasNext()) {
            union.add(selfIterator.next());
        }
        return union;
    }

    /**
     * Clear current Set
     */
    @Override
    public void clear() {
        this.size = this.balanceCount = 0;
        this.root = null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new SelfIterator();
    }

    private class SelfIterator implements Iterator<E> {

        private final AbstractStack<TNode<E>> stack;

        private TNode<E> next;

        public SelfIterator() {
            this.stack = new LinkedStack<>();
            next = root;
        }

        @Override
        public boolean hasNext() {
            return next != null || !stack.isEmpty();
        }

        @Override
        public E next() {
            if (next == null && stack.isEmpty()) throw new NoSuchElementException();
            TNode<E> current = next;
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            current = stack.poll();
            next = current.right;
            return current.element;
        }
    }

    /**
     * Utility method for which provides to collect all elements of Set to string
     *
     * @param root root of the set
     */
    private void traversePrint(TNode<E> root, AbstractDynamicString result) {
        if (root != null) {
            traversePrint(root.left, result);
            result.add(root.element).add(", ");
            traversePrint(root.right, result);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        AbstractDynamicString res = new DynamicLinkedString();
        traversePrint(root, res);
        return res.subSequence(0, res.getSize() - 2).addFirst('{').add("}").toString();
    }
}
