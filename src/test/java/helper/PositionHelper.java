package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.Board;
import model.ByteBoard;
import model.Coordinate;
import model.Move;
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
     * Mirrors the board access horizontally.
     * <p>
     * E.g.:
     * <p> 
     * <code>(0, 0) = (a8) -> (7, 0) = (a1)</code>
     * <p>
     * <code>(3, 1) = (b3) -> (4, 1) = (b4)</code>
     * @param rank rank of the access that should be mirrored
     * @param file file of the access that should be mirrored
     * @param board board that is accessed
     * @return the byte at the mirrored access
     */

    public static byte mirroredGetByteAt(int rank, int file, Board board) {
        return board.getByteAt(7 - rank, file);
    }

    /**
     * Mirrors the board access horizontally
     * and sets the corresponding piece.
     * <p>
     * E.g.:
     * <p> 
     * <code>(0, 0) = (a8) -> (7, 0) = (a1)</code>
     * <p>
     * <code>(3, 1) = (b3) -> (4, 1) = (b4)</code>
     * @param rank rank of the access that should be mirrored
     * @param file file of the access that should be mirrored
     * @param board board that the piece should be placed on
     * @param piece the piece that should be placed
     */

    public static void mirroredsetByteAt(int rank, int file, Board board, byte piece) {
        board.setByteAt(7 - rank, file, piece);
    }

    /**
     * Mirrors the coordiante horizontally.
     * @param coordinate the coordinate that should be mirrored
     * @return the mirrored coordinate or null if the coordinate is null
     */
    public static Coordinate mirrorCoordinate(Coordinate coordinate) {
        if (coordinate == null) {
            return null;
        }
        return new Coordinate(7 - coordinate.getRank(), coordinate.getFile());
    }

    /**
     * Mirrors the move horizontally.
     */
    public static Move mirrorMove(Move move) {
        byte promotedTo = changeColor(move.getPromotedTo());
        return new Move(mirrorCoordinate(move.getStartingSpace()), mirrorCoordinate(move.getTargetSpace()), promotedTo);
    }

    /**
     * Changes the color of the piece from black to white and vice versa.
     * @param piece the piece whose color should be changed
     * @return the piece with the changed color
     */
    public static byte changeColor(byte piece) {
        if (piece == 0) {
            return 0;
        }
        if (piece > 6) {
            //make white piece
            return (byte) (piece - 6);
        }
        if (piece <= 6) {
            return (byte) (piece + 6);
        }
        throw new IllegalArgumentException("piece must be a value between 0 and 12");
    }

    /**
     * Mirrors the board horizontally and replaces white with black pieces.
     * @param board the board that should be mirrored
     * @return the mirrored board
     */
    public static Board mirrorBoard(Board board) {
        Board mirroredBoard = ByteBoard.createEmpty();
        byte piece;

        for (byte file = 0; file < 8; file++) {
            for (byte rank = 0; rank < 8; rank++) {
                assertEquals(MoveGenerator.EMPTY_SQUARE, PositionHelper.mirroredGetByteAt(rank, file, mirroredBoard));
                piece = PositionHelper.changeColor(board.getByteAt(rank, file));
                PositionHelper.mirroredsetByteAt(rank, file, mirroredBoard, piece);
            }
        }
        return mirroredBoard;
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

    public static Position mirrorPosition(Position position) {

        //replace every piece with the opposite color and place it at the mirrored position

        //attack maps should swap and be mirrorred (attacked by white = mirror (attacked by black)

        /* private Board board;
        
        private boolean whiteInCheck; <- swap with blackincheck
        private boolean blackInCheck; <- swap with whiteincheck
        
        private boolean whiteNextMove;
        
        private boolean whiteCastlingKingside; <- swap with blackCastlingKingside
        private boolean whiteCastlingQueenside; <- swap with blackCastlingQueenside
        private boolean blackCastlingKingside; <- swap with whiteCastlingKingside
        private boolean blackCastlingQueenside; <- swap with  blackCastlingQueenside
        
        private Coordinate enPassantTargetSpace; <- do not change
        
        private int halfMovesSincePawnMoveOrCapture; <- do not change
        private int fullMoveCount; <- do not change
        
        private boolean[][] attackedByWhite; <- mirror and make black
        private boolean[][] attackedByBlack; <- mirror and make white
        
        private Move generatedByMove;  <- mirror */

        Board mirroredBoard = PositionHelper.mirrorBoard(position.getBoard());

        boolean mirroredWhiteInCheck = position.getBlackInCheck();
        boolean mirroredBlackInCheck = position.getWhiteInCheck();

        boolean mirroredWhiteNextMove = !position.getWhiteNextMove();

        boolean mirroredWhiteCastlingKingside = position.getBlackCastlingKingside();
        boolean mirroredWhiteCastlingQueenside = position.getBlackCastlingQueenside();

        boolean mirroredBlackCastlingKingside = position.getWhiteCastlingKingside();
        boolean mirroredBlackCastlingQueenside = position.getBlackCastlingQueenside();

        Coordinate mirroredEnPAssantTargetSpace = mirrorCoordinate(position.getEnPassantTargetSpace());
        int mirroredEnPassantTargetRank;
        int mirroredEnPassantTargetFile;
        if (mirroredEnPAssantTargetSpace != null) {
            mirroredEnPassantTargetRank = mirroredEnPAssantTargetSpace.getRank();
            mirroredEnPassantTargetFile = mirroredEnPAssantTargetSpace.getFile();
        } else {
            mirroredEnPassantTargetRank = -1;
            mirroredEnPassantTargetFile = -1;
        }

        int mirroredHalfMoves = position.getHalfMoves();
        int mirroredFullMoves = position.getFullMoves();

        Position mirroredPosition = new Position(mirroredWhiteInCheck, mirroredBlackInCheck, mirroredBoard, mirroredWhiteNextMove,
                mirroredWhiteCastlingKingside, mirroredWhiteCastlingQueenside, mirroredBlackCastlingKingside,
                mirroredBlackCastlingQueenside, mirroredEnPassantTargetRank, mirroredEnPassantTargetFile,
                mirroredHalfMoves, mirroredFullMoves);

        if (position.getMove() != null) {
            Move mirroredGeneratedByMove = PositionHelper.mirrorMove(position.getMove());
            mirroredPosition.setMove(mirroredGeneratedByMove);
        }

        return mirroredPosition;
    }
}
