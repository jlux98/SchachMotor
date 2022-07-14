package gametree;

import model.Board;

/**
 * Interface for classes implementing a game tree.
 */
public interface GameTree {

    /**
     * Used to create the initial game tree from a board.
     * @param gameState the game state that the root node should represent
     * @return the initial game tree with a node representing the specified board
     */
    public GameTree createGameTree(Board gameState);
    //TODO add depth as argument?


    /**
     * Used to create a game tree from an existing game tree.
     * The tree is created as a subtree of the existing tree with the specified node as root.
     * The tree is then deepened further.
     * 
     * @param root the root node of the new tree
     * @return a game tree with the passed node as root
     */
    public GameTree createGameTree(GameNode root);
    //TODO add depth as argument?

    //TODO doc
    public abstract GameNode calculateBestMove(Board incoming, int maxDepth);
}
