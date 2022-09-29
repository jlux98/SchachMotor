package minimax;

import gametree.Node;
import gametree.Tree;
import utility.TimeUtility;

public class IterativeDeepening<T> {

    private long stopTime;
    public static Node<?> lastResult;

    protected void isTimeLeft() throws OutOfTimeException {
        if (System.nanoTime() >= stopTime - 1 * TimeUtility.SECOND_TO_NANO) {
            throw new OutOfTimeException();
        }
    }

    /**
     * Used to save an intermediate result of iterative deepning.
     * @param bestMove the move that should be saved
     */
    private void saveMove(Node<T> bestMove) {
        lastResult = bestMove;
        //System.out.println(bestMove);
        //System.out.println(((GameNode)bestMove).getRepresentedMove().toStringAlgebraic());
    }

    /**
    * Evaluates the game tree using iterative deepening and returns the Node that should be played.
    * @param tree the tree to be evaluated
    * @param secondsToCompute the maximum time in seconds that the computation may take
    * @param whitesTurn whether the turn to be searched is played by white
    * @param maxDepth the max depth to which the tree should be evaluated
    * @return the Node representing the turn to be played
    */
    public void evaluateTree(Tree<? extends Node<T>> tree, TreeEvaluator<T> evaluator, boolean whitesTurn, int secondsToCompute,
            int maxDepth) {
        long start = System.nanoTime();
        stopTime = start + secondsToCompute * TimeUtility.SECOND_TO_NANO;
        int depth = 1;
        Node<T> bestMove = null;
        while (depth <= maxDepth) {
            System.out.println("depth " + depth);
            bestMove = evaluator.evaluateTree(tree, depth, whitesTurn);
            saveMove(bestMove);
            depth += 1;
        }
        //System.out.println("finished with: "  + (((GameNode)bestMove).getRepresentedMove().toStringAlgebraic()));
    }
}
