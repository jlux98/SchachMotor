package movegenerator;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.PieceEncoding;
import model.Position;

public class KingMoveGenerator extends PieceMoveGenerator {
    
    public KingMoveGenerator(Position position, int rank, int file, List<Position> resultList) {
        super(position, rank, file, resultList);
    }

    @Override
    public List<Position> computePieceMoves(Position position, int rank, int file) {
        return computeKingMoves(position, rank, file);
    }

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
        boolean isWhite;
        int relevantRank = -1;
        boolean relevantCastlingRight = false;
        if (bs.getWhiteNextMove()){
            if (bs.getWhiteInCheck()) {
                return;
            }
            isWhite = true;
            relevantRank = 7;
            relevantCastlingRight = bs.getWhiteCastlingKingside();
        } else {
            if (bs.getBlackInCheck()) {
                return;
            }
            isWhite = false;
            relevantRank = 0;
            relevantCastlingRight = bs.getBlackCastlingKingside();
        }
        if (relevantCastlingRight){
            // check whether the traversed spaces are free and not attacked
            if (bs.isKingsideAttacked(isWhite)){
                return;
            }
            for (int i = 5; i < 7; i++){
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
        boolean isWhite;
        int relevantRank = -1;
        boolean relevantCastlingRight = false;
        if (bs.getWhiteNextMove()){
            if (bs.getWhiteInCheck()) {
                return;
            }
            isWhite = true;
            relevantRank = 7;
            relevantCastlingRight = bs.getWhiteCastlingQueenside();
        } else {
            if (bs.getBlackInCheck()) {
                return;
            }
            isWhite = false;
            relevantRank = 0;
            relevantCastlingRight = bs.getBlackCastlingQueenside();
        }
        if (relevantCastlingRight){
            // check whether the traversed spaces are free and not attacked
            if (bs.isQueensideAttacked(isWhite)){
                return;
            }
            for (int file = 2; file < 4; file++){
                if (bs.getByteAt(relevantRank, file) != 0){
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
            if (PieceEncoding.isBytePieceWhite(targetPiece) == position.getWhiteNextMove()){
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
        byte halfMoves = (byte) (bs.getHalfMoves()+1);
        if (hasCaptured){
            halfMoves = 0;
        }
        boolean whiteCastlingKingside = bs.getWhiteCastlingKingside();
        boolean whiteCastlingQueenside = bs. getWhiteCastlingQueenside();
        boolean blackCastlingKingside = bs.getBlackCastlingKingside();
        boolean blackCastlingQueenside = bs.getBlackCastlingQueenside();

        if (bs.getWhiteNextMove()){
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
        if ( bs.getWhiteNextMove() && !resultingPosition.getWhiteInCheck()||
            !bs.getWhiteNextMove() && !resultingPosition.getBlackInCheck()){
            resultingPosition.setMove(startingRank, startingFile, targetRank, targetFile);
            // resultingPosition.appendAncestor(bs);
            results.add(resultingPosition);
        }
    }
}
