package tests;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import data.IntNodeAsymmetricTestTree;
import helper.IntTreeEvaluationHelper;
import minimax.SelfDestructingAlphaBetaPruning;

public class SelfDestructingAlphaBetaIntTreeTest extends IntTreeEvaluationTest {
    public SelfDestructingAlphaBetaIntTreeTest() {
        super(new IntTreeEvaluationHelper(() -> new SelfDestructingAlphaBetaPruning<Integer>(), false));
    }

    @Test
    public void verifyDeletionWhiteTest() {
        IntNodeAsymmetricTestTree tree = new IntNodeAsymmetricTestTree();
        helper.evaluateTree(tree, 7, true);
        assertFalse(tree.getRoot().hasChildren());
    }

    @Test
    public void verifyDeletionBlackTest() {
        IntNodeAsymmetricTestTree tree = new IntNodeAsymmetricTestTree();
        helper.evaluateTree(tree, 7, false);
        assertFalse(tree.getRoot().hasChildren());
    }
}
