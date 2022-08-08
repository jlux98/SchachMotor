package gametree;

import positionevaluator.Evaluable;

public interface TreeEvaluator<T extends Evaluable> {

    /**
     * Evaluates the game tree and returns the GameNode that should be played.
     * @param gameTree the tree to be evaluated
     * @param depth the maximum depth of the tree
     * @param whitesTurn whether the turn to be searched is played by white
     * @return the GameNode representing the turn to be played
     */
    public abstract Node<T> evaluateTree(Tree<? extends Node<T>> tree ,int depth, boolean whitesTurn) ;

    /**
     * Evaluates the sub tree starting with the passed node and returns the GameNode that should be played.
     * @param node the subtree to be evaluated
     * @param depth the maximum depth of the tree
     * @param whitesTurn whether the turn to be searched is played by white
     * @return the GameNode representing the turn to be played
     * @deprecated will probably be removed from interfaces and changed to a private method, use {@link #evaluateTree(Tree, int, boolean)} instead
     */
    public abstract Node<T> evaluateNode(Node<T> node, int depth, boolean whitesTurn);
    
}
