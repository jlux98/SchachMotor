package minimax;

import application.Conductor;
import gametree.Node;
import gametree.Tree;
import model.Position;

public class IterativeDeepening<ContentType> {

    /**
     * lastResult of an evaluation of a non Node < Position > tree
     * (e.g. Tree < Node < Position > >)
     */
    public Node<ContentType> lastResult;
    /**
     * Used to save an intermediate result of iterative deepning.
     * @param bestNode the move that should be saved
     */
    private void saveMove(Node<ContentType> bestNode, int depth) {
        if (bestNode.getContent() instanceof Position){
            Position bestFollowUp   = (Position) bestNode.getContent();
            Conductor.bestFollowUp  = bestFollowUp;
            Conductor.depthCompleted  = depth;
        } else {
            lastResult = bestNode;
        }
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
    public void evaluateTree(Tree<? extends Node<ContentType>> tree, TreeEvaluator<ContentType> evaluator, boolean whitesTurn, int secondsToCompute,
            int maxDepth) {
        
        Conductor.cleanup();
        int depth = 1;
        Node<ContentType> bestMove = null;
        while (depth <= maxDepth && !Conductor.stopCalculating) {
            System.out.println("depth " + depth);
            bestMove = evaluator.evaluateTree(tree, depth, whitesTurn);
            saveMove(bestMove, depth);
            depth += 1;
        }
        Conductor.stopCalculating = true;
        System.out.println("finished");
    }
}
