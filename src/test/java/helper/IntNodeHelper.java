package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import classes.FailureException;
import classes.IntNode;
import data.IntNodeWikipediaTestTree;
import gametree.ComputeChildrenException;
import gametree.ImpTree;
import gametree.Node;
import gametree.Tree;
import gametree.UninitializedValueException;

public class IntNodeHelper {

    /**
    * Creates a parent IntNode for the passed children.
    * @param children IntNodes that a parent should be generated for
    * @return a parent IntNode with the specified nodes as children
    */
    public static IntNode createParent(IntNode... children) {
        return createParent(Arrays.asList(children));
    }

    /**
    * Creates a parent IntNode for the passed children.
    * @param children IntNodes that a parent should be generated for
    * @return a parent IntNode with the specified nodes as children
    */
    public static IntNode createParent(Iterable<IntNode> children) {
        IntNode parent = new IntNode(0);
        for (IntNode child : children) {
            parent.insertChild(child);
        }
        return parent;
    }

    /**
     * Asserts that the IntNode's value is equal to the expected value.
     * This does <b>not</b> compare the Integer stored as content by the IntNode.
     * @param expected the expected value
     * @param node the IntNode that should be storing this value
     */
    public static void compareIntNodeValue(int expected, IntNode node) {
        try {
        assertEquals(expected, node.getValue());
        } catch (UninitializedValueException exception) {
            throw new FailureException("atempted to compare against a node without value");
        }
    }

    /**
     * Compares the IntNode's static value (= stored integer) to the expected value.
     * @param expectedStaticValue the expected static value
     * @param node the node
     */
    public static void compareStaticIntNodeValue(int expectedStaticValue, IntNode node) {
        //intnodes guarantees that roughlyEvaluateStatically and evaluateStatically are equal
        assertEquals(expectedStaticValue, node.computeOrGetStaticValueOrBetter());
    }

    /**
     * Creates a parent layer for the passed nodes.
     * Parent nodes are created for every n children, where n is the specified degree.
     * If not sufficient nodes are available in a layer, the last node of the parent layer will have a reduced degree.
     * <br><br>
     * Every node is child to one parent only. 
     * Iterating over the children of the returned nodes will yield every node exactly once.
     * @param degree the maximum amount of children a node may have
     * @param level the nodes to which parent nodes should be generated
     * @return a list of nodes that are parents that are parents to the passed nodes
     */
    private static List<IntNode> createParentLevel(int degree, List<IntNode> level) {
        List<IntNode> parents = new ArrayList<IntNode>();
        List<IntNode> children = new ArrayList<IntNode>();
        int index = 0;

        while (index < level.size()) {
            children.clear();

            //copy next n nodes into child list
            for (int i = 0; i < degree; i++) {
                //out of bounds if level.size() is not a multiple of maxChildCountPerParent
                if (index + i < level.size()) {
                    children.add(level.get(index + i));
                }
            }

            //create parent to child nodes
            //add that parent to parent list
            parents.add(createParent(children));

            //shift index to the nodes for next parent
            index = index + degree;
        }

        return parents;
    }

    /**
     * Creates a tree whose leafs have the specified values.
     * 
     * Parent nodes and levels are created as required to reach a single root node.
     * Parent nodes are created for every n children, where n is the degree of the tree.
     * If not sufficient nodes are available in a layer, the last node of the parent layer will have a reduced degree.
     * @param values values of the tree's leaves
     * @param degree the maximum amount of children a node may have
     * @return the tree's root node
     */
    public static IntNode createRootIntNode(int degree, int... values) {

        //translate values into intnodes wihtout parents
        List<IntNode> children = new ArrayList<IntNode>();
        List<IntNode> parents = new ArrayList<IntNode>();
        for (int i : values) {
            children.add(new IntNode(i));
        }

        while (parents.size() != 1) {

            parents = createParentLevel(degree, children);
            children = parents; //last iteration's parents are next iteration's children
        }

        if (parents.size() != 1) {
            throw new IllegalStateException("debug: this should not have happened");
        }

        return parents.get(0);
    }

    /**
     * See {@link #createRootIntNode(int, int...)}.
     * @return a tree with the specified leaf values
     */
    public static Tree<IntNode> createIntNodeTree(int degree, int... values) {
        return new ImpTree<IntNode>(createRootIntNode(degree, values));
    }

    /**
     * Inverts the content stored in the tree's leaf nodes (content = -1 * content).
     * @param tree tree whose leaves should be inverted
     */
    public static void invertLeaves(Tree<? extends Node<Integer>> tree) {
        invertLeaves(tree.getRoot());
    }

    /**
     * Inverts the content stored in this node if it is a leaf (cont = -1 * content).
     * Otherwise, inverts the content stored by its children.
     * @param node node whose value or whose childrens' value should be inverted
     */
    public static void invertLeaves(Node<Integer> node) {
        //invert node if it has no children
        if (!node.hasChildren()) {
            invertLeaf(node);
            return;
        }
        //iterate tree and call invertLeaves on each node
        for (Node<Integer> child : ((IntNode) node).getChildren()) {
            invertLeaves((IntNode) child);
        }
    }

    /**
     * Inverts a leaf by multiplying its content with -1.
     * @param node leaf node whose value should be inverted
     */
    public static void invertLeaf(Node<Integer> node) {
        int content = node.getContent();
        if (content == Integer.MIN_VALUE) {
            //required as MIN_VALUE * -1 == MIN_VALUE
            node.setContent(Integer.MAX_VALUE);
            return;
        }
        if (content == Integer.MAX_VALUE) {
            //required as MAX_VALUE * -1 == MIN_VALUE + 1
            node.setContent(Integer.MIN_VALUE);
            return;
        }
        node.setContent(content * -1);
    }

    /**
     * Asserts that this node does not have children.
     * If it does, asserts that this node was not evaluted.
     * Used to confirm the deletion of nodes by self-destructing evaluators.
     * @param node the node that should not have children
     */
    public static void assertHasNoChildrenOrWasPruned(IntNode node) {
        if (node.hasChildren()) {
            // if node has children it may not have an explicit value
            assertThrows(UninitializedValueException.class, () -> node.getExplicitValue());
        }
    }

    @Test
    public void createRootNodeTest() throws ComputeChildrenException {
        Tree<IntNode> tree = IntNodeHelper.createIntNodeTree(2, 1, 2, 3, 4);
        IntNode root = tree.getRoot();

        //list containing 2 inner nodes
        List<? extends Node<Integer>> layer1 = root.getOrComputeChildren();
        assertEquals(layer1.size(), 2);

        //lists containing 2 leaf nodes each
        List<? extends Node<Integer>> layer2children1 = layer1.get(0).getOrComputeChildren();
        List<? extends Node<Integer>> layer2children2 = layer1.get(1).getOrComputeChildren();

        assertEquals(layer2children1.size(), 2);
        assertEquals(layer2children2.size(), 2);
        assertThrows(ComputeChildrenException.class, () -> layer2children1.get(0).getOrComputeChildren());
        assertThrows(ComputeChildrenException.class, () -> layer2children2.get(1).getOrComputeChildren());
        assertEquals(1, layer2children1.get(0).getContent());
        assertEquals(2, layer2children1.get(1).getContent());
        assertEquals(3, layer2children2.get(0).getContent());
        assertEquals(4, layer2children2.get(1).getContent());
    }

    @Test
    public void invertLeavesTest() throws ComputeChildrenException {
        IntNodeWikipediaTestTree testTree = new IntNodeWikipediaTestTree();
        invertLeaves(testTree);

        NodeHelper.verifyChildren(testTree.root, testTree.layer1Node0, testTree.layer1Node1);
        //layer 1
        NodeHelper.verifyChildren(testTree.layer1Node0, testTree.layer2Node0, testTree.layer2Node1);
        NodeHelper.verifyChildren(testTree.layer1Node1, testTree.layer2Node2, testTree.layer2Node3);
        //layer2
        NodeHelper.verifyChildren(testTree.layer2Node0, testTree.layer3Node0, testTree.layer3Node1);
        NodeHelper.verifyChildren(testTree.layer2Node1, testTree.layer3Node2);
        NodeHelper.verifyChildren(testTree.layer2Node2, testTree.layer3Node3, testTree.layer3Node4);
        NodeHelper.verifyChildren(testTree.layer2Node3, testTree.layer3Node5);
        //layer 3
        NodeHelper.verifyChildren(testTree.layer3Node0, testTree.layer4Node0, testTree.layer4Node1);
        NodeHelper.verifyChildren(testTree.layer3Node1, testTree.layer4Node2);
        NodeHelper.verifyChildren(testTree.layer3Node2, testTree.layer4Node3);
        NodeHelper.verifyChildren(testTree.layer3Node3, testTree.layer4Node4, testTree.layer4Node5);
        NodeHelper.verifyChildren(testTree.layer3Node4, testTree.layer4Node6);
        NodeHelper.verifyChildren(testTree.layer3Node5, testTree.layer4Node7, testTree.layer4Node8);
        //layer 4: leaf values
        assertEquals(-10, testTree.layer4Node0.getContent());
        assertEquals(Integer.MIN_VALUE, testTree.layer4Node1.getContent());
        assertEquals(-5, testTree.layer4Node2.getContent());
        assertEquals(10, testTree.layer4Node3.getContent());
        assertEquals(-7, testTree.layer4Node4.getContent());
        assertEquals(-5, testTree.layer4Node5.getContent());
        assertEquals(Integer.MAX_VALUE, testTree.layer4Node6.getContent());
        assertEquals(7, testTree.layer4Node7.getContent());
        assertEquals(5, testTree.layer4Node8.getContent());
    }

}
