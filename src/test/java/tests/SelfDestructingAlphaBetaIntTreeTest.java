package tests;

import helper.IntTreeEvaluationHelper;
import minimax.SelfDestructingAlphaBetaPruning;

public class SelfDestructingAlphaBetaIntTreeTest extends IntTreeEvaluationTest {
    public SelfDestructingAlphaBetaIntTreeTest() {
        super(new IntTreeEvaluationHelper(() -> new SelfDestructingAlphaBetaPruning<Integer>(), false));
    }
}
