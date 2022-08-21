package tests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import gametree.ComputeChildrenException;
import gametree.GameNode;
import gametree.Node;
import model.Position;
import movegenerator.MoveGenerator;
import testclasses.TestHelper;
import uciservice.FenParser;

public class GameNodeTest {
    public static void main(String[] args) {
        Position mate = FenParser.parseFen("8/3k4/8/2RRR3/8/2rrr3/8/3K4 b - - 0 1");
        Position[] followUps = MoveGenerator.generatePossibleMoves(mate);
        System.out.println(followUps.length);
    }

    /**
     * Extracts the positions stored by nodes children.
     * Calls queryChildren() to retrieve the nodes children.
     * @param parent the node whose childrens' positions should be extracted
     * @return a list of the positions stored by the node's children
     */
    private List<Position> extractChildPositions(GameNode parent) throws ComputeChildrenException {
        List<? extends Node<Position>> children = parent.queryChildren();
        List<Position> childPositions = new ArrayList<Position>(children.size());
        for (int i = 0; i < children.size(); i++) {
            childPositions.add(children.get(i).getContent());
        }
        return childPositions;
    }

    /**
     * uses data from {@link MoveGeneratorTest#generatePossibleMovesTest()}()
     */
    @Test
    public void queryChildrenGeneratesChildren() throws ComputeChildrenException {
        Position position = FenParser.parseFen(TestHelper.generatePossibleMovesFen);
        GameNode root = GameNode.createRoot(position);

        //extract positions from child nodes
        List<Position> followUpPositions = extractChildPositions(root);

        TestHelper.compareFenStringsToPosition(TestHelper.followUpMoves, followUpPositions);
    }

    /**
     * uses data from {@link MoveGeneratorTest#generatePossibleMovesTest()}().
     * queryChildren() changes behavior after the first call:
     * instead of generating children it will returned the stored children
     */
    @Test
    public void queryChildrenCalledTwice() throws ComputeChildrenException {

        Position position = FenParser.parseFen(TestHelper.generatePossibleMovesFen);
        GameNode root = GameNode.createRoot(position);
        root.queryChildren(); //initial call; computes children
        List<Position> followUpPositions = extractChildPositions(root); //2nd call, retrieves stored children

        TestHelper.compareFenStringsToPosition(TestHelper.followUpMoves, followUpPositions);
    }

    /**
     * checkmate in which the king is currently attacked
     */
    @Test
    public void queryChildrenCheckmateKingAttackedThrowsComputeChildrenException() throws ComputeChildrenException {
        Position whiteMate = FenParser.parseFen("8/3K4/8/2rrr3/8/8/8/3k4 w - - 0 1");
        GameNode mateNode = GameNode.createRoot(whiteMate);
        assertThrows(ComputeChildrenException.class, () -> mateNode.queryChildren());
    }

    /**
     * checkmate in which the king is not currently attacked
     */
    @Test
    public void queryChildrenCheckmateKingNotAttackedThrowsComputeChildrenException() throws ComputeChildrenException {
        Position blackMate = FenParser.parseFen("k7/4R3/8/1R6/8/8/8/K7 b - - 0 1");
        GameNode mateNode = GameNode.createRoot(blackMate);
        assertThrows(ComputeChildrenException.class, () -> mateNode.queryChildren());
    }

}
