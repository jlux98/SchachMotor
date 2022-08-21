package gametree;

/**
 * Generic class providing a basic tree implementation.
 */
public class BaseTree<T extends Node<?>> implements Tree<T> {

    private T root;

    /**
     * Creates a tree with the specified node as root.
     * @param root
     */
    public BaseTree(T root) {
        this.root = root;
    }

    @Override
    public T getRoot() {
        return this.root;
    }

}
