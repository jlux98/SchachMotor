package tests;

import helper.AlphaBetaHelper;

/**
 * Subtype of {@link TreeEvaluationTest} for testing GenericAlphaBetaPruning.
 */
public class AlphaBetaTest extends TreeEvaluationTest {

   public AlphaBetaTest() {
        super(new AlphaBetaHelper());
    }
}
