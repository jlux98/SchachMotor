package gametree;


/**
 * Interface for classes implementing a game tree.
 * GameTree extends Tree < GameNode > and adds a method calculateBestMove().
 */
public interface GameTree extends Tree<GameNode> {


    //TODO doc
    public abstract GameNode calculateBestMove(int maxTime);

}