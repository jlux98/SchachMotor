package gametree;

/**
 * Interface for the nodes of a game tree.
 */
public interface GameNode {

    /**
     * @param node adds the passed node as child to this node
     */
    public abstract void insertChild(GameNode node);

    /**
     * Removes this node from its parent's child list.
     */
    public abstract void deleteSelf();

    /**
     * Removes the specified child node.
     * <br>
     * <br>
     * <b>Note:</b> Nodes are compared by reference for better performance and ease
     * of implementation.
     * 
     * @param node the node that should be removed from this parent
     * @return true if the node was successfully removed, false if the node could not be found
     */
    public abstract boolean deleteChild(GameNode node);

    /**
     * Removes the specified child node.
     * <br>
     * <br>
     * <b>Note:</b> Nodes are compared by reference for better performance and ease
     * of implementation.
     * 
     * @param node the node that should be removed from this parent
     * @throws IllegalArgumentException if the node could not be found
     */
    public abstract void deleteChildExceptionIfNotFound(GameNode node);

    /**
     * Removes all children of this node.
     */
    public abstract void deleteChildren();

    /**
     * @return true if this node has children, false otherwise
     */
    public abstract boolean hasChildren();

    /**
     * @param value the value to be set for this node
     */
    public abstract void setValue(int value);

    /**
     * Computes this node's children.
     * (children are added to the node's child list)
     */
    public abstract void computeChildren();

    /**
     * @return the node corresponding to the turn that has to be played for this node to be reachable.
     */
    public abstract GameNode findPlayableAncestor();

    /**
     * @return this node's parent
     */
    public abstract GameNode getParent();

}
