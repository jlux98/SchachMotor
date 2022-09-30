package tests;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import gametree.GameNode;
import helper.GameTreeEvaluationHelper;
import minimax.GameNodeMoveOrderingSelfDestructingAlphaBetaPruning;
import uciservice.FenParser;

public class MoveOrderingSelfDestructingAlphaBetaPruningGameTreeTest extends ExpensiveGameTreeEvaluationTest {

    public MoveOrderingSelfDestructingAlphaBetaPruningGameTreeTest() {
        super(new GameTreeEvaluationHelper(() -> new GameNodeMoveOrderingSelfDestructingAlphaBetaPruning()));
    }

    @Test
    public void verifyDeletionWhiteTest() {
        GameNode root = new GameNode(FenParser.parseFen("1b2q1p1/n1k1r3/3p4/2pp4/5PR1/7P/1PP3KB/2Q5 w - - 0 1"));
        helper.evaluate(root, 4, true);
        assertFalse(root.hasChildren());
    }

    @Test
    public void verifyDeletionBlackTest() {
        GameNode root = new GameNode(FenParser.parseFen("1b2q1p1/n1k1r3/3p4/2pp4/5PR1/7P/1PP3KB/2Q5 b - - 0 1"));
        helper.evaluate(root, 4, false);
        assertFalse(root.hasChildren());
    }
}
