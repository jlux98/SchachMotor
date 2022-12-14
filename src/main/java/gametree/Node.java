package gametree;

import java.util.List;

/**
 * Interface for nodes that store objects of a specific type.
 * Nodes can be linked to create tree structures.
 * Nodes have a value associated with them as specified by {@link Evaluable}.
 */
public interface Node<ContentType> extends Evaluable {

    /**
     * @return the content stored by this node
     */
    public abstract ContentType getContent();

    /**
     * Overwrites this node's current content.
     * @param content content that should be stored by this node
     */
    public void setContent(ContentType content);

    /**
     * Deletes the content stored by this node
     * (deletes the reference to it).
     */
    public abstract void deleteContent();

    /**
     * Adds a child node to this node.
     */
    public abstract void insertChild(Node<ContentType> node);

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
    public abstract void deleteChild(Node<ContentType> node);

    /**
     * Removes all children of this node.
     * Does nothing if this node has no children.
     */
    public abstract void deleteChildren();

    /**
     * @return this node's parent
     */
    public abstract Node<ContentType> getParent();

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
    public abstract void setParent(Node<ContentType> parent);

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
     * Adds the content of a Node to a type-specific log
     */
    public abstract void writeContentToHistory();

    /**
     * removes the last entry from a type-specific log
     */
    public abstract void deleteContentFromHistory();

    /**
     * Whether this node has children. Note that returning false does not imply that this node cannot generate 
     * children when calling {@link #getOrComputeChildren()}.
     * @return true - if this node currently has any children, false - if not
     */
    public abstract boolean hasChildren();

    /**
    * Returns this node's children.
    * If necessary, calculates the children.
    * <p>
    * If children are calculated, they should be stored so invoking queryChildren() again can
    * read the stored children instead of recalculating them.
    * <p>
    * This method will never return null or an empty list.
    * <p>
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
    public abstract List<? extends Node<ContentType>> getOrComputeChildren() throws ComputeChildrenException;


    /**
     * Returns the this node's children.
     * <p>
     * Different from {@link #getOrComputeChildren()}, this method may return both null and an empty list.
     * If the demanded children can be computed, use {@link #getOrComputeChildren()} instead.
     * @return
     */
    public abstract List<? extends Node<ContentType>> getChildren(); 
}
