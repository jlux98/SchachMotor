package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;

import classes.IntNode;
import gametree.Node;
import gametree.Tree;
import minimax.TreeEvaluator;
import tests.TreeEvaluationTest;

/**
 * Class used to help with testing instances of {@link TreeEvaluator TreeEvaluator < Integer >}.
 * This class offers a factory method {@link #instantiateTreeEvaluator()}
 * which allows for testing various implementations of TreeEvaluators
 * while only writing tests once.
 * 
 * <p>
 * The class containing the tests just has to store an instance of TreeEvaluationHelper
 * and can then use it to instantiate TreeEvaluators as needed for testing.
 * </p>
 * 
 * <p>
 * This class can be instantiated by providing its constructor with a suitable lambda expression,
 * for example: 
 * <pre>new IntTreeEvaluationHelper(() -> new GenericAlphaBetaPruning < Integer > ())</pre>
 * </p>
 * 
 * <p>
 * This class offers some additional methods useful in testing TreeEvaluators.
 * </p>
 * 
 * <p>
 * See the documentation of {@link TreeEvaluationTest} for a code example.
 * </p>
 */
public class IntTreeEvaluationHelper {

    private Supplier<TreeEvaluator<Integer>> treeEvaluatorSupplier;

    public IntTreeEvaluationHelper(Supplier<TreeEvaluator<Integer>> treeEvaluatorSupplier) {
        this.treeEvaluatorSupplier = treeEvaluatorSupplier;
    }

    /**
     * Constructs an instance of the TreeEvaluator implementation that
     * should be tested (e.g. miniax, alpha-beta-pruning).
     * @return an instance of the TreeEvaluator
     */
    public TreeEvaluator<Integer> instantiateTreeEvaluator() {
        return treeEvaluatorSupplier.get();
    }

    /**
     * Tests whether applying alpha-beta-pruning to a binary tree returns the
     * expected result.
     * 
     * @param expectedResult the expected result value
     * @param depth          the depth of the tree (a tree consisting only of a root
     *                       node has depth = 0)
     * @param whitesTurn     whether the turn to be played is played by white
     * @param leafValues         the leaf nodes' values
     */
    public void testBinaryTree(int expectedResult, int depth, boolean whitesTurn, int... leafValues) {
        testNaryTree(expectedResult, depth, 2, whitesTurn, leafValues);
    }

    /**
     * Tests whether applying alpha-beta-pruning to a tree returns the expected
     * result.
     * 
     * @param expectedResult the expected result value
     * @param depth          the depth of the tree (a tree consisting only of a root
     *                       node has depth = 0)
     * @param degree         the tree's degree (= the maximum amount of children a
     *                       node may have)
     * @param whitesTurn     whether the turn to be played is played by white
     * @param leafValues         the leaf nodes' values
     */
    public void testNaryTree(int expectedResult, int depth, int degree, boolean whitesTurn, int... leafValues) {
        Tree<IntNode> binaryTree = IntNodeHelper.createIntNodeTree(degree, leafValues);
        verifyEvaluateTree(expectedResult, binaryTree, depth, whitesTurn);
    }

    /**
     * Evaluates the tree using the TreeEvaluator provided by {@link IntTreeEvaluationHelper}.
     * @param tree the tree to be evaluated
     * @param depth the depth to which the tree should be evaluated
     * @param whitesTurn whether the node searched for is played by white
     * @return the node that should be played
     */
    public Node<Integer> evaluateTree(Tree<? extends Node<Integer>> tree, int depth, boolean whitesTurn) {
        return instantiateTreeEvaluator().evaluateTree(tree, depth, whitesTurn);
    }

    /**
     * Evaluates the tree using the TreeEvaluator provided by {@link IntTreeEvaluationHelper}
     * and asserts that evaluateTree(tree) returns a node with the expected value.
     * Returns the number of nodes that were evaluated.
     * @param expectedResult the value of the node expected to be returned by evaluateTree()
     * @param tree the tree to be evaluated
     * @param depth the depth to which the tree should be evaluated
     * @param whitesTurn whether the node searched for is played by white
     * @return the number of nodes that were evaluated
     */
    public int verifyEvaluateTree(int expectedResult, Tree<? extends Node<Integer>> tree, int depth, boolean whitesTurn) {
        TreeEvaluator<Integer> evaluator = instantiateTreeEvaluator();
        int result = evaluator.evaluateTree(tree, depth, whitesTurn).getValue();
        assertEquals(expectedResult, result);
        return evaluator.getEvaluatedNodeCount();
    }

    /**
     * Evaluates the tree using the TreeEvaluator provided by {@link IntTreeEvaluationHelper}.
     * Asserts that evaluateTree(tree) returns a node with the expected value.
     * Asserts that the number of evaluated nodes is equal to or less than maxEvaluatedNodeCount.
     * @param expectedResult the value of the node expected to be returned by evaluateTree()
     * @param maxEvaluatedNodeCount the maximum number of nodes that may be evaluated
     * @param tree the tree to be evaluated
     * @param depth the depth to which the tree should be evaluated
     * @param whitesTurn whether the node searched for is played by white
     */
    public void verifyEvaluateTree(int expectedResult, int maxEvaluatedNodeCount, Tree<? extends Node<Integer>> tree, int depth,
            boolean whitesTurn) {
        int evaluatedNodeCount = verifyEvaluateTree(expectedResult, tree, depth, whitesTurn);
        assertTrue(evaluatedNodeCount <= maxEvaluatedNodeCount);
    }

    /**
     * Evaluates the tree using the TreeEvaluator provided by {@link IntTreeEvaluationHelper}
     * and asserts that evaluateTree(tree) returns a node with the expected value.
     * Additionally, inverts the the tree's static leaf values (multiplies them by -1) and asserts 
     * that evaluation of the inverted tree for the opposing player returns -expectedResult.
     * <p>
     * This method requires a supplier to construct the tree twice - 
     * once for the initial test, and once for the inverted test.
     * The supplier has to always provide a new but equal tree.
     * <p>
     * Example invocation: 
     * <pre>intTreeEvaluator.verifyTreeAndInvertedTree(0,  4, false, 
     *                       (() -> IntNodeHelper.createIntNodeTree(2,-1, 3, 5, 1, -6, -4, 0, 9)
     *                        ));</pre>
     * @param expectedResult  the value of the node expected to be returned by evaluateTree()
     * @param depth the depth to which the tree should be evaluated
     * @param whitesTurn whether the node searched for is played by white
     * @param treeSupplier a supplier providing equal but new instances of a Tree < Integer >
     * @see #verifyTreeAndInvertedTree(int, int, int, boolean, int...)
     * @see #verifyTreeAndInvertedTreeBinary(int, int, boolean, int...)
     */
    public void verifyTreeAndInvertedTree(int expectedResult, int depth, boolean whitesTurn,
            Supplier<Tree<? extends Node<Integer>>> treeSupplier) {
        //verify evaluation of tree
        verifyEvaluateTree(expectedResult, treeSupplier.get(), depth, whitesTurn);
        //verify evaluation of tree for opposing player
        Tree<? extends Node<Integer>> invertedTree = treeSupplier.get();
        IntNodeHelper.invertLeaves(invertedTree);
        verifyEvaluateTree(-1 * expectedResult, invertedTree, depth, !whitesTurn);
    }

    /**
    * Builds a tree with the specified degree and leaf values and tests its evaluation as described by
    * {@link #verifyTreeAndInvertedTree(int, int, boolean, Supplier)}.
    * @param leafValues the leaf nodes' values
    */
    public void verifyTreeAndInvertedTree(int expectedResult, int depth, int degree, boolean whitesTurn, int... leafValues) {
        verifyTreeAndInvertedTree(expectedResult, depth, whitesTurn, () -> IntNodeHelper.createIntNodeTree(degree, leafValues));
    }

    /**
    * Builds a binary tree with the specified leaf values and tests its evaluation as described by
    * {@link #verifyTreeAndInvertedTree(int, int, boolean, Supplier)}.
    * @param leafValues the leaf nodes' values
    */
    public void verifyTreeAndInvertedTreeBinary(int expectedResult, int depth, boolean whitesTurn, int... leafValues) {
        verifyTreeAndInvertedTree(expectedResult, depth, whitesTurn, () -> IntNodeHelper.createIntNodeTree(2, leafValues));
    }

}
