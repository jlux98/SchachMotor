package tests;

import helper.TreeEvaluationHelper;
import minimax.MoveOrderingSelfDestructingAlphaBetaPruning;

public class MoveOrderingSelfDestructingAlphaBetaPruningTest extends TreeEvaluationTest {
    
    public MoveOrderingSelfDestructingAlphaBetaPruningTest() {
        super(new TreeEvaluationHelper(() -> new MoveOrderingSelfDestructingAlphaBetaPruning<Integer>()));
    }
}
