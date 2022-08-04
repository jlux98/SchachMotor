package gametree;

public interface TreeEvaluator<T extends Node<?>> {

    /**
     * Evaluates the game tree and returns the GameNode that should be played.
     * @param gameTree the tree to be evaluated
     * @return the GameNode representing the turn to be played
     */
    public abstract T evaluateTree(Tree<T> gameTree, int depth);
    
}
