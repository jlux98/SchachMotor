package tests;

import helper.IntTreeEvaluationHelper;
import minimax.StoringMoveOrderingSelfDestructingAlphaBetaPruning;

public class StoringMoveOrderingSelfDestructingAlphaBetaPruningIntTreeTest extends IntTreeEvaluationTest {
    
    public StoringMoveOrderingSelfDestructingAlphaBetaPruningIntTreeTest() {
        super(new IntTreeEvaluationHelper(() -> new StoringMoveOrderingSelfDestructingAlphaBetaPruning<Integer>()));
    }

    //TODO add tests verifying storage depth
}
