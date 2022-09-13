package minimax;

import gametree.GameNode;
import gametree.Node;
import gametree.Tree;
import model.Position;

/**
 * GameNodeAlphaBetaPruning serves as a wrapper for the generic class
 * AlphaBetaPruningBase < Position > which it extends.
 * This class narrows the return types of methods from Node < Position > to
 * GameNode.
 * <br><br>
 * <b>Note:</b>
 * This class performs casts from Node < Position > to GameNode which should be
 * safe so long as the passed tree consists only of gamenodes
 * and subtypes of gamenode.
 */
public class GameNodeAlphaBetaPruning extends GenericAlphaBetaPruning<Position> implements GameTreeEvaluator {

    @Override
    public GameNode evaluateTree(Tree<? extends Node<Position>> tree, int depth, boolean whitesTurn) {
        return evaluateNode(tree.getRoot(), depth, whitesTurn);
    }

    /**
     * @deprecated
     */
    @Override
    public GameNode evaluateNode(Node<Position> node, int depth, boolean whitesTurn) {
        return (GameNode) super.evaluateNode(node, depth, whitesTurn);
    }

}
