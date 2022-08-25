package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import classes.IntNode;

public class IntNodeHelper {

    /**
    * Creates a parent IntNode for the passed children.
    * @param children IntNodes that a parent should be generated for
    * @return a parent IntNode with the specified nodes as children
    */
    public static IntNode createParent(IntNode... children) {
        IntNode parent = new IntNode(0);
        for (IntNode child : children) {
            parent.insertChild(child);
        }
        return parent;
    }

    /**
     * asserts that the IntNode holds the specified value
     * @param expected the expected value
     * @param node the IntNode that should be storing this value
     */
    public static void compareIntNodeValue(int expected, IntNode node) {
        assertEquals(expected, node.getContent().getValue());
    }
}
