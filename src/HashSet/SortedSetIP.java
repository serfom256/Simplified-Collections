package HashSet;

import Additional.DynamicString.AbstractDynamicString;
import Additional.DynamicString.DynamicLinkedString;
import Additional.Nodes.TreeNode;
import Lists.AbstractList;
import Lists.impl.ArrayList;
import Stack.AbstractStack;
import Stack.LinkedStack;

import java.util.Iterator;

public class SortedSetIP<E extends Comparable<E>> implements AbstractSortedSet<E> {

    public enum Color {RED, BLACK}

    private static final Color red = Color.RED;
    private static final Color black = Color.BLACK;

    static class TNode<E> implements TreeNode<E> {
        private TNode<E> left, right, parent;
        private Color color;
        private E element;

        public TNode(E value) {
            this(value, red);
        }

        public TNode(E element, Color color) {
            this.element = element;
            this.color = color;
            this.parent = null;
        }

        TNode<E> getGrandParent() {
            if (parent == null) return null;
            return parent.parent;
        }

        TNode<E> getParent() {
            return parent;
        }

        TNode<E> getUncle() {
            return isLeftChild() ? parent.right : parent.left;
        }

        boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        @Override
        public TNode<E> getLeft() {
            return left;
        }

        @Override
        public TNode<E> getRight() {
            return right;
        }


        @Override
        public E getElement() {
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

    public SortedSetIP() {
        this.root = null;
        this.size = 0;
    }

    @SafeVarargs
    public final void addAll(E... data) {
        for (E el : data) add(el);
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
            root = new TNode<>(element, black);
            size++;
            return;
        }
        TNode<E> node = insert(root, element);
        if (node == null) return;
        size++;
        if (node.getGrandParent() != null) rebalance(node);
    }

    /**
     * Insert new element to the Set
     *
     * @param curr  root of Binary Tree
     * @param value data to insert
     * @return Root after insertion
     */
    private TNode<E> insert(TNode<E> curr, E value) {
        curr = getParentNodeForValue(curr, value);
        if (curr.element.equals(value)) return null;
        return linkNodes(curr, new TNode<>(value));
    }

    /**
     * Links node to parentNode node
     *
     * @return linked childNode node
     */
    private TNode<E> linkNodes(TNode<E> parentNode, TNode<E> childNode) {
        E value = childNode.element;
        if (parentNode.element.compareTo(value) < 0) {
            childNode.right = parentNode.right;
            parentNode.right = childNode;
        } else {
            childNode.left = parentNode.left;
            parentNode.left = childNode;
        }
        childNode.parent = parentNode;
        return childNode;
    }

    /**
     * Returns parent Node for specified value
     */
    private TNode<E> getParentNodeForValue(TNode<E> node, E value) {
        while (node.left != null || node.right != null) {
            if (node.element.compareTo(value) > 0 && node.left != null) node = node.left;
            else if (node.element.compareTo(value) < 0 && node.right != null) node = node.right;
            else return node;
        }
        return node;
    }

    /**
     * Provides to balance tree after insertion of the new node
     * Rebalances all node from specified node to root
     *
     * @param node new inserted node
     */
    private void rebalance(TNode<E> node) {
        TNode<E> uncle = node.parent.getUncle();
        while (node != root && node.parent.color == red) {
            if (node.parent.isLeftChild()) {
                if (uncle != null && uncle.color == red) {
                    node = repaint(node, uncle);
                } else {
                    if (node.isRightChild()) {
                        doLeftRotate(node);
                        node = node.left;
                    }
                    node.getParent().color = black;
                    node.getGrandParent().color = red;
                    doRightRotate(node.getParent());
                }
            } else {
                if (uncle != null && uncle.color == red) {
                    node = repaint(node, uncle);
                } else {
                    if (node.isLeftChild()) {
                        doRightRotate(node);
                        node = node.right;
                    }
                    node.getParent().color = black;
                    node.getGrandParent().color = red;
                    doLeftRotate(node.getParent());
                }
            }
        }
        root.color = black;
    }

    /**
     * Repaints nodes which linked to the specified node
     */
    private TNode<E> repaint(TNode<E> node, TNode<E> uncle) {
        uncle.color = black;
        node.parent.color = black;
        node.parent.parent.color = red;
        return node.parent.parent;
    }

    /**
     * Rotate nodes which linked to specified node to left side
     */
    private void doLeftRotate(TNode<E> node) {
        TNode<E> parent = node.parent;
        if (node.parent == root) root = node;
        else if (parent.parent.right == parent) parent.parent.right = node;
        else parent.parent.left = node;
        node.parent = parent.parent;
        parent.right = node.left;
        if (node.left != null) node.left.parent = parent;
        node.left = parent;
        parent.parent = node;
    }

    /**
     * Rotate nodes which linked to specified node to right side
     */
    private void doRightRotate(TNode<E> node) {
        TNode<E> parent = node.parent;
        if (node.parent == root) root = node;
        else if (parent.parent.left == parent) parent.parent.left = node;
        else parent.parent.right = node;
        node.parent = parent.parent;
        parent.left = node.right;
        if (node.right != null) node.right.parent = parent;
        node.right = parent;
        parent.parent = node;
    }

    @Override
    public void update(E oldElement, E newElement) {
        throw new RuntimeException("Method not implemented yet!");
    }


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

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public E remove(E element) {
        throw new RuntimeException("Method not implemented yet!");
    }

    @Override
    public boolean contains(E element) {
        if (root == null || element == null) return false;
        TNode<E> curr = root;
        while (!curr.element.equals(element)) {
            if (element.compareTo(curr.element) < 0) curr = curr.left;
            else curr = curr.right;
            if (curr == null)  return false;
        }
        return true;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public SortedSetIP<E> left(AbstractSet<E> set) {
        SortedSetIP<E> left = new SortedSetIP<>();
        for (E element : set) {
            if (!this.contains(element)) left.add(element);
        }
        return left;
    }

    @Override
    public SortedSetIP<E> right(AbstractSet<E> set) {
        SortedSetIP<E> right = new SortedSetIP<>();
        for (E element : this) {
            if (!set.contains(element)) right.add(element);
        }
        return right;
    }

    @Override
    public SortedSetIP<E> between(AbstractSet<E> set) {
        SortedSetIP<E> mid = new SortedSetIP<>();
        for (E element : set) {
            if (this.contains(element)) mid.add(element);
        }
        return mid;
    }

    @Override
    public SortedSetIP<E> union(AbstractSet<E> set) {
        SortedSetIP<E> union = new SortedSetIP<>();
        Iterator<E> foreignIterator = set.iterator();
        Iterator<E> thisIterator = this.iterator();
        while (foreignIterator.hasNext() && iterator().hasNext()) {
            union.add(foreignIterator.next());
            union.add(thisIterator.next());
        }
        while (foreignIterator.hasNext()) {
            union.add(foreignIterator.next());
        }
        while (thisIterator.hasNext()) {
            union.add(thisIterator.next());
        }
        return union;
    }

    /**
     * Append all values of the root to the list
     *
     * @param lst  list to append values
     * @param node root of Binary Tree
     */
    private void collectAllNodes(AbstractList<E> lst, TNode<E> node) {
        if (node != null) {
            collectAllNodes(lst, node.left);
            lst.add(node.element);
            collectAllNodes(lst, node.right);
        }
    }

    @Override
    public Object[] toObjectArray() {
        AbstractList<E> result = new ArrayList<>(size + 1);
        collectAllNodes(result, root);
        return result.toObjectArray();
    }

    @Override
    public Iterator<E> iterator() {
        return new SelfIterator();
    }

    private class SelfIterator implements Iterator<E> {

        private final AbstractStack<TNode<E>> stack;

        private TNode<E> current, next;

        public SelfIterator() {
            this.stack = new LinkedStack<>();
            current = next = root;
        }

        @Override
        public boolean hasNext() {
            return next != null || !stack.isEmpty();
        }

        @Override
        public E next() {
            current = next;
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
        return res.subSequence(0, res.getSize() - 2).addFirst('{').add('}').toString();
    }

}

