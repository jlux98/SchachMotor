package gametree;

/**
 * Interface for classes representing trees.
 */
public interface Tree<T extends Node<?>> {
    /**
     * @return this tree's root node
     */
    public abstract T getRoot();

    /**
     * Deletes all nodes from this tree.
     * <p>
     * This deletes all children from the root node and removes
     * this tree's reference to the root node.
     */
    public abstract void delete();
}
