package sets;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.NullableArgumentException;
import additional.nodes.TreeNode;
import lists.List;
import lists.impl.ArrayList;
import stack.Stack;
import stack.LinkedStack;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RBTSet<E extends Comparable<E>> implements SortedSet<E> {

    private enum Color {RED, BLACK}

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

        TNode<E> getSibling() {
            if (parent == null) return null;
            return isLeftChild() ? parent.right : parent.left;
        }

        boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        public int countOfRedC() {
            int cnt = 0;
            if (left != null && left.color == red) cnt++;
            if (right != null && right.color == red) cnt++;
            return cnt;
        }

        public int countOfBlackC() {
            int cnt = 0;
            if (left != null && left.color == black) cnt++;
            if (right != null && right.color == black) cnt++;
            return cnt;
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

    public RBTSet() {
        this.root = null;
        this.size = 0;
    }

    /**
     * @throws NullableArgumentException if one of specified  arguments is null
     */
    @SafeVarargs
    public final void addAll(E... data) {
        for (E el : data) add(el);
    }

    /**
     * @throws NullableArgumentException if one of specified  arguments is null
     */
    @Override
    public <T extends Iterable<E>> void addFrom(T iterable) {
        for (E e : iterable) add(e);
    }

    /**
     * Add element to the Set
     *
     * @param element element to append
     * @throws NullableArgumentException if element is null
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new NullableArgumentException();
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
        curr = findNodeByValue(curr, value);
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
     * Unlink node to parentNode node
     */
    private void unlinkNodes(TNode<E> parentNode, TNode<E> childNode) {
        TNode<E> successor = null;
        if (parentNode.left != null && parentNode.left.element.equals(childNode.element)) {
            if (childNode.right != null) {
                childNode.right.parent = parentNode;
                successor = childNode.right;
            } else if (childNode.left != null) {
                childNode.left.parent = parentNode;
                successor = childNode.left;
            }
            parentNode.left = successor;

        } else if (parentNode.right != null && parentNode.right.element.equals(childNode.element)) {
            if (childNode.left != null) {
                childNode.left.parent = parentNode;
                successor = childNode.left;
            } else if (childNode.right != null) {
                childNode.right.parent = parentNode;
                successor = childNode.right;
            }
            parentNode.right = successor;
        }
        childNode.parent = null;
    }

    /**
     * Returns parent Node for specified value
     */
    private TNode<E> findNodeByValue(TNode<E> node, E value) {
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
        while (node != root && node.parent.color == red) {
            TNode<E> sibling = node.parent.getSibling();
            if (node.parent.isLeftChild()) {
                if (sibling != null && sibling.color == red) {
                    node = repaint(node, sibling);
                } else {
                    if (node.isRightChild()) {
                        doLeftRotate(node);
                        node = node.left;
                    }
                    node.parent.color = black;
                    node.getGrandParent().color = red;
                    doRightRotate(node.parent);
                }
            } else {
                if (sibling != null && sibling.color == red) {
                    node = repaint(node, sibling);
                } else {
                    if (node.isLeftChild()) {
                        doRightRotate(node);
                        node = node.right;
                    }
                    node.parent.color = black;
                    node.getGrandParent().color = red;
                    doLeftRotate(node.parent);
                }
            }
        }
        root.color = black;
    }

    /**
     * Repaints nodes which linked to the specified node
     */
    private TNode<E> repaint(TNode<E> node, TNode<E> sibling) {
        sibling.color = black;
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

    /**
     * Search specified element by the position in the Set
     *
     * @param pos position of element
     * @return Element if element present in the Set, otherwise null
     * @throws NullableArgumentException if position out of Set bounds
     */
    @Override
    public E get(int pos) {
        if (pos < 0 || pos >= size) throw new NullableArgumentException();
        Wrapper<E> obj = new Wrapper<>();
        searchValue(root, 0, pos, obj);
        return obj.value;
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
     * Removes specified element from set if set contains element
     *
     * @param element element to remove
     * @return true if element present in the set otherwise false
     * @throws NullableArgumentException if the specified element is null
     */
    @Override
    public boolean delete(E element) {
        if (element == null) {
            throw new NullableArgumentException();
        }
        TNode<E> toRemove = findNodeByValue(root, element);
        if (!toRemove.element.equals(element)) return false;
        TNode<E> successor = getSuccessor(toRemove);
        size--;
        if (toRemove == root && size == 0) {
            return true;
        }
        unlinkNodes(successor.parent, successor);
        // case 2
        toRemove.element = successor.element;
        toRemove.color = successor.color;

        if (toRemove == successor) return true;
        successor = toRemove;
        // case 3
        while (successor != root && successor.color == black) {
            // case 3.1
            TNode<E> sibling = successor.getSibling();
            if (sibling == null) break;
            boolean isLeft = successor.isLeftChild();
            if (sibling.color == red) {
                sibling.color = black;
                if (isLeft) {
                    doLeftRotate(successor.getSibling());
                } else {
                    doRightRotate(successor.getSibling());
                }
                successor.parent.color = red;
                sibling = successor.getSibling();
                if (sibling == null) break;
            }
            //case 3.2
            if (sibling.countOfBlackC() == 2) {
                sibling.color = red;
                successor = successor.parent;
            }
            //case 3.3 && case 3.4
            else {
                // case 3.3
                if (isLeft && sibling.right != null && sibling.right.color == black) {
                    // swap colors and do rotate
                    sibling.color = red;
                    if (sibling.left != null) {
                        sibling.left.color = black;
                        doRightRotate(sibling.left);
                    }
                    sibling = successor.parent.right;
                } else if (!isLeft && sibling.left != null && sibling.left.color == black) {
                    // swap colors and do rotate
                    sibling.color = red;
                    if (sibling.right != null) {
                        sibling.right.color = black;
                        doLeftRotate(sibling.right);
                    }
                    sibling = successor.parent.left;
                }
                // case 3.4
                sibling.color = successor.parent.color;
                successor.parent.color = black;
                if (isLeft) {
                    if (sibling.right != null) sibling.right.color = black;
                    doLeftRotate(successor.getSibling());
                } else {
                    if (sibling.left != null) sibling.left.color = black;
                    doRightRotate(successor.getSibling());
                }
                break;
            }
        }
        root.color = black;
        return true;
    }

    /**
     * Returns successor of current node
     *
     * @param node node to get successor
     * @return successor of specified node
     */
    private TNode<E> getSuccessor(TNode<E> node) {
        if (node.right != null) {
            node = node.right;
            while (node.left != null) {
                node = node.left;
            }
        } else if (node.left != null) {
            node = node.left;
            while (node.right != null) {
                node = node.right;
            }
        }
        return node;
    }

    /**
     * Remove specified element from set if set contains element
     *
     * @param element element to remove
     * @return removed element if element present in the set otherwise null
     * @throws NullableArgumentException if the specified element is null
     */
    @Override
    public boolean contains(E element) {
        if (element == null) {
            throw new NullableArgumentException();
        }
        if (root == null) return false;
        TNode<E> curr = root;
        while (!curr.element.equals(element)) {
            if (element.compareTo(curr.element) < 0) curr = curr.left;
            else curr = curr.right;
            if (curr == null) return false;
        }
        return true;
    }

    private void collectAllNodes(List<E> lst, TNode<E> node) {
        if (node != null) {
            collectAllNodes(lst, node.left);
            lst.add(node.element);
            collectAllNodes(lst, node.right);
        }
    }

    @Override
    public Object[] toObjectArray() {
        List<E> result = new ArrayList<>(size + 1);
        collectAllNodes(result, root);
        return result.toObjectArray();
    }

    /**
     * Returns set of elements from the specified set which isn't presents in the specified set
     * {1, 2, 3}.left({3, 4, 5, 6}) => {1, 2}
     *
     * @throws NullableArgumentException if specified set is null
     */
    @Override
    public RBTSet<E> left(Set<E> set) {
        if (set == null) throw new NullableArgumentException();
        RBTSet<E> left = new RBTSet<>();
        for (E element : set) {
            if (!this.contains(element)) left.add(element);
        }
        return left;
    }

    /**
     * Returns set of elements from the specified set which isn't presents in this set
     * {1, 2, 3}.right({3, 4, 5, 6}) => {4, 5, 6}
     *
     * @throws NullableArgumentException if specified set is null
     */
    @Override
    public RBTSet<E> right(Set<E> set) {
        if (set == null) throw new NullableArgumentException();
        RBTSet<E> right = new RBTSet<>();
        for (E element : this) {
            if (!set.contains(element)) right.add(element);
        }
        return right;
    }

    /**
     * Returns set with crossing elements from this set and specified set
     * {1, 2, 3, 4}.between({1, 3, 4, 5, 6}) => {1, 3, 4}
     *
     * @throws NullableArgumentException if specified set is null
     */
    @Override
    public RBTSet<E> between(Set<E> set) {
        if (set == null) throw new NullableArgumentException();
        RBTSet<E> mid = new RBTSet<>();
        for (E element : set) {
            if (this.contains(element)) mid.add(element);
        }
        return mid;
    }

    /**
     * Returns union of this set and specified set
     * {1, 2, 3, 4}.union({4, 5, 6}) => {1, 2, 3, 4, 5, 6}
     *
     * @throws NullableArgumentException if specified set is null
     */
    @Override
    public RBTSet<E> union(Set<E> set) {
        if (set == null) throw new NullableArgumentException();
        RBTSet<E> union = new RBTSet<>();
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

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
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

        private final Stack<TNode<E>> stack;

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
    private void traversePrint(TNode<E> root, DynamicString result) {
        if (root != null) {
            traversePrint(root.left, result);
            result.add(root.element).add(", ");
            traversePrint(root.right, result);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        DynamicString res = new DynamicLinkedString();
        traversePrint(root, res);
        return res.subSequence(0, res.getSize() - 2).addFirst('{').add('}').toString();
    }

}

