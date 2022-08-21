package tests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import data.MoveGeneratorData;
import gametree.ComputeChildrenException;
import gametree.GameNode;
import helper.FenHelper;
import helper.GameNodeHelper;
import model.Position;
import uciservice.FenParser;

public class GameNodeTest {


    /**
     * uses data from {@link MoveGeneratorTest#generatePossibleMovesTest()}()
     */
    @Test
    public void queryChildrenGeneratesChildren() throws ComputeChildrenException {
        Position position = FenParser.parseFen(MoveGeneratorData.allBlackPiecesFen);
        GameNode root = GameNode.createRoot(position);

        //extract positions from child nodes
        List<Position> followUpPositions = GameNodeHelper.extractChildPositions(root);

        FenHelper.compareFenStringsToPosition(MoveGeneratorData.allBlacKPiecesFenFollowUpMoves, followUpPositions);
    }

    /**
     * uses data from {@link MoveGeneratorTest#generatePossibleMovesTest()}().
     * queryChildren() changes behavior after the first call:
     * instead of generating children it will returned the stored children
     */
    @Test
    public void queryChildrenCalledTwice() throws ComputeChildrenException {

        Position position = FenParser.parseFen(MoveGeneratorData.allBlackPiecesFen);
        GameNode root = GameNode.createRoot(position);
        root.queryChildren(); //initial call; computes children
        List<Position> followUpPositions = GameNodeHelper.extractChildPositions(root); //2nd call, retrieves stored children

        FenHelper.compareFenStringsToPosition(MoveGeneratorData.allBlacKPiecesFenFollowUpMoves, followUpPositions);
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
