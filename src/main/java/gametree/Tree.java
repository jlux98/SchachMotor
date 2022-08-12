package gametree;

/**
 * Interface for classes representing trees.
 */
public interface Tree<T extends Node<?>> {
    /**
     * @return this tree's root node
     */
    public abstract T getRoot();
}
