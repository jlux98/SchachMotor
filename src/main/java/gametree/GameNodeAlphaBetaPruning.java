package gametree;

import model.Position;

/**
 * GameNodeAlphaBetaPruning extends serves as a wrapper for the generic class AlphaBetaPruningBase < Position > and extends it.
 * <br><br>
 * <b>Note:</b>
 * This class performs casts from Node < Position > to GameNode which should be safe so long as the passed tree consists only of gamenodes
 * and subtypes of gamenode.
 */
public class GameNodeAlphaBetaPruning extends AlphaBetaPruningBase<Position> {

    /**
     * See {@link AlphaBetaPruningBase#evaluateTree(Node, int, boolean)}.
     * <br><br>
     * <b>Note:</b>
     * Only type safe if the subtree starting with node consists only of GameNodes or subtypes of GameNodes.
     * If a subtype of Node<Position> that is not also subtype of GameNode is used a CastException might arise.
     */
    @Override
    public GameNode evaluateTree(Node<Position> node, int depth, boolean whitesTurn) {
        return (GameNode) super.evaluateTree(node, depth, whitesTurn);
    }

}
