package minimax;

import model.Position;
import gametree.GameNode;
import gametree.Node;
import gametree.Tree;

public class GameNodeIterativeDeepeningMoveOrderingSelfDestructingAlphaBetaPruning
        extends IterativeDeepeningMoveOrderingSelfDestructingAlphaBetaPruning<Position> {

    @Override
    protected void saveMove(Node<Position> bestMove) {
        //TODO call save method in conductor or similar
    }

    @Override
    public GameNode evaluateTree(Tree<? extends Node<Position>> tree, int depth, boolean whitesTurn) {
        return (GameNode) super.evaluateTree(tree, depth, whitesTurn);
    }

    /**
     * @deprecated
     */
    @Override
    public GameNode evaluateNode(Node<Position> node, int depth, boolean whitesTurn) {
        return (GameNode) super.evaluateNode(node, depth, whitesTurn);
    }
}
