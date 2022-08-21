package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.Position;
import movegenerator.MoveGenerator;
import uciservice.FenParser;

public class PositionHelper {

    /**
     * Compares the positions denoted as fen strings against the list of positions.
     */
    public static void compareFenStringsToPosition(Collection<String> fenStrings, Collection<Position> positions) {
        //assure that the caluclated positions are stored in a sortable data structure
        List<Position> calculatedPositions = new ArrayList<Position>(positions);
        //translate the fen strings into boards
        List<Position> fenStringPositions = new ArrayList<Position>(fenStrings.size());
        for (String fen : fenStrings) {
            fenStringPositions.add(FenParser.parseFen(fen));

        }
        //sort expected and calculated boards
        Collections.sort(fenStringPositions);
        Collections.sort(calculatedPositions);
        //compare both lists
        assertEquals(fenStringPositions, calculatedPositions);
    }

    /**
     * Verifies that no moves can be generated for the given position
     */
    public static void verifyStaleMate(Position mate) {
        Position[] followUps = MoveGenerator.generatePossibleMoves(mate);
        assertEquals(0, followUps.length);
    }

    /**
    * Verifies that no moves can be generated for the position represented by the fen string
    */
    public static void verifyStaleMate(String mateFen) {
        Position mate = FenParser.parseFen(mateFen);
        verifyStaleMate(mate);
    }

    /**
     * Verifies that the specified player is checkmated and has their king currently being attacked.
     * Only the specified player may be checkmated.
     */
    public static void verifyCheckMate(Position mate, boolean whiteCheckMated) {
        verifyStaleMate(mate);
        if (whiteCheckMated) {
            assertTrue(mate.getWhiteInCheck());
            assertFalse(mate.getBlackInCheck());
        } else {
            assertFalse(mate.getWhiteInCheck());
            assertTrue(mate.getBlackInCheck());
        }
    }

    /**
     * Verifies that the specified player is checkmated and has their king currently being attacked.
     * Only the specified player may be checkmated.
     */
    public static void verifyCheckMate(String mateFen, boolean whiteCheckMated) {
        Position mate = FenParser.parseFen(mateFen);
        verifyCheckMate(mate, whiteCheckMated);
    }
}
