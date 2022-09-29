package minimax;

import model.Position;
import gametree.GameNode;
import gametree.GameTree;
import gametree.ImpGameTree;
import gametree.Node;
import gametree.Tree;

public class GameNodeIterativeDeepeningMoveOrderingSelfDestructingAlphaBetaPruning
        extends IterativeDeepeningMoveOrderingSelfDestructingAlphaBetaPruning<Position> implements GameTreeEvaluator {

    @Override
    public GameNode evaluateTree(Tree<? extends Node<Position>> tree, int depth, boolean whitesTurn) {
        System.out.println("evaluateTree");
        super.evaluateTreeIterativeDeepening(tree, -1, whitesTurn, depth);
        return (GameNode) IterativeDeepeningMoveOrderingSelfDestructingAlphaBetaPruning.lastResult;
    }

    @Override
    public GameNode evaluateNode(Node<Position> node, int depth, boolean whitesTurn) {
        System.out.println("evaluate node");
        return (GameNode) super.evaluateNode(node, depth, whitesTurn);
    }

    @Override
    public void evaluateTreeIterativeDeepening(Tree<? extends Node<Position>> tree, long secondsToCompute, boolean whitesTurn,
            int maxDepth) {
                System.out.println("evaluateTreeIterativeDeepening");
        super.evaluateTreeIterativeDeepening(tree, secondsToCompute, whitesTurn, maxDepth);
    }
}
