package tests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import data.MoveGeneratorData;
import gametree.ComputeChildrenException;
import gametree.GameNode;
import helper.GameNodeHelper;
import helper.MoveGeneratorHelper;
import model.Position;
import uciservice.FenParser;

/**
 * Abstract class providing tests for implementations of GameNode.
 * Extend this class and implement {@link #createRoot(Position)} 
 * to create instances of the GameNode subtype that should be tested.
 */
public abstract class GameNodeTestBase {

    /**
    * Creates a node without parent.
    * @param position the position to be stored in the node
    * @return a node serving as root for a gametree
    */
    protected abstract GameNode createRoot(Position position);

    /**
     * uses data from {@link MoveGeneratorTest#generatePossibleMovesTest()}()
     */
    @Test
    public void queryChildrenGeneratesChildren() throws ComputeChildrenException {
        Position position = FenParser.parseFen(MoveGeneratorData.allBlackPiecesFen);
        GameNode root = createRoot(position);

        //extract positions from child nodes
        List<Position> followUpPositions = GameNodeHelper.extractChildPositions(root);

        MoveGeneratorHelper.compareFenStringsToPosition(MoveGeneratorData.getExpectedAllBlackPiecesFenFollowUpMoves(), followUpPositions);
    }

    /**
     * uses data from {@link MoveGeneratorTest#generatePossibleMovesTest()}().
     * queryChildren() changes behavior after the first call:
     * instead of generating children it will returned the stored children
     */
    @Test
    public void queryChildrenCalledTwice() throws ComputeChildrenException {

        Position position = FenParser.parseFen(MoveGeneratorData.allBlackPiecesFen);
        GameNode root = createRoot(position);
        root.getOrCompute(); //initial call; computes children
        List<Position> followUpPositions = GameNodeHelper.extractChildPositions(root); //2nd call, retrieves stored children

        MoveGeneratorHelper.compareFenStringsToPosition(MoveGeneratorData.getExpectedAllBlackPiecesFenFollowUpMoves(), followUpPositions);
    }

    /**
     * checkmate in which the king is currently attacked
     */
    @Test
    public void queryChildrenCheckmateKingAttackedThrowsComputeChildrenException() throws ComputeChildrenException {
        Position whiteMate = FenParser.parseFen("8/3K4/8/2rrr3/8/8/8/3k4 w - - 0 1");
        GameNode mateNode = createRoot(whiteMate);
        assertThrows(ComputeChildrenException.class, () -> mateNode.getOrCompute());
    }

    /**
     * checkmate in which the king is not currently attacked
     */
    @Test
    public void queryChildrenCheckmateKingNotAttackedThrowsComputeChildrenException() throws ComputeChildrenException {
        Position blackMate = FenParser.parseFen("k7/4R3/8/1R6/8/8/8/K7 b - - 0 1");
        GameNode mateNode = createRoot(blackMate);
        assertThrows(ComputeChildrenException.class, () -> mateNode.getOrCompute());
    }

}
