package movegenerator;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.PieceEncoding;
import model.Position;
import static model.PieceEncoding.*;

public class PawnMoveGenerator extends PieceMoveGenerator{

    public PawnMoveGenerator(Position position, int rank, int file, List<Position> resultList) {
        super(position, rank, file, resultList);
    }

    @Override
    public List<Position> computePieceMoves(Position position, int rank, int file) {
        return computePawnMoves(position, rank, file);
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
        byte currentPiece = position.getByteAt(rank, file);
        List<Position> results = new ArrayList<Position>();
        int sign = 0;
        if (PieceEncoding.isBytePieceWhite(currentPiece)){
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
        byte offset = 0;
        if (sign == 1){
            offset = PIECE_OFFSET;
        }
        if ((sign == -1) && targetRank == 0 ||
            (sign == 1) && targetRank == 7){
            computePawnPromotion(position, results, startingRank, startingFile, 
                targetRank, targetFile, sign, (byte) (WHITE_BISHOP + offset));
            computePawnPromotion(position, results, startingRank, startingFile, 
                targetRank, targetFile, sign, (byte) (WHITE_KNIGHT + offset));
            computePawnPromotion(position, results, startingRank, startingFile, 
                targetRank, targetFile, sign, (byte) (WHITE_QUEEN + offset));
            computePawnPromotion(position, results, startingRank, startingFile, 
                targetRank, targetFile, sign, (byte) (WHITE_ROOK + offset));
            return true;
        } else {
            return false;
        }
    }

    private static void computePawnPromotion(Position bs, List<Position> results,
    int startingRank, int startingFile, int targetRank, int targetFile, int sign, byte promoteTo){
        Board promotion = bs.copyBoard();
            promotion.setByteAt(targetRank, targetFile, promoteTo);
            Position resultingPosition = new Position(promotion, bs.getWhiteNextMove(),
                bs.getWhiteCastlingKingside(), bs.getWhiteCastlingQueenside(),
                bs.getBlackCastlingKingside(), bs.getBlackCastlingQueenside(),
                -1,-1, bs.getHalfMoves(), bs.getFullMoves());
            resultingPosition.setMove(startingRank, startingFile, targetRank, targetFile, promoteTo);
            resultingPosition.appendAncestor(bs);
            results.add(resultingPosition);
    }

    private static void computePawnSingleStep(Position bs, List<Position> results, int rank,
        int file, int sign){
        if (bs.getByteAt(rank+(sign*1), file) == 0){
            Board resultingSpaces = MoveGenerator.getBoardAfterMove(bs.copyBoard(), rank,
                file, rank+(sign*1), file);
            addPawnMove(bs, results, rank, file, sign, resultingSpaces, false, false, false);
        }
    }

    private static void computePawnDoubleStep(Position bs, List<Position> results, int rank,
        int file, int sign){
        if (checkPawnDoubleStep(bs, rank, file, sign)){
            Board resultingSpaces = MoveGenerator.getBoardAfterMove(bs.copyBoard(), rank,
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
                bs.getByteAt(rank+(sign*1), file) == 0 &&
                bs.getByteAt(rank+(sign*2), file) == 0;
    }

    public static void computePawnCaptureLeft(Position bs, List<Position> results, int rank,
        int file, int sign){
        if (file != 0){
            byte targetPiece = bs.getByteAt(rank+(sign*1), file-1);
            if (targetPiece != 0 &&
                PieceEncoding.isBytePieceWhite(targetPiece) != (sign == -1)){
                Board resultingSpaces = MoveGenerator.getBoardAfterMove(bs.copyBoard(),
                    rank, file, rank+(sign*1), file-1);
                addPawnMove(bs, results, rank, file, sign, resultingSpaces, false, true, false);
            }
        }
    }

    public static void computePawnCaptureRight(Position bs, List<Position> results, int rank,
        int file, int sign){
        if (file != 7){
            byte targetPiece = bs.getByteAt(rank+(sign*1), file+1);
            if (targetPiece != 0 &&
                PieceEncoding.isBytePieceWhite(targetPiece) != (sign == -1)){
                Board resultingSpaces = MoveGenerator.getBoardAfterMove(bs.copyBoard(),
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
                Board resultingSpaces = MoveGenerator.getBoardAfterMove(position.copyBoard(),
                    rank, file, targetRank, targetFile);
                resultingSpaces.setByteAt(targetRank-(sign*1),targetFile,(byte)0);
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
                resultingPosition.appendAncestor(bs);
                results.add(resultingPosition);
            }
        }
    }
}
