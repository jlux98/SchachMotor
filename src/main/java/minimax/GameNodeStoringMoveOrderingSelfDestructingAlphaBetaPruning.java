package minimax;

import model.Position;
import gametree.GameNode;
import gametree.Node;
import gametree.Tree;

public class GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning
        extends StoringMoveOrderingSelfDestructingAlphaBetaPruning<Position> implements GameTreeEvaluator {

    public GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning(int storedLevels) {
        super(storedLevels);
    }

    @Override
    public GameNode evaluateTree(Tree<? extends Node<Position>> tree, int depth, boolean whitesTurn) {
        return (GameNode) super.evaluateTree(tree, depth, whitesTurn);
    }

}
