package gametree;

import java.util.List;

public interface Node<T> {

    /**
     * Adds a child node to this node.
     */
    public abstract void insertChild(Node<T> node);

    /**
     * Removes this node from its parent's child list.
     */
    public abstract void deleteSelf();

    /**
     * Removes the specified child node.
     * <br><br>
     * <b>Note:</b> Nodes are compared by reference for better performance and ease
     * of implementation.
     * 
     * @param node the node that should be removed from this parent
     * @throws IllegalArgumentException if the node could not be found
     */
    public abstract void deleteChild(Node<T> node);

    /**
     * @return this node's parent
     */
    public abstract Node<T> getParent();

    /**
     * Removes all children of this node.
     */
    public abstract void deleteChildren();

    /**
     * Whether this node has children. Note that returning false does not imply that this note cannot generate 
     * children when calling {@link #queryChildren()}.
     * @return true - if this node currently has any children, false - if not
     */
    public abstract boolean hasChildren();




    /**
     * @return the content stored by this node
     */
    public abstract T getContent();

    /**
     * Returns this node's children.
     * If necessary, calculates the children.
     * <b>Specify behavior if no children can be generated (e.g. no possible follow-up moves)</b>
     */

    //TODO specify behavior if no children can be generated (terminal node)

    //List<Node<T>> would not allow GameNode to return a List<GameNode> as List<GameNode> is not a subtype of List<Node<Position>>
    //in case of GameNode this Class is Node<Position>
    //although GameNode extends Node<Position> a List<GameNode> is not compatible with a List<S extends Node<Position>> 
    //the latter might be more "specific" and thus doesnt necessarily accept the same types
    //e.g: A extends T, B extends T
    //List<S extends T> list = new List<A>;
    //list.add(new B()) <- not type safe as a List<A> does not accept B
    public abstract List<? extends Node<T>> queryChildren();



}
