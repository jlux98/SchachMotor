package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import classes.GeneratingIntNode;
import gametree.ImpTree;
import gametree.Node;
import helper.IntTreeEvaluationHelper;
import minimax.GenericAlphaBetaPruning;

public class AlphaBetaIntTreeTest extends IntTreeEvaluationTest {
    public AlphaBetaIntTreeTest() {
        super(new IntTreeEvaluationHelper(() -> new GenericAlphaBetaPruning<Integer>()));
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
        helper.evaluateTree(new ImpTree<GeneratingIntNode>(parent), 2, true);
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
        helper.evaluateTree(new ImpTree<GeneratingIntNode>(parent), 2, false);
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
}
