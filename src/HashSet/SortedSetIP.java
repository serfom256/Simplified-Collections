package HashSet;

import Additional.DynamicString.AbstractDynamicString;
import Additional.DynamicString.DynamicLinkedString;
import Additional.Nodes.TreeNode;

public class SortedSetIP<E extends Comparable<E>> implements AbstractSortedSet<E> {

    public enum Color {RED, BLACK}

    private static final Color red = Color.RED;
    private static final Color black = Color.BLACK;

    private static class TNode<E> implements TreeNode<E> {
        E element;
        TNode<E> left, right, parent;

        Color color;

        public TNode(E value) {
            this(value, red);
        }

        public TNode(E element, Color color) {
            this.element = element;
            this.color = color;
            this.left = this.right = this.parent = null;
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

    private TNode<E> root;
    private int size;

    public SortedSetIP() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public void update(E OldElement, E newElement) {

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
            if (node.isLeftChild()) {
                if (uncle != null && uncle.color == red) {
                    node = repaint(node, uncle);
                } else {
                    if (node.isRightChild()) {
                        doLeftRotate(node);
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
    public E getMax() {
        return null;
    }

    @Override
    public E getMin() {
        return null;
    }

    @Override
    public E get(int pos) {
        return null;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
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
    public Object[] toObjectArray() {
        return new Object[0];
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

