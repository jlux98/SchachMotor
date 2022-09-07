package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import classes.EvaluableInteger;
import classes.IntNode;
import gametree.GenericAlphaBetaPruning;
import gametree.Node;
import gametree.Tree;

public class AlphaBetaHelper {

    /**
     * Tests whether applying alpha-beta-pruning to a binary tree returns the expected result.
     * @param expectedResult the expected result value
     * @param depth the depth of the tree (a tree consisting only of a root node has depth = 0)
     * @param whitesTurn whether the turn to be played is played by white
     * @param values the leaf nodes' values
     */
    public static void testBinaryTree(int expectedResult, int depth, boolean whitesTurn, int... values) {
        testNaryTree(expectedResult, depth, 2, whitesTurn, values);
    }

    /**
     * Tests whether applying alpha-beta-pruning to a tree returns the expected result.
     * @param expectedResult the expected result value
     * @param depth the depth of the tree (a tree consisting only of a root node has depth = 0)
     * @param degree the tree's degree (= the maximum amount of children a node may have)
     * @param whitesTurn whether the turn to be played is played by white
     * @param values the leaf nodes' values
     */
    public static void testNaryTree(int expectedResult, int depth, int degree, boolean whitesTurn, int ... values) {
        Tree<IntNode> binaryTree = IntNodeHelper.createIntNodeTree(degree, values);
        Node<EvaluableInteger> bestMove = new GenericAlphaBetaPruning<EvaluableInteger>().evaluateTree(binaryTree, depth, whitesTurn);
        assertEquals(expectedResult, bestMove.getContent().getValue());
    }
}
