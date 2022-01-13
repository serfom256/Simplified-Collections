package additional.nodes;

public interface TreeNode<E> {
    TreeNode<E> getLeft();

    TreeNode<E> getRight();

    E getElement();
}
