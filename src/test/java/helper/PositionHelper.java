package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.Position;
import movegenerator.MoveGenerator;
import uciservice.FenParser;

public class PositionHelper {

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
