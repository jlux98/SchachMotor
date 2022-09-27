package tests;

import helper.GameTreeEvaluationHelper;
import helper.IntTreeEvaluationHelper;
import minimax.GameNodeMoveOrderingSelfDestructingAlphaBetaPruning;
import minimax.MoveOrderingSelfDestructingAlphaBetaPruningPastPositions;

public class MoveOrderingSelfDestructingAlphaBetaPruningTest extends TreeEvaluationTest {

    public MoveOrderingSelfDestructingAlphaBetaPruningTest() {
        super(new IntTreeEvaluationHelper(() -> new MoveOrderingSelfDestructingAlphaBetaPruningPastPositions<Integer>()),
                new GameTreeEvaluationHelper(() -> new GameNodeMoveOrderingSelfDestructingAlphaBetaPruning()));
    }
}
