package tests;

import helper.GameTreeEvaluationHelper;
import helper.IntTreeEvaluationHelper;
import minimax.GameNodeSelfDestructingAlphaBetaPruning;
import minimax.SelfDestructingAlphaBetaPruning;

/**
 * Subtype of {@link TreeEvaluationTest} for testing SelfDestructingAlphaBetaPruning.
 */
public class SelfDestructingAlphaBetaTest extends TreeEvaluationTest {

    public SelfDestructingAlphaBetaTest() {
        super(new IntTreeEvaluationHelper(() -> new SelfDestructingAlphaBetaPruning<Integer>()),
                new GameTreeEvaluationHelper(() -> new GameNodeSelfDestructingAlphaBetaPruning()));
    }
}
