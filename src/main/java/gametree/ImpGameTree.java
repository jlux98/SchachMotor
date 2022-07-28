package gametree;

import java.util.List;

import model.Board;

public class ImpGameTree implements GameTree {

    private int depth = -1;
    private GameNode root;
    private List<GameNode> leafList; //TODO will this actually be used?

    /**
     * Used to create the initial game tree from a board.
     * @param gameState the game state that the root node should represent
     */
    public ImpGameTree(Board gameState) {
        this.root = ImpGameNode.createRoot(gameState);
    }


    /**
     * Used to create a game tree from an existing game tree.
     * The tree is created as a subtree of the existing tree with the specified node as root.
     * The tree is then deepened further.
     * 
     * @param root the root node of the new tree
     */
    public ImpGameTree(GameNode root) {
        this.root = root;
    }

    @Override
    public GameNode calculateBestMove(Board incoming, int maxDepth) {
        //TODO implement mini-max, alpha-beta pruning, iterative deepening
        return null;
    }
    
}
