package minimax;

import gametree.GameNode;
import gametree.Node;
import gametree.Tree;
import model.Position;

public class GameNodeMoveOrderingSelfDestructingAlphaBetaPruning extends MoveOrderingSelfDestructingAlphaBetaPruning<Position>
        implements GameTreeEvaluator {

    @Override
    public GameNode evaluateTree(Tree<? extends Node<Position>> tree, int depth, boolean whitesTurn) {
        return (GameNode) super.evaluateTree(tree, depth, whitesTurn);
    }

}
