package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import classes.IntNode;
import gametree.Node;
import gametree.Tree;
import minimax.TreeEvaluator;
import tests.TreeEvaluationTest;

/**
 * Class used to help with testing {@link TreeEvaluator TreeEvaluators}.
 * This class offers an abstract factory method {@link #instantiateTreeEvaluator()}
 * which allows for testing various implementations of TreeEvaluators
 * while only writing tests once.
 * 
 * <p>
 * The class containing the tests just has to store an instance of TreeEvaluationHelper
 * and can then use it to instantiate TreeEvaluators as needed for testing.
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
public abstract class TreeEvaluationHelper {

    /**
     * Constructs an instance of the TreeEvaluator implementation that
     * should be tested (e.g. miniax, alpha-beta-pruning).
     * @return an instance of the TreeEvaluator
     */
    public abstract TreeEvaluator<Integer> instantiateTreeEvaluator();

    /**
     * Tests whether applying alpha-beta-pruning to a binary tree returns the
     * expected result.
     * 
     * @param expectedResult the expected result value
     * @param depth          the depth of the tree (a tree consisting only of a root
     *                       node has depth = 0)
     * @param whitesTurn     whether the turn to be played is played by white
     * @param values         the leaf nodes' values
     */
    public void testBinaryTree(int expectedResult, int depth, boolean whitesTurn, int... values) {
        testNaryTree(expectedResult, depth, 2, whitesTurn, values);
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
     * @param values         the leaf nodes' values
     */
    public void testNaryTree(int expectedResult, int depth, int degree, boolean whitesTurn, int... values) {
        Tree<IntNode> binaryTree = IntNodeHelper.createIntNodeTree(degree, values);
        TreeEvaluator<Integer> evaluator = instantiateTreeEvaluator();
        Node<Integer> bestMove = evaluator.evaluateTree(binaryTree, depth, whitesTurn);
        assertEquals(expectedResult, bestMove.getValue());
    }

    public Node<Integer> evaluateTree(Tree<? extends Node<Integer>> tree, int depth, boolean whitesTurn) {
        return instantiateTreeEvaluator().evaluateTree(tree, depth, whitesTurn);
    }
}
