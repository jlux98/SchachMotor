package tests;

import helper.GameTreeEvaluationHelper;
import helper.IntTreeEvaluationHelper;
import minimax.GameNodeMoveOrderingSelfDestructingAlphaBetaPruning;
import minimax.MoveOrderingSelfDestructingAlphaBetaPruning;

public class MoveOrderingSelfDestructingAlphaBetaPruningTest extends TreeEvaluationTest {

    public MoveOrderingSelfDestructingAlphaBetaPruningTest() {
        super(new IntTreeEvaluationHelper(() -> new MoveOrderingSelfDestructingAlphaBetaPruning<Integer>()),
                new GameTreeEvaluationHelper(() -> new GameNodeMoveOrderingSelfDestructingAlphaBetaPruning()));
    }
}
