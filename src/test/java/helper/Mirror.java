package helper;

import model.Board;
import model.ByteBoard;
import model.Coordinate;
import model.Move;
import model.Position;
import static model.PieceEncoding.*;

/**
 * Class used to mirror positions, boards, board accessing, moves and coordinates horizontally.
 */
public class Mirror {
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
        return new Move(mirrorCoordinate(move.getStartingSquare()), mirrorCoordinate(move.getTargetSquare()), promotedTo);
    }

    /**
     * Changes the color of the piece from black to white and vice versa.
     * @param piece the piece whose color should be changed
     * @return the piece with the changed color
     */
    public static byte changeColor(byte piece) {
        return switchBytePieceColor(piece);
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
                if (mirroredGetByteAt(rank, file, mirroredBoard) != EMPTY_SQUARE) {
                    throw new IllegalStateException("mirrored piece to an occupied square");
                }
                piece = changeColor(board.getByteAt(rank, file));
                mirroredsetByteAt(rank, file, mirroredBoard, piece);
            }
        }
        return mirroredBoard;
    }

   /**
    * Corrects the full-move count of a mirrored follow-up position.
    * @param followUp a follow-up position that was mirrored using {@link #mirrorPosition(Position)}
    */
    private static void correctMirroredFollowUpPositionMoveCount(Position followUp) {
        //Position mirroredPosition = mirrorPosition(followUp);
        if (followUp.getWhiteNextMove()) {
            //mirrored follow-up position represents a black move
            //(because next move after the position is white's)
            //thus the mirrored starting position (the position that followUp is a follow-up to) represents a white move
            //thus the un-mirrored starting position represents a black move

            //full-move count is incremented after black moves
            //follow-ups to the un-mirrored starting position are white moves and do not increase full-move count

            //increase full-move count of this mirrored white move to make it a correct black move
            followUp.setFullMoveCount(followUp.getFullMoves() + 1);
        } else {
            //mirrored follow-up position represents a white move
            //(because next move after the position is black's)
            //thus the mirrored starting position (the position that followUp is a follow-up to) represents a black move
            //thus the un-mirrored starting position represents a white move

            //full move count is incremented after black moves
            //follow-ups to the unmirrored starting position are black moves and increase full-move count

            //decrease full-move count of this mirrored black move to make it a correct white move
            followUp.setFullMoveCount(followUp.getFullMoves() - 1);
        }
    }

    /**
     * Mirrors a follow-up positions and corrects its full-move count.
     */
    public static Position mirrorFollowUpPosition(Position followUp) {
        Position mirrored = mirrorPosition(followUp);
        correctMirroredFollowUpPositionMoveCount(mirrored);
        return mirrored;
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
        
        private Coordinate enPassantTargetSquare; <- do not change
        
        private int halfMovesSincePawnMoveOrCapture; <- do not change
        private int fullMoveCount; <- do not change
        
        private boolean[][] attackedByWhite; <- mirror and make black
        private boolean[][] attackedByBlack; <- mirror and make white
        
        private Move generatedByMove;  <- mirror */

        Board mirroredBoard = mirrorBoard(position.getBoard());

        boolean mirroredWhiteInCheck = position.getBlackInCheck();
        boolean mirroredBlackInCheck = position.getWhiteInCheck();

        boolean mirroredWhiteNextMove = !position.getWhiteNextMove();

        boolean mirroredWhiteCastlingKingside = position.getBlackCastlingKingside();
        boolean mirroredWhiteCastlingQueenside = position.getBlackCastlingQueenside();

        boolean mirroredBlackCastlingKingside = position.getWhiteCastlingKingside();
        boolean mirroredBlackCastlingQueenside = position.getWhiteCastlingQueenside();

        Coordinate mirroredEnPAssantTargetSquare = mirrorCoordinate(position.getEnPassantTargetSquare());
        int mirroredEnPassantTargetRank;
        int mirroredEnPassantTargetFile;
        if (mirroredEnPAssantTargetSquare != null) {
            mirroredEnPassantTargetRank = mirroredEnPAssantTargetSquare.getRank();
            mirroredEnPassantTargetFile = mirroredEnPAssantTargetSquare.getFile();
        } else {
            mirroredEnPassantTargetRank = -1;
            mirroredEnPassantTargetFile = -1;
        }

        byte mirroredHalfMoves = position.getHalfMoves();
        int mirroredFullMoves = position.getFullMoves();

        Position mirroredPosition = new Position(mirroredWhiteInCheck, mirroredBlackInCheck, mirroredBoard, mirroredWhiteNextMove,
                mirroredWhiteCastlingKingside, mirroredWhiteCastlingQueenside, mirroredBlackCastlingKingside,
                mirroredBlackCastlingQueenside, mirroredEnPassantTargetRank, mirroredEnPassantTargetFile, mirroredHalfMoves,
                mirroredFullMoves);

        if (position.getMove() != null) {
            Move mirroredGeneratedByMove = mirrorMove(position.getMove());
            mirroredPosition.setMove(mirroredGeneratedByMove);
        }

        return mirroredPosition;
    }
}
