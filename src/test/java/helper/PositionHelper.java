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
     * Asserts that expected and actual follow-ups are equal.
     * @param expectedFollowUps the list of expected follow-up positions
     * @param actualFollowUps the list of actually computed follow-up positions
     */
    public static void comparePositions(List<Position> expectedFollowUps, List<Position> actualFollowUps) {
        //sort because equals takes ordering into account for lists
        Collections.sort(expectedFollowUps);
        Collections.sort(actualFollowUps);
         
        assertEquals(expectedFollowUps, actualFollowUps);
    }

    /**
     * Mirrors the expected follow-up positions and compares them to the actual follow-up positions.
     * The actual follow-up positions must be compute by MoveGenerator for the mirrored position and passed to this method.
     * @param expectedFollowUps the unmirrored expected follow-ups
     * @param actualMirroredPositionFollowUps the follow-ups generated by MoveGenerator for the mirrored Position
     */
    public static void compareMirroredExpectedPositions(List<Position> expectedFollowUps, List<Position> actualMirroredPositionFollowUps) {
        //mirror expected
        List<Position> mirroredExpectedFollowUps = new ArrayList<Position>(expectedFollowUps.size());
        for (Position expected : expectedFollowUps) {
            mirroredExpectedFollowUps.add(Mirror.mirrorFollowUpPosition(expected));
        }

        comparePositions(mirroredExpectedFollowUps, actualMirroredPositionFollowUps);
    }

    /**
     * Mirrors the expected follow-up positions denoted by FEN-strings and compares them to the actual follow-up positions.
     * The actual follow-up positions must be compute by MoveGenerator for the mirrored position and passed to this method.
     * @param expectedFollowUps the unmirrored expected follow-ups as FEN-strings
     * @param actualMirroredPositionFollowUps the follow-ups generated by MoveGenerator for the mirrored Position
     */
    public static void compareMirroredFenStringsToPosition(Collection<String> fenStrings, Collection<Position> actualMirroredPositionFollowUps) {
        compareMirroredExpectedPositions(fenStringsToPositions(fenStrings), toList(actualMirroredPositionFollowUps));
    }

    /**
     * Compares the positions denoted as fen strings against the list of positions.
     * @param fenStrings the expected follow-up positions as fen strings
     * @param positions the actually computed follow-up positions
     */
    public static void compareFenStringsToPosition(Collection<String> fenStrings, Collection<Position> positions) {
        //assure that expected and actual positions are stored in the same type of data structure (list)
        List<Position> calculatedPositions = toList(positions);
        //translate the fen strings into positions
        List<Position> fenStringPositions = fenStringsToPositions(fenStrings);
        comparePositions(fenStringPositions, calculatedPositions);
    }

    /**
    * Converts the collection into a list if it is not already one.
    * @param positions 
    * @return positions as a list
    */
    private static List<Position> toList(Collection<Position> positions) {
        if (positions instanceof List<Position>) {
            return (List<Position>) positions;
        }
        return new ArrayList<Position>(positions);
    }

    /**
     * Parses the list of FEN-Strings into a list of Positions.
     * @param fenStrings 
     * @return the corresponding positions
     */
    private static List<Position> fenStringsToPositions(Collection<String> fenStrings) {
        List<Position> positions = new ArrayList<Position>(fenStrings.size());
        for (String fen : fenStrings) {
            positions.add(FenParser.parseFen(fen));
        }
        return positions;
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
