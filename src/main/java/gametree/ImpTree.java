package gametree;

/**
 * Generic class providing a basic tree implementation.
 */
public class ImpTree<T extends Node<?>> implements Tree<T> {

    private T root;

    /**
     * Creates a tree with the specified node as root.
     * @param root
     */
    public ImpTree(T root) {
        this.root = root;
    }

    @Override
    public T getRoot() {
        return this.root;
    }

}
