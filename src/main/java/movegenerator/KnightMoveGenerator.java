package movegenerator;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.Position;

public class KnightMoveGenerator extends PieceMoveGenerator{

    

    public KnightMoveGenerator(Position position, int rank, int file, List<Position> resultList) {
        super(position, rank, file, resultList);
    }

    @Override
    public List<Position> computePieceMoves(Position position, int rank, int file) {
        return computeKnightMoves(position, rank, file);
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
        byte targetPiece = position.getByteAt(targetRank, targetFile);
        if (targetPiece != 0){
            if (targetPiece < MoveGenerator.BLACK_BISHOP == position.getWhitesTurn()){
                return;
            } else {
                hasCaptured = true;
            }
        }
        Board resultingSpaces =
            MoveGenerator.getBoardAfterMove(position.copyBoard(), rank, file, targetRank, targetFile);
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

}
