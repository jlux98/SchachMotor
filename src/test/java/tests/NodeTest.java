package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gametree.ComputeChildrenException;
import gametree.Node;
import testclasses.EvaluableInteger;
import testclasses.IntNode;
import testclasses.TestTree;

public class NodeTest {

    private TestTree testTree;

    @BeforeEach
    //tree data source: https://en.wikipedia.org/wiki/File:Minimax.svg
    public void setUpTree() {
        testTree = new TestTree();
    }

    /**
     * Verify that the node passed as "deleted" was deleted.
     * If the deleted node was the parent's only child, an empty array or null must be passed to this method.
     * @param parent the parent of the node that was deleted
     * @param deleted the node that was deleted
     * @param leftOver the leftover children of the parent node, in the same order as returned by parent.queryChildren()
     */
    private void verifyDeletion(IntNode parent, IntNode deleted, IntNode... leftOver) throws ComputeChildrenException {
        assertTrue(deleted.getParent() == null);
        verifyChildren(parent, leftOver);
    }

    /**
     * Verify that the node passed as parent has exactly the specified children (compared by reference).
     * If the node has no children, an empty array or null must be passed to this method.
     * <br><br>
     * If this method throws UnsupportedOperationException, 
     * queryChildren() attempted to generate children although parent stores children that should simply be returned instead.
     * @param parent the node whose children should be verified
     * @param expectedChildren the expected children of the parent node, in the same order as returned by parent.queryChildren()
     */
    private void verifyChildren(IntNode parent, IntNode... expectedChildren) throws ComputeChildrenException {
        if (expectedChildren == null || expectedChildren.length == 0) {
            //verify that parent has no children
            assertFalse(parent.hasChildren());
            //queryChildren() cannot be called:
            //calling queryChildren() calls computeChildren() because node has no children
            //computeChildren() is not implemented by inttnode -> UnsupportedOperationException
            //assertEquals(0, parent.queryChildren().size());
        } else {
            //verify that parent has the expected children
            assertTrue(parent.hasChildren());
            List<? extends Node<EvaluableInteger>> actualChildren = parent.queryChildren();
            assertEquals(expectedChildren.length, actualChildren.size());

            //compare expected and actual children
            //also verify reference to parent
            for (int i = 0; i < expectedChildren.length; i++) {
                assertTrue(expectedChildren[i] == actualChildren.get(i));
                assertTrue(actualChildren.get(i).getParent() == parent);
            }
        }
    }

    /**
     * verifies that the tree starting with testTree.root was successfully build to represent
     * https://en.wikipedia.org/wiki/File:Minimax.svg
     */
    @Test
    public void verifyTestTree() throws ComputeChildrenException {
        verifyChildren(testTree.root, testTree.layer1Node0, testTree.layer1Node1);
        //layer 1
        verifyChildren(testTree.layer1Node0, testTree.layer2Node0, testTree.layer2Node1);
        verifyChildren(testTree.layer1Node1, testTree.layer2Node2, testTree.layer2Node3);
        //layer2
        verifyChildren(testTree.layer2Node0, testTree.layer3Node0, testTree.layer3Node1);
        verifyChildren(testTree.layer2Node1, testTree.layer3Node2);
        verifyChildren(testTree.layer2Node2, testTree.layer3Node3, testTree.layer3Node4);
        verifyChildren(testTree.layer2Node3, testTree.layer3Node5);
        //layer 3
        verifyChildren(testTree.layer3Node0, testTree.layer4Node0, testTree.layer4Node1);
        verifyChildren(testTree.layer3Node1, testTree.layer4Node2);
        verifyChildren(testTree.layer3Node2, testTree.layer4Node3);
        verifyChildren(testTree.layer3Node3, testTree.layer4Node4, testTree.layer4Node5);
        verifyChildren(testTree.layer3Node4, testTree.layer4Node6);
        verifyChildren(testTree.layer3Node5, testTree.layer4Node7, testTree.layer4Node8);
        //layer 4: leaf values
        assertEquals(10, testTree.layer4Node0.getContent().getValue());
        assertEquals(Integer.MAX_VALUE, testTree.layer4Node1.getContent().getValue());
        assertEquals(5, testTree.layer4Node2.getContent().getValue());
        assertEquals(-10, testTree.layer4Node3.getContent().getValue());
        assertEquals(7, testTree.layer4Node4.getContent().getValue());
        assertEquals(5, testTree.layer4Node5.getContent().getValue());
        assertEquals(Integer.MIN_VALUE, testTree.layer4Node6.getContent().getValue());
        assertEquals(-7, testTree.layer4Node7.getContent().getValue());
        assertEquals(-5, testTree.layer4Node8.getContent().getValue());

    }

    @Test
    public void createRootTest() {
        IntNode root = new IntNode(5);
        assertEquals(5, root.getContent().getValue());
        assertFalse(root.hasChildren());
    }

    @Test
    public void createChildNodeTest() throws ComputeChildrenException {
        IntNode root = new IntNode(3);
        IntNode child = new IntNode(18, root);
        List<? extends Node<EvaluableInteger>> children = root.queryChildren();
        Node<EvaluableInteger> retrievedChild = children.get(0);
        assertTrue(root.hasChildren());
        assertEquals(1, children.size());
        assertTrue(retrievedChild == child);
        assertTrue(child.getParent() == root);
        assertEquals(18, retrievedChild.getContent().getValue());
    }

    @Test
    public void insertChildTest() throws ComputeChildrenException {
        IntNode root = new IntNode(4);
        //create a second root node called child and add it to the root node
        IntNode child = new IntNode(7);
        root.insertChild(child);
        List<? extends Node<EvaluableInteger>> children = root.queryChildren();
        Node<EvaluableInteger> retrievedChild = children.get(0);
        assertTrue(root.hasChildren());
        assertEquals(1, children.size());
        assertTrue(retrievedChild == child);
        assertTrue(child.getParent() == root);
        assertEquals(7, retrievedChild.getContent().getValue());
    }

    @Test
    public void deleteSelfLeafOnlyChildOfParentTest() throws ComputeChildrenException {
        IntNode child = testTree.layer4Node3;
        child.deleteSelf();
        IntNode parent = testTree.layer3Node2;

        verifyDeletion(parent, child, (IntNode[]) null);
    }

    @Test
    public void deleteSelfLeafTest() throws ComputeChildrenException {
        IntNode child = testTree.layer4Node4;
        IntNode parent = testTree.layer3Node3;

        child.deleteSelf();
        verifyDeletion(parent, child, testTree.layer4Node5);
    }

    @Test
    public void deleteSelfInnerNodeTest() throws ComputeChildrenException {
        IntNode child = testTree.layer2Node0;
        IntNode parent = testTree.layer1Node0;

        child.deleteSelf();
        verifyDeletion(parent, child, testTree.layer2Node1);
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
    public void deleteSelfInvalidParentReference() {
        IntNode faultyLinkedNode = new IntNode(12);
        faultyLinkedNode.setParent(testTree.root);
        assertThrows(NoSuchElementException.class, () -> faultyLinkedNode.deleteSelf());
    }

    @Test
    public void deleteExistingChild() throws ComputeChildrenException {
        IntNode parent = testTree.layer1Node1;
        IntNode deleted = testTree.layer2Node2;

        parent.deleteChild(deleted);
        verifyDeletion(parent, deleted, testTree.layer2Node3);
    }

    @Test
    public void deleteInexistentChild() {
        IntNode parent = testTree.layer2Node2;
        assertThrows(NoSuchElementException.class, () -> parent.deleteChild(new IntNode(42)));
    }

    @Test
    public void deleteChildren1Child() throws ComputeChildrenException {
        IntNode parent = testTree.layer3Node1;
        parent.deleteChildren();
        verifyDeletion(parent, testTree.layer4Node2, (IntNode[]) null);
    }

    @Test
    public void deleteChildren2Children() throws ComputeChildrenException {
        IntNode parent = testTree.layer2Node2;
        parent.deleteChildren();
        verifyDeletion(parent, testTree.layer3Node3, (IntNode[]) null);
        verifyDeletion(parent, testTree.layer3Node4, (IntNode[]) null);
    }

    @Test
    public void deleteChildrenNoChildren() {
        IntNode leaf = testTree.layer4Node1;
        leaf.deleteChildren();
    }

    /**
     * Calls queryChildren() on a node without children,
     * which calls computeChildren() to generate children.
     * IntNode does not implemented computeChildren() and throws an UnsupportedOperationException instead.
     */
    @Test
    public void queryChildrenCallsComputeChildrenIfNoChildrenStored() {
        IntNode leaf = testTree.layer4Node3;
        assertThrows(UnsupportedOperationException.class, () -> leaf.queryChildren());
    }

    /**
    * Calls queryChildren() on a node with children,
    * returning the stored children without calling computeChildren().
    */
    @Test
    public void queryChildrenReturnsStoredChildren() throws ComputeChildrenException {
        IntNode parent = testTree.layer3Node3;
        verifyChildren(parent, testTree.layer4Node4,testTree.layer4Node5);
    }
}
