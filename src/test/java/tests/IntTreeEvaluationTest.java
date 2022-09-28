package tests;

import org.junit.jupiter.api.Test;

import data.IntNodeAsymmetricTestTree;
import data.IntNodeSmallAsymmetricTestTree;
import data.IntNodeWikipediaTestTree;
import helper.IntNodeHelper;
import helper.IntTreeEvaluationHelper;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

 /**
 * This abstract class implements test for TreeEvaluators such as minimax
 * or alpha-beta-pruning that are applied to nodes storing integers.
 * <p>
 * As this class is abstract its tests cannot be run directly.
 * To apply the tests to an implementation of a TreeEvaluator,
 * extend this class and pass a suitable TreeEvaluationHelper to its constructor.
 * </p>
 * <p>
 * A suitable instance of {@link IntTreeEvaluationHelper} can be created by providing a
 * <pre> Supplier&lt;TreeEvaluator&lt;Integer&gt;&gt;</pre>  to its constructor. 
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
 *                  () -> new GenericAlphaBetaPruning&lt;Integer&gt;()));
 *     }
 * }
 * 
 * </pre>
 */
public abstract class IntTreeEvaluationTest {

    protected IntTreeEvaluationHelper intTreeEvaluator;

    /**
    * Constructs a new Test instance using the passed TreeEvaluationHelper
    * to instantiate the {@link TreeEvaluator} to be tested.
    * @param intTreeEvaluator the IntTreeEvaluationHelper used to execute these tests
    */
    public IntTreeEvaluationTest(IntTreeEvaluationHelper intTreeEvaluator) {
        this.intTreeEvaluator = intTreeEvaluator;
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
}
