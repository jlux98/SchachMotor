package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import application.Conductor;
import gametree.ComputeChildrenException;
import gametree.GameNode;
import gametree.ImpGameTree;
import gametree.UninitializedValueException;
import helper.GameTreeEvaluationHelper;
import helper.Mirror;
import model.Move;
import model.Position;
import uciservice.FenParser;

 /**
 * This abstract class implements test for TreeEvaluators such as alpha-beta-pruning 
 * that are applied to nodes storing positions.
 * Tests in this class are considered to be computationally inexepensive
 * and can be applied to all alpha-beta implementations. 
 * Pure minimax however should not be used to run them.
 * <p>
 * As this class is abstract its tests cannot be run directly.
 * To apply the tests to an implementation of a TreeEvaluator,
 * extend this class and pass a suitable TreeEvaluationHelper to its constructor.
 * </p>
 * <p>
 * A suitable instance of {@link GameTreeEvaluationHelper} can be created by providing a
 * <pre> Supplier&lt;TreeEvaluator&lt;Position&gt;&gt;</pre>  to its constructor. 
 * This can be done by using a lambda expressions.
 * 
 * 
 * <p>
 * Code example for testing GenericAlphaBetaPruning:
 * 
 * <pre>
 *
 * 
 * public class AlphaBetaTest extends TreeEvaluationTest {
 * 
 *     public AlphaBetaTest() {
 *         super(new IntTreeEvaluationHelper(
 *                  () -> new GenericAlphaBetaPruning&lt;Position&gt;()));
 *     }
 * }
 * 
 * </pre>
 */
public abstract class GameTreeEvaluationTest {

    protected GameTreeEvaluationHelper helper;

    /**
    * Constructs a new Test instance using the passed TreeEvaluationHelper
    * to instantiate the {@link TreeEvaluator} to be tested.
    * @param gameTreeEvaluator the GameTreeEvaluationHelper used to execute these tests
    */
    public GameTreeEvaluationTest(GameTreeEvaluationHelper gameTreeEvaluator) {
        this.helper = gameTreeEvaluator;
    }

    @Test
    public void findCheckMateBlack() {
        List<Move> expected = new ArrayList<Move>();
        expected.add(new Move("d2d1"));
        expected.add(new Move("e2e1"));
        expected.add(new Move("f2f1"));
        Position position = FenParser.parseFen("2k5/8/8/8/8/3rrr2/N2rrr2/2K5 b - - 1 1");
        // Move actual = new ImpGameTree(position, new GameNodeAlphaBetaPruning()).calculateBestMove(1).getContent().getMove();
        // assertTrue(expected.contains(actual));
        Move actual = new ImpGameTree(position, helper.instantiateTreeEvaluator()).calculateBestMove(3).getContent()
                .getMove();
        Conductor.getPastPositions();
        assertTrue(expected.contains(actual));
        actual = new ImpGameTree(position, helper.instantiateTreeEvaluator()).calculateBestMove(5).getContent()
                .getMove();
        assertTrue(expected.contains(actual));
    }

    @Test
    public void findCheckMateWhite() {
        List<Move> expected = new ArrayList<Move>();
        expected.add(new Move("d2d1"));
        expected.add(new Move("e2e1"));
        expected.add(new Move("f2f1"));
        Position position = FenParser.parseFen("2K5/8/8/8/8/3RRR2/n2RRR2/2k5 w - - 1 1");
        // Move actual = new ImpGameTree(position, new GameNodeAlphaBetaPruning()).calculateBestMove(1).getContent().getMove();
        // assertTrue(expected.contains(actual));
        Move actual = new ImpGameTree(position, helper.instantiateTreeEvaluator()).calculateBestMove(3).getContent()
                .getMove();
        assertTrue(expected.contains(actual));
        actual = new ImpGameTree(position, helper.instantiateTreeEvaluator()).calculateBestMove(5).getContent()
                .getMove();
        assertTrue(expected.contains(actual));
    }

    @Test
    public void findStaleMate() {
        Move expected = new Move("d1c1");
        Position position = FenParser.parseFen("5K2/8/8/8/5B2/3RPR2/1R6/3k4 b - - 0 1");
        Move actual = new ImpGameTree(position, helper.instantiateTreeEvaluator()).calculateBestMove(3).getContent()
                .getMove();
        assertEquals(expected, actual);
        actual = new ImpGameTree(position, helper.instantiateTreeEvaluator()).calculateBestMove(5).getContent()
                .getMove();
        assertEquals(expected, actual);
    }

    @Test
    public void conductorHistoryEmptyAfterMovegen(){
        assertEquals(0, Conductor.getPastPositions().size());
        new ImpGameTree(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"),
        helper.instantiateTreeEvaluator()).calculateBestMove(3);
        assertEquals(0, Conductor.getPastPositions().size());
    }

    @Test
    public void firstMoveDepth1Test() throws UninitializedValueException {
        GameNode whiteMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 1, true);
        GameNode blackMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 1, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }

    @Test
    public void firstMoveDepth2Test() throws UninitializedValueException {
        GameNode whiteMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 2, true);
        GameNode blackMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 2, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }

    @Test
    public void firstMoveDepth3Test() throws UninitializedValueException {
        GameNode whiteMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 3, true);
        GameNode blackMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 3, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }

    @Test
    public void firstMoveDepth4Test() throws UninitializedValueException {
        GameNode whiteMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 4, true);
        GameNode blackMove = helper.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 4, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }

    @Test
    public void bishopCaptureDepth1BlackTest() throws UninitializedValueException {
        GameNode bestMove = helper.evaluate("rn1qkbnr/pbpppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 1, false);
        assertEquals(-115, bestMove.getValue());
    }

    @Test
    public void bishopCaptureDepth1WhiteTest() throws UninitializedValueException {
        GameNode bestMove = helper.evaluate("1rbqkbnr/pppppppp/n7/8/8/1P6/PBPPPPPP/RN1QKBNR w KQk - 0 1", 1, true);
        assertEquals(115, bestMove.getValue());
    }

    @Test
    public void bishopCaptureDepth2WhiteTest() throws ComputeChildrenException {
        helper.assertBestMoveNotIn("1rbqkbnr/pppppppp/n7/8/8/1P6/PBPPPPPP/RN1QKBNR w KQk - 0 1", 2, true, "b2g7");
    }

    @Test
    public void bishopCaptureDepth2BlackTest() throws ComputeChildrenException {
        helper.assertBestMoveNotIn("rn1qkbnr/pbpppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 2, false, "b7g2");

    }

    @Test
    public void prepareBishopCaptureDepth3WhiteTest() throws ComputeChildrenException {
        helper.assertBestMoveIn("r1bqkbnr/pppppppp/n7/8/8/1P6/P1PPPPPP/NRBQKBNR w Kkq - 0 1", 3, true, "c1b2");
    }

    @Test
    public void prepareBishopCaptureDepth3BlackTest() throws ComputeChildrenException {
        helper.assertBestMoveIn("rnbqkbnr/p1pppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 3, false, "c8b7");
    }

    @Test
    public void bishopNotCaptureDepth4WhiteTest() throws ComputeChildrenException {
        helper.assertBestMoveNotIn("1rbqkbnr/pppppppp/n7/8/8/1P6/PBPPPPPP/RN1QKBNR w KQk - 0 1", 4, true, "b2g7");
    }

    @Test
    public void bishopNotCaptureDepth4BlackTest() throws ComputeChildrenException {
        helper.assertBestMoveNotIn("rn1qkbnr/pbpppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 4, false, "b7g2");

    }

    //TODO apply mirroring to these tests
}
