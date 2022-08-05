package gametree;

import positionevaluator.Evaluable;

public interface TreeEvaluator<T extends Evaluable> {

    /**
     * Evaluates the game tree and returns the GameNode that should be played.
     * @param gameTree the tree to be evaluated
     * @param depth the maximum depth of the tree
     * @param whitesTurn
     * @return the GameNode representing the turn to be played
     */
    //TODO documentation (depth, whitesturn)
    public abstract Node<T> evaluateTree(Node<T> node, int depth, boolean whitesTurn);
    
}
