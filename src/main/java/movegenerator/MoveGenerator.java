package movegenerator;

import java.util.HashSet;
import java.util.Set;

import model.Position;
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
     * A method that generates all possible follow-up-positions for a given game
     * state
     * @param position the given game state
     * @return an array with all possible follow-up-positions
     */
    public static Position[] generatePossibleMoves(Position position) {
        Set<Position> followUpBoards = new HashSet<>(); //is hashset preferable over array for us? set initial size of hashset in constructor
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Set<Position> results = generatePossibleMovesPerPiece(position, rank, file);
                if (results != null){
                    followUpBoards.addAll(results);
                }
            }
        }
        Position[] output = new Position[followUpBoards.size()];
        followUpBoards.toArray(output);
        return output;
    }


    /**
     * @param position the game for which to compute follow-ups
     * @param rank the horizontal coordinate of a given piece
     * @param file the vertical coordinate of a given piece
     * @return a set with all possible follow-up-positions for the given position
     * and the given piece
     */
    public static Set<Position> generatePossibleMovesPerPiece(Position position, int rank, int file) {
        Piece currentPiece = position.getSpaces()[rank][file];
        if (currentPiece == null) {
            return null;
        }
        if (currentPiece.getIsWhite() != position.getWhitesTurn()){
            return null;
        }
        PieceType currentType = currentPiece.getPieceType();
        switch (currentType) {
        case BISHOP:
            return computeBishopMoves(position, rank, file);
        case KING:
            return computeKingMoves(position, rank, file);
        case KNIGHT:
            return computeKnightMoves(position, rank, file);
        case PAWN:
            return computePawnMoves(position, rank, file);
        case QUEEN:
            return computeQueenMoves(position, rank, file);
        case ROOK:
            return computeRookMoves(position, rank, file);
        default:
            return null;
        }
    }

    /**
     * Computes all possible follow-up positions for a specific pawn on a 
     * given position
     * @param position the given game state
     * @param rank the horizontal position of the given pawn
     * @param file the vertical position of the given pawn
     * @return a set with all possible follow-up-positions for the given position
     * and the given pawn
     */
    public static Set<Position> computePawnMoves(Position position, int rank, int file) {
        Piece currentPiece = position.getPieceAt(rank, file);
        Set<Position> results = new HashSet<Position>();
        int sign = 0;
        if (currentPiece.getIsWhite()){
            sign = -1;
        } else {
            sign = 1;
        }
        computePawnSingleStep(position, results, rank, file, sign);
        computePawnDoubleStep(position, results, rank, file, sign);
        computePawnCaptureLeft(position, results, rank, file, sign);
        computePawnCaptureRight(position, results, rank, file, sign);
        computeEnPassant(position, results, rank, file, sign);
        return results;
    }

    /**
     * Checks, if a pawn in a given location can be promoted. If it can then this
     * method calls the promotion function .
     * @param position the current position state
     * @param results the set in which to add the promotions
     * @param targetRank the horizontal position of the pawn whose promotion is checked
     * @param targetFile the vertical position of the pawn whose promotion is checked
     * @param sign the direction in which the pawn moves (-1 is up, 1 is down)
     * @param sign2
     * @param sign3
     * @return true if the pawn was able to be promoted, false if not
     */
    private static boolean checkForPawnPromotions(Position position, Set<Position> results, 
    int startingRank, int startingFile, int targetRank, int targetFile, int sign) {
        if ((sign == -1) && targetRank == 0 ||
            (sign == 1) && targetRank == 7){
            computePawnPromotion(position, results, startingRank, startingFile, 
                targetRank, targetFile, sign, PieceType.BISHOP);
            computePawnPromotion(position, results, startingRank, startingFile, 
                targetRank, targetFile, sign, PieceType.KNIGHT);
            computePawnPromotion(position, results, startingRank, startingFile, 
                targetRank, targetFile, sign, PieceType.QUEEN);
            computePawnPromotion(position, results, startingRank, startingFile, 
                targetRank, targetFile, sign, PieceType.ROOK);
            return true;
        } else {
            return false;
        }
    }

    private static void computePawnPromotion(Position bs, Set<Position> results,
    int startingRank, int startingFile, int targetRank, int targetFile, int sign, PieceType promoteTo){
        Piece[][] promotion = bs.copySpaces();
            promotion[targetRank][targetFile] = new Piece(promoteTo,
                (sign == -1));
            Position resultingBoard = new Position(promotion, bs.getWhiteNextMove(),
                bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
                bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
                -1,-1, bs.getHalfMoves(), bs.getFullMoves());
            resultingBoard.setMove(startingRank, startingFile, targetRank, targetFile);
            results.add(resultingBoard);
    }

    private static void computePawnSingleStep(Position bs, Set<Position> results, int rank,
        int file, int sign){
        if (bs.getPieceAt(rank+(sign*1), file) == null){
            Piece[][] resultingSpaces = getBoardAfterMove(bs.copySpaces(), rank,
                file, rank+(sign*1), file);
            addPawnMove(bs, results, rank, file, sign, resultingSpaces, false);
        }
    }

    private static void computePawnDoubleStep(Position bs, Set<Position> results, int rank,
        int file, int sign){
        if (rank < 6 &&
            bs.getPieceAt(rank+(sign*1), file) == null &&
            bs.getPieceAt(rank+(sign*2), file) == null){
            Piece[][] resultingSpaces = getBoardAfterMove(bs.copySpaces(), rank,
                file, rank+(sign*2), file);
            addPawnMove(bs, results, rank, file, sign, resultingSpaces, true);
        }
    }

    public static void computePawnCaptureLeft(Position bs, Set<Position> results, int rank,
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

    public static void computePawnCaptureRight(Position bs, Set<Position> results, int rank,
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
    public static void computeEnPassant(Position bs, Set<Position> results, int rank,
        int file, int sign){
        int targetRank = bs.getEnPassantTargetRank();
        int targetFile = bs.getEnPassantTargetFile(); 
        if (targetRank != -1 &&
            targetFile != -1){
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
     * a position out of the old position and adds the new one to the result set.
     * @param results
     * @param newSpaces
     * @param previousBoard
     * @param sign the direction in which the specified pawn moves (if sign is 
     * 1 then the pawn moves from bottom to top, if sign is -1 then the pawn
     * moves from top to bottom)
     */
    private static void addPawnMove(Position bs, Set<Position> results, int startingRank,
        int startingFile, int sign, Piece[][] resultingSpaces, boolean doubleStep){
        int doubleStepRank = -1;
        int doubleStepFile = -1;
        if (doubleStep){
            doubleStepRank = startingRank+(sign*1);
            doubleStepFile = startingFile;
        }
        int fullMoves = bs.getFullMoves();
        if (!bs.getWhiteNextMove()) {
            fullMoves ++;
        }
        Position resultingBoard = new Position(resultingSpaces, !bs.getWhiteNextMove(),
            bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
            bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
            doubleStepRank,doubleStepFile, 0, fullMoves);
        if ((sign == -1) && !resultingBoard.getWhiteInCheck()||
            (sign == 1) && !resultingBoard.getBlackInCheck()){
            int targetRank = -1;
            if (doubleStep){
                targetRank = startingRank + (sign*2);
            } else {
                targetRank = startingRank + (sign*1);
            }
            if (!checkForPawnPromotions(resultingBoard, results, startingRank,
                startingFile, targetRank, startingFile, sign)){
                resultingBoard.setMove(startingRank, startingFile, targetRank, startingFile);
                results.add(resultingBoard);
            }
        }
    }

    public static Set<Position> computeKnightMoves(Position position, int rank, int file) {
        Set<Position> results = new HashSet<Position>();
        knightMoveSupervisor(position, results, rank, file, rank-2, file-1);
        knightMoveSupervisor(position, results, rank, file, rank-2, file+1);
        knightMoveSupervisor(position, results, rank, file, rank-1, file+2);
        knightMoveSupervisor(position, results, rank, file, rank+1, file+2);
        knightMoveSupervisor(position, results, rank, file, rank+2, file+1);
        knightMoveSupervisor(position, results, rank, file, rank+2, file-1);
        knightMoveSupervisor(position, results, rank, file, rank+1, file-2);
        knightMoveSupervisor(position, results, rank, file, rank-1, file-2);
        return results;
    }

    private static void knightMoveSupervisor(Position position, Set<Position> results,
        int rank, int file, int targetRank, int targetFile){
        if ((targetRank < 0) || (targetRank > 7) || 
            (targetFile < 0) || (targetFile > 7)) {
            return;
        }
        boolean hasCaptured = false;
        Piece targetPiece = position.getPieceAt(targetRank, targetFile);
        if (targetPiece != null){
            if (targetPiece.getIsWhite() == position.getWhitesTurn()){
                return;
            } else {
                hasCaptured = true;
            }
        }
        Piece[][] resultingSpaces =
            getBoardAfterMove(position.copySpaces(), rank, file, targetRank, targetFile);
        addKnightMove(position, results, rank, file, targetRank, targetFile,
            resultingSpaces, hasCaptured);
    }



    private static void addKnightMove(Position bs, Set<Position> results,
        int startingRank, int startingFile, int targetRank, int targetFile,
        Piece[][] resultingSpaces, boolean hasCaptured){
        int fullMoves = bs.getFullMoves();
        if (!bs.getWhiteNextMove()) {
            fullMoves ++;
        }
        int halfMoves = bs.getHalfMoves()+1;
        if (hasCaptured){
            halfMoves = 0;
        }
        Position resultingBoard = new Position(resultingSpaces, !bs.getWhiteNextMove(),
            bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
            bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
            -1,-1, halfMoves, fullMoves);
        if ( bs.getWhitesTurn() && !resultingBoard.getWhiteInCheck()||
            !bs.getWhitesTurn() && !resultingBoard.getBlackInCheck()){
            resultingBoard.setMove(startingRank, startingFile, targetRank, targetFile);
            results.add(resultingBoard);
        }
    }

    private static Set<Position> computeKingMoves(Position position, int rank, int file) {
        Set<Position> results = new HashSet<>();
        // Attack north
        computeKingStep(position, results, rank, file, rank-1, file);
        // Attack northeast
        computeKingStep(position, results, rank, file, rank-1, file+1);
        // Attack east
        computeKingStep(position, results, rank, file, rank, file+1);
        // Attack southeast
        computeKingStep(position, results, rank, file, rank+1, file+1);
        // Attack south
        computeKingStep(position, results, rank, file, rank+1, file);
        // Attack southwest
        computeKingStep(position, results, rank, file, rank+1, file-1);
        // Attack west
        computeKingStep(position, results, rank, file, rank, file-1);
        // Attack northwest
        computeKingStep(position, results, rank, file, rank-1, file-1);
        
        computeCastlingKingside(position, results);
        computeCastlingQueenside(position, results);
        return results;
    }

    private static void computeCastlingKingside(Position bs, Set<Position> results){
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
            addKingMove(bs, results, relevantRank, 4, relevantRank, 6,
                resultingSpaces, false);
        }
    }

    private static void computeCastlingQueenside(Position bs, Set<Position> results){
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
            addKingMove(bs, results, relevantRank, 4, relevantRank, 2,
                resultingSpaces, false);
        }
    }


    private static void computeKingStep(Position position, Set<Position> results,
        int startingRank, int startingFile, int targetRank, int targetFile){
        if ((targetRank < 0) || (targetRank > 7) || 
            (targetFile < 0) || (targetFile > 7)) {
            return;
        }
        boolean hasCaptured = false;
        Piece targetPiece = position.getPieceAt(targetRank, targetFile);
        if (targetPiece != null){
            if (targetPiece.getIsWhite() == position.getWhitesTurn()){
                return;
            } else {
                hasCaptured = true;
            }
        }
        Piece[][] resultingSpaces =
            getBoardAfterMove(position.copySpaces(), startingRank, startingFile, targetRank, targetFile);
        addKingMove(position, results, startingRank, startingFile, targetRank,
            targetFile, resultingSpaces, hasCaptured);
    }

    private static void addKingMove(Position bs, Set<Position> results, 
        int startingRank, int startingFile, int targetRank, int targetFile,
        Piece[][] resultingSpaces, boolean hasCaptured){
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

        Position resultingBoard = new Position(resultingSpaces, !bs.getWhiteNextMove(),
            whiteCastlingKingside, whiteCastlingQueenside,
            blackCastlingKingside, blackCastlingQueenside,
            -1,-1, halfMoves, fullMoves);
        if ( bs.getWhitesTurn() && !resultingBoard.getWhiteInCheck()||
            !bs.getWhitesTurn() && !resultingBoard.getBlackInCheck()){
            resultingBoard.setMove(startingRank, startingFile, targetRank, targetFile);
            results.add(resultingBoard);
        }
    }

    /**
     * Generates all legal moves for a specific bishop.
     * @param position the position for which a follow-up position should be generated
     * @param rank the rank of the bishop
     * @param file the file of the bishop
     * @return the positions that the generated moves result in
     */
    public static Set<Position> computeBishopMoves(Position position, int rank, int file) {
        return computeDiagonalMoves(position, rank, file);
    }

    /**
    * Generates all legal moves for a specific rook.
    * @param position the position for which a follow-up position should be generated
    * @param rank the rank of the rook
    * @param file the file of the rook
    * @return the positions that the generated moves result in
    */
    public static Set<Position> computeRookMoves(Position position, int rank, int file) {
        return computeHorizontalAndVerticalMoves(position, rank, file, true);
    }

    /**
    * Generates all legal moves for a specific queen.
    * @param position the position for which a follow-up position should be generated
    * @param rank the rank of the queen
    * @param file the file of the queen
    * @return the positions that the generated moves result in
    */
    public  static Set<Position> computeQueenMoves(Position position, int rank, int file) {
        HashSet<Position> moves = new HashSet<Position>();
        moves.addAll(computeHorizontalAndVerticalMoves(position, rank, file, false));
        moves.addAll(computeDiagonalMoves(position, rank, file));
        return moves;
    }

    /**
      * Generates all legal horizontal and vertical moves (used for rook and queen).
      * @param position the position for which a follow-up position should be generated
      * @param rank the rank of the piece to be moved
      * @param file the file of the piece to be moved
      * @param isRook whether the piece is a rook
      * @return the positions that the generated moves result in
      */
    private static Set<Position> computeHorizontalAndVerticalMoves(Position position, int rank, int file, boolean isRook) {
        HashSet<Position> moves = new HashSet<Position>();
        boolean blackCastlingKingside = position.getBlackCastlingKingside();
        boolean blackCastlingQueenside = position.getBlackCastlingQueenside();
        boolean whiteCastlingKingSide = position.getWhiteCastlingKingside();
        boolean whiteCastlingQueenside = position.getWhiteCastlingQueenside();

        if (isRook) {
            Piece rook = position.getPieceAt(rank, file);
            //nullpointer possible here
            if (rook.getPieceType() != PieceType.ROOK) {
                throw new IllegalStateException("tried to move a rook, but the piece on rank " + rank + " file " + file + " is a " + rook.getPieceType());
            }
            if (rook.getIsWhite()) {
                //white rook
                if (file == 0) {
                    whiteCastlingQueenside = false;
                } else {
                    whiteCastlingKingSide = false;
                }
            } else {
                //black rook
                if (file == 0) {
                    blackCastlingQueenside = false;
                } else {
                    blackCastlingKingside = false;
                }
            }
        }
        //left
        moves.addAll(computeRay(position, rank, file, -1, 0, whiteCastlingKingSide, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside));
        //right
        moves.addAll(computeRay(position, rank, file, 1, 0, whiteCastlingKingSide, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside));
        //up
        moves.addAll(computeRay(position, rank, file, 0, -1, whiteCastlingKingSide, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside));
        //down
        moves.addAll(computeRay(position, rank, file, 0, 1, whiteCastlingKingSide, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside));
        return moves;
    }

    /**
     * Generates all legal diagonal moves (used for bishop and queen).
     * @param position the position for which a follow-up position should be generated
     * @param rank the rank of the piece to be moved
     * @param file the file of the piece to be moved
     * @return the positions that the generated moves result in
     */

    private static Set<Position> computeDiagonalMoves(Position position, int rank, int file) {
        HashSet<Position> diagonalMoves = new HashSet<Position>();
        //upper left
        diagonalMoves.addAll(computeRay(position, rank, file, -1, -1));
        //bottom left
        diagonalMoves.addAll(computeRay(position, rank, file, -1, 1));
        //upper right
        diagonalMoves.addAll(computeRay(position, rank, file, 1, -1));
        //bottom right
        diagonalMoves.addAll(computeRay(position, rank, file, 1, 1));
        return diagonalMoves;
    }

    public static boolean targetLegal(int targetRank, int targetFile, boolean isWhite, Position position){
        if ((targetRank < 0) || (targetRank > 7) || 
            (targetFile < 0) || (targetFile > 7)) {
                return false;
            }
        Piece targetPiece = position.getPieceAt(targetRank, targetFile);
        boolean targetPieceIsWhite;
        if (targetPiece != null){
            targetPieceIsWhite = targetPiece.getIsWhite();
        } else {
            targetPieceIsWhite = !isWhite;
        }
        return (isWhite != targetPieceIsWhite);
    }

    /**
     * Shorthand for {@link #computeRay(Position, int, int, int, int, boolean, boolean, boolean, boolean)}
     * using the current castling rights (= the castling rights stored in currentBoard) 
     * @return a set containing the positions generated by moving along the ray
     */
    private static Set<Position> computeRay(Position currentBoard, int rank, int file, int xOffset, int yOffset) {
        return computeRay(currentBoard, rank, file, xOffset, yOffset, currentBoard.getBlackCastlingKingside(),
                currentBoard.getBlackCastlingQueenside(), currentBoard.getWhiteCastlingKingside(),
                currentBoard.getWhiteCastlingQueenside());
    }

    /**
     * Generates a piece's legal moves along a ray facing the direction specified by xOffset and yOffset (e.g. upper left).
     * @param currentBoard the position to generate follow-up moves / positions for
     * @param startingRank starting rank of the piece
     * @param startingFile starting file of the piece
     * @param xOffset value added to x coordinate in each step, should range from -1 to 1
     * @param yOffset value added to y coordinate in each step, should range from -1 to 1
     * @param newWhiteCastlingKingside whether white may still castle kingside after this move
     * @param newWhiteCastlingQueenside whether white may still castle queenside after this move
     * @param newBlackCastlingKingside whether black may still castle kingside after this move
     * @param newBlackCastlingQueenside whether black may still castle queenside after this move
     * @return a set containing the positions generated by moving along the ray
     * @throws IllegalArgumentException if both xOffset and yOffset are 0,
     * which would result in generation of the same space over and over
     *
     */
    private static Set<Position> computeRay(Position currentBoard, int startingRank, int startingFile, int xOffset, int yOffset,
            boolean newWhiteCastlingKingside, boolean newWhiteCastlingQueenside, boolean newBlackCastlingKingside,
            boolean newBlackCastlingQueenside) {
        if (xOffset == 0 && yOffset == 0) {
            throw new IllegalArgumentException("must specify an offset (other than 0,0) for ray generation");
        }

        Piece piece = currentBoard.getPieceAt(startingRank, startingFile);
        if (piece == null) {
            throw new IllegalArgumentException("the specified square does not contain a piece");
        }
        HashSet<Position> moves = new HashSet<Position>();
        Position generatedBoard = null;
        Piece newSpaces[][] = null;
        boolean didCapture = false;

        int rank = startingRank;
        int file = startingFile;

        //while next step legal and last step did not capture
        while (targetLegal(rank + yOffset, file + xOffset, piece.getIsWhite(), currentBoard) && !didCapture) {
            //get a new copy every time
            newSpaces = currentBoard.copySpaces();

            //leave starting space
            newSpaces[startingRank][startingFile] = null;

            int targetFile = file + xOffset;
            int targetRank = rank + yOffset;

            //move to next space, capture by overwriting existing pieces if needed
            if (newSpaces[targetRank][targetFile] != null) {
                didCapture = true;
            }
            newSpaces[targetRank][targetFile] = piece;

            generatedBoard = currentBoard.generateFollowUpBoard(newSpaces, -1, -1, newWhiteCastlingKingside,
                    newWhiteCastlingQueenside, newBlackCastlingKingside, newBlackCastlingQueenside, didCapture);
            generatedBoard.setMove(startingRank, startingFile, targetRank, targetFile);
            moves.add(generatedBoard);

            //current position is the space that was moved on
            rank += yOffset;
            file += xOffset;
        }

        return moves;
    }

    /**
     * <p>
     * This method gets a position, the coordinates for a piece and the coordinates
     * for where to place a piece after it has been moved, capturing other 
     * pieces occupying the target space in the progress.<br>
     * </p><p>
     * This method does not check for the legality of a move since that is the 
     * job of the compute[Piece]Moves()-methods like computeQueenMoves
     * </p>
     * Does not automically capture a pawn during en-passant, this needs to be
     * handled in computePawnMoves()
     * @param spaces the position on which to execute the move
     * @param startingRank the horizontal line in which the moving figure starts
     * @param startingFile the vertical line in which the moving figure starts
     * @param targetRank the horizontal line in which the moving figure ends
     * @param targetFile the vertical line in which the moving figure ends
     * @return the position after the move has been executed
     */
    public static Piece[][] getBoardAfterMove(Piece[][] spaces, int startingRank, int startingFile, int targetRank, int targetFile){
        Piece movingPiece = spaces[startingRank][startingFile];
        spaces[startingRank][startingFile] = null;
        spaces[targetRank][targetFile] = movingPiece;
        return spaces;
    }
}
