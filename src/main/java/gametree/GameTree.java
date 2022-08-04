package gametree;

import java.util.List;

import model.Position;

/**
 * Interface for classes implementing a game tree.
 */
public interface GameTree {
    
    //TODO doc
    public abstract GameNode calculateBestMove(Position incoming, int maxTime);

    public abstract GameNode getRoot();
    public abstract List<GameNode> getLeafList();

}