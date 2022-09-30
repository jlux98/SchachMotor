package tests;

import helper.IntTreeEvaluationHelper;
import minimax.StoringMoveOrderingSelfDestructingAlphaBetaPruning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import classes.DifferentValuesIntNode;
import gametree.ImpTree;
import gametree.Node;
import gametree.Tree;
import gametree.UninitializedValueException;

public class StoringMoveOrderingSelfDestructingAlphaBetaPruningIntTreeTest extends IntTreeEvaluationTest {

    public StoringMoveOrderingSelfDestructingAlphaBetaPruningIntTreeTest() {
        super(new IntTreeEvaluationHelper(() -> new StoringMoveOrderingSelfDestructingAlphaBetaPruning<Integer>()));
    }

    //TODO add tests verifying storage depth

    /**
     * Verifies that information is retained and used for move ordering over multiple iterations.
     * @throws UninitializedValueException
     */
    @Test
    public void orderByValuesOfPreviousIterationsBlackTest() throws UninitializedValueException {
        DifferentValuesIntNode root = new DifferentValuesIntNode(-1, 1);
        Tree<DifferentValuesIntNode> tree = new ImpTree<DifferentValuesIntNode>(root);

        DifferentValuesIntNode rootChildA = new DifferentValuesIntNode(1, 1);
        DifferentValuesIntNode rootChildB = new DifferentValuesIntNode(2, 2);

        DifferentValuesIntNode childAchild1 = new DifferentValuesIntNode(3, 2);

        root.insertChild(rootChildB); //insert higher node first (forces reordering)
        root.insertChild(rootChildA);

        rootChildA.insertChild(childAchild1);

        //layer 1 nodes are sorted by their static value after first search
        helper.evaluateTree(tree, 2, false); //whitesTurn false -> ascending ordering
        List<? extends Node<Integer>> children = root.getChildren();
        assertTrue(rootChildA == children.get(0));
        assertTrue(rootChildB == children.get(1));
        assertEquals(3, rootChildA.getValue()); //assumes child value
        assertEquals(2, rootChildB.getValue()); //evaluated as leaf

        //next search orders nodes by their final values of last search
        helper.evaluateTree(tree, 2, false);
        assertTrue(rootChildB == children.get(0));
        assertTrue(rootChildA == children.get(1));
        assertEquals(3, rootChildA.getValue()); //no change in values
        assertEquals(2, rootChildB.getValue()); //no change in values    
    }

    /**
     * Verifies that information is retained and used for move ordering over multiple iterations.
     * @throws UninitializedValueException
     */
    @Test
    public void orderByValuesOfPreviousIterationsWhiteTest() throws UninitializedValueException {
        DifferentValuesIntNode root = new DifferentValuesIntNode(-1, 1);
        Tree<DifferentValuesIntNode> tree = new ImpTree<DifferentValuesIntNode>(root);

        DifferentValuesIntNode rootChildA = new DifferentValuesIntNode(1, 1);
        DifferentValuesIntNode rootChildB = new DifferentValuesIntNode(2, 2);

        DifferentValuesIntNode childAchild1 = new DifferentValuesIntNode(3, 2);

        root.insertChild(rootChildA); //insert lower node first (forces reordering)
        root.insertChild(rootChildB);

        rootChildA.insertChild(childAchild1);

        //layer 1 nodes are sorted by their static value after first search
        helper.evaluateTree(tree, 2, true); //whitesTurn false -> ascending ordering
        List<? extends Node<Integer>> children = root.getChildren();
        assertTrue(rootChildB == children.get(0));
        assertTrue(rootChildA == children.get(1));
        assertEquals(3, rootChildA.getValue()); //assumes child value
        assertEquals(2, rootChildB.getValue()); //evaluated as leaf

        //next search orders nodes by their final values of last search
        helper.evaluateTree(tree, 2, true);
        assertTrue(rootChildA == children.get(0));
        assertTrue(rootChildB == children.get(1));
        assertEquals(3, rootChildA.getValue()); //no change in values
        assertEquals(2, rootChildB.getValue()); //no change in values    
    }
}
