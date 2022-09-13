package minimax;

import gametree.GameNode;
import gametree.Node;
import gametree.Tree;
import model.Position;

/**
 * GameNodeMiniMax serves as a wrapper for the generic class MiniMaxBase < Position > and extends it.
 * This class narrows the return types of methods from Node < Position > to GameNode.
 * <br><br>
 * <b>Note:</b>
 * This class performs casts from Node < Position > to GameNode which should be safe so long as the passed tree consists only of gamenodes
 * and subtypes of gamenode.
 */
public class GameNodeMiniMax extends GenericMiniMax<Position> implements GameTreeEvaluator {

    @Override
    public GameNode evaluateTree(Tree<? extends Node<Position>> tree, int depth, boolean whitesTurn) {
        return evaluateNode(tree.getRoot(), depth, whitesTurn);
    }

    /**
     * @deprecated
     */
    @Override
    public GameNode evaluateNode(Node<Position> gameTree, int depth, boolean whitesTurn) {
        return (GameNode) super.evaluateNode(gameTree, depth, whitesTurn);
    }

}
