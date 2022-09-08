package gametree;

import java.util.List;

import positionevaluator.Evaluable;

/**
 * Interface for nodes that store values of a specific type.
 * Nodes can be linked to create tree structures.
 */
public interface Node<T> extends Evaluable {

    /**
     * @return the content stored by this node
     */
    public abstract T getContent();

    /**
     * Adds a child node to this node.
     */
    public abstract void insertChild(Node<T> node);

    /**
     * Removes this node from its parent's child list.
     * Does nothing if this node has no parent.
     * @throws NoSuchElementException if this node has a parent but it could not be deleted from it
     */
    public abstract void deleteSelf();

    /**
     * Removes the specified child node.
     * <br><br>
     * <b>Note:</b> Nodes are compared by reference for better performance and ease
     * of implementation.
     * 
     * @param node the node that should be removed from this parent
     * @throws NoSuchElementException if the node could not be found
     */
    public abstract void deleteChild(Node<T> node);

    /**
     * Removes all children of this node.
     * Does nothing if this node has no children.
     */
    public abstract void deleteChildren();

    /**
     * @return this node's parent
     */
    public abstract Node<T> getParent();

    /**
     * Sets this node's parent.
     * <br><br>
     * <b>Note:</b>
     * This is a low level method.
     * Do not use this to add children to a node.
     * Use {@link #insertChild()} or a suitable constructor instead.
     * <br><br>
     * Cannot be used to unset the parent (parent = null).
     * Use {@link #unsetParent()} instead.
     * @param parent this node's parent
     * @throws IllegalStateException if this node already has a parent
     */
    public abstract void setParent(Node<T> parent);

    /**
     * Deletes this node's parent reference.
     * <br><br>
     * <b>Note:</b>
     * This is a low level method.
     * Do not use this to remove children from a node.
     * Use {@link #deleteChild()} or {@link #deleteSelf()} instead.
     */
    public abstract void unsetParent();

    /**
     * Whether this node has children. Note that returning false does not imply that this node cannot generate 
     * children when calling {@link #queryChildren()}.
     * @return true - if this node currently has any children, false - if not
     */
    public abstract boolean hasChildren();

    /**
    * Returns this node's children.
    * If necessary, calculates the children.
    * <br><br>
    * If children are calculated, they should be stored so invoking queryChildren() again can
    * read the stored children instead of recalculating them.
    * <br><br>
    * <b>Note:</b>
    * Results of this methods should be referenced as List < ? extends Node < Type > > which allows reading Node < Type > from the list.
    * Writing to the list is not possible.
    * @return this node's children
    * @throws ComputeChildrenException if this node has no children and no children can be generated
    */

    //List<Node<T>> would not allow GameNode to return a List<GameNode> as List<GameNode> is not a subtype of List<Node<Position>>
    //in case of GameNode this Class is Node<Position>
    //although GameNode extends Node<Position> a List<GameNode> is not compatible with a List<S extends Node<Position>> 
    //the latter might be more "specific" and thus doesnt necessarily accept the same types
    //e.g: A extends T, B extends T
    //List<S extends T> list = new List<A>;
    //list.add(new B()) <- not type safe as a List<A> does not accept B
    public abstract List<? extends Node<T>> queryChildren() throws ComputeChildrenException;

}
