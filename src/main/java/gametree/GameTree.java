package gametree;

import model.Position;

/**
 * Interface for classes implementing a game tree.
 */
public interface GameTree {
    
    //TODO doc
    public abstract GameNode calculateBestMove(Position incoming, int maxTime);

}