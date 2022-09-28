package tests;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import application.Conductor;
import data.IntNodeAsymmetricTestTree;
import data.IntNodeSmallAsymmetricTestTree;
import data.IntNodeWikipediaTestTree;
import gametree.ComputeChildrenException;
import gametree.GameNode;
import gametree.ImpGameTree;
import helper.GameTreeEvaluationHelper;
import helper.IntNodeHelper;
import helper.IntTreeEvaluationHelper;
import helper.Mirror;
import minimax.GameNodeMiniMax;
import minimax.TreeEvaluator;
import model.Move;
import model.Position;
import uciservice.FenParser;

/**
 * This abstract class implements test for TreeEvaluators such as minimax
 * or alpha-beta-pruning. 
 * As this class is abstract its tests cannot be run directly.
 * <p>
 * To apply the tests to an implementation of a TreeEvaluator,
 * extend this class and pass a suitable TreeEvaluationHelper to its constructor.
 * </p>
 * <p>
 * A suitable instance of {@link IntTreeEvaluationHelper} can be created by providing a
 * <pre> Supplier&lt;TreeEvaluator&lt;Integer&gt;&gt;</pre>
 * and a <pre> Supplier&lt;GameTreeEvaluator&gt;</pre>
 *  to its constructor. 
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
 *                  () -> new GenericAlphaBetaPruning&lt;Integer&gt;()),
 *               new GameTreeEvaluationHelper(
 *                  () -> new GameNodeAlphaBetaPruning())
 *               );
 *     }
 * }
 * 
 * </pre>
 */
public abstract class TreeEvaluationTest {

    protected IntTreeEvaluationHelper intTreeEvaluator;
    protected GameTreeEvaluationHelper gameTreeEvaluator;

    /**
     * Constructs a new Test instance using the passed TreeEvaluationHelper
     * to instantiate the {@link TreeEvaluator} to be tested.
     * @param evaluator the evaluator used to execute these tests
     */
    public TreeEvaluationTest(IntTreeEvaluationHelper intTreeEvaluator, GameTreeEvaluationHelper gameTreeEvaluator) {
        this.intTreeEvaluator = intTreeEvaluator;
        this.gameTreeEvaluator = gameTreeEvaluator;
    }

    /**
     * tree data taken from https://en.wikipedia.org/wiki/File:Minimax.svg
     */
    // FIXME this test assumes that no pruning is happening whatsoever
    @Test
    public void incompleteBinaryTreeDepth4Test() {
        IntNodeWikipediaTestTree testTree = new IntNodeWikipediaTestTree();
        intTreeEvaluator.evaluateTree(testTree, 4, true);
        // root
        IntNodeHelper.compareIntNodeValue(-7, testTree.root);
        // layer1
        IntNodeHelper.compareIntNodeValue(-10, testTree.layer1Node0);
        IntNodeHelper.compareIntNodeValue(-7, testTree.layer1Node1);
        // layer2
        IntNodeHelper.compareIntNodeValue(10, testTree.layer2Node0);
        IntNodeHelper.compareIntNodeValue(-10, testTree.layer2Node1);
        IntNodeHelper.compareIntNodeValue(5, testTree.layer2Node2);
        IntNodeHelper.compareIntNodeValue(-7, testTree.layer2Node3);
        // layer3
        IntNodeHelper.compareIntNodeValue(10, testTree.layer3Node0);
        IntNodeHelper.compareIntNodeValue(5, testTree.layer3Node1);
        IntNodeHelper.compareIntNodeValue(-10, testTree.layer3Node2);
        IntNodeHelper.compareIntNodeValue(5, testTree.layer3Node3);
        IntNodeHelper.compareIntNodeValue(MIN_VALUE, testTree.layer3Node4);
        IntNodeHelper.compareIntNodeValue(-7, testTree.layer3Node5);
        // assure that leaf layer was not changed
        IntNodeHelper.compareIntNodeValue(10, testTree.layer4Node0);
        IntNodeHelper.compareIntNodeValue(MAX_VALUE, testTree.layer4Node1);
        IntNodeHelper.compareIntNodeValue(5, testTree.layer4Node2);
        IntNodeHelper.compareIntNodeValue(-10, testTree.layer4Node3);
        IntNodeHelper.compareIntNodeValue(7, testTree.layer4Node4);
        IntNodeHelper.compareIntNodeValue(5, testTree.layer4Node5);
        IntNodeHelper.compareIntNodeValue(MIN_VALUE, testTree.layer4Node6);
        IntNodeHelper.compareIntNodeValue(-7, testTree.layer4Node7);
        IntNodeHelper.compareIntNodeValue(-5, testTree.layer4Node8);

        intTreeEvaluator.verifyTreeAndInvertedTree(-7, 4, true, () -> new IntNodeWikipediaTestTree());
    }

    /**
     * tree data taken from https://youtu.be/l-hh51ncgDI?t=145
     */
    @Test
    public void lagueDepth3BinaryTreeWhiteTest() {
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(3, 3, true, -1, 3, 5, 1, -6, -4, 0, 9);
    }

    /**
     * like {@link #lagueDepth3BinaryTreeWhiteTest()}
     */
    @Test
    public void lagueDepth3BinaryTreeBlackTest() {
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(0, 3, false, -1, 3, 5, 1, -6, -4, 0, 9);
    }

    /**
     * tree data taken from https://youtu.be/l-hh51ncgDI?t=145.
     * Same test data as {@link #lagueDepth3BinaryTreeTest()} but with a depth that
     * exceeds the actual depth of the tree.
     * Alpha-beta pruning is expected to handle this as trees are not guaranteed to
     * be complete.
     */
    @Test
    public void lagueDepth3binaryTreeExaggeratedDepthWhiteTest() {
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(3, 4, true, -1, 3, 5, 1, -6, -4, 0, 9);
    }

    /**
     * like {@link #lagueDepth3binaryTreeExaggeratedDepthWhiteTest()} but with black
     * being the active player
     */
    @Test
    public void lagueDepth3binaryTreeExaggeratedDepthBlackTest() {
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(0, 4, false, -1, 3, 5, 1, -6, -4, 0, 9);
    }

    /**
     * tree data taken from https://youtu.be/l-hh51ncgDI?t=486
     */
    @Test
    public void lagueDepth4BinaryTreeWhiteTest() {
        // nodes storing MIN_VALUE or MAX_VALUE are pruned regardless of their value due
        // to their siblings' values
        // they are assigned extreme values to verify that they do not affect the tree
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(3, 4, true, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE,
                MIN_VALUE, MAX_VALUE, MAX_VALUE);
    }

    /**
     * like {@link #lagueDepth4BinaryTreeWhiteTest()} but with black being the
     * active player
     */
    @Test
    public void lagueDepth4BinaryTreeBlackTest() {
        // nodes storing MIN_VALUE or MAX_VALUE are pruned regardless of their value due
        // to their siblings' values
        // they are assigned extreme values to verify that they do not affect the tree
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(1, 4, false, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE,
                MIN_VALUE, MAX_VALUE, MAX_VALUE);
    }

    /**
     * tree data taken from https://youtu.be/l-hh51ncgDI?t=486
     */
    @Test
    public void lagueDepth4BinaryTreeExaggeratedDepthWhiteTest() {
        // nodes storing MIN_VALUE or MAX_VALUE are pruned regardless of their value due
        // to their siblings' values
        // they are assigned extreme values to verify that they do not affect the tree
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(3, 8, true, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE,
                MIN_VALUE, MAX_VALUE, MAX_VALUE);
    }

    /**
     * like {@link #lagueDepth4BinaryTreeExaggeratedDepthWhiteTest()} but with black
     * being the active player
     */
    @Test
    public void lagueDepth4BinaryTreeExaggeratedDepthBlackTest() {
        // nodes storing MIN_VALUE or MAX_VALUE are pruned regardless of their value due
        // to their siblings' values
        // they are assigned extreme values to verify that they do not affect the tree
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(1, 8, false, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE,
                MIN_VALUE, MAX_VALUE, MAX_VALUE);
    }

    /**
     * tree data taken from https://www.javatpoint.com/mini-max-algorithm-in-ai
     */

    @Test
    public void javatpointBinaryTreeDepth3WhiteTest() {
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(4, 3, true, -1, 4, 2, 6, -3, -5, 0, 7);
    }

    /**
     * like {@link #javatpointBinaryTreeDepth3WhiteTest()} but with black being the
     * active player
     */
    @Test
    public void javatpointBinaryTreeDepth3BlackTest() {
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(0, 3, false, -1, 4, 2, 6, -3, -5, 0, 7);
    }

    /**
     * tree data taken from https://www.javatpoint.com/mini-max-algorithm-in-ai
     */
    @Test
    public void javatpointBinaryTreeDepth3ExaggeratedDepthWhiteTest() {
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(4, 4, true, -1, 4, 2, 6, -3, -5, 0, 7);
    }

    /**
     * like {@link #javatpointBinaryTreeDepth3ExaggeratedDepthWhiteTest()} but with
     * black being the active player
     */
    @Test
    public void javatpointBinaryTreeDepth3ExaggeratedDepthBlackTest() {
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(0, 4, false, -1, 4, 2, 6, -3, -5, 0, 7);
    }

    @Test
    public void asymmetricTestTreeWhiteTest() {
        intTreeEvaluator.verifyTreeAndInvertedTree(23, 7, true, () -> new IntNodeAsymmetricTestTree());
    }

    @Test
    public void asymmetricTestTreeBlackTest() {
        intTreeEvaluator.verifyTreeAndInvertedTree(-20, 7, false, () -> new IntNodeAsymmetricTestTree());
    }

    @Test
    public void smallAsymmetricTestTreeWhiteTest() {
        intTreeEvaluator.verifyTreeAndInvertedTree(3, 4, true, () -> new IntNodeSmallAsymmetricTestTree());
    }

    @Test
    public void smallAsymmetricTestTreeBlackTest() {
        intTreeEvaluator.verifyTreeAndInvertedTree(-6, 4, false, () -> new IntNodeSmallAsymmetricTestTree());
    }
    // TODO add test with more extensive pruning (black)
    // TODO add test with more extensive pruning (white)

    //TODO move game tree code to a new class GameTreeEvaluationTest, also move gameTreeEvaluator-supplier there
    //TODO apply mirroring to these tests
    @Test
    public void findCheckMateBlack() {
        List<Move> expected = new ArrayList<Move>();
        expected.add(new Move("d2d1"));
        expected.add(new Move("e2e1"));
        expected.add(new Move("f2f1"));
        Position position = FenParser.parseFen("2k5/8/8/8/8/3rrr2/N2rrr2/2K5 b - - 1 1");
        // Move actual = new ImpGameTree(position, new GameNodeAlphaBetaPruning()).calculateBestMove(1).getContent().getMove();
        // assertTrue(expected.contains(actual));
        Move actual = new ImpGameTree(position, gameTreeEvaluator.instantiateTreeEvaluator()).calculateBestMove(3).getContent()
                .getMove();
        Conductor.getPastPositions();
        assertTrue(expected.contains(actual));
        actual = new ImpGameTree(position, gameTreeEvaluator.instantiateTreeEvaluator()).calculateBestMove(5).getContent()
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
        Move actual = new ImpGameTree(position, gameTreeEvaluator.instantiateTreeEvaluator()).calculateBestMove(3).getContent()
                .getMove();
        assertTrue(expected.contains(actual));
        actual = new ImpGameTree(position, gameTreeEvaluator.instantiateTreeEvaluator()).calculateBestMove(5).getContent()
                .getMove();
        assertTrue(expected.contains(actual));
    }

    @Test
    public void findStaleMate() {
        Move expected = new Move("d1c1");
        Position position = FenParser.parseFen("5K2/8/8/8/5B2/3RPR2/1R6/3k4 b - - 0 1");
        Move actual = new ImpGameTree(position, gameTreeEvaluator.instantiateTreeEvaluator()).calculateBestMove(3).getContent()
                .getMove();
        assertEquals(expected, actual);
        actual = new ImpGameTree(position, gameTreeEvaluator.instantiateTreeEvaluator()).calculateBestMove(5).getContent()
                .getMove();
        assertEquals(expected, actual);
    }

    @Test
    public void conductorHistoryEmptyAfterMovegen(){
        assertEquals(0, Conductor.getPastPositions().size());
        new ImpGameTree(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"),
        gameTreeEvaluator.instantiateTreeEvaluator()).calculateBestMove(3);
        assertEquals(0, Conductor.getPastPositions().size());
    }
    public void zugzwangStallTest() throws ComputeChildrenException {
        //FIXME c7c8 is fine
        GameNode node = gameTreeEvaluator.assertBestMoveNotIn("1q1k4/2Rr4/8/2Q3K1/8/8/8/8 w - - 0 1", 5, true, "c7c8", "c5f8");
    }

    @Test
    public void bestMoveTest() throws ComputeChildrenException {

        //FIXME d7d5 is fine
        //GameNode node = gameTreeEvaluator.assertBestMoveIn("1q1k4/2Rr4/8/2Q3K1/8/8/8/8 b - - 0 1", 5, false, "d7d5");
        GameNode node = gameTreeEvaluator.evaluate("1q1k4/2Rr4/8/2Q3K1/8/8/8/8 b - - 0 1", 5, false);
        System.out.println(node.getRepresentedMove().toStringAlgebraic());
        
    }

    @Test
    public void Test() throws ComputeChildrenException {
        GameTreeEvaluationHelper minimax = new GameTreeEvaluationHelper(() -> new GameNodeMiniMax());
        minimax.assertBestMoveIn("1q1k4/2Rr4/8/2Q3K1/8/8/8/8 b - - 0 1", 4, false, "d7c7", "d7d5");
        minimax.assertBestMoveNotIn("1q1k4/2Rr4/8/2Q3K1/8/8/8/8 w - - 0 1", 4, true, "c7c8");
    }

    @Test
    public void firstMoveDepth3Test() {
        GameNode whiteMove = gameTreeEvaluator.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 3, true);
        GameNode blackMove = gameTreeEvaluator.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 3, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }

    @Test
    public void firstMoveDepth4Test() {
        GameNode whiteMove = gameTreeEvaluator.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 4, true);
        GameNode blackMove = gameTreeEvaluator.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 4, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }

    @Test
    public void firstMoveDepth5Test() {
        GameNode whiteMove = gameTreeEvaluator.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 5, true);
        GameNode blackMove = gameTreeEvaluator.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 5, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }

    @Test
    public void firstMoveDepth6Test() {
        GameNode whiteMove = gameTreeEvaluator.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 6, true);
        GameNode blackMove = gameTreeEvaluator.evaluate("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", 6, false);
        assertEquals(whiteMove.getValue(), -blackMove.getValue());
        assertEquals(whiteMove.getRepresentedMove(), Mirror.mirrorMove(blackMove.getRepresentedMove()));
    }

    @Test
    public void bishopCaptureDepth1BlackTest() {
        GameNode bestMove = gameTreeEvaluator.evaluate("rn1qkbnr/pbpppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 1, false);
        assertEquals(-100, bestMove.getValue());
    }

    @Test
    public void bishopCaptureDepth1WhiteTest() {
        GameNode bestMove = gameTreeEvaluator.evaluate("1rbqkbnr/pppppppp/n7/8/8/1P6/PBPPPPPP/RN1QKBNR w KQk - 0 1", 1, true);
        assertEquals(100, bestMove.getValue());
    }

    @Test
    public void bishopCaptureDepth2WhiteTest() {
        GameNode bestMove = gameTreeEvaluator.evaluate("1rbqkbnr/pppppppp/n7/8/8/1P6/PBPPPPPP/RN1QKBNR w KQk - 0 1", 2, true);
        assertEquals(100, bestMove.getValue());
    }

    @Test
    public void bishopCaptureDepth2BlackTest() throws ComputeChildrenException {
        GameNode bestMove = gameTreeEvaluator.evaluate("rn1qkbnr/pbpppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 2, false);
        assertEquals(-100, bestMove.getValue());

    }

    @Test
    public void prepareBishopCaptureDepth3WhiteTest() throws ComputeChildrenException {
        GameNode bestMove = gameTreeEvaluator.evaluate("r1bqkbnr/pppppppp/n7/8/8/1P6/P1PPPPPP/NRBQKBNR w Kkq - 0 1", 3, true);
        assertEquals(100, bestMove.getValue());
    }

    @Test
    public void prepareBishopCaptureDepth3BlackTest() throws ComputeChildrenException {
        GameNode bestMove = gameTreeEvaluator.assertBestMoveIn("rnbqkbnr/p1pppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 3,
                false, "c8b7", "c8a6");
        assertEquals(-100, bestMove.getValue());
    }

    @Test
    public void prepareBishopCaptureDepth4WhiteTest() throws ComputeChildrenException {
        GameNode bestMove = gameTreeEvaluator.evaluate("r1bqkbnr/pppppppp/n7/8/8/1P6/P1PPPPPP/NRBQKBNR w Kkq - 0 1", 4, true);
        assertEquals(100, bestMove.getValue());
    }

    @Test
    public void prepareBishopCaptureDepth4BlackTest() throws ComputeChildrenException {
        GameNode bestMove = gameTreeEvaluator.evaluate("rnbqkbnr/p1pppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 4, false);
        assertEquals(-100, bestMove.getValue());
    }

    @Test
    public void prepareBishopCaptureDepth5WhiteTest() throws ComputeChildrenException {
        GameNode bestMove = gameTreeEvaluator.evaluate("r1bqkbnr/pppppppp/n7/8/8/1P6/P1PPPPPP/NRBQKBNR w Kkq - 0 1", 5, true);
        assertEquals(100, bestMove.getValue());
    }

    @Test
    public void prepareBishopCaptureDepth5BlackTest() throws ComputeChildrenException {
        GameNode bestMove = gameTreeEvaluator.evaluate("rnbqkbnr/p1pppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 5, false);
        assertEquals(-100, bestMove.getValue());
    }

    @Test
    public void prepareBishopCaptureDepth6WhiteTest() throws ComputeChildrenException {
        GameNode bestMove = gameTreeEvaluator.evaluate("r1bqkbnr/pppppppp/n7/8/8/1P6/P1PPPPPP/NRBQKBNR w Kkq - 0 1", 6, true);
        assertEquals(100, bestMove.getValue());
    }

    @Test
    public void prepareBishopCaptureDepth6BlackTest() throws ComputeChildrenException {
        GameNode bestMove = gameTreeEvaluator.evaluate("rnbqkbnr/p1pppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 6, false);
        assertEquals(-100, bestMove.getValue());
    }
}
