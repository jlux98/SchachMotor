package movegenerator;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.Position;


public class KingMoveGenerator {
    public static List<Position> computeKingMoves(Position position, int rank, int file) {
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
                if (bs.getByteAt(relevantRank, i) != 0){
                    return;
                }
            }
            // move king first
            Board resultingSpaces = MoveGenerator.getBoardAfterMove(
                bs.copyBoard(), relevantRank, 4, relevantRank, 6);
            // then move rook
            resultingSpaces = MoveGenerator.getBoardAfterMove(
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
                if (bs.getByteAt(relevantRank, i) != 0){
                    return;
                }
            }
            /*  even though the rook can move through attacks the space still 
                needs to be empty */
            if (bs.getByteAt(relevantRank, 1)!= 0){
                return;
            }
            // move king first
            Board resultingSpaces = MoveGenerator.getBoardAfterMove(
                bs.copyBoard(), relevantRank, 4, relevantRank, 2);
            // then move rook
            resultingSpaces = MoveGenerator.getBoardAfterMove(
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
        byte targetPiece = position.getByteAt(targetRank, targetFile);
        if (targetPiece != 0){
            if (targetPiece < MoveGenerator.BLACK_BISHOP == position.getWhitesTurn()){
                return;
            } else {
                hasCaptured = true;
            }
        }
        Board resultingSpaces =
            MoveGenerator.getBoardAfterMove(position.copyBoard(), startingRank, startingFile, targetRank, targetFile);
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
}