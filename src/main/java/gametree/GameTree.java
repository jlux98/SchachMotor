package gametree;


/**
 * Interface for classes implementing a game tree.
 * GameTree extends Tree < GameNode > and adds a method calculateBestMove().
 */
public interface GameTree extends Tree<GameNode> {


    /**
     * Evaluates the tree to the specified depth and returns the best node.
     * @param depth the depth to which the tree should be evaluated
     * @return the GameNode representing the best move
     */
    public abstract GameNode calculateBestMove(int depth);

}