package tests;

import helper.GameTreeEvaluationHelper;
import minimax.GameNodeAlphaBetaPruning;

/**
 * Subtype of {@link TreeEvaluationTest} for testing GenericAlphaBetaPruning.
 */
public class AlphaBetaGameTreeTest extends GameTreeEvaluationTest {
    /* public static void main(String[] args) throws ComputeChildrenException {
        AlphaBetaGameTreeTest test = new AlphaBetaGameTreeTest();
        //test.Test();
        //test.bestMoveTest();
        //test.zugzwangStallTest();
        //test.startTest();
    } */

    public AlphaBetaGameTreeTest() {
        super(new GameTreeEvaluationHelper(() -> new GameNodeAlphaBetaPruning()));
    }

   
}
