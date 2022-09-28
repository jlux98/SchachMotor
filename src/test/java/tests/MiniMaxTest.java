package tests;

import gametree.ComputeChildrenException;
import helper.GameTreeEvaluationHelper;
import helper.IntTreeEvaluationHelper;
import minimax.GameNodeMiniMax;
import minimax.GenericMiniMax;

public class MiniMaxTest extends TreeEvaluationTest {
    public MiniMaxTest() {
        super(new IntTreeEvaluationHelper(() -> new GenericMiniMax<Integer>()),
        new GameTreeEvaluationHelper(() -> new GameNodeMiniMax()));
    }

    public static void main(String[] args) throws ComputeChildrenException {
        MiniMaxTest test = new MiniMaxTest();
        //test.prepareBishopCaptureDepth2Test();
    }
}
