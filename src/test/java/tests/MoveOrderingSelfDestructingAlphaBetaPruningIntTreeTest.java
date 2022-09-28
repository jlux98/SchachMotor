package tests;

import helper.IntTreeEvaluationHelper;
import minimax.MoveOrderingSelfDestructingAlphaBetaPruning;

public class MoveOrderingSelfDestructingAlphaBetaPruningIntTreeTest extends IntTreeEvaluationTest {

    public MoveOrderingSelfDestructingAlphaBetaPruningIntTreeTest() {
        super(new IntTreeEvaluationHelper(() -> new MoveOrderingSelfDestructingAlphaBetaPruning<Integer>()));
    }
}
