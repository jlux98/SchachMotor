package movegenerator;

import java.util.HashSet;
import java.util.Set;

import model.Board;
import model.Piece;
import model.PieceType;

/**
 * In the end the only public method will be generatePossibleMoves, but for
 * testing purposes I have decided to keep the other methods public until then
 *      -johannes
 * it might be better to have the methods private and write a test class that extends this class
 * making the private methods publicly available for testing. this way the "production" class MoveGenerator
 * would not have to be adapted for testing
 *      - Moritz
 */
public abstract class MoveGenerator {
    public Board[] generatePossibleMoves(Board boardState) {
        // TODO: Ticket #9 - generate alle moves for a board
        Set<Board> followUpBoards = new HashSet<>();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                followUpBoards.addAll(generatePossibleMovesPerPiece(boardState, rank, file));
            }
        }
        Board[] output = new Board[followUpBoards.size()];
        followUpBoards.toArray(output);
        return output;
    }

    private Set<Board> generatePossibleMovesPerPiece(Board boardState, int rank, int file) {
        Piece currentPiece = boardState.getSpaces()[rank][file];
        if (currentPiece == null) {
            //TODO what to do if there is no piece at the specified position?
        }
        PieceType currentType = currentPiece.getPieceType();
        switch (currentType) {
        case Bishop:
            return computeBishopMoves(boardState, rank, file);
        case King:
            return computeKingMoves(boardState, rank, file);
        case Knight:
            return computeKnightMoves(boardState, rank, file);
        case Pawn:
            return computePawnMoves(boardState, rank, file);
        case Queen:
            return computeQueenMoves(boardState, rank, file);
        case Rook:
            return computeRookMoves(boardState, rank, file);
        default:
            return null;
        }
    }

    public Set<Board> computePawnMoves(Board boardState, int rank, int file) {
        // TODO: Ticket #3 - generate pawn moves
        return null;
    }

    public Set<Board> computeKnightMoves(Board boardState, int rank, int file) {
        // TODO: Ticket #4 - generate knight moves
        return null;
    }

    public Set<Board> computeKingMoves(Board boardState, int rank, int file) {
        // TODO: Ticket #8 - generate king moves
        return null;
    }

    public Set<Board> computeBishopMoves(Board boardState, int rank, int file) {
        // TODO: Ticket #5 - generate bishop moves
        Piece spaces[][] = boardState.copySpaces();
        return null;
    }

    /**
     * generates all legal diagonal moves (used for bishop and queen)
     */

    private Set<Board> computeDiagonalMoves(Board boardState, int rank, int file) {
        HashSet<Board> diagonalMoves = new HashSet<Board>();
        //upper left
        diagonalMoves.addAll(computeDiagonal(boardState, rank, file, -1, -1));
        //bottom left
        diagonalMoves.addAll(computeDiagonal(boardState, rank, file, -1, 1));
        //upper right
        diagonalMoves.addAll(computeDiagonal(boardState, rank, file, 1, -1));
        //bottom right
        diagonalMoves.addAll(computeDiagonal(boardState, rank, file, 1, 1));
        return diagonalMoves;
    }

    /**
     * generates all legal moves along one diagonal (e.g. upper left)
     * @param xOffset 
     * @param yOffset 
     *
     */
    private Set<Board> computeDiagonal(Board boardState, int rank, int file, int xOffset, int yOffset) {
        HashSet<Board> diagonal = new HashSet<Board>();
        Piece spaces[][] = boardState.copySpaces();
        Piece piece = spaces[rank][file];

        Board generatedBoard = null;
        boolean legal = true;
        while (legal) {
            //todo test if legal before placing
            //walk along diagonal
            spaces[rank][file] = null;
            spaces[rank + xOffset][file + yOffset] = piece;
            //FIXME how to determine check and castling data
            //generatedBoard = new Board(whiteInCheck, blackInCheck, spaces, whiteNextMove, whiteCastlingKingside, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside, enPassantTargetRank, enPassantTargetFile, halfMoves, fullMoves)
            diagonal.add(generatedBoard);

        }
        return diagonal;
    }

    private boolean isMoveLegal() {
        //TODO implement
        return false;
    }


    public Set<Board> computeRookMoves(Board boardState, int rank, int file) {
        // TODO: Ticket #6 - generate rook moves
        return null;
    }

    public Set<Board> computeQueenMoves(Board boardState, int rank, int file) {
        // TODO: Ticket #7 - generate queen moves
        return null;
    }
}
