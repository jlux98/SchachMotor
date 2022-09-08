package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import classes.IntNode;
import gametree.ComputeChildrenException;
import gametree.ImpTree;
import gametree.Node;
import gametree.Tree;

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
     * This does <i>not</i> compare the Integer stored as content by the IntNode.
     * @param expected the expected value
     * @param node the IntNode that should be storing this value
     */
    public static void compareIntNodeValue(int expected, IntNode node) {
        assertEquals(expected, node.getValue());
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
            //System.out.println("parents: " + parents.size() + " children: " + children.size()); //TODO remove (debug)
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
     * Creates a binary tree with the specified leaf values.
     * @param values the values that should be stored by the leaf nodes
     * @return the tree's root node
     */
    /* public static IntNode createBinaryTree(int depth, int... values) {
        if (Math.pow(2, depth) != values.length) { //no delta for comparison should be fine in this value range
            throw new IllegalArgumentException("value count != 2^depth, but a value for each leaf is required");
        }
        //create a binary tree by using createParent
        //add a variant of createParent that takes values instead of nodes
    
        //translate values into intnodes wihtout parents
        List<IntNode> children = new ArrayList<IntNode>();
        for (int i : values) {
            children.add(new IntNode(i));
        }
    
        List<IntNode> parents = new ArrayList<IntNode>();
        for (int j = 0; j < depth; j++) { //while(children.size() != 1)
            //System.out.println(children.size());
            for (int i = 0; i < children.size(); i++) {
                parents.add(createParent(children.get(i), children.get(i + 1)));
                i++; //skip the child at i+1
            }
            children = parents; //last iteration's parents are next iteration's children
            parents = new ArrayList<IntNode>(children.size() / 2); //create a new parent list (cant call parents.clear() because that would delete the children list)
        }
        if (children.size() != 1) {
            //number of iterations was wrong
            throw new IllegalStateException("createBinaryTree failed to construct a tree and identify its root");
        }
        return children.get(0);
    } */

    @Test
    public void createRootNodeTest() throws ComputeChildrenException {
        Tree<IntNode> tree = IntNodeHelper.createIntNodeTree(2, 1, 2, 3, 4);
        IntNode root = tree.getRoot();

        //list containing 2 inner nodes
        List<? extends Node<Integer>> layer1 = root.queryChildren();
        assertEquals(layer1.size(), 2);

        //lists containing 2 leaf nodes each
        List<? extends Node<Integer>> layer2children1 = layer1.get(0).queryChildren();
        List<? extends Node<Integer>> layer2children2 = layer1.get(1).queryChildren();

        assertEquals(layer2children1.size(), 2);
        assertEquals(layer2children2.size(), 2);
        assertThrows(ComputeChildrenException.class, () -> layer2children1.get(0).queryChildren());
        assertThrows(ComputeChildrenException.class, () -> layer2children2.get(1).queryChildren());
        assertEquals(1, layer2children1.get(0).getContent());
        assertEquals(2, layer2children1.get(1).getContent());
        assertEquals(3, layer2children2.get(0).getContent());
        assertEquals(4, layer2children2.get(1).getContent());
    }
}
