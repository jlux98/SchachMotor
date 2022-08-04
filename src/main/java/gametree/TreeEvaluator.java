package gametree;

import positionevaluator.Evaluable;

public interface TreeEvaluator<T extends Node<? extends Evaluable>> {

    /**
     * Evaluates the game tree and returns the GameNode that should be played.
     * @param gameTree the tree to be evaluated
     * @param depth the maximum depth of the tree
     * @param whitesTurn
     * @return the GameNode representing the turn to be played
     */
    //TODO documentation (depth, whitesturn)
    public abstract T evaluateTree(T node, int depth, boolean whitesTurn);
    
}
