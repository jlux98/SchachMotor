package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gametree.ComputeChildrenException;
import gametree.GameNode;
import helper.GameTreeEvaluationHelper;
import helper.Mirror;

/**
 * Tree evaluation tests that should only be applied to performant minimax variations.
 * These test will generally perform searches in depth 5 and higher.
 * Contains all tests of {@link GameTreeEvaluationTest}.
 */
public abstract class ExpensiveGameTreeEvaluationTest extends GameTreeEvaluationTest {

    /**
    * Constructs a new Test instance using the passed TreeEvaluationHelper
    * to instantiate the {@link TreeEvaluator} to be tested.
    * @param gameTreeEvaluator the GameTreeEvaluationHelper used to execute these tests
    */
    public ExpensiveGameTreeEvaluationTest(GameTreeEvaluationHelper gameTreeEvaluator) {
        super(gameTreeEvaluator);
    }

    @Test
    public void bestMoveTest() throws ComputeChildrenException {

        //FIXME d7d5 is fine
        //GameNode node = gameTreeEvaluator.assertBestMoveIn("1q1k4/2Rr4/8/2Q3K1/8/8/8/8 b - - 0 1", 5, false, "d7d5");
        GameNode node = helper.evaluate("1q1k4/2Rr4/8/2Q3K1/8/8/8/8 b - - 0 1", 5, false);
        System.out.println(node.getRepresentedMove().toStringAlgebraic());

    }

    @Test
    public void prepareBishopCaptureDepth5WhiteTest() throws ComputeChildrenException {
        GameNode bestMove = helper.evaluate("r1bqkbnr/pppppppp/n7/8/8/1P6/P1PPPPPP/NRBQKBNR w Kkq - 0 1", 5, true);
        assertEquals(100, bestMove.getValue());
    }

    @Test
    public void prepareBishopCaptureDepth5BlackTest() throws ComputeChildrenException {
        GameNode bestMove = helper.evaluate("rnbqkbnr/p1pppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 5, false);
        assertEquals(-100, bestMove.getValue());
    }

    @Test
    public void prepareBishopCaptureDepth6WhiteTest() throws ComputeChildrenException {
        GameNode bestMove = helper.evaluate("r1bqkbnr/pppppppp/n7/8/8/1P6/P1PPPPPP/NRBQKBNR w Kkq - 0 1", 6, true);
        assertEquals(100, bestMove.getValue());
    }

    @Test
    public void prepareBishopCaptureDepth6BlackTest() throws ComputeChildrenException {
        GameNode bestMove = helper.evaluate("rnbqkbnr/p1pppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 6, false);
        assertEquals(-100, bestMove.getValue());
    }

    @Test
    public void firstMoveDepth5Test() {
        GameNode whiteMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 5, true);
        GameNode blackMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 5, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }

    @Test
    public void firstMoveDepth6Test() {
        GameNode whiteMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 6, true);
        GameNode blackMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 6, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }
}
