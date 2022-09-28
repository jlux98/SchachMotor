package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gametree.GameNode;
import gametree.UninitializedValueException;
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
    public void firstMoveDepth5Test() throws UninitializedValueException {
        GameNode whiteMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 5, true);
        GameNode blackMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 5, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }

    @Test
    public void firstMoveDepth6Test() throws UninitializedValueException {
        GameNode whiteMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 6, true);
        GameNode blackMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 6, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }
}
