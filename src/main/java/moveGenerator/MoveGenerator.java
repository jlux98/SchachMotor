package moveGenerator;

import java.util.HashSet;
import java.util.Set;

import model.Board;
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
    public Board[] generatePossibleMoves(Board boardState){
        // TODO: Ticket #9 - generate alle moves for a board
        Set<Board> followUpBoards = new HashSet<>();
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                followUpBoards.addAll(
                    generatePossibleMovesPerPiece(boardState, rank, file));
            }
        }
        Board[] output = new Board[followUpBoards.size()];
        followUpBoards.toArray(output);
        return output;
    }

    private Set<Board> generatePossibleMovesPerPiece(Board boardState, int rank,
        int file){
        PieceType currentType = 
            boardState.getSpaces()[rank][file].get().getPieceType();
        switch (currentType){
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


    public Set<Board> computePawnMoves(Board boardState, int rank, int file){
        // TODO: Ticket #3 - generate pawn moves
        return null;
    }

    public Set<Board> computeKnightMoves(Board boardState, int rank, int file){
        // TODO: Ticket #4 - generate knight moves
        return null;
    }


    public Set<Board> computeKingMoves(Board boardState, int rank, int file){
        // TODO: Ticket #8 - generate king moves
        return null;
    }

    public Set<Board> computeBishopMoves(Board boardState, int rank, int file){
        // TODO: Ticket #5 - generate bishop moves
        return null;
    }

    public Set<Board> computeRookMoves(Board boardState, int rank, int file){
        // TODO: Ticket #6 - generate rook moves
        return null;
    }

    public Set<Board> computeQueenMoves(Board boardState, int rank, int file){
        // TODO: Ticket #7 - generate queen moves
        return null;
    }
}
