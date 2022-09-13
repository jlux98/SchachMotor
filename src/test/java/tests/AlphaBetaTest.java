package tests;

import helper.TreeEvaluationHelper;
import minimax.GenericAlphaBetaPruning;

/**
 * Subtype of {@link TreeEvaluationTest} for testing GenericAlphaBetaPruning.
 */
public class AlphaBetaTest extends TreeEvaluationTest {

    public AlphaBetaTest() {
        super(new TreeEvaluationHelper(() -> new GenericAlphaBetaPruning<Integer>()));
    }
}
