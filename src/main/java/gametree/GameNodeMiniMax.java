package gametree;

import model.Position;

/**
 * GameNodeMiniMax extends serves as a wrapper for the generic class MiniMaxBase < Position > and extends it.
 * <br><br>
 * <b>Note:</b>
 * This class performs casts from Node < Position > to GameNode which should be safe so long as the passed tree consists only of gamenodes
 * and subtypes of gamenode.
 */
public class GameNodeMiniMax extends MiniMaxBase<Position> implements GameTreeEvaluator {
    

    @Override
    public GameNode evaluateTree(Tree<? extends Node<Position>> tree, int depth, boolean whitesTurn) {
        return evaluateNode(tree.getRoot(), depth, whitesTurn);
    }

    /**
     * 
     * See {@link MiniMaxBase#evaluateTree(Node, int, boolean)}.
     * <br><br>
     * <b>Note:</b>
     * Only type safe if the subtree starting with node consists only of GameNodes or subtypes of GameNodes.
     * If a subtype of Node<Position> that is not also subtype of GameNode is used a CastException might arise. 
     */
    @Override
    public GameNode evaluateNode(Node<Position> gameTree, int depth, boolean whitesTurn) {
        return (GameNode) super.evaluateNode(gameTree, depth, whitesTurn);
    }

}
