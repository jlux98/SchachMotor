package tests;

import helper.GameTreeEvaluationHelper;
import minimax.GameNodeMoveOrderingSelfDestructingAlphaBetaPruning;

public class MoveOrderingSelfDestructingAlphaBetaPruningGameTreeTest extends ExpensiveGameTreeEvaluationTest {

    public MoveOrderingSelfDestructingAlphaBetaPruningGameTreeTest() {
        super(new GameTreeEvaluationHelper(() -> new GameNodeMoveOrderingSelfDestructingAlphaBetaPruning()));
    }
}
