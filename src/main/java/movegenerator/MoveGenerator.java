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
        Set<Board> followUpBoards = new HashSet<>(); //is hashset preferable over array for us? set initial size of hashset in constructor
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
                //FIXME sign * 1 leftover or typo? (again in addPawnMove)
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
        diagonalMoves.addAll(computeRay(boardState, rank, file, -1, -1));
        //bottom left
        diagonalMoves.addAll(computeRay(boardState, rank, file, -1, 1));
        //upper right
        diagonalMoves.addAll(computeRay(boardState, rank, file, 1, -1));
        //bottom right
        diagonalMoves.addAll(computeRay(boardState, rank, file, 1, 1));
        return diagonalMoves;
    }

    /**
     * Generates a piece's legal moves along a ray facing the direction specified by xOffset and yOffset (e.g. upper left).
     * @param currentBoard the board to generate follow-up moves / boards for
     * @param rank starting rank of the piece
     * @param file starting file of the piece
     * @param xOffset value added to x coordinate in each step, should range from -1 to 1
     * @param yOffset value added to y coordinate in each step, should range from -1 to 1
     * @return a set containing the boards generated by moving along the ray
     * @throws IllegalArgumentException if both xOffset and yOffset are 0,
     * which would result in generation of the same space over and over
     *
     */
    private Set<Board> computeRay(Board currentBoard, int rank, int file, int xOffset, int yOffset) {
        if (xOffset == 0 && yOffset == 0) {
            throw new IllegalArgumentException("must specify an offset (other than 0,0) for ray generation");
        }

        HashSet<Board> moves = new HashSet<Board>();
        Piece piece = currentBoard.getPieceAt(rank, file);
        Board generatedBoard = null;
        Piece newSpaces[][]  = null;
        //TODO adjust rank and file index in array ([rank][file] or [file][rank])
        //rank number depends on y axis, file number on x axis
        //while next step legal
        while (targetLegal(rank+yOffset, file+xOffset, piece.getIsWhite(), currentBoard)) {
            newSpaces = currentBoard.copySpaces();

            //leave current space
            newSpaces[rank][file] = null;

            //move to next space, capture by overwriting existing pieces if needed
            newSpaces[rank + xOffset][file + yOffset] = piece;

            generatedBoard = currentBoard.generateFollowUpBoard(newSpaces);
            moves.add(generatedBoard);

            //current position is the space that was moved on
            rank += yOffset;
            file += xOffset;
        }

        return moves;
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
