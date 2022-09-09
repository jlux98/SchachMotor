package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import classes.GeneratingIntNode;
import data.IntNodeTestTree;
import gametree.ImpTree;
import gametree.GenericAlphaBetaPruning;
import gametree.Node;
import gametree.TreeEvaluator;
import helper.AlphaBetaHelper;
import helper.IntNodeHelper;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

public class AlphaBetaTest {

    /**
     * tree data taken from https://en.wikipedia.org/wiki/File:Minimax.svg
     */
    // FIXME this test assumes that no pruning is happening whatsoever
    @Test
    public void incompleteBinaryTreeDepth4Test() {
        IntNodeTestTree testTree = new IntNodeTestTree();
        TreeEvaluator<Integer> alphaBeta = new GenericAlphaBetaPruning<Integer>();
        alphaBeta.evaluateTree(testTree, 4, true);
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
    }

    // the first line's whitespace within the <pre> block is trimmed ("Tree:" has to
    // be inside <pre></pre>)
    /**
     * Tree visualized
     * (number of a node = number of children that can be generated by each node)
     * 
     * <pre>
     * Tree:
     *              3               layer0
     *      2       1       0       layer1
     *   1    0     0               layer2
     *   0                          layer3 (not generated because depth=2 is set for alpha-beta)
     * 
     * </pre>
     */
    @Test
    public void alphaBetaRespectsDepthWhiteTest() {
        GeneratingIntNode parent = new GeneratingIntNode(0, 3);
        new GenericAlphaBetaPruning<Integer>().evaluateTree(new ImpTree<GeneratingIntNode>(parent), 2, true);
        assertTrue(parent.hasChildren());
        List<? extends Node<Integer>> children = parent.getChildren();
        GeneratingIntNode layer1Node0 = (GeneratingIntNode) children.get(0);
        GeneratingIntNode layer1Node1 = (GeneratingIntNode) children.get(1);
        GeneratingIntNode layer1Node2 = (GeneratingIntNode) children.get(2);
        assertEquals(3, children.size());
        assertFalse(layer1Node0.hasChildren());
        assertTrue(layer1Node1.hasChildren());
        assertTrue(layer1Node2.hasChildren());
        List<? extends Node<Integer>> layer1Node1Children = layer1Node1.getChildren();
        List<? extends Node<Integer>> layer1Node2Children = layer1Node2.getChildren();
        assertEquals(1, layer1Node1Children.size());
        assertEquals(2, layer1Node2Children.size());

        // assert that no children were generated in layer 3 (layer0 = root, layer2 =
        // leaves = alpha-beta-depth)
        // layer1Node1 only has one child
        assertFalse(layer1Node1.getChildren().get(0).hasChildren());

        // layer1Node2 has two children
        assertFalse(layer1Node2.getChildren().get(0).hasChildren());
        assertFalse(layer1Node2.getChildren().get(1).hasChildren());
    }

    /**
     * like {@link #alphaBetaRespectsDepthWhiteTest()} but with black being the
     * active player
     */
    @Test
    public void alphaBetaRespectsDepthBlackTest() {
        GeneratingIntNode parent = new GeneratingIntNode(0, 3);
        new GenericAlphaBetaPruning<Integer>().evaluateTree(new ImpTree<GeneratingIntNode>(parent), 2, false);
        assertTrue(parent.hasChildren());
        List<? extends Node<Integer>> children = parent.getChildren();
        GeneratingIntNode layer1Node0 = (GeneratingIntNode) children.get(0);
        GeneratingIntNode layer1Node1 = (GeneratingIntNode) children.get(1);
        GeneratingIntNode layer1Node2 = (GeneratingIntNode) children.get(2);
        assertEquals(3, children.size());
        assertFalse(layer1Node0.hasChildren());
        assertTrue(layer1Node1.hasChildren());
        assertTrue(layer1Node2.hasChildren());
        List<? extends Node<Integer>> layer1Node1Children = layer1Node1.getChildren();
        List<? extends Node<Integer>> layer1Node2Children = layer1Node2.getChildren();
        assertEquals(1, layer1Node1Children.size());
        assertEquals(2, layer1Node2Children.size());

        // assert that no children were generated in layer 3 (layer0 = root, layer2 =
        // leaves = alpha-beta-depth)
        // layer1Node1 only has one child
        assertFalse(layer1Node1.getChildren().get(0).hasChildren());

        // layer1Node2 has two children
        assertFalse(layer1Node2.getChildren().get(0).hasChildren());
        assertFalse(layer1Node2.getChildren().get(1).hasChildren());
    }

    /**
     * tree data taken from https://youtu.be/l-hh51ncgDI?t=145
     */
    @Test
    public void lagueDepth3BinaryTreeWhiteTest() {
        AlphaBetaHelper.testBinaryTree(3, 3, true, -1, 3, 5, 1, -6, -4, 0, 9);
    }

    /**
     * like {@link #lagueDepth3BinaryTreeWhiteTest()}
     */
    @Test
    public void lagueDepth3BinaryTreeBlackTest() {
        AlphaBetaHelper.testBinaryTree(0, 3, false, -1, 3, 5, 1, -6, -4, 0, 9);
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
        AlphaBetaHelper.testBinaryTree(3, 4, true, -1, 3, 5, 1, -6, -4, 0, 9);
    }

    /**
     * like {@link #lagueDepth3binaryTreeExaggeratedDepthWhiteTest()} but with black
     * being the active player
     */
    @Test
    public void lagueDepth3binaryTreeExaggeratedDepthBlackTest() {
        AlphaBetaHelper.testBinaryTree(0, 4, false, -1, 3, 5, 1, -6, -4, 0, 9);
    }

    /**
     * tree data taken from https://youtu.be/l-hh51ncgDI?t=486
     */
    @Test
    public void lagueDepth4BinaryTreeWhiteTest() {
        // nodes storing MIN_VALUE or MAX_VALUE are pruned regardless of their value due
        // to their siblings' values
        // they are assigned extreme values to verify that they do not affect the tree
        AlphaBetaHelper.testBinaryTree(3, 4, true, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE, MIN_VALUE,
                MAX_VALUE, MAX_VALUE);
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
        AlphaBetaHelper.testBinaryTree(1, 4, false, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE, MIN_VALUE,
                MAX_VALUE, MAX_VALUE);
    }

    /**
     * tree data taken from https://youtu.be/l-hh51ncgDI?t=486
     */
    @Test
    public void lagueDepth4BinaryTreeExaggeratedDepthWhiteTest() {
        // nodes storing MIN_VALUE or MAX_VALUE are pruned regardless of their value due
        // to their siblings' values
        // they are assigned extreme values to verify that they do not affect the tree
        AlphaBetaHelper.testBinaryTree(3, 8, true, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE, MIN_VALUE,
                MAX_VALUE, MAX_VALUE);
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
        AlphaBetaHelper.testBinaryTree(1, 8, false, 8, 5, 6, -4, 3, 8, 4, -6, 1, MIN_VALUE, 5, 2, MIN_VALUE, MIN_VALUE,
                MAX_VALUE, MAX_VALUE);
    }

    /**
     * tree data taken from https://www.javatpoint.com/mini-max-algorithm-in-ai
     */

    @Test
    public void javatpointBinaryTreeDepth3WhiteTest() {
        AlphaBetaHelper.testBinaryTree(4, 3, true, -1, 4, 2, 6, -3, -5, 0, 7);
    }

    /**
     * like {@link #javatpointBinaryTreeDepth3WhiteTest()} but with black being the
     * active player
     */
    @Test
    public void javatpointBinaryTreeDepth3BlackTest() {
        AlphaBetaHelper.testBinaryTree(0, 3, false, -1, 4, 2, 6, -3, -5, 0, 7);
    }

    /**
     * tree data taken from https://www.javatpoint.com/mini-max-algorithm-in-ai
     */
    @Test
    public void javatpointBinaryTreeDepth3ExaggeratedDepthWhiteTest() {
        AlphaBetaHelper.testBinaryTree(4, 4, true, -1, 4, 2, 6, -3, -5, 0, 7);
    }

    /**
     * like {@link #javatpointBinaryTreeDepth3ExaggeratedDepthWhiteTest()} but with
     * black being the active player
     */
    @Test
    public void javatpointBinaryTreeDepth3ExaggeratedDepthBlackTest() {
        AlphaBetaHelper.testBinaryTree(0, 4, false, -1, 4, 2, 6, -3, -5, 0, 7);
    }

    // TODO add test with more extensive pruning (black)
    // TODO add test with more extensive pruning (white)

}
