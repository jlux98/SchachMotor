package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import application.Conductor;
import gametree.ComputeChildrenException;
import gametree.GameNode;
import gametree.GameTree;
import gametree.ImpGameTree;
import gametree.UninitializedValueException;
import helper.GameTreeEvaluationHelper;
import helper.Mirror;
import minimax.GameTreeEvaluator;
import minimax.IterativeDeepening;
import minimax.TreeEvaluator;
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
    public void illegalDepthTest() {
        GameTree tree = new ImpGameTree(new GameNode(FenParser.parseFen("2k5/8/8/8/8/3rrr2/N2rrr2/2K5 b - - 1 1")), helper.instantiateTreeEvaluator());
        assertThrows(IllegalArgumentException.class, () -> helper.instantiateTreeEvaluator().evaluateTree(tree, 0, true));
        assertThrows(IllegalArgumentException.class, () -> helper.instantiateTreeEvaluator().evaluateTree(tree, -1, true));
        assertThrows(IllegalArgumentException.class, () -> helper.instantiateTreeEvaluator().evaluateTree(tree, Integer.MIN_VALUE, true));
        assertThrows(IllegalArgumentException.class, () -> tree.calculateBestMove(0));
        assertThrows(IllegalArgumentException.class, () -> tree.calculateBestMove(-1));
        assertThrows(IllegalArgumentException.class, () -> tree.calculateBestMove(Integer.MIN_VALUE));
    }
    
    /**
    * Calculates the next move for the given fen twice using
    * {@link TreeEvaluator#evaluateTree(gametree.Tree, int, boolean)}
    * and compares both results.
    * Also compares the result with that of 
    * {@link IterativeDeepening#evaluateTree(gametree.Tree, TreeEvaluator, boolean, int, int)}.
    * 
    * @param fen        the position to calculate the move to be plaed for
    * @param depth      the depth used by alpha-beta pruning
    * @param whitesTurn whether the move to be played is played by white
    */
    private void testEvaluatorEvaluateTreeConsistency(String fen, int depth, boolean whitesTurn) {
        Position pos1 = FenParser.parseFen(fen);
        GameTreeEvaluator evaluator1 = helper.instantiateTreeEvaluator();
        Move result1 = evaluator1.evaluateTree(new ImpGameTree(pos1, evaluator1), depth, whitesTurn).getRepresentedMove();

        Position pos2 = FenParser.parseFen(fen);
        GameTreeEvaluator evaluator2 = helper.instantiateTreeEvaluator();
        Move result2 = evaluator2.evaluateTree(new ImpGameTree(pos2, evaluator2), depth, whitesTurn).getRepresentedMove();

        IterativeDeepening<Position> iterativeDeepening = new IterativeDeepening<Position>();
        Position pos3 = FenParser.parseFen(fen);
        GameTreeEvaluator evaluator3 = helper.instantiateTreeEvaluator();
        iterativeDeepening.evaluateTree(new ImpGameTree(pos3, evaluator3), evaluator3, whitesTurn, -1, depth);
        Move result3 = Conductor.bestFollowUp.getMove();

        assertEquals(result1, result2);
        assertEquals(result2, result3);
        assertEquals(result1, result3);
    }

    /**
     * Calculates the next move for the given fen twice using
     * {@link ImpGameTree#calculateBestMove(int)}
     * and compares both results.
     * Also compares the result with that of 
     * {@link IterativeDeepening#evaluateTree(gametree.Tree, TreeEvaluator, boolean, int, int)}.
     * 
     * @param fen        the position to calculate the move to be plaed for
     * @param depth      the depth used by alpha-beta pruning
     * @param whitesTurn whether the move to be played is played by white
     */
    private void testTreeCalculateBestMoveConsistency(String fen, int depth, boolean whitesTurn) {
        Position pos1 = FenParser.parseFen(fen);
        Move result1 = new ImpGameTree(pos1, helper.instantiateTreeEvaluator()).calculateBestMove(depth).getRepresentedMove();
        Position pos2 = FenParser.parseFen(fen);
        Move result2 = new ImpGameTree(pos2, helper.instantiateTreeEvaluator()).calculateBestMove(depth).getRepresentedMove();

        IterativeDeepening<Position> iterativeDeepening = new IterativeDeepening<Position>();
        Position pos3 = FenParser.parseFen(fen);
        GameTreeEvaluator evaluator3 = helper.instantiateTreeEvaluator();
        iterativeDeepening.evaluateTree(new ImpGameTree(pos3, evaluator3), evaluator3, whitesTurn, -1, depth);
        Move result3 = Conductor.bestFollowUp.getMove();

        assertEquals(result1, result2);
        assertEquals(result2, result3);
        assertEquals(result1, result3);
    }

    /**
     * Calculates the next move for the given fen using
     * {@link ImpGameTree#calculateBestMove(int)}
     * and {@link TreeEvaluator#evaluateTree(gametree.Tree, int, boolean)}
     * and comapares both results.
     * Also compares the result with that of 
     * {@link IterativeDeepening#evaluateTree(gametree.Tree, TreeEvaluator, boolean, int, int)}.
     * 
     * @param fen        the position to calculate the move to be plaed for
     * @param depth      the depth used by alpha-beta pruning
     * @param whitesTurn whether the move to be played is played by white
     */
    private void testEvaluatorEvaluateTreeEqualsTreeCalculateBestMove(String fen, int depth, boolean whitesTurn) {

        Position pos1 = FenParser.parseFen(fen);
        GameTreeEvaluator evaluator = helper.instantiateTreeEvaluator();
        Move result1 = evaluator.evaluateTree(new ImpGameTree(pos1, evaluator), depth, whitesTurn).getRepresentedMove();

        Position pos2 = FenParser.parseFen(fen);
        Move result2 = new ImpGameTree(pos2, helper.instantiateTreeEvaluator()).calculateBestMove(depth).getRepresentedMove();

        IterativeDeepening<Position> iterativeDeepening = new IterativeDeepening<Position>();
        Position pos3 = FenParser.parseFen(fen);
        GameTreeEvaluator evaluator3 = helper.instantiateTreeEvaluator();
        iterativeDeepening.evaluateTree(new ImpGameTree(pos3, evaluator3), evaluator3, whitesTurn, -1, depth);
        Move result3 = Conductor.bestFollowUp.getMove();

        assertEquals(result1, result2);
        assertEquals(result2, result3);
        assertEquals(result1, result3);
    }

    @Test
    public void evaluatorEvaluateTreeConsistentTest1() {
        String fen = "k7/3r4/1p1b4/4n3/1R6/8/2N1P3/K7 b - - 0 1";
        testEvaluatorEvaluateTreeConsistency(fen, 3, false);
    }

    @Test
    public void evaluatorEvaluateTreeConsistentTest2() {
        String fen = "2K5/8/8/8/8/3RRR2/n2RRR2/2k5 w - - 0 1";
        testEvaluatorEvaluateTreeConsistency(fen, 3, true);
    }

    @Test
    public void treeCalculateBestMoveConsistentTest1() {
        String fen = "1r1b4/6k1/5n2/6p1/3N4/4B3/3R4/K7 w - - 0 1";
        testTreeCalculateBestMoveConsistency(fen, 3, true);
    }

    @Test
    public void treeCalculateBestMoveConsistentTest2() {
        String fen = "k7/6r1/8/3n1P2/8/1P3N1r/3NB3/K7 b - - 0 1";
        testTreeCalculateBestMoveConsistency(fen, 3, false);
    }

    @Test
    public void evaluatorEvaluateTreeEqualsTreeCalculateBestMoveTest1() {
        String fen = "2K5/8/8/8/8/3RRR2/n2RRR2/2k5 w - - 0 1";
        testEvaluatorEvaluateTreeEqualsTreeCalculateBestMove(fen, 3, true);
    }

    @Test
    public void evaluatorEvaluateTreeEqualsTreeCalculateBestMoveTest3() {
        String fen = "k7/2n5/3b1b2/3p4/8/2N3P1/4R3/K7 b - - 0 1";
        testEvaluatorEvaluateTreeEqualsTreeCalculateBestMove(fen, 3, false);
    }

    /**
     * apparently never fails
     */
    @Test
    public void evaluatorEvaluateTreeEqualsTreeCalculateBestMoveTestApparentlyWorkingFen() {
        String fen = "k7/8/6p1/3rR3/2n5/1P2Bb2/8/K7 b - - 0 1";
        testEvaluatorEvaluateTreeEqualsTreeCalculateBestMove(fen, 3, false);
    }

    @Test
    public void evaluatorEvaluateTreeConsistencyTestApparentlyWorkingFen() {
        String fen = "k7/8/6p1/3rR3/2n5/1P2Bb2/8/K7 b - - 0 1";
        testEvaluatorEvaluateTreeConsistency(fen, 3, false);
    }

    @Test
    public void treeCalculateBestMoveConsistencyTestApparentlyWorkingFen() {
        String fen = "k7/8/6p1/3rR3/2n5/1P2Bb2/8/K7 b - - 0 1";
        testTreeCalculateBestMoveConsistency(fen, 3, false);
    }

    @Test
    public void chaosTest() {
        String fen = "2K5/8/8/8/8/3RRR2/n2RRR2/2k5 w - - 0 1";
        int depth = 3;
        Position pos = FenParser.parseFen(fen);
        GameTreeEvaluator evaluatorA = helper.instantiateTreeEvaluator();
        GameNode faulty = evaluatorA.evaluateTree(new ImpGameTree(pos, evaluatorA), depth, true);

        pos = FenParser.parseFen(fen);
        GameTreeEvaluator evaluatorB = helper.instantiateTreeEvaluator();
        GameNode sensible = new ImpGameTree(pos, evaluatorB).calculateBestMove(depth);

        pos = FenParser.parseFen(fen);
        GameNode sensible2 = new ImpGameTree(pos, evaluatorB).calculateBestMove(depth);

        pos = FenParser.parseFen(fen);
        GameTreeEvaluator evaluatorC = helper.instantiateTreeEvaluator();
        GameNode sensible3 = new ImpGameTree(pos, evaluatorC).calculateBestMove(depth);

        assertEquals(sensible.getRepresentedMove(), sensible3.getRepresentedMove());
        assertEquals(sensible.getRepresentedMove(), sensible2.getRepresentedMove());
        assertEquals(sensible.getRepresentedMove(), faulty.getRepresentedMove());
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

}
