package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import classes.EvaluableTestNode;
import classes.IntNode;
import gametree.GameNode;
import gametree.Node;
import minimax.AscendingValueComparator;
import minimax.DescendingValueComparator;
import model.Position;
import uciservice.FenParser;

public class ValueComparatorTest {
    private DescendingValueComparator whiteComparator = new DescendingValueComparator();
    private AscendingValueComparator blackComparator = new AscendingValueComparator();

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
        List<Node<Integer>> assortedNodes = Arrays.asList(new IntNode(-21), new IntNode(-5), new IntNode(-17), new IntNode(95),
                new IntNode(70), new IntNode(81), new IntNode(-6), new IntNode(71), new IntNode(0));
        List<Node<Integer>> sortedNodes = Arrays.asList(new IntNode(-21), new IntNode(-17), new IntNode(-6), new IntNode(-5),
                new IntNode(0), new IntNode(70), new IntNode(71), new IntNode(81), new IntNode(95));

        //white looks at highest value first
        Collections.reverse(sortedNodes);
        assortedNodes.sort(whiteComparator);
        assertEquals(sortedNodes, assortedNodes);
    }

    @Test
    public void blackSortingTest() {
        List<Node<Integer>> assortedNodes = Arrays.asList(new IntNode(-21), new IntNode(-5), new IntNode(-17), new IntNode(95),
                new IntNode(70), new IntNode(81), new IntNode(-6), new IntNode(71), new IntNode(0));
        List<Node<Integer>> sortedNodes = Arrays.asList(new IntNode(-21), new IntNode(-17), new IntNode(-6), new IntNode(-5),
                new IntNode(0), new IntNode(70), new IntNode(71), new IntNode(81), new IntNode(95));

        assortedNodes.sort(blackComparator);
        assertEquals(sortedNodes, assortedNodes);
    }

    @Test
    public void blackComparReusesOldLeafValuesTest() {
        List<EvaluableTestNode> nodes = Arrays.asList(new EvaluableTestNode(0), new EvaluableTestNode(1), new EvaluableTestNode(2),
                new EvaluableTestNode(3), new EvaluableTestNode(4), new EvaluableTestNode(5), new EvaluableTestNode(6),
                new EvaluableTestNode(7));
        for (EvaluableTestNode node : nodes) {
            node.computeOrGetLeafValueOrBetter(3); //increment each value by 3
        }
        nodes.sort(blackComparator);
        EvaluableTestNode node;
        for (int i = 0; i < nodes.size(); i++) {
            node = nodes.get(i);
            assertEquals(i + 3, node.computeOrGetStaticValueOrBetter());
            assertEquals(0, node.getComputeStaticValueCalls());
            assertEquals(1, node.getComputeLeafValueCalls());
        }
    }

    @Test
    public void blackComparatorReusesOldExplicitValuesTest() {
        List<EvaluableTestNode> nodes = Arrays.asList(new EvaluableTestNode(42), new EvaluableTestNode(42),
                new EvaluableTestNode(42));
        for (EvaluableTestNode node : nodes) {
            node.setValue(7);
        }

        nodes.sort(blackComparator);

        EvaluableTestNode node;
        for (int i = 0; i < nodes.size(); i++) {
            node = nodes.get(i);
            assertEquals(7, nodes.get(i).computeOrGetStaticValueOrBetter());
            assertEquals(0, node.getComputeStaticValueCalls());
            assertEquals(0, node.getComputeLeafValueCalls());
        }
    }

    @Test
    public void whiteComparatorReusesOldLeafValuesTest() {
        List<EvaluableTestNode> nodes = Arrays.asList(new EvaluableTestNode(0), new EvaluableTestNode(1), new EvaluableTestNode(2),
                new EvaluableTestNode(3), new EvaluableTestNode(4), new EvaluableTestNode(5), new EvaluableTestNode(6),
                new EvaluableTestNode(7));
        for (EvaluableTestNode node : nodes) {
            node.computeOrGetLeafValueOrBetter(3); //increment each value by 3
        }

        nodes.sort(whiteComparator);

        EvaluableTestNode node;
        for (int i = 0; i < nodes.size(); i++) {
            node = nodes.get(i);
            assertEquals(7 - i + 3, node.computeOrGetStaticValueOrBetter());
            assertEquals(0, node.getComputeStaticValueCalls());
            assertEquals(1, node.getComputeLeafValueCalls());
        }
    }

    @Test
    public void whiteComparatorReusesOldExplicitValuesTest() {
        List<EvaluableTestNode> nodes = Arrays.asList(new EvaluableTestNode(42), new EvaluableTestNode(42),
                new EvaluableTestNode(42));
        for (EvaluableTestNode node : nodes) {
            node.setValue(7);
        }

        nodes.sort(whiteComparator);

        EvaluableTestNode node;
        for (int i = 0; i < nodes.size(); i++) {
            node = nodes.get(i);
            assertEquals(7, nodes.get(i).computeOrGetStaticValueOrBetter());
            assertEquals(0, node.getComputeStaticValueCalls());
            assertEquals(0, node.getComputeLeafValueCalls());
        }
    }

    @Test
    public void sortGameNodesDescendingTest() {
        GameNode whiteLosing = new GameNode(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/R1B1K1NR b KQkq - 0 1"));
        GameNode blackLosing = new GameNode(FenParser.parseFen("2bqkbnr/3ppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 0 1"));
        GameNode neutral = new GameNode(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        List<GameNode> nodes = Arrays.asList(neutral, whiteLosing, blackLosing);

        nodes.sort(whiteComparator);

        assertTrue(nodes.get(0) == blackLosing); 
        assertTrue(nodes.get(1) == neutral);
        assertTrue(nodes.get(2) == whiteLosing);
    }

    @Test
    public void sortGameNodeAscendingTest() {
        GameNode whiteLosing = new GameNode(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/R1B1K1NR b KQkq - 0 1"));
        GameNode blackLosing = new GameNode(FenParser.parseFen("2bqkbnr/3ppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 0 1"));
        GameNode neutral = new GameNode(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        List<GameNode> nodes = Arrays.asList(neutral, whiteLosing, blackLosing);

        nodes.sort(blackComparator);

        assertTrue(nodes.get(0) == whiteLosing);
        assertTrue(nodes.get(1) == neutral);
        assertTrue(nodes.get(2) == blackLosing);
    }
}
