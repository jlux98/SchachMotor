package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import classes.EvaluableTestNode;
import classes.GeneratingIntNode;
import classes.IntNode;
import data.IntNodeWikipediaTestTree;
import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.UninitializedValueException;
import helper.NodeHelper;

public class NodeTest {

    //TODO add tests for new evaluable methods

    private IntNodeWikipediaTestTree testTree;

    @BeforeEach
    //tree data source: https://en.wikipedia.org/wiki/File:Minimax.svg
    public void setUpTree() {
        testTree = new IntNodeWikipediaTestTree();
    }

    @Test
    public void createRootTest() {
        IntNode root = new IntNode(5);
        assertEquals(5, root.getContent());
        assertFalse(root.hasChildren());
    }

    @Test
    public void createChildNodeTest() throws ComputeChildrenException {
        IntNode root = new IntNode(3);
        IntNode child = new IntNode(18, root);
        List<? extends Node<Integer>> children = root.queryChildren();
        Node<Integer> retrievedChild = children.get(0);
        assertTrue(root.hasChildren());
        assertEquals(1, children.size());
        assertTrue(retrievedChild == child);
        assertTrue(child.getParent() == root);
        assertEquals(18, retrievedChild.getContent());
    }

    @Test
    public void insertChildTest() throws ComputeChildrenException {
        IntNode root = new IntNode(4);
        //create a second root node called child and add it to the root node
        IntNode child = new IntNode(7);
        root.insertChild(child);
        List<? extends Node<Integer>> children = root.queryChildren();
        Node<Integer> retrievedChild = children.get(0);
        assertTrue(root.hasChildren());
        assertEquals(1, children.size());
        assertTrue(retrievedChild == child);
        assertTrue(child.getParent() == root);
        assertEquals(7, retrievedChild.getContent());
    }

    @Test
    public void deleteSelfLeafOnlyChildOfParentTest() throws ComputeChildrenException {
        IntNode child = testTree.layer4Node3;
        child.deleteSelf();
        IntNode parent = testTree.layer3Node2;

        NodeHelper.verifyDeletion(parent, child, (IntNode[]) null);
    }

    @Test
    public void deleteSelfLeafTest() throws ComputeChildrenException {
        IntNode child = testTree.layer4Node4;
        IntNode parent = testTree.layer3Node3;

        child.deleteSelf();
        NodeHelper.verifyDeletion(parent, child, testTree.layer4Node5);
    }

    @Test
    public void deleteSelfInnerNodeTest() throws ComputeChildrenException {
        IntNode child = testTree.layer2Node0;
        IntNode parent = testTree.layer1Node0;

        child.deleteSelf();
        NodeHelper.verifyDeletion(parent, child, testTree.layer2Node1);
    }

    @Test

    public void deleteSelfNoParentTest() {
        assertTrue(testTree.root.getParent() == null);
        testTree.root.deleteSelf();
        assertTrue(testTree.root.getParent() == null);
    }

    /**
     * calls node.deleteSelf() on a node that has a parent reference,
     * but is not stored as a child in that parent node (not part of the parent's child list)
     */
    @Test
    public void deleteSelfInvalidParentReferenceTest() {
        IntNode faultyLinkedNode = new IntNode(12);
        faultyLinkedNode.setParent(testTree.root);
        assertThrows(NoSuchElementException.class, () -> faultyLinkedNode.deleteSelf());
    }

    @Test
    public void deleteExistingChildTest() throws ComputeChildrenException {
        IntNode parent = testTree.layer1Node1;
        IntNode deleted = testTree.layer2Node2;

        parent.deleteChild(deleted);
        NodeHelper.verifyDeletion(parent, deleted, testTree.layer2Node3);
    }

    @Test
    public void deleteInexistentChildTest() {
        IntNode parent = testTree.layer2Node2;
        assertThrows(NoSuchElementException.class, () -> parent.deleteChild(new IntNode(42)));
    }

    @Test
    public void deleteChildren1ChildTest() throws ComputeChildrenException {
        IntNode parent = testTree.layer3Node1;
        parent.deleteChildren();
        NodeHelper.verifyDeletion(parent, testTree.layer4Node2, (IntNode[]) null);
    }

    @Test
    public void deleteChildren2ChildrenTest() throws ComputeChildrenException {
        IntNode parent = testTree.layer2Node2;
        parent.deleteChildren();
        NodeHelper.verifyDeletion(parent, testTree.layer3Node3, (IntNode[]) null);
        NodeHelper.verifyDeletion(parent, testTree.layer3Node4, (IntNode[]) null);
    }

    @Test
    public void deleteChildrenNoChildrenTest() {
        IntNode leaf = testTree.layer4Node1;
        leaf.deleteChildren();
    }

    /**
     * Calls queryChildren() on a node without children,
     * which calls computeChildren() to generate children.
     * IntNode does not implemented computeChildren() and throws an ComputeChildrenException instead.
     */
    @Test
    public void queryChildrenCallsComputeChildrenIfNoChildrenStoredTest() {
        IntNode leaf = testTree.layer4Node3;
        assertThrows(ComputeChildrenException.class, () -> leaf.queryChildren());
    }

    /**
    * Calls queryChildren() on a node with children,
    * returning the stored children without calling computeChildren().
    */
    @Test
    public void queryChildrenReturnsStoredChildrenTest() throws ComputeChildrenException {
        IntNode parent = testTree.layer3Node3;
        NodeHelper.verifyChildren(parent, testTree.layer4Node4, testTree.layer4Node5);
    }

    @Test
    public void queryChildrenGeneratesChildrenTest() throws ComputeChildrenException {
        GeneratingIntNode parent = new GeneratingIntNode(0, 3);
        List<? extends Node<Integer>> children = parent.queryChildren();
        //test that parent generated exactly 3 children
        assertTrue(parent.hasChildren());
        assertEquals(3, children.size());
        //test that no children were generated recursively
        assertFalse(children.get(0).hasChildren());
        assertFalse(children.get(1).hasChildren());
        assertFalse(children.get(2).hasChildren());
    }

    @Test
    public void TestGetValueUnitialized() {
        EvaluableTestNode node = new EvaluableTestNode(42);
        assertThrows(UninitializedValueException.class, () -> node.getValue());
    }

    @Test
    public void testGetValueStaticValueTest() throws UninitializedValueException {
        EvaluableTestNode node = new EvaluableTestNode(42);
        assertThrows(UninitializedValueException.class, () -> node.getValue());
        int value = node.computeOrGetStaticValueOrBetter();
        assertEquals(42, value);
        assertEquals(42, node.getValue());
        assertEquals(47, node.computeOrGetLeafValueOrBetter(5)); //leaf supersedes
    }

    @Test
    public void testStaticLeafValueTest() throws UninitializedValueException {
        EvaluableTestNode node = new EvaluableTestNode(42);
        assertThrows(UninitializedValueException.class, () -> node.getValue());
        int value = node.computeOrGetLeafValueOrBetter(5); //leaf -> value = 42 + depth = 47 expected
        assertEquals(47, value);
        assertEquals(47, node.getValue());
        assertEquals(47, node.computeOrGetStaticValueOrBetter()); //is superseded
    }

    @Test
    public void testExplicitValueTest() throws UninitializedValueException {
        EvaluableTestNode node = new EvaluableTestNode(42);
        assertThrows(UninitializedValueException.class, () -> node.getExplicitValue());        
        assertEquals(42, node.computeOrGetStaticValueOrBetter());
        assertThrows(UninitializedValueException.class, () -> node.getExplicitValue());
        assertEquals(45, node.computeOrGetLeafValueOrBetter(3)); //leaf supersedes "normal" static value
        assertThrows(UninitializedValueException.class, () -> node.getExplicitValue());
        node.setValue(13);
        assertEquals(13, node.getValue());
        assertEquals(13, node.computeOrGetStaticValueOrBetter()); //is superseded
        assertEquals(13, node.computeOrGetLeafValueOrBetter(3)); //is superseded
        assertEquals(13, node.getExplicitValue());
    }

    @Test
    public void setValueOverwritesStaticValueTest() throws UninitializedValueException {        
        EvaluableTestNode node = new EvaluableTestNode(7);
        assertEquals(7, node.computeOrGetStaticValueOrBetter());
        node.setValue(1);
        assertEquals(1, node.getExplicitValue());
        assertEquals(1, node.computeOrGetStaticValueOrBetter());
        assertEquals(1, node.computeOrGetLeafValueOrBetter(3));
    }

    @Test
    public void setValueOverwritesStaticLeafValueTest() throws UninitializedValueException {        
        EvaluableTestNode node = new EvaluableTestNode(7);
        assertEquals(8, node.computeOrGetLeafValueOrBetter(1));
        node.setValue(2);
        assertEquals(2, node.getExplicitValue());
        assertEquals(2, node.computeOrGetLeafValueOrBetter(3));        
        assertEquals(2, node.computeOrGetStaticValueOrBetter());
    }

}
