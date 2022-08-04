package gametree;

import java.util.List;

/**
 * Interface for classes implementing a game tree.
 */
public interface GameTree extends Tree<GameNode> {
    
    //TODO doc
    public abstract GameNode calculateBestMove(int maxTime);

    public abstract GameNode getRoot();
    public abstract List<GameNode> getLeafList();

}