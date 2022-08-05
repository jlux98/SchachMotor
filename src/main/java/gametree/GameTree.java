package gametree;


/**
 * Interface for classes implementing a game tree.
 */
public interface GameTree extends Tree<GameNode> {

    //TODO doc
    public abstract GameNode calculateBestMove(int maxTime);

}