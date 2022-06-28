package positionevaluator;

import model.Board;
import model.Piece;
import model.PieceType;

public abstract class PositionEvaluator {
    public static int evaluatePosition(Board boardState){
        int result = 0;
        int blackBishops = 0;
        int whiteBishops = 0;
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                Piece currentPiece = boardState.getPieceAt(rank, file);
                if (currentPiece == null){
                    continue;
                }
                int sign = 0;
                if (currentPiece.getIsWhite()){
                    if (currentPiece.getPieceType() == PieceType.BISHOP){
                        whiteBishops++;
                    }
                    sign = 1;
                } else {
                    if (currentPiece.getPieceType() == PieceType.BISHOP){
                        blackBishops++;
                    }
                    sign = -1;
                }
                result += sign*currentPiece.getPieceType().getPointValue();
            }
        }
        if (whiteBishops >= 2){
            result += 1;
        }
        if (blackBishops >= 2){
            result -= 1;
        }
        return result;
    }
}
