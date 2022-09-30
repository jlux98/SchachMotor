package tests;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import data.IntNodeAsymmetricTestTree;
import helper.IntTreeEvaluationHelper;
import minimax.MoveOrderingSelfDestructingAlphaBetaPruning;

public class MoveOrderingSelfDestructingAlphaBetaPruningIntTreeTest extends IntTreeEvaluationTest {

    public MoveOrderingSelfDestructingAlphaBetaPruningIntTreeTest() {
        super(new IntTreeEvaluationHelper(() -> new MoveOrderingSelfDestructingAlphaBetaPruning<Integer>(), false));
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
