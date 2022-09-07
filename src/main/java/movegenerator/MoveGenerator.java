package movegenerator;

import java.util.ArrayList;
import java.util.List;

import model.Position;
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
     * A method that generates all possible follow-up-positions for a given game
     * state
     * @param position the given game state
     * @return an array with all possible follow-up-positions (empty if no moves can be made)
     */
    public static Position[] generatePossibleMoves(Position position) {
        List<Position> followUpPositions = new ArrayList<>(); //is hashset preferable over array for us? set initial size of hashset in constructor
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                List<Position> results = generatePossibleMovesPerPiece(position, rank, file);
                if (results != null){
                    followUpPositions.addAll(results);
                }
            }
        }
        Position[] output = new Position[followUpPositions.size()];
        followUpPositions.toArray(output);
        return output;
    }


    /**
     * @param position the game for which to compute follow-ups
     * @param rank the horizontal coordinate of a given piece
     * @param file the vertical coordinate of a given piece
     * @return a set with all possible follow-up-positions for the given position
     * and the given piece
     */
    public static List<Position> generatePossibleMovesPerPiece(Position position, int rank, int file) {
        Piece currentPiece = position.getPieceAt(rank, file);
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
    public static List<Position> computePawnMoves(Position position, int rank, int file) {
        Piece currentPiece = position.getPieceAt(rank, file);
        List<Position> results = new ArrayList<Position>();
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
    private static boolean checkForPawnPromotions(Position position, List<Position> results, 
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

    private static void computePawnPromotion(Position bs, List<Position> results,
    int startingRank, int startingFile, int targetRank, int targetFile, int sign, PieceType promoteTo){
        Board promotion = bs.copyBoard();
        Piece promotedPiece = new Piece(promoteTo, (sign == -1));
            promotion.setPieceAt(targetRank, targetFile, promotedPiece);
            Position resultingPosition = new Position(promotion, bs.getWhiteNextMove(),
                bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
                bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
                -1,-1, bs.getHalfMoves(), bs.getFullMoves());
            resultingPosition.setMove(startingRank, startingFile, targetRank, targetFile, promotedPiece);
            results.add(resultingPosition);
    }

    private static void computePawnSingleStep(Position bs, List<Position> results, int rank,
        int file, int sign){
        if (bs.getPieceAt(rank+(sign*1), file) == null){
            Board resultingSpaces = getBoardAfterMove(bs.copyBoard(), rank,
                file, rank+(sign*1), file);
            addPawnMove(bs, results, rank, file, sign, resultingSpaces, false, false, false);
        }
    }

    private static void computePawnDoubleStep(Position bs, List<Position> results, int rank,
        int file, int sign){
        if (checkPawnDoubleStep(bs, rank, file, sign)){
            Board resultingSpaces = getBoardAfterMove(bs.copyBoard(), rank,
                file, rank+(sign*2), file);
            addPawnMove(bs, results, rank, file, sign, resultingSpaces, true, false, false);
        }
    }

    private static boolean checkPawnDoubleStep(Position bs, int rank, int file, int sign){
        int relevantRank = -1;
        if (sign == 1){
            relevantRank = 1;
        }
        if (sign == -1){
            relevantRank = 6;
        }
        return rank == relevantRank &&
                bs.getPieceAt(rank+(sign*1), file) == null &&
                bs.getPieceAt(rank+(sign*2), file) == null;
    }

    public static void computePawnCaptureLeft(Position bs, List<Position> results, int rank,
        int file, int sign){
        if (file != 0){
            Piece targetPiece = bs.getPieceAt(rank+(sign*1), file-1);
            if (targetPiece != null &&
                targetPiece.getIsWhite() != (sign == -1)){
                Board resultingSpaces = getBoardAfterMove(bs.copyBoard(),
                    rank, file, rank+(sign*1), file-1);
                addPawnMove(bs, results, rank, file, sign, resultingSpaces, false, true, false);
            }
        }
    }

    public static void computePawnCaptureRight(Position bs, List<Position> results, int rank,
        int file, int sign){
        if (file != 7){
            Piece targetPiece = bs.getPieceAt(rank+(sign*1), file+1);
            if (targetPiece != null &&
                targetPiece.getIsWhite() != (sign == -1)){
                Board resultingSpaces = getBoardAfterMove(bs.copyBoard(),
                    rank, file, rank+(sign*1), file+1);
                addPawnMove(bs, results, rank, file, sign, resultingSpaces, false, false, true);
            }
        }
    }

    /**
     * Checks whether an en-passant-capture is possible from the pawn whose 
     * whose position is specified by rank and file on the position. Adds the
     * capture to the results if it is.
     * @param position
     * @param results
     * @param rank
     * @param file
     * @param sign the direction in which the specified pawn moves (if sign is 
     * 1 then the pawn moves from bottom to top, if sign is -1 then the pawn
     * moves from top to bottom)
     */
    public static void computeEnPassant(Position position, List<Position> results, int rank,
        int file, int sign){
        int targetRank = position.getEnPassantTargetRank();
        int targetFile = position.getEnPassantTargetFile(); 
        if (targetRank != -1 &&
            targetFile != -1){
            if (targetRank == rank+(sign*1) &&
                (targetFile == file+1 || targetFile == file-1)){
                Board resultingSpaces = getBoardAfterMove(position.copyBoard(),
                    rank, file, targetRank, targetFile);
                resultingSpaces.setPieceAt(targetRank-(sign*1),targetFile,null);
                addPawnMove(position, results, rank, file, sign, resultingSpaces, false, false, false);
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
     * @param previousPosition
     * @param sign the direction in which the specified pawn moves (if sign is 
     * 1 then the pawn moves from bottom to top, if sign is -1 then the pawn
     * moves from top to bottom)
     */
    private static void addPawnMove(Position bs, List<Position> results, int startingRank,
        int startingFile, int sign, Board resultingSpaces, boolean doubleStep, boolean hasCapturedLeft, boolean hasCapturedRight){
        int doubleStepRank = -1;
        int doubleStepFile = -1;
        int horizontalOffset = 0;
        if (hasCapturedLeft) {
            horizontalOffset = -1;
        } else if (hasCapturedRight) {
            horizontalOffset = 1;
        }
        if (doubleStep){
            doubleStepRank = startingRank+(sign*1);
            doubleStepFile = startingFile;
        }
        int fullMoves = bs.getFullMoves();
        if (!bs.getWhiteNextMove()) {
            fullMoves ++;
        }
        Position resultingPosition = new Position(resultingSpaces, !bs.getWhiteNextMove(),
            bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
            bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
            doubleStepRank,doubleStepFile, 0, fullMoves);
        if ((sign == -1) && !resultingPosition.getWhiteInCheck()||
            (sign == 1) && !resultingPosition.getBlackInCheck()){
            int targetRank = -1;
            int targetFile = startingFile;
            if (doubleStep){
                targetRank = startingRank + (sign*2);
            } else {
                targetRank = startingRank + (sign*1);
                targetFile = startingFile + horizontalOffset;
            }
            if (!checkForPawnPromotions(resultingPosition, results, startingRank,
                startingFile, targetRank, targetFile, sign)){
                resultingPosition.setMove(startingRank, startingFile, targetRank, targetFile);
                results.add(resultingPosition);
            }
        }
    }

    public static List<Position> computeKnightMoves(Position position, int rank, int file) {
        List<Position> results = new ArrayList<Position>();
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

    private static void knightMoveSupervisor(Position position, List<Position> results,
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
        Board resultingSpaces =
            getBoardAfterMove(position.copyBoard(), rank, file, targetRank, targetFile);
        addKnightMove(position, results, rank, file, targetRank, targetFile,
            resultingSpaces, hasCaptured);
    }



    private static void addKnightMove(Position bs, List<Position> results,
        int startingRank, int startingFile, int targetRank, int targetFile,
        Board resultingSpaces, boolean hasCaptured){
        int fullMoves = bs.getFullMoves();
        if (!bs.getWhiteNextMove()) {
            fullMoves ++;
        }
        int halfMoves = bs.getHalfMoves()+1;
        if (hasCaptured){
            halfMoves = 0;
        }
        Position resultingPosition = new Position(resultingSpaces, !bs.getWhiteNextMove(),
            bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
            bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
            -1,-1, halfMoves, fullMoves);
        if ( bs.getWhitesTurn() && !resultingPosition.getWhiteInCheck()||
            !bs.getWhitesTurn() && !resultingPosition.getBlackInCheck()){
            resultingPosition.setMove(startingRank, startingFile, targetRank, targetFile);
            results.add(resultingPosition);
        }
    }

    private static List<Position> computeKingMoves(Position position, int rank, int file) {
        List<Position> results = new ArrayList<>();
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

    private static void computeCastlingKingside(Position bs, List<Position> results){
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
            Board resultingSpaces = getBoardAfterMove(
                bs.copyBoard(), relevantRank, 4, relevantRank, 6);
            // then move rook
            resultingSpaces = getBoardAfterMove(
                resultingSpaces, relevantRank, 7, relevantRank, 5);
            addKingMove(bs, results, relevantRank, 4, relevantRank, 6,
                resultingSpaces, false);
        }
    }

    private static void computeCastlingQueenside(Position bs, List<Position> results){
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
            Board resultingSpaces = getBoardAfterMove(
                bs.copyBoard(), relevantRank, 4, relevantRank, 2);
            // then move rook
            resultingSpaces = getBoardAfterMove(
                resultingSpaces, relevantRank, 0, relevantRank, 3);
            addKingMove(bs, results, relevantRank, 4, relevantRank, 2,
                resultingSpaces, false);
        }
    }


    private static void computeKingStep(Position position, List<Position> results,
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
        Board resultingSpaces =
            getBoardAfterMove(position.copyBoard(), startingRank, startingFile, targetRank, targetFile);
        addKingMove(position, results, startingRank, startingFile, targetRank,
            targetFile, resultingSpaces, hasCaptured);
    }

    private static void addKingMove(Position bs, List<Position> results, 
        int startingRank, int startingFile, int targetRank, int targetFile,
        Board resultingSpaces, boolean hasCaptured){
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

        Position resultingPosition = new Position(resultingSpaces, !bs.getWhiteNextMove(),
            whiteCastlingKingside, whiteCastlingQueenside,
            blackCastlingKingside, blackCastlingQueenside,
            -1,-1, halfMoves, fullMoves);
        if ( bs.getWhitesTurn() && !resultingPosition.getWhiteInCheck()||
            !bs.getWhitesTurn() && !resultingPosition.getBlackInCheck()){
            resultingPosition.setMove(startingRank, startingFile, targetRank, targetFile);
            results.add(resultingPosition);
        }
    }

    /**
     * Generates all legal moves for a specific bishop.
     * @param position the position for which a follow-up position should be generated
     * @param rank the rank of the bishop
     * @param file the file of the bishop
     * @return the positions that the generated moves result in
     */
    public static List<Position> computeBishopMoves(Position position, int rank, int file) {
        return computeDiagonalMoves(position, rank, file);
    }

    /**
    * Generates all legal moves for a specific rook.
    * @param position the position for which a follow-up position should be generated
    * @param rank the rank of the rook
    * @param file the file of the rook
    * @return the positions that the generated moves result in
    */
    public static List<Position> computeRookMoves(Position position, int rank, int file) {
        return computeHorizontalAndVerticalMoves(position, rank, file, true);
    }

    /**
    * Generates all legal moves for a specific queen.
    * @param position the position for which a follow-up position should be generated
    * @param rank the rank of the queen
    * @param file the file of the queen
    * @return the positions that the generated moves result in
    */
    public  static List<Position> computeQueenMoves(Position position, int rank, int file) {
        List<Position> moves = new ArrayList<Position>();
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
    private static List<Position> computeHorizontalAndVerticalMoves(Position position, int rank, int file, boolean isRook) {
        List<Position> moves = new ArrayList<Position>();
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

    private static List<Position> computeDiagonalMoves(Position position, int rank, int file) {
        List<Position> diagonalMoves = new ArrayList<Position>();
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
     * using the current castling rights (= the castling rights stored in currentPosition) 
     * @return a set containing the positions generated by moving along the ray
     */
    private static List<Position> computeRay(Position currentPosition, int rank, int file, int xOffset, int yOffset) {
        return computeRay(currentPosition, rank, file, xOffset, yOffset, currentPosition.getBlackCastlingKingside(),
                currentPosition.getBlackCastlingQueenside(), currentPosition.getWhiteCastlingKingside(),
                currentPosition.getWhiteCastlingQueenside());
    }

    /**
     * Generates a piece's legal moves along a ray facing the direction specified by xOffset and yOffset (e.g. upper left).
     * @param currentPosition the position to generate follow-up moves / positions for
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
    private static List<Position> computeRay(Position currentPosition, int startingRank, int startingFile, int xOffset, int yOffset,
            boolean newWhiteCastlingKingside, boolean newWhiteCastlingQueenside, boolean newBlackCastlingKingside,
            boolean newBlackCastlingQueenside) {
        if (xOffset == 0 && yOffset == 0) {
            throw new IllegalArgumentException("must specify an offset (other than 0,0) for ray generation");
        }

        Piece piece = currentPosition.getPieceAt(startingRank, startingFile);
        if (piece == null) {
            throw new IllegalArgumentException("the specified square does not contain a piece");
        }
        List<Position> moves = new ArrayList<Position>();
        Position generatedPosition = null;
        Board newSpaces = null;
        boolean didCapture = false;

        int rank = startingRank;
        int file = startingFile;

        //while next step legal and last step did not capture
        while (targetLegal(rank + yOffset, file + xOffset, piece.getIsWhite(), currentPosition) && !didCapture) {
            //get a new copy every time
            newSpaces = currentPosition.copyBoard();

            //leave starting space
            newSpaces.setPieceAt(startingRank, startingFile, null);

            int targetFile = file + xOffset;
            int targetRank = rank + yOffset;

            //move to next space, capture by overwriting existing pieces if needed
            if (newSpaces.getPieceAt(targetRank,targetFile) != null) {
                didCapture = true;
            }
            newSpaces.setPieceAt(targetRank,targetFile,piece);

            generatedPosition = currentPosition.generateFollowUpPosition(newSpaces, -1, -1, newWhiteCastlingKingside,
                    newWhiteCastlingQueenside, newBlackCastlingKingside, newBlackCastlingQueenside, didCapture);
            generatedPosition.setMove(startingRank, startingFile, targetRank, targetFile);
            moves.add(generatedPosition);

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
    public static Board getBoardAfterMove(Board spaces, int startingRank, int startingFile, int targetRank, int targetFile){
        Piece movingPiece = spaces.getPieceAt(startingRank,startingFile);
        spaces.setPieceAt(startingRank,startingFile,null);
        spaces.setPieceAt(targetRank,targetFile,movingPiece);
        return spaces;
    }
}
