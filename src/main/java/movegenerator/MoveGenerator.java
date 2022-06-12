package movegenerator;

import java.util.Arrays;
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
        Set<Board> followUpBoards = new HashSet<>();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Set<Board> results = generatePossibleMovesPerPiece(boardState, rank, file);
                if (results != null){
                    followUpBoards.addAll(results);
                }
            }
        }
        Board[] output = new Board[followUpBoards.size()];
        followUpBoards.toArray(output);
        return output;
    }

    private Set<Board> generatePossibleMovesPerPiece(Board boardState, int rank, int file) {
        Piece currentPiece = boardState.getSpaces()[rank][file];
        if (currentPiece == null) {
            return null;
        }
        PieceType currentType = currentPiece.getPieceType();
        switch (currentType) {
        case BISHOP:
            return computeBishopMoves(boardState, rank, file);
        case KING:
            return computeKingMoves(boardState, rank, file);
        case KNIGHT:
            return computeKnightMoves(boardState, rank, file);
        case PAWN:
            return computePawnMoves(boardState, rank, file);
        case QUEEN:
            return computeQueenMoves(boardState, rank, file);
        case ROOK:
            return computeRookMoves(boardState, rank, file);
        default:
            return null;
        }
    }

    public Set<Board> computePawnMoves(Board boardState, int rank, int file) {
        // TODO: Ticket #3 - generate pawn moves
        Piece currentPiece = boardState.getPieceAt(rank, file);
        Set<Board> results = new HashSet<Board>();
        int sign = 0;
        if (currentPiece.getIsWhite()){
            sign = -1;
        } else {
            sign = 1;
        }
        computePawnSingleStep(boardState, results, rank, file, sign, currentPiece);
        computePawnDoubleStep(boardState, results, rank, file, sign, currentPiece);
        // TODO: capturing
        computePawnCaptureLeft(boardState, results, rank, file, sign, currentPiece);
        computePawnCaptureRight(boardState, results, rank, file, sign, currentPiece);
        // TODO: capturing en-passant
        return results;
    }

    /**
     * Checks, if a pawn in a given location can be promoted. If it can then the
     * function adds all possible promotions into the Set results.
     * @param bs
     * @param results
     * @param rank
     * @param file
     * @param sign
     * @param currentPiece
     * @return true if the pawn was able to be promoted, false if not
     */
    private boolean computePawnPromotions(Board bs, Set<Board> results, int rank,
        int file, int sign, Piece currentPiece) {
        if (currentPiece.getIsWhite() && rank == 0 ||
            !currentPiece.getIsWhite() && rank == 7){
            
            Piece[][] bishopPromotion = bs.copySpaces();
            bishopPromotion[rank][file] = new Piece(PieceType.BISHOP,
                currentPiece.getIsWhite());
            results.add(new Board(bishopPromotion, bs.getWhiteNextMove(),
                bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
                bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
                -1,-1, bs.getHalfMoves(), bs.getFullMoves()));

            Piece[][] knightPromotion = bs.copySpaces();
            knightPromotion[rank][file] = new Piece(PieceType.KNIGHT,
                currentPiece.getIsWhite());
            results.add(new Board(knightPromotion, bs.getWhiteNextMove(),
                bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
                bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
                -1,-1, bs.getHalfMoves(), bs.getFullMoves()));
            
            Piece[][] queenPromotion = bs.copySpaces();
            queenPromotion[rank][file] = new Piece(PieceType.QUEEN,
                currentPiece.getIsWhite());
            results.add(new Board(queenPromotion, bs.getWhiteNextMove(),
                bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
                bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
                -1,-1, bs.getHalfMoves(), bs.getFullMoves()));
            
            Piece[][] rookPromotion = bs.copySpaces();
            rookPromotion[rank][file] = new Piece(PieceType.ROOK,
                currentPiece.getIsWhite());
            results.add(new Board(rookPromotion, bs.getWhiteNextMove(),
                bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
                bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
                -1,-1, bs.getHalfMoves(), bs.getFullMoves()));
            
            return true;
        } else {
            return false;
        }
    }

    private void computePawnSingleStep(Board bs, Set<Board> results, int rank,
        int file, int sign, Piece currentPiece){
        if (bs.getPieceAt(rank+(sign*1), file) == null){
            Piece[][] resultingSpaces = getBoardAfterMove(bs.getSpaces(), rank,
                file, rank+(sign*1), file);
            addPawnMove(bs, results, rank, file, sign, currentPiece, resultingSpaces);;
        }
    }

    private void computePawnDoubleStep(Board bs, Set<Board> results, int rank,
        int file, int sign, Piece currentPiece){
        if (bs.getPieceAt(rank+(sign*1), file) == null &&
            bs.getPieceAt(rank+(sign*2), file) == null){
            Piece[][] resultingSpaces = getBoardAfterMove(bs.getSpaces(), rank,
                file, rank+(sign*2), file);
            addPawnMove(bs, results, rank, file, sign, currentPiece, resultingSpaces);
        }
    }

    public void computePawnCaptureLeft(Board bs, Set<Board> results, int rank,
        int file, int sign, Piece currentPiece){
        if (rank != 0){
            Piece targetPiece = bs.getPieceAt(rank+(sign*1), file-1);
            if (targetPiece != null &&
                targetPiece.getIsWhite() != currentPiece.getIsWhite())){
                Piece[][] resultingSpaces = getBoardAfterMove(bs.getSpaces(),
                    rank, file, rank+(sign*1), file-1);
                addPawnMove(bs, results, rank, file, sign, currentPiece, resultingSpaces);
            }
        }
    }

    public void computePawnCaptureRight(Board bs, Set<Board> results, int rank,
        int file, int sign, Piece currentPiece){
        if (rank != 7){
            Piece targetPiece = bs.getPieceAt(rank+(sign*1), file+1);
            if (targetPiece != null &&
                targetPiece.getIsWhite() != currentPiece.getIsWhite())){
                Piece[][] resultingSpaces = getBoardAfterMove(bs.getSpaces(),
                    rank, file, rank+(sign*1), file+1);
                addPawnMove(bs, results, rank, file, sign, currentPiece, resultingSpaces);
            }
        }
    }

    /**
     * Checks, whether a pawn move leaves the pawn's king in check. If it doesn't
     * then the method checks, whether the pawn can be promoted. If it can, then
     * the promotion-method takes care of it; if it can't then this method creates
     * a board out of the old board and adds the new one to the result set.
     * @param results
     * @param newSpaces
     * @param previousBoard
     */
    private void addPawnMove(Board bs, Set<Board> results, int rank,
        int file, int sign, Piece currentPiece, Piece[][] resultingSpaces){
        Board resultingBoard = new Board(resultingSpaces, !bs.getWhiteNextMove(),
            bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
            bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
            -1,-1, 0, bs.getFullMoves()+1);
        if (currentPiece.getIsWhite() == !resultingBoard.getWhiteInCheck()||
           !currentPiece.getIsWhite() == !resultingBoard.getBlackInCheck()){
            if (!computePawnPromotions(resultingBoard, results, rank,
                file, sign, currentPiece)){
                results.add(resultingBoard);
            }
        }
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

    /**
     * <p>
     * This method gets a board, the coordinates for a piece and the coordinates
     * for where to place a piece after it has been moved, capturing other 
     * pieces occupying the target space in the progress.<br>
     * </p><p>
     * This method does not check for the legality of a move since that is the 
     * job of the compute[Piece]Moves()-methods like computeQueenMoves
     * </p>
     * Does not automically capture a pawn during en-passant, this needs to be
     * handled in computePawnMoves()
     * @param spaces the board on which to execute the move
     * @param startingRank the horizontal line in which the moving figure starts
     * @param startingFile the vertical line in which the moving figure starts
     * @param targetRank the horizontal line in which the moving figure ends
     * @param targetFile the vertical line in which the moving figure ends
     * @return the board after the move has been executed
     */
    public Piece[][] getBoardAfterMove(Piece[][] spaces, int startingRank, int startingFile, int targetRank, int targetFile){
        Piece movingPiece = spaces[startingRank][startingFile];
        Piece[][] spacesAfterMove = Arrays.copyOf(spaces, spaces.length);
        spacesAfterMove[startingRank][startingFile] = null;
        spacesAfterMove[targetRank][targetFile] = movingPiece;
        return spacesAfterMove;
    }
}
