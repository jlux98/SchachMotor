package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import gametree.ComputeChildrenException;
import gametree.Node;

public class NodeHelper {

    /**
    * Verify that the node passed as "deleted" was deleted.
    * If the deleted node was the parent's only child, an empty array or null must be passed to this method.
    * @param parent the parent of the node that was deleted
    * @param deleted the node that was deleted
    * @param leftOver the leftover children of the parent node, in the same order as returned by parent.queryChildren()
    */
    public static void verifyDeletion(Node<?> parent, Node<?> deleted, Node<?>... leftOver) throws ComputeChildrenException {
        assertTrue(deleted.getParent() == null);
        verifyChildren(parent, leftOver);
    }

    /**
     * Verify that the node passed as parent has exactly the specified children (compared by reference).
     * If the node has no children, an empty array or null must be passed to this method.
     * <br><br>
     * If this method throws ComputeChildrenException, 
     * queryChildren() attempted to generate children although parent stores children that should simply be returned instead.
     * @param parent the node whose children should be verified
     * @param expectedChildren the expected children of the parent node, in the same order as returned by parent.queryChildren()
     */
    public static void verifyChildren(Node<?> parent, Node<?>... expectedChildren) throws ComputeChildrenException {
        if (expectedChildren == null || expectedChildren.length == 0) {
            //verify that parent has no children
            assertFalse(parent.hasChildren());

            //queryChildren() cannot be called:

            //calling queryChildren() calls computeChildren() because node has no children
            //computeChildren() is not implemented by inttnode -> ComputeChildrenException

            //but as this method accepts any Node, Nodes are not guaranteed to throw ComputeChildrenException, so
            //assertthrows(ComputeChildrenException.class, ...) is not possible either

            //assertEquals(0, parent.queryChildren().size());
        } else {
            //verify that parent has the expected children
            assertTrue(parent.hasChildren());
            List<? extends Node<?>> actualChildren = parent.getOrCompute(); //should never throw ComputeChildrenException
            assertEquals(expectedChildren.length, actualChildren.size());

            //compare expected and actual children
            //also verify reference to parent
            for (int i = 0; i < expectedChildren.length; i++) {
                assertTrue(expectedChildren[i] == actualChildren.get(i));
                assertTrue(actualChildren.get(i).getParent() == parent);
            }
        }
    }
}
