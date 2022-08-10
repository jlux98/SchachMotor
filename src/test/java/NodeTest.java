
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gametree.Node;
import testclasses.EvaluableInteger;
import testclasses.IntNode;

public class NodeTest {

    private IntNode treeRoot;

    /**
     * Adds two IntNodes storing the value 0 as children to this node.
     */
    private void addTwoChildren(IntNode node, int depth) {
        if (depth = 0) {
        node.insertChild(new IntNode(0));
        node.insertChild(new IntNode(0));
        }
    }

    private IntNode createParent(IntNode... children) {
        IntNode parent = new IntNode(0);
        for (IntNode child : children) {
            parent.insertChild(child);
        }
        return parent;
    }

    @BeforeEach
    //tree data source: https://en.wikipedia.org/wiki/File:Minimax.svg
    public void setUpTree() {
        //layers and nodes are 0 based
        //root would be layer0Node0
        //children of root are layer1Node0 and layer1Node1

        //layer 3
        IntNode layer3Node0 = createParent(new IntNode(10), new IntNode(Integer.MAX_VALUE));
        IntNode layer3Node1 = createParent(new IntNode(5));
        IntNode layer3Node2 = createParent(new IntNode(-10));
        IntNode layer3Node3 = createParent(new IntNode(7), new IntNode(5));
        IntNode layer3Node4 = createParent(new IntNode(Integer.MIN_VALUE));
        IntNode layer3Node5 = createParent(new IntNode(-7), new IntNode(-5));
        //layer 2
        IntNode layer2Node0 = createParent(layer3Node0, layer3Node1);
        IntNode layer2Node1 = createParent(layer3Node2);
        IntNode layer2Node2 = createParent(layer3Node3, layer3Node4);
        IntNode layer2Node3 = createParent(layer3Node5);
        //layer1
        IntNode layer1Node0 = createParent(layer2Node0, layer2Node1);
        IntNode layer1Node1 = createParent(layer2Node2, layer2Node3);
        //root
        treeRoot = createParent(layer1Node0, layer1Node1);



    }

    @Test
    public void createRoot() {
        IntNode root = new IntNode(5);
        assertEquals(5, root.getContent().getValue());
        assertFalse(root.hasChildren());
    }

    @Test
    public void createChildNode() {
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
    public void insertChild() {
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
    
}
