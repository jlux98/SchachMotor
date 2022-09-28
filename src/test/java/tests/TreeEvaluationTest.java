package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import data.IntNodeAsymmetricTestTree;
import data.IntNodeSmallAsymmetricTestTree;
import data.IntNodeWikipediaTestTree;
import helper.IntTreeEvaluationHelper;
import helper.MoveGeneratorHelper;
import helper.TreePrinter;
import helper.GameTreeEvaluationHelper;
import helper.IntNodeHelper;
import minimax.TreeEvaluator;
import model.Move;
import model.Position;
import uciservice.FenParser;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import gametree.*;

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
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(3, 4, true, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE, MIN_VALUE, MAX_VALUE,
                MAX_VALUE);
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
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(1, 4, false, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE, MIN_VALUE, MAX_VALUE,
                MAX_VALUE);
    }

    /**
     * tree data taken from https://youtu.be/l-hh51ncgDI?t=486
     */
    @Test
    public void lagueDepth4BinaryTreeExaggeratedDepthWhiteTest() {
        // nodes storing MIN_VALUE or MAX_VALUE are pruned regardless of their value due
        // to their siblings' values
        // they are assigned extreme values to verify that they do not affect the tree
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(3, 8, true, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE, MIN_VALUE, MAX_VALUE,
                MAX_VALUE);
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
        intTreeEvaluator.verifyTreeAndInvertedTreeBinary(1, 8, false, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE, MIN_VALUE, MAX_VALUE,
                MAX_VALUE);
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
    public void bishopCaptureDepth1Test() {
    GameNode bestMove = gameTreeEvaluator.evaluate("rn1qkbnr/pbpppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 1, false);
    System.out.println(bestMove);
    System.out.println(bestMove.getRepresentedMove().toStringAlgebraic());

    }

    @Test
    public void prepareBishopCaptureDepth2Test() throws ComputeChildrenException {
        GameNode bestMove = gameTreeEvaluator.assertBestMoveIn("rnbqkbnr/p1pppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", 3,
                false, "c8b7", "c8a6");
        assertEquals(-100, bestMove.getValue());

    }
}
