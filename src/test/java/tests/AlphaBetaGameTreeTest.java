package tests;

import helper.GameTreeEvaluationHelper;
import minimax.GameNodeAlphaBetaPruning;

/**
 * Subtype of {@link TreeEvaluationTest} for testing GenericAlphaBetaPruning.
 */
public class AlphaBetaGameTreeTest extends GameTreeEvaluationTest {

    public AlphaBetaGameTreeTest() {
        super(new GameTreeEvaluationHelper(() -> new GameNodeAlphaBetaPruning()));
    }

}
