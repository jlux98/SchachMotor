package tests;

import helper.GameTreeEvaluationHelper;
import minimax.GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning;

public class StoringMoveOrderingSelfDestructingAlphaBetaPruningGameTreeTest extends GameTreeEvaluationTest {
    public StoringMoveOrderingSelfDestructingAlphaBetaPruningGameTreeTest() {
        super(new GameTreeEvaluationHelper(() -> new GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning()));
    }

    //TODO add tests verifying storage depth
}
