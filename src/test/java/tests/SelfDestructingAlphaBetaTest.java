package tests;

import helper.TreeEvaluationHelper;
import minimax.SelfDestructingAlphaBetaPruning;

/**
 * Subtype of {@link TreeEvaluationTest} for testing SelfDestructingAlphaBetaPruning.
 */
public class SelfDestructingAlphaBetaTest extends TreeEvaluationTest {
    
    public SelfDestructingAlphaBetaTest() {
        super(new TreeEvaluationHelper(() -> new SelfDestructingAlphaBetaPruning<Integer>()));
    }
}
