package tests;

import helper.GameTreeEvaluationHelper;
import minimax.GameNodeSelfDestructingAlphaBetaPruning;

/**
 * Subtype of {@link TreeEvaluationTest} for testing SelfDestructingAlphaBetaPruning.
 */
public class SelfDestructingAlphaBetaGameTreeTest extends GameTreeEvaluationTest {

    public SelfDestructingAlphaBetaGameTreeTest() {
        super(new GameTreeEvaluationHelper(() -> new GameNodeSelfDestructingAlphaBetaPruning()));
    }
}
