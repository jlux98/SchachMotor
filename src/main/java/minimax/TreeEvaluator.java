package minimax;

import gametree.Node;
import gametree.Tree;

/**
 * Interface for classes evaluating trees.
 */
public interface TreeEvaluator<T> {

    /**
     * Evaluates the game tree and returns the Node that should be played.
     * @param tree the tree to be evaluated
     * @param depth the maximum depth of the tree (a tree consisting only of a root node has depth = 0)
     * @param whitesTurn whether the turn to be searched is played by white
     * @return the Node representing the turn to be played
     */
    public abstract Node<T> evaluateTree(Tree<? extends Node<T>> tree, int depth, boolean whitesTurn);

    /**
     * Evaluates the sub tree starting with the passed node and returns the Node that should be played.
     * @param node the subtree to be evaluated
     * @param depth the maximum depth of the tree
     * @param whitesTurn whether the turn to be searched is played by white
     * @return the Node representing the turn to be played
     * @deprecated will probably be removed from interfaces and changed to a private method, use {@link #evaluateTree(Tree, int, boolean)} instead
     */
    public abstract Node<T> evaluateNode(Node<T> node, int depth, boolean whitesTurn);

    /**
     * @return the number of nodes that were evaluted by this TreeEvaluator
     */
    public abstract int getEvaluatedNodeCount();

    /**
     * Resets the counter for evaluated nodes to 0.
     */
    public abstract void resetEvaluatedNodeCount();

}
