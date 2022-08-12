
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gametree.Node;
import testclasses.EvaluableInteger;
import testclasses.IntNode;

public class NodeTest {

    /**
     * Root node of a tree consisting of IntNodes.
     * Leaf values are the same as in https://en.wikipedia.org/wiki/File:Minimax.svg.
     * Inner nodes have a value of 0.
     */
    private IntNode treeRoot;
    //layer 4
    private IntNode layer4Node0;
    private IntNode layer4Node1;
    private IntNode layer4Node2;
    private IntNode layer4Node3;
    private IntNode layer4Node4;
    private IntNode layer4Node5;
    private IntNode layer4Node6;
    private IntNode layer4Node7;
    private IntNode layer4Node8;
    //layer 3
    private IntNode layer3Node0;
    private IntNode layer3Node1;
    private IntNode layer3Node2;
    private IntNode layer3Node3;
    private IntNode layer3Node4;
    private IntNode layer3Node5;
    //layer 2
    private IntNode layer2Node0;
    private IntNode layer2Node1;
    private IntNode layer2Node2;
    private IntNode layer2Node3;
    //layer1
    private IntNode layer1Node0;
    private IntNode layer1Node1;

    @BeforeEach
    //tree data source: https://en.wikipedia.org/wiki/File:Minimax.svg
    public void setUpTree() {
        //layers and nodes are 0 based
        //root would be layer0Node0
        //children of root are layer1Node0 and layer1Node1
        //layer 4 = leaves
        layer4Node0 = new IntNode(10);
        layer4Node1 = new IntNode(Integer.MAX_VALUE);
        layer4Node2 = new IntNode(5);
        layer4Node3 = new IntNode(-10);
        layer4Node4 = new IntNode(7);
        layer4Node5 = new IntNode(5);
        layer4Node6 = new IntNode(Integer.MIN_VALUE);
        layer4Node7 = new IntNode(-7);
        layer4Node8 = new IntNode(-5);
        //layer 3
        layer3Node0 = createParent(layer4Node0, layer4Node1);
        layer3Node1 = createParent(layer4Node2);
        layer3Node2 = createParent(layer4Node3);
        layer3Node3 = createParent(layer4Node4, layer4Node5);
        layer3Node4 = createParent(layer4Node6);
        layer3Node5 = createParent(layer4Node7, layer4Node8);
        //layer 2
        layer2Node0 = createParent(layer3Node0, layer3Node1);
        layer2Node1 = createParent(layer3Node2);
        layer2Node2 = createParent(layer3Node3, layer3Node4);
        layer2Node3 = createParent(layer3Node5);
        //layer1
        layer1Node0 = createParent(layer2Node0, layer2Node1);
        layer1Node1 = createParent(layer2Node2, layer2Node3);
        //root
        treeRoot = createParent(layer1Node0, layer1Node1);

    }

    //TODO verify/test data setup

    /**
    * Creates a parent node for the passed children.
    * @param children nodes that a parent should be generated for
    * @return a parent node with the specified nodes as children
    */
    private IntNode createParent(IntNode... children) {
        IntNode parent = new IntNode(0);
        for (IntNode child : children) {
            parent.insertChild(child);
        }
        return parent;
    }

    /**
     * Verified that the node passed as "deleted" was deleted.
     * If the deleted node was the parent's only child, an empty array must be passed to this method.
     * @param parent the parent of the node that was deleted
     * @param deleted the node that was deleted
     * @param leftOver the leftover children of the parent node, in the same order as returned by parent.queryChildren()
     */
    private void verifyDeletion(IntNode parent, IntNode deleted, IntNode... leftOver) {

        assertTrue(deleted.getParent() == null);

        if (leftOver == null || leftOver.length == 0) {
            //parent has no leftover children
            assertFalse(parent.hasChildren());
            //queryChildren() cannot be called:
            //calling queryChildren() calls computeChildren() because node has no children
            //computeChildren() is not implemented by inttnode -> UnsupportedOperationException
            //assertEquals(0, parent.queryChildren().size());
        } else {
            //parent has leftover children
            List<? extends Node<EvaluableInteger>> children = parent.queryChildren();
            assertTrue(parent.hasChildren());
            assertEquals(leftOver.length, children.size());

            //compare children with leftOver
            for (int i = 0; i < leftOver.length; i++) {
                assertTrue(leftOver[i] == children.get(i));
            }

        }
    }

    @Test
    public void createRootTest() {
        IntNode root = new IntNode(5);
        assertEquals(5, root.getContent().getValue());
        assertFalse(root.hasChildren());
    }

    @Test
    public void createChildNodeTest() {
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
    public void insertChildTest() {
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
    public void deleteSelfLeafOnlyChildOfParentTest() {
        IntNode child = layer4Node3;
        child.deleteSelf();
        IntNode parent = layer3Node2;

        verifyDeletion(parent, child, null);
    }

    @Test
    public void deleteSelfLeafTest() {
        IntNode child = layer4Node4;
        IntNode parent = layer3Node3;

        child.deleteSelf();
        verifyDeletion(parent, child, layer4Node5);
    }

    @Test
    public void deleteSelfInnerNodeTest() {
        IntNode child = layer2Node0;
        IntNode parent = layer1Node0;

        child.deleteSelf();
        verifyDeletion(parent, child, layer2Node1);
    }

    @Test

    public void deleteSelfNoParentTest() {
        assertTrue(treeRoot.getParent() == null);
        treeRoot.deleteSelf();
        assertTrue(treeRoot.getParent() == null);
    }

    @Test
    public void deleteExistingChild() {
        IntNode parent = layer1Node1;
        IntNode deleted = layer2Node2;

        parent.deleteChild(deleted);
        verifyDeletion(parent, deleted, layer2Node3);
    }

    @Test
    public void deleteInexistentChild() {
        IntNode parent = layer2Node2;
        assertThrows(NoSuchElementException.class, () -> parent.deleteChild(new IntNode(42)));
    }

    //TODO deleteChild, deleteChildreN, getparent, setParent, unsetParent, queryChildren (if neccessary)

}
