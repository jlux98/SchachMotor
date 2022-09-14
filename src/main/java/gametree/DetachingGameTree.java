package gametree;

import minimax.GameTreeEvaluator;
import model.Position;

public class DetachingGameTree extends ImpGameTree {
    /**
    * Used to create the initial game tree from a position.
    * @param gameState the game state that the root node should represent
    */
    public DetachingGameTree(Position position, GameTreeEvaluator evaluator) {
        this(new DetachingGameNode(position), evaluator);
    }

    /**
     * Used to create a game tree from an existing game tree.
     * The tree is created as a subtree of the existing tree with the specified node as root.
     * The tree is then deepened further.
     * @param root the root node of the new tree
     */
    public DetachingGameTree(GameNode root, GameTreeEvaluator evaluator) {
        super(root, evaluator);
    }
}
