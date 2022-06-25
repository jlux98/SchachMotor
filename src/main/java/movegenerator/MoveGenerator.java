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

    /**
     * A method that generates all possible follow-up-boards for a given game
     * state
     * @param boardState the given game state
     * @return an array with all possible follow-up-boards
     */
    public static Board[] generatePossibleMoves(Board boardState) {
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
    public static Set<Board> generatePossibleMovesPerPiece(Board boardState, int rank, int file) {
        Piece currentPiece = boardState.getSpaces()[rank][file];
        if (currentPiece == null) {
            return null;
        }
        if (currentPiece.getIsWhite() != boardState.getWhitesTurn()){
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
    public static Set<Board> computePawnMoves(Board boardState, int rank, int file) {
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
    private static boolean checkForPawnPromotions(Board bs, Set<Board> results, int rank,
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

    private static void computePawnPromotion(Board bs, Set<Board> results, int rank,
        int file, int sign, PieceType promoteTo){
        Piece[][] promotion = bs.copySpaces();
            promotion[rank][file] = new Piece(promoteTo,
                (sign == -1));
            results.add(new Board(promotion, bs.getWhiteNextMove(),
                bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
                bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
                -1,-1, bs.getHalfMoves(), bs.getFullMoves()));
    }

    private static void computePawnSingleStep(Board bs, Set<Board> results, int rank,
        int file, int sign){
        if (bs.getPieceAt(rank+(sign*1), file) == null){
            Piece[][] resultingSpaces = getBoardAfterMove(bs.copySpaces(), rank,
                file, rank+(sign*1), file);
            addPawnMove(bs, results, rank, file, sign, resultingSpaces, false);
        }
    }

    private static void computePawnDoubleStep(Board bs, Set<Board> results, int rank,
        int file, int sign){
        if (rank < 6 &&
            bs.getPieceAt(rank+(sign*1), file) == null &&
            bs.getPieceAt(rank+(sign*2), file) == null){
            Piece[][] resultingSpaces = getBoardAfterMove(bs.copySpaces(), rank,
                file, rank+(sign*2), file);
            addPawnMove(bs, results, rank, file, sign, resultingSpaces, true);
        }
    }

    public static void computePawnCaptureLeft(Board bs, Set<Board> results, int rank,
        int file, int sign){
        if (file != 0){
            Piece targetPiece = bs.getPieceAt(rank+(sign*1), file-1);
            if (targetPiece != null &&
                targetPiece.getIsWhite() != (sign == -1)){
                Piece[][] resultingSpaces = getBoardAfterMove(bs.copySpaces(),
                    rank, file, rank+(sign*1), file-1);
                addPawnMove(bs, results, rank, file, sign, resultingSpaces, false);
            }
        }
    }

    public static void computePawnCaptureRight(Board bs, Set<Board> results, int rank,
        int file, int sign){
        if (file != 7){
            Piece targetPiece = bs.getPieceAt(rank+(sign*1), file+1);
            if (targetPiece != null &&
                targetPiece.getIsWhite() != (sign == -1)){
                Piece[][] resultingSpaces = getBoardAfterMove(bs.copySpaces(),
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
    public static void computeEnPassant(Board bs, Set<Board> results, int rank,
        int file, int sign){
        int targetRank = bs.getEnPassantTargetRank();
        int targetFile = bs.getEnPassantTargetFile(); 
        if (targetRank != -1 &&
            targetFile != -1){
                //FIXME sign * 1 leftover or typo? (again in addPawnMove)
                //FIXME: Neither, it's intended behaviour 😅
            if (targetRank == rank+(sign*1) &&
                (targetFile == file+1 || targetFile == file-1)){
                Piece[][] resultingSpaces = getBoardAfterMove(bs.copySpaces(),
                    rank, file, targetRank, targetFile);
                resultingSpaces[targetRank-(sign*1)][targetFile] = null;
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
    private static void addPawnMove(Board bs, Set<Board> results, int rank,
        int file, int sign, Piece[][] resultingSpaces, boolean doubleStep){
        int doubleStepRank = -1;
        int doubleStepFile = -1;
        if (doubleStep){
            doubleStepRank = rank+(sign*1);
            doubleStepFile = file;
        }
        int fullMoves = bs.getFullMoves();
        if (!bs.getWhiteNextMove()) {
            fullMoves ++;
        }
        Board resultingBoard = new Board(resultingSpaces, !bs.getWhiteNextMove(),
            bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
            bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
            doubleStepRank,doubleStepFile, 0, fullMoves);
        if ((sign == -1) && !resultingBoard.getWhiteInCheck()||
            (sign == 1) && !resultingBoard.getBlackInCheck()){
            int targetRank = -1;
            if (doubleStep){
                targetRank = rank + (sign*2);
            } else {
                targetRank = rank + (sign*1);
            }
            if (!checkForPawnPromotions(resultingBoard, results, targetRank,
                file, sign)){
                results.add(resultingBoard);
            }
        }
    }

    public static Set<Board> computeKnightMoves(Board boardState, int rank, int file) {
        Set<Board> results = new HashSet<Board>();
        knightMoveSupervisor(boardState, results, rank, file, rank-2, file-1);
        knightMoveSupervisor(boardState, results, rank, file, rank-2, file+1);
        knightMoveSupervisor(boardState, results, rank, file, rank-1, file+2);
        knightMoveSupervisor(boardState, results, rank, file, rank+1, file+2);
        knightMoveSupervisor(boardState, results, rank, file, rank+2, file+1);
        knightMoveSupervisor(boardState, results, rank, file, rank+2, file-1);
        knightMoveSupervisor(boardState, results, rank, file, rank+1, file-2);
        knightMoveSupervisor(boardState, results, rank, file, rank-1, file-2);
        return results;
    }

    private static void knightMoveSupervisor(Board boardState, Set<Board> results,
        int rank, int file, int targetRank, int targetFile){
        if ((targetRank < 0) || (targetRank > 7) || 
            (targetFile < 0) || (targetFile > 7)) {
            return;
        }
        boolean hasCaptured = false;
        Piece targetPiece = boardState.getPieceAt(targetRank, targetFile);
        if (targetPiece != null){
            if (targetPiece.getIsWhite() == boardState.getWhitesTurn()){
                return;
            } else {
                hasCaptured = true;
            }
        }
        Piece[][] resultingSpaces =
            getBoardAfterMove(boardState.copySpaces(), rank, file, targetRank, targetFile);
        addKnightMove(boardState, results, rank, file, resultingSpaces, hasCaptured);
    }



    private static void addKnightMove(Board bs, Set<Board> results, int rank,
        int file, Piece[][] resultingSpaces, boolean hasCaptured){
        int fullMoves = bs.getFullMoves();
        if (!bs.getWhiteNextMove()) {
            fullMoves ++;
        }
        int halfMoves = bs.getHalfMoves()+1;
        if (hasCaptured){
            halfMoves = 0;
        }
        Board resultingBoard = new Board(resultingSpaces, !bs.getWhiteNextMove(),
            bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
            bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
            -1,-1, halfMoves, fullMoves);
        if ( bs.getWhitesTurn() && !resultingBoard.getWhiteInCheck()||
            !bs.getWhitesTurn() && !resultingBoard.getBlackInCheck()){
            results.add(resultingBoard);
        }
    }

    private static Set<Board> computeKingMoves(Board boardState, int rank, int file) {
        Set<Board> results = new HashSet<>();
        // Attack north
        computeKingStep(boardState, results, rank, file, rank-1, file);
        // Attack northeast
        computeKingStep(boardState, results, rank, file, rank-1, file+1);
        // Attack east
        computeKingStep(boardState, results, rank, file, rank, file+1);
        // Attack southeast
        computeKingStep(boardState, results, rank, file, rank+1, file+1);
        // Attack south
        computeKingStep(boardState, results, rank, file, rank+1, file);
        // Attack southwest
        computeKingStep(boardState, results, rank, file, rank+1, file-1);
        // Attack west
        computeKingStep(boardState, results, rank, file, rank, file-1);
        // Attack northwest
        computeKingStep(boardState, results, rank, file, rank-1, file-1);
        
        computeCastlingKingside(boardState, results);
        computeCastlingQueenside(boardState, results);
        return results;
    }

    private static void computeCastlingKingside(Board bs, Set<Board> results){
        boolean[][] relevantAttackMap = null;
        int relevantRank = -1;
        boolean relevantCastlingRight = false;
        if (bs.getWhitesTurn()){
            if (bs.getWhiteInCheck()) {
                return;
            }
            relevantAttackMap = bs.getAttackedByBlack();
            relevantRank = 7;
            relevantCastlingRight = bs.getWhiteCastlingKingside();
        } else {
            if (bs.getBlackInCheck()) {
                return;
            }
            relevantAttackMap = bs.getAttackedByWhite();
            relevantRank = 0;
            relevantCastlingRight = bs.getBlackCastlingKingside();
        }
        if (relevantCastlingRight){
            // check whether the traversed spaces are free and not attacked
            for (int i = 5; i < 7; i++){
                if (relevantAttackMap[relevantRank][i]){
                    return;
                }
                if (bs.getPieceAt(relevantRank, i) != null){
                    return;
                }
            }
            // move king first
            Piece[][] resultingSpaces = getBoardAfterMove(
                bs.copySpaces(), relevantRank, 4, relevantRank, 6);
            // then move rook
            resultingSpaces = getBoardAfterMove(
                resultingSpaces, relevantRank, 7, relevantRank, 5);
            addKingMove(bs, results, resultingSpaces, false);
        }
    }

    private static void computeCastlingQueenside(Board bs, Set<Board> results){
        boolean[][] relevantAttackMap = null;
        int relevantRank = -1;
        boolean relevantCastlingRight = false;
        if (bs.getWhitesTurn()){
            if (bs.getWhiteInCheck()) {
                return;
            }
            relevantAttackMap = bs.getAttackedByBlack();
            relevantRank = 7;
            relevantCastlingRight = bs.getWhiteCastlingQueenside();
        } else {
            if (bs.getBlackInCheck()) {
                return;
            }
            relevantAttackMap = bs.getAttackedByWhite();
            relevantRank = 0;
            relevantCastlingRight = bs.getBlackCastlingQueenside();
        }
        if (relevantCastlingRight){
            // check whether the traversed spaces are free and not attacked
            for (int i = 2; i < 4; i++){
                if (relevantAttackMap[relevantRank][i]){
                    return;
                }
                if (bs.getPieceAt(relevantRank, i) != null){
                    return;
                }
            }
            /*  even though the rook can move through attacks the space still 
                needs to be empty */
            if (bs.getPieceAt(relevantRank, 1)!=null){
                return;
            }
            // move king first
            Piece[][] resultingSpaces = getBoardAfterMove(
                bs.copySpaces(), relevantRank, 4, relevantRank, 2);
            // then move rook
            resultingSpaces = getBoardAfterMove(
                resultingSpaces, relevantRank, 0, relevantRank, 3);
            addKingMove(bs, results, resultingSpaces, false);
        }
    }


    private static void computeKingStep(Board boardState, Set<Board> results,
        int rank, int file, int targetRank, int targetFile){
        if ((targetRank < 0) || (targetRank > 7) || 
            (targetFile < 0) || (targetFile > 7)) {
            return;
        }
        boolean hasCaptured = false;
        Piece targetPiece = boardState.getPieceAt(targetRank, targetFile);
        if (targetPiece != null){
            if (targetPiece.getIsWhite() == boardState.getWhitesTurn()){
                return;
            } else {
                hasCaptured = true;
            }
        }
        Piece[][] resultingSpaces =
            getBoardAfterMove(boardState.copySpaces(), rank, file, targetRank, targetFile);
        addKingMove(boardState, results, resultingSpaces, hasCaptured);
    }

    private static void addKingMove(Board bs, Set<Board> results, Piece[][] resultingSpaces, boolean hasCaptured){
        int fullMoves = bs.getFullMoves();
        if (!bs.getWhiteNextMove()) {
            fullMoves ++;
        }
        int halfMoves = bs.getHalfMoves()+1;
        if (hasCaptured){
            halfMoves = 0;
        }
        boolean whiteCastlingKingside = bs.getWhiteCastlingKingside();
        boolean whiteCastlingQueenside = bs. getWhiteCastlingQueenside();
        boolean blackCastlingKingside = bs.getBlackCastlingKingside();
        boolean blackCastlingQueenside = bs.getBlackCastlingQueenside();

        if (bs.getWhitesTurn()){
            whiteCastlingKingside = false;
            whiteCastlingQueenside = false;
        } else {
            blackCastlingKingside = false;
            blackCastlingQueenside = false;
        }

        Board resultingBoard = new Board(resultingSpaces, !bs.getWhiteNextMove(),
            whiteCastlingKingside, whiteCastlingQueenside,
            blackCastlingKingside, blackCastlingQueenside,
            -1,-1, halfMoves, fullMoves);
        if ( bs.getWhitesTurn() && !resultingBoard.getWhiteInCheck()||
            !bs.getWhitesTurn() && !resultingBoard.getBlackInCheck()){
            results.add(resultingBoard);
        }
    }

    /**
     * Generates all legal moves for a specific bishop.
     * @param boardState the board for which a follow-up board should be generated
     * @param rank the rank of the bishop
     * @param file the file of the bishop
     * @return the boards that the generated moves result in
     */
    public static Set<Board> computeBishopMoves(Board boardState, int rank, int file) {
        return computeDiagonalMoves(boardState, rank, file);
    }

    /**
    * Generates all legal moves for a specific rook.
    * @param boardState the board for which a follow-up board should be generated
    * @param rank the rank of the rook
    * @param file the file of the rook
    * @return the boards that the generated moves result in
    */
    public static Set<Board> computeRookMoves(Board boardState, int rank, int file) {
        return computeHorizontalAndVerticalMoves(boardState, rank, file);
    }

    /**
    * Generates all legal moves for a specific queen.
    * @param boardState the board for which a follow-up board should be generated
    * @param rank the rank of the queen
    * @param file the file of the queen
    * @return the boards that the generated moves result in
    */
    public  static Set<Board> computeQueenMoves(Board boardState, int rank, int file) {
        HashSet<Board> moves = new HashSet<Board>();
        moves.addAll(computeHorizontalAndVerticalMoves(boardState, rank, file));
        moves.addAll(computeDiagonalMoves(boardState, rank, file));
        return moves;
    }

    /**
      * Generates all legal horizontal and vertical moves (used for rook and queen).
      * @param boardState the board for which a follow-up board should be generated
      * @param rank the rank of the piece to be moved
      * @param file the file of the piece to be moved
      * @return the boards that the generated moves result in
      */
    private static Set<Board> computeHorizontalAndVerticalMoves(Board boardState, int rank, int file) {
        HashSet<Board> moves = new HashSet<Board>();
        //left
        moves.addAll(computeRay(boardState, rank, file, -1, 0));
        //right
        moves.addAll(computeRay(boardState, rank, file, 1, 0));
        //up
        moves.addAll(computeRay(boardState, rank, file, 0, -1));
        //down
        moves.addAll(computeRay(boardState, rank, file, 0, 1));
        return moves;
    }

    /**
     * Generates all legal diagonal moves (used for bishop and queen).
     * @param boardState the board for which a follow-up board should be generated
     * @param rank the rank of the piece to be moved
     * @param file the file of the piece to be moved
     * @return the boards that the generated moves result in
     */

    private static Set<Board> computeDiagonalMoves(Board boardState, int rank, int file) {
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

    public static boolean targetLegal(int targetRank, int targetFile, boolean isWhite, Board boardState){
        if ((targetRank < 0) || (targetRank > 7) || 
            (targetFile < 0) || (targetFile > 7)) {
                return false;
            }
        Piece targetPiece = boardState.getPieceAt(targetRank, targetFile);
        boolean targetPieceIsWhite;
        if (targetPiece != null){
            targetPieceIsWhite = targetPiece.getIsWhite();
        } else {
            targetPieceIsWhite = !isWhite;
        }
        return (isWhite != targetPieceIsWhite);
    }

    /**
     * Generates boards for pieces other than pawns.
     * Shorthand for {@link #computeRay(Board, int, int, int, int, boolean) computeRay(currentBoard, rank, file, xOffset, yOffset, false)}.
     * @param currentBoard
     * @param rank
     * @param file
     * @param xOffset
     * @param yOffset
     * @return
     */
    private static Set<Board> computeRay(Board currentBoard, int rank, int file, int xOffset, int yOffset) {
        return  computeRay(currentBoard, rank, file, xOffset, yOffset, false);
    }

    /**
     * Generates a piece's legal moves along a ray facing the direction specified by xOffset and yOffset (e.g. upper left).
     * @param currentBoard the board to generate follow-up moves / boards for
     * @param rank starting rank of the piece
     * @param file starting file of the piece
     * @param xOffset value added to x coordinate in each step, should range from -1 to 1
     * @param yOffset value added to y coordinate in each step, should range from -1 to 1
     * @param isPawnMove whether this board is created for a pawn move. if true, resets half move count
     * @return a set containing the boards generated by moving along the ray
     * @throws IllegalArgumentException if both xOffset and yOffset are 0,
     * which would result in generation of the same space over and over
     *
     */
    private static Set<Board> computeRay(Board currentBoard, int rank, int file, int xOffset, int yOffset, boolean isPawnMove) {
        if (xOffset == 0 && yOffset == 0) {
            throw new IllegalArgumentException("must specify an offset (other than 0,0) for ray generation");
        }

        Piece piece = currentBoard.getPieceAt(rank, file);
        if (piece == null) {
            throw new IllegalArgumentException("the specified square does not contain a piece");
        }
        HashSet<Board> moves = new HashSet<Board>();
        Board generatedBoard = null;
        Piece newSpaces[][] = null;
        boolean captureOrPawnMove = isPawnMove;
        //TODO adjust rank and file index in array ([rank][file] or [file][rank])
        //rank number depends on y axis, file number on x axis
        //while next step legal
        while (targetLegal(rank + yOffset, file + xOffset, piece.getIsWhite(), currentBoard)) {
            newSpaces = currentBoard.copySpaces();

            //leave current space
            newSpaces[rank][file] = null;

            //move to next space, capture by overwriting existing pieces if needed
            if (newSpaces[rank + xOffset][file + yOffset] != null) {
                captureOrPawnMove = true;
            }
            newSpaces[rank + xOffset][file + yOffset] = piece;

            generatedBoard = currentBoard.generateFollowUpBoard(newSpaces, captureOrPawnMove);
            moves.add(generatedBoard);

            //current position is the space that was moved on
            rank += yOffset;
            file += xOffset;
        }

        return moves;
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
    public static Piece[][] getBoardAfterMove(Piece[][] spaces, int startingRank, int startingFile, int targetRank, int targetFile){
        Piece movingPiece = spaces[startingRank][startingFile];
        spaces[startingRank][startingFile] = null;
        spaces[targetRank][targetFile] = movingPiece;
        return spaces;
    }
}
