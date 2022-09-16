package minimax;

import gametree.GameNode;
import gametree.Node;
import gametree.Tree;
import model.Position;

public class GameNodeSelfDestructingAlphaBetaPruning extends SelfDestructingAlphaBetaPruning<Position> implements GameTreeEvaluator {
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
