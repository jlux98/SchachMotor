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

    /**
     * A method that generates all possible follow-up-boards for a given game
     * state
     * @param boardState the given game state
     * @return an array with all possible follow-up-boards
     */
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


    /**
     * @param boardState the game for which to compute follow-ups
     * @param rank the horizontal coordinate of a given piece
     * @param file the vertical coordinate of a given piece
     * @return a set with all possible follow-up-boards for the given board
     * and the given piece
     */
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

    /**
     * Computes all possible follow-up boards for a specific pawn on a 
     * given board
     * @param boardState the given game state
     * @param rank the horizontal position of the given pawn
     * @param file the vertical position of the given pawn
     * @return a set with all possible follow-up-boards for the given board
     * and the given pawn
     */
    public Set<Board> computePawnMoves(Board boardState, int rank, int file) {
        Piece currentPiece = boardState.getPieceAt(rank, file);
        Set<Board> results = new HashSet<Board>();
        int sign = 0;
        if (currentPiece.getIsWhite()){
            sign = -1;
        } else {
            sign = 1;
        }
        computePawnSingleStep(boardState, results, rank, file, sign);
        computePawnDoubleStep(boardState, results, rank, file, sign);
        computePawnCaptureLeft(boardState, results, rank, file, sign);
        computePawnCaptureRight(boardState, results, rank, file, sign);
        computeEnPassant(boardState, results, rank, file, sign);
        return results;
    }

    /**
     * Checks, if a pawn in a given location can be promoted. If it can then this
     * method calls the promotion function .
     * @param bs the current board state
     * @param results the set in which to add the promotions
     * @param rank the horizontal position of the pawn whose promotion is checked
     * @param file the vertical position of the pawn whose promotion is checked
     * @param sign the direction in which the pawn moves (-1 is up, 1 is down)
     * @return true if the pawn was able to be promoted, false if not
     */
    private boolean checkForPawnPromotions(Board bs, Set<Board> results, int rank,
        int file, int sign) {
        if ((sign == -1) && rank == 0 ||
            (sign == 1) && rank == 7){
            computePawnPromotion(bs, results, rank, file, sign, PieceType.BISHOP);
            computePawnPromotion(bs, results, rank, file, sign, PieceType.KNIGHT);
            computePawnPromotion(bs, results, rank, file, sign, PieceType.QUEEN);
            computePawnPromotion(bs, results, rank, file, sign, PieceType.ROOK);
            return true;
        } else {
            return false;
        }
    }

    private void computePawnPromotion(Board bs, Set<Board> results, int rank,
        int file, int sign, PieceType promoteTo){
        Piece[][] promotion = bs.copySpaces();
            promotion[rank][file] = new Piece(promoteTo,
                (sign == -1));
            results.add(new Board(promotion, bs.getWhiteNextMove(),
                bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
                bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
                -1,-1, bs.getHalfMoves(), bs.getFullMoves()));
    }

    private void computePawnSingleStep(Board bs, Set<Board> results, int rank,
        int file, int sign){
        if (bs.getPieceAt(rank+(sign*1), file) == null){
            Piece[][] resultingSpaces = getBoardAfterMove(bs.getSpaces(), rank,
                file, rank+(sign*1), file);
            addPawnMove(bs, results, rank, file, sign, resultingSpaces, false);
        }
    }

    private void computePawnDoubleStep(Board bs, Set<Board> results, int rank,
        int file, int sign){
        if (bs.getPieceAt(rank+(sign*1), file) == null &&
            bs.getPieceAt(rank+(sign*2), file) == null){
            Piece[][] resultingSpaces = getBoardAfterMove(bs.getSpaces(), rank,
                file, rank+(sign*2), file);
            addPawnMove(bs, results, rank, file, sign, resultingSpaces, true);
        }
    }

    public void computePawnCaptureLeft(Board bs, Set<Board> results, int rank,
        int file, int sign){
        if (rank != 0){
            Piece targetPiece = bs.getPieceAt(rank+(sign*1), file-1);
            if (targetPiece != null &&
                targetPiece.getIsWhite() != (sign == -1)){
                Piece[][] resultingSpaces = getBoardAfterMove(bs.getSpaces(),
                    rank, file, rank+(sign*1), file-1);
                addPawnMove(bs, results, rank, file, sign, resultingSpaces, false);
            }
        }
    }

    public void computePawnCaptureRight(Board bs, Set<Board> results, int rank,
        int file, int sign){
        if (rank != 7){
            Piece targetPiece = bs.getPieceAt(rank+(sign*1), file+1);
            if (targetPiece != null &&
                targetPiece.getIsWhite() != (sign == -1)){
                Piece[][] resultingSpaces = getBoardAfterMove(bs.getSpaces(),
                    rank, file, rank+(sign*1), file+1);
                addPawnMove(bs, results, rank, file, sign, resultingSpaces, false);
            }
        }
    }

    /**
     * Checks whether an en-passant-capture is possible from the pawn whose 
     * whose position is specified by rank and file on the Board bs. Adds the
     * capture to the results if it is.
     * @param bs
     * @param results
     * @param rank
     * @param file
     * @param sign the direction in which the specified pawn moves (if sign is 
     * 1 then the pawn moves from bottom to top, if sign is -1 then the pawn
     * moves from top to bottom)
     */
    public void computeEnPassant(Board bs, Set<Board> results, int rank,
        int file, int sign){
        int targetRank = bs.getEnPassantTargetRank();
        int targetFile = bs.getEnPassantTargetFile(); 
        if (targetRank != -1 &&
            targetFile != -1){
            if (targetRank == rank+(sign*1) &&
                (targetFile == file+1 || targetFile == file-1)){
                Piece[][] resultingSpaces = getBoardAfterMove(bs.getSpaces(),
                    rank, file, targetRank, targetFile);
                resultingSpaces[rank+(sign*2)][targetFile] = null;
                addPawnMove(bs, results, rank, file, sign, resultingSpaces, false);
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
     * @param sign the direction in which the specified pawn moves (if sign is 
     * 1 then the pawn moves from bottom to top, if sign is -1 then the pawn
     * moves from top to bottom)
     */
    private void addPawnMove(Board bs, Set<Board> results, int rank,
        int file, int sign, Piece[][] resultingSpaces, boolean doubleStep){
        int doubleStepRank = -1;
        int doubleStepFile = -1;
        if (doubleStep){
            doubleStepRank = rank+(sign*1);
            doubleStepFile = file;
        }
        Board resultingBoard = new Board(resultingSpaces, !bs.getWhiteNextMove(),
            bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
            bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
            doubleStepRank,doubleStepFile, 0, bs.getFullMoves()+1);
        if ((sign == -1) && !resultingBoard.getWhiteInCheck()||
            (sign == 1) && !resultingBoard.getBlackInCheck()){
            if (!checkForPawnPromotions(resultingBoard, results, rank,
                file, sign)){
                results.add(resultingBoard);
            }
        }
    }

    public Set<Board> computeKnightMoves(Board boardState, int rank, int file) {
        // TODO: Ticket #4 - generate knight moves
        boolean isWhite = boardState.getPieceAt(rank, file).getIsWhite();
        int targetRank;
        int targetFile;


        return null;
    }

    private boolean targetLegal(int targetRank, int targetFile, boolean isWhite, Board boardState){
        Piece targetPiece = boardState.getPieceAt(targetRank, targetFile);
        boolean targetPieceIsWhite;
        if (targetPiece != null){
            targetPieceIsWhite = targetPiece.getIsWhite();
        } else {
            targetPieceIsWhite = !isWhite;
        }
        return  (targetRank >= 0) && (targetRank <= 7) && 
                (targetFile >= 0) && (targetFile <= 7) &&
                (isWhite != targetPieceIsWhite);
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
        // TODO: Notieren, ob eine Figur geschlagen wurde - somehow
        return spacesAfterMove;
    }
}
