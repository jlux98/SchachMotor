package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import classes.IntNode;
import gametree.Node;
import minimax.BlackNodeComparator;
import minimax.WhiteNodeComparator;

public class NodeComparatorTest {
    private WhiteNodeComparator<Integer> whiteComparator = new WhiteNodeComparator<Integer>();
    private BlackNodeComparator<Integer> blackComparator = new BlackNodeComparator<Integer>();

    @Test
    public void positiveLessThanTest() {
        IntNode less = new IntNode(5);
        IntNode more = new IntNode(8);
        assertTrue(whiteComparator.compare(less, more) > 0);
        assertTrue(blackComparator.compare(less, more) < 0);
    }

    @Test
    public void negativeLessThanTest() {
        IntNode less = new IntNode(-8);
        IntNode more = new IntNode(-5);
        assertTrue(whiteComparator.compare(less, more) > 0);
        assertTrue(blackComparator.compare(less, more) < 0);
    }

    @Test
    public void mixedLessThanTest() {
        IntNode less = new IntNode(-8);
        IntNode more = new IntNode(5);
        assertTrue(whiteComparator.compare(less, more) > 0);
        assertTrue(blackComparator.compare(less, more) < 0);
    }

    @Test
    public void positiveGreaterThanTest() {
        IntNode less = new IntNode(5);
        IntNode more = new IntNode(8);
        assertTrue(whiteComparator.compare(more, less) < 0);
        assertTrue(blackComparator.compare(more, less) > 0);

    }

    @Test
    public void negativeGreaterThanTest() {
        IntNode less = new IntNode(-8);
        IntNode more = new IntNode(-5);
        assertTrue(whiteComparator.compare(more, less) < 0);
        assertTrue(blackComparator.compare(more, less) > 0);

    }

    @Test
    public void mixedGreaterThanTest() {
        IntNode less = new IntNode(-8);
        IntNode more = new IntNode(5);
        assertTrue(whiteComparator.compare(more, less) < 0);
        assertTrue(blackComparator.compare(more, less) > 0);

    }

    @Test
    public void positiveEqualToTest() {
        IntNode node = new IntNode(3);
        IntNode equalNode = new IntNode(3);
        assertTrue(whiteComparator.compare(node, equalNode) == 0);
        assertTrue(blackComparator.compare(node, equalNode) == 0);
    }

    @Test
    public void negativeEqualToTest() {
        IntNode node = new IntNode(-3);
        IntNode equalNode = new IntNode(-3);
        assertTrue(whiteComparator.compare(node, equalNode) == 0);
        assertTrue(blackComparator.compare(node, equalNode) == 0);
    }

    @Test
    public void flippedSignNotEqualTest() {
        IntNode node = new IntNode(-3);
        IntNode unequalNode = new IntNode(3);
        assertTrue(whiteComparator.compare(node, unequalNode) != 0);
        assertTrue(whiteComparator.compare(unequalNode, node) != 0);

        assertTrue(blackComparator.compare(node, unequalNode) != 0);
        assertTrue(blackComparator.compare(unequalNode, node) != 0);
    }

    @Test
    public void whiteSortingTest() {
        List<Node<Integer>> assortedNodes = Arrays.asList(new IntNode(-21), new IntNode(-5), new IntNode(-17),
                new IntNode(95), new IntNode(70), new IntNode(81), new IntNode(-6), new IntNode(71), new IntNode(0));
        List<Node<Integer>> sortedNodes = Arrays.asList(new IntNode(-21), new IntNode(-17), new IntNode(-6),
                new IntNode(-5), new IntNode(0), new IntNode(70), new IntNode(71), new IntNode(81), new IntNode(95));

        Collections.reverse(sortedNodes);
        assortedNodes.sort(new WhiteNodeComparator<Integer>());
        assertEquals(sortedNodes, assortedNodes);
    }

    @Test
    public void blackSortingTest() {
        List<Node<Integer>> assortedNodes = Arrays.asList(new IntNode(-21), new IntNode(-5), new IntNode(-17),
                new IntNode(95), new IntNode(70), new IntNode(81), new IntNode(-6), new IntNode(71), new IntNode(0));
        List<Node<Integer>> sortedNodes = Arrays.asList(new IntNode(-21), new IntNode(-17), new IntNode(-6),
                new IntNode(-5), new IntNode(0), new IntNode(70), new IntNode(71), new IntNode(81), new IntNode(95));

        assortedNodes.sort(new BlackNodeComparator<Integer>());
        assertEquals(sortedNodes, assortedNodes);
    }
}
