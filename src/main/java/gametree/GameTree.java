package gametree;

import model.Board;

/**
 * Interface for classes implementing a game tree.
 */
public interface GameTree {
    
    //TODO doc
    public abstract GameNode calculateBestMove(Board incoming, int maxDepth);
}
