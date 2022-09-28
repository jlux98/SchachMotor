package tests;

import helper.IntTreeEvaluationHelper;
import minimax.GenericMiniMax;

public class MiniMaxIntTreeTest extends IntTreeEvaluationTest {
    public MiniMaxIntTreeTest() {
        super(new IntTreeEvaluationHelper(() -> new GenericMiniMax<Integer>()));
    }

}
