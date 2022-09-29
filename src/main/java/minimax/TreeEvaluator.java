package minimax;

import gametree.Node;
import gametree.Tree;

/**
 * Interface for classes evaluating trees.
 */
public interface TreeEvaluator<ContentType> {

    /**
     * Evaluates the game tree and returns the Node that should be played.
     * @param tree the tree to be evaluated
     * @param depth the maximum depth of the tree (a tree consisting only of a root node has depth = 0)
     * @param whitesTurn whether the turn to be searched is played by white
     * @return the Node representing the turn to be played
     */
    public abstract Node<ContentType> evaluateTree(Tree<? extends Node<ContentType>> tree, int depth, boolean whitesTurn);

    /**
    * @return the number of nodes that were evaluted by this TreeEvaluator
    */
    public abstract int getEvaluatedNodeCount();

    /**
     * Resets the counter for evaluated nodes to 0.
     */
    public abstract void resetEvaluatedNodeCount();

}
