package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import classes.MoveComparator;
import model.Coordinate;
import model.Move;
import model.Position;
import movegenerator.MoveGenerator;
import uciservice.FenParser;
import static model.PieceEncoding.*;

public class MoveGeneratorHelper {

    /**
     * Asserts that MoveGeneration generates the same moves for black as for white.
     * Mirrors the given position and compares the follow-ups of
     * the original and mirrored position.
     * @param position position for which should be verified that black and white generate the same moves
     */
    public static void compareWhiteAndBlackMoveGeneration(Position position) {
        Position mirroredPosition = Mirror.mirrorPosition(position);

        Position[] followUps = MoveGenerator.generatePossibleMoves(position);
        Position[] mirroredPositionFollowUps = MoveGenerator.generatePossibleMoves(mirroredPosition);

        List<Position> followUpsList = new ArrayList<Position>(followUps.length);
        List<Position> mirroredPositionFollowUpsList = new ArrayList<Position>(mirroredPositionFollowUps.length);

        Collections.addAll(followUpsList, followUps);
        Collections.addAll(mirroredPositionFollowUpsList, mirroredPositionFollowUps);

        mirrorExpectedAndComparePositions(followUpsList, mirroredPositionFollowUpsList);
    }

    /**
     * Wrapper for {@link #compareWhiteAndBlackMoveGeneration(Position)} accepting FEN-strings for positions.
     * @param fen the fen representing the testing position
     */
    public static void compareWhiteAndBlackMoveGeneration(String fen) {
        compareWhiteAndBlackMoveGeneration(FenParser.parseFen(fen));
    }

    /**
     * Sorts a list of positions by the algebraic representation of the moves stored by the positions.
     * @param positions
     */
    private static void sortPositionsByMove(List<Position> positions) {
        positions.sort(new MoveComparator());
    }

    /**
     * 
     * @param positions
     * @return the list of moves in algebraic notation stored by the positions
     */
    private static List<String> extractAlgebraicMoves(List<Position> positions) {
        List<String> moves = new ArrayList<String>(positions.size());
        for (Position position : positions) {
            moves.add(position.getMove().toStringAlgebraic());
        }
        return moves;
    }

    /**
     * Mirrors the passed move and returns its algebraic representation.
     * @param algebraicMove a move in algebraic notation
     * @return the mirrored move
     */
    private static String mirrorAlgebraicMove(String algebraicMove) {
        Move move = new Move(algebraicMove);
        return Mirror.mirrorMove(move).toStringAlgebraic();
    }

    /**
     * Verifies that the move generation generates exactly the expected moves.
     * <p>
     * Additionally, mirrors the passed position and expected moves and verifies that the move generation
     * also generates the expected moves for the mirrored position.
     * @param position the test position
     * @param expectedAlgebraicMovesArray the expected moves in algebraic notation
     */
    public static void verifyMoveGeneration(Position position, String... expectedAlgebraicMovesArray) {
        List<String> expectedAlgebraicMoves = toList(expectedAlgebraicMovesArray);

        Position mirroredPosition = Mirror.mirrorPosition(position);

        //get follow-up moves
        List<String> actualFollowUpMoves = extractAlgebraicMoves(toList(MoveGenerator.generatePossibleMoves(position)));

        //get follow ups to mirrored position
        List<String> mirroredPositionActualFollowUpMoves = extractAlgebraicMoves(
                toList(MoveGenerator.generatePossibleMoves(mirroredPosition)));

        //mirror expected followups
        List<String> mirroredExpectedAlgebraicMoves = new ArrayList<String>(expectedAlgebraicMoves.size());
        for (String expectedMove : expectedAlgebraicMoves) {
            mirroredExpectedAlgebraicMoves.add(mirrorAlgebraicMove(expectedMove));
        }

        Collections.sort(expectedAlgebraicMoves);        
        Collections.sort(actualFollowUpMoves);
        Collections.sort(mirroredExpectedAlgebraicMoves);
        Collections.sort(mirroredPositionActualFollowUpMoves);

        assertEquals(expectedAlgebraicMoves, actualFollowUpMoves);
        assertEquals(mirroredExpectedAlgebraicMoves, mirroredPositionActualFollowUpMoves);
    }

    public static void verifyMoveGeneration(String fen, String... expectedAlgebraicMoves) {
        verifyMoveGeneration(FenParser.parseFen(fen), expectedAlgebraicMoves);
    }

    /**
    * Verifies that the MoveGenerator generates the expected follow-up positions.
    * Computes the follow-ups using 
    * {@link MoveGenerator#generatePossibleMovesPerPiece(Position, int, int)
    *  MoveGenerator.generatePossibleMovesPerPiece(position, rank, file)}
    * and compares them to the expected follow ups.
    * <p>
    * Additionally, mirrors the passed position and the follow-up positions, invokes the MoveGeneration
    * on the mirrored position and compares the mirrored follow-ups with the actual follow-ups
    * of the mirrored position.
    * @param position the position that follow-up positions should be generated for
    * @param piece the piece on the specified rank and file 
    * (used to make it easier to spot faulty invocations)
    * @param rank rank of the piece to move
    * @param file file of the piece to move
    * @param expectedPositions the expected follow-up positions
    */
    public static void verifyPieceMoveGeneration(Position position, byte piece, int rank, int file,
    List<Position> expectedPositions) {
        
        if (piece < 1 || piece > UPPER_LIMIT) {
            throw new IllegalArgumentException("piece must be value between 1 and 12");
        }

        Position mirroredPosition = Mirror.mirrorPosition(position);
        Coordinate coordinate = new Coordinate(rank, file);
        Coordinate mirroredCoordinate = Mirror.mirrorCoordinate(coordinate);
        int mirroredRank = mirroredCoordinate.getRank();
        int mirroredFile = mirroredCoordinate.getFile();

        //verify correct piece on (rank, file)
        assertEquals(piece, position.getByteAt(rank, file), "incorrect piece in un-mirrored position");
        assertEquals(Mirror.changeColor(piece), mirroredPosition.getByteAt(mirroredRank, mirroredFile),
                "incorrect piece in mirrored position");

        List<Position> actualFollowUps = MoveGenerator.generatePossibleMovesPerPiece(position, rank, file);
        List<Position> actualMirroredPositionFollowUps = MoveGenerator.generatePossibleMovesPerPiece(mirroredPosition, mirroredRank,
                mirroredFile);

        //compare expected positions and actual follow-ups
        comparePositions(expectedPositions, actualFollowUps);
        //compare mirrored expected positions and actual follow-ups for mirrored position
        mirrorExpectedAndComparePositions(expectedPositions, actualMirroredPositionFollowUps);
    }

    /**
     * Wrapper for {@link #verifyPieceMoveGeneration(Position, byte, int, int, List)} 
     * that accepts the expected positions as a list of FEN-strings.
     * @param expectedFenStrings the expected follow-up positions as list of FEN-strings
     * @see #verifyPieceMoveGeneration(Position, byte, int, int, List)
     */
    public static void verifyPieceMoveGenerationWithFenStrings(Position position, byte piece, int rank, int file,
            List<String> expectedFenStrings) {
        verifyPieceMoveGeneration(position, piece, rank, file, fenStringsToPositions(expectedFenStrings));
    }

    /**
     * Asserts that expected and actual follow-ups are equal.
     * @param expectedFollowUps the list of expected follow-up positions
     * @param actualFollowUps the list of actually computed follow-up positions
     */
    public static void comparePositions(List<Position> expectedFollowUps, List<Position> actualFollowUps) {

        //check if no follow-ups are expected if actualFollowUps = null
        if (actualFollowUps == null) {
            assertEquals(expectedFollowUps.size(), 0);
            return;
        }

        //sort because equals takes ordering into account for lists
        Collections.sort(expectedFollowUps);
        Collections.sort(actualFollowUps);

        //compare
        assertEquals(expectedFollowUps, actualFollowUps);
    }

    /**
     * Mirrors the expected follow-up positions and compares them to the actual follow-up positions.
     * The actual follow-up positions must be compute by MoveGenerator for the mirrored position and passed to this method.
     * @param expectedFollowUps the unmirrored expected follow-ups
     * @param actualMirroredPositionFollowUps the follow-ups generated by MoveGenerator for the mirrored Position
     */
    public static void mirrorExpectedAndComparePositions(List<Position> expectedFollowUps,
            List<Position> actualMirroredPositionFollowUps) {
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
    public static void mirrorFenStringsAndCompareToPosition(Collection<String> fenStrings,
            Collection<Position> actualMirroredPositionFollowUps) {
        mirrorExpectedAndComparePositions(fenStringsToPositions(fenStrings), toList(actualMirroredPositionFollowUps));
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
    * @param collection 
    * @return the collection as a list
    */
    private static <ElementType> List<ElementType> toList(Collection<ElementType> collection) {
        if (collection instanceof List<ElementType>) {
            return (List<ElementType>) collection;
        }
        return new ArrayList<ElementType>(collection);
    }

    /**
    * Converts the array into a list.
    * @param array 
    * @return the array as a list
    */
    private static <ElementType> List<ElementType> toList(ElementType[] array) {
        return Arrays.asList(array);
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
}
