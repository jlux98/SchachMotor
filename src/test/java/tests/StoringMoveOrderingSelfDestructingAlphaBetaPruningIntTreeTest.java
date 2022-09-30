package tests;

import helper.IntNodeHelper;
import helper.IntTreeEvaluationHelper;
import minimax.StoringMoveOrderingSelfDestructingAlphaBetaPruning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import classes.DifferentValuesIntNode;
import classes.IntNode;
import data.IntNodeAsymmetricTestTree;
import data.IntNodeSmallAsymmetricTestTree;
import data.IntNodeWikipediaTestTree;
import gametree.ImpTree;
import gametree.Node;
import gametree.Tree;
import gametree.UninitializedValueException;

public class StoringMoveOrderingSelfDestructingAlphaBetaPruningIntTreeTest extends IntTreeEvaluationTest {

    public StoringMoveOrderingSelfDestructingAlphaBetaPruningIntTreeTest() {
        super(new IntTreeEvaluationHelper(() -> new StoringMoveOrderingSelfDestructingAlphaBetaPruning<Integer>(2), false));
    }

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

    // storage depth tests need to search the tree in full depth
    // otherwise children of the last searched layer will not be deleted
    // this is because self destructing tree evaluators are not intended to be applied
    // to "pre-build" trees and do not delete the children of nodes that
    // are deemed to be leaves due to depth (rather than due to not having/generating children)
    @Test
    public void verifyStorageDepthWikipediaTreeWhiteTest() throws UninitializedValueException {
        IntNodeWikipediaTestTree tree = new IntNodeWikipediaTestTree();
        helper.evaluateTree(tree, 4, true);

        assertTrue(tree.root.hasChildren());

        assertTrue(tree.layer1Node0.hasChildren());
        assertTrue(tree.layer1Node1.hasChildren());

        assertFalse(tree.layer2Node0.hasChildren());
        assertFalse(tree.layer2Node1.hasChildren());
        assertFalse(tree.layer2Node2.hasChildren());
        assertFalse(tree.layer2Node3.hasChildren());
    }

    @Test
    public void verifyStorageDepthWikipediaTreeBlackTest() {
        IntNodeWikipediaTestTree tree = new IntNodeWikipediaTestTree();
        helper.evaluateTree(tree, 4, false);

        assertTrue(tree.root.hasChildren());

        assertTrue(tree.layer1Node0.hasChildren());
        assertTrue(tree.layer1Node1.hasChildren());

        assertFalse(tree.layer2Node0.hasChildren());
        assertFalse(tree.layer2Node1.hasChildren());
        assertFalse(tree.layer2Node2.hasChildren());
        assertFalse(tree.layer2Node3.hasChildren());
    }

    @Test
    public void verifyStorageDepthAsymmetricTreeWhiteTest() throws UninitializedValueException {
        IntNodeAsymmetricTestTree tree = new IntNodeAsymmetricTestTree();

        helper.evaluateTree(tree, 7, true);

        assertFalse(tree.layer2Node0.hasChildren());
        assertFalse(tree.layer2Node1.hasChildren());
        assertFalse(tree.layer2Node2.hasChildren());
        assertFalse(tree.layer2Node3.hasChildren());
        assertFalse(tree.layer2Node4.hasChildren());
        IntNodeHelper.assertHasNoChildrenOrWasPruned(tree.layer2Node5);

        assertFalse(tree.layer2Node6.hasChildren());
        IntNodeHelper.assertHasNoChildrenOrWasPruned(tree.layer2Node7);
        assertFalse(tree.layer2Node8.hasChildren());

        assertTrue(tree.layer1Node0.hasChildren());
        assertTrue(tree.layer1Node1.hasChildren());
        // layer1Node2 does not have children
        assertTrue(tree.layer1Node3.hasChildren());
        assertTrue(tree.layer1Node4.hasChildren());
        assertTrue(tree.root.hasChildren());
    }

    @Test
    public void verifyStorageDepthAsymmetricTreeBlackTest() {
        IntNodeAsymmetricTestTree tree = new IntNodeAsymmetricTestTree();

        helper.evaluateTree(tree, 7, false);

        IntNodeHelper.assertHasNoChildrenOrWasPruned(tree.layer2Node0);
        assertFalse(tree.layer2Node1.hasChildren());
        IntNodeHelper.assertHasNoChildrenOrWasPruned(tree.layer2Node2);
        assertFalse(tree.layer2Node3.hasChildren());
        assertFalse(tree.layer2Node4.hasChildren());
        IntNodeHelper.assertHasNoChildrenOrWasPruned(tree.layer2Node5);
        assertFalse(tree.layer2Node6.hasChildren());
        assertFalse(tree.layer2Node7.hasChildren());
        assertFalse(tree.layer2Node8.hasChildren());

        assertTrue(tree.layer1Node0.hasChildren());
        assertTrue(tree.layer1Node1.hasChildren());
        // layer1Node2 does not have children
        assertTrue(tree.layer1Node3.hasChildren());
        assertTrue(tree.layer1Node4.hasChildren());
        assertTrue(tree.root.hasChildren());
    }

    @Test
    public void verifyStorageDepthSmallAsymmetricTreeWhiteTest() {
        IntNodeSmallAsymmetricTestTree tree = new IntNodeSmallAsymmetricTestTree();
        helper.evaluateTree(tree, 4, true);

        assertFalse(tree.layer2Node0.hasChildren());
        assertFalse(tree.layer2Node1.hasChildren());
        assertFalse(tree.layer2Node2.hasChildren());
        assertFalse(tree.layer2Node3.hasChildren());
        assertFalse(tree.layer2Node4.hasChildren());
        assertFalse(tree.layer2Node5.hasChildren());
        assertFalse(tree.layer2Node6.hasChildren());
        assertFalse(tree.layer2Node7.hasChildren());
        assertFalse(tree.layer2Node8.hasChildren());
        assertFalse(tree.layer2Node9.hasChildren());
        IntNodeHelper.assertHasNoChildrenOrWasPruned(tree.layer2Node10);
        assertFalse(tree.layer2Node11.hasChildren());
        assertFalse(tree.layer2Node12.hasChildren());

        assertTrue(tree.layer1Node0.hasChildren());
        assertTrue(tree.layer1Node1.hasChildren());
        assertTrue(tree.layer1Node2.hasChildren());
        assertTrue(tree.layer1Node3.hasChildren());
        assertTrue(tree.layer1Node4.hasChildren());

        assertTrue(tree.root.hasChildren());

    }

    @Test
    public void verifyStorageDepthSmallAsymmetricTreeBlackTest() {

        IntNodeSmallAsymmetricTestTree tree = new IntNodeSmallAsymmetricTestTree();
        helper.evaluateTree(tree, 4, false);

        assertFalse(tree.layer2Node0.hasChildren());
        assertFalse(tree.layer2Node1.hasChildren());
        assertFalse(tree.layer2Node2.hasChildren());
        assertFalse(tree.layer2Node3.hasChildren());
        IntNodeHelper.assertHasNoChildrenOrWasPruned(tree.layer2Node4);
        IntNodeHelper.assertHasNoChildrenOrWasPruned(tree.layer2Node5);
        assertFalse(tree.layer2Node6.hasChildren());
        assertFalse(tree.layer2Node7.hasChildren());
        assertFalse(tree.layer2Node8.hasChildren());
        assertFalse(tree.layer2Node9.hasChildren());
        assertFalse(tree.layer2Node10.hasChildren());
        assertFalse(tree.layer2Node11.hasChildren());
        assertFalse(tree.layer2Node12.hasChildren());

        assertTrue(tree.layer1Node0.hasChildren());
        assertTrue(tree.layer1Node1.hasChildren());
        assertTrue(tree.layer1Node2.hasChildren());
        assertTrue(tree.layer1Node3.hasChildren());
        assertTrue(tree.layer1Node4.hasChildren());

        assertTrue(tree.root.hasChildren());
    }

    @Test
    public void store1LayerWhiteTest() {
        IntNodeWikipediaTestTree tree = new IntNodeWikipediaTestTree();
        StoringMoveOrderingSelfDestructingAlphaBetaPruning<Integer> evaluator = new StoringMoveOrderingSelfDestructingAlphaBetaPruning<>(
                1);
        evaluator.evaluateTree(tree, 4, true);

        assertTrue(tree.root.hasChildren());

        assertFalse(tree.layer1Node0.hasChildren());
        assertFalse(tree.layer1Node1.hasChildren());
    }

    @Test
    public void store1LayerBlackTest() {
        IntNodeWikipediaTestTree tree = new IntNodeWikipediaTestTree();
        StoringMoveOrderingSelfDestructingAlphaBetaPruning<Integer> evaluator = new StoringMoveOrderingSelfDestructingAlphaBetaPruning<>(
                1);
        evaluator.evaluateTree(tree, 4, false);

        assertTrue(tree.root.hasChildren());

        assertFalse(tree.layer1Node0.hasChildren());
        assertFalse(tree.layer1Node1.hasChildren());
    }

    @Test
    public void store0LayersWhiteTest() {
        IntNodeWikipediaTestTree tree = new IntNodeWikipediaTestTree();
        StoringMoveOrderingSelfDestructingAlphaBetaPruning<Integer> evaluator = new StoringMoveOrderingSelfDestructingAlphaBetaPruning<>(
                0);
        evaluator.evaluateTree(tree, 4, false);

        assertFalse(tree.root.hasChildren());
    }

    @Test
    public void store0LayersBlackTest() {
        IntNodeWikipediaTestTree tree = new IntNodeWikipediaTestTree();
        StoringMoveOrderingSelfDestructingAlphaBetaPruning<Integer> evaluator = new StoringMoveOrderingSelfDestructingAlphaBetaPruning<>(
                0);
        evaluator.evaluateTree(tree, 4, false);

        assertFalse(tree.root.hasChildren());
    }
}
