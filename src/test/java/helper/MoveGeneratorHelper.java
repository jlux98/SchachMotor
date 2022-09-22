package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import model.Coordinate;
import model.Position;
import movegenerator.MoveGenerator;

public class MoveGeneratorHelper {
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
     * @param expectedFenStrings the expected follow-up positions as FEN-strings
     */
    public static void verifyPieceMoveGeneration(Position position, byte piece, int rank, int file, List<String> expectedFenStrings) {
        Position mirroredPosition = Mirror.mirrorPosition(position);
        Coordinate coordinate = new Coordinate(rank,file);
        Coordinate mirroredCoordinate = Mirror.mirrorCoordinate(coordinate);
        int mirroredRank = mirroredCoordinate.getRank();
        int mirroredFile = mirroredCoordinate.getFile();

        //verify correct piece on (rank, file)
        assertEquals(piece, position.getByteAt(rank, file), "incorrect piece in un-mirrored position");
        assertEquals(Mirror.changeColor(piece), mirroredPosition.getByteAt(mirroredRank, mirroredFile),"incorrect piece in mirrored position" );

        
        List<Position> actualFollowUps = MoveGenerator.generatePossibleMovesPerPiece(position, rank, file);
        List<Position> actualMirroredPositionFollowUps = MoveGenerator.generatePossibleMovesPerPiece(mirroredPosition, mirroredRank, mirroredFile);

        //compare expected positions and actual follow-ups
        PositionHelper.compareFenStringsToPosition(expectedFenStrings, actualFollowUps);
        //compare mirrored expected positions and actual follow-ups for mirrored position
        PositionHelper.mirrorFenStringsAndCompareToPosition(expectedFenStrings, actualMirroredPositionFollowUps);
    }
}
