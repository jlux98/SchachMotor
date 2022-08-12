package positionevaluator;

import model.Position;
import model.Piece;
import model.PieceType;

public class PositionEvaluator implements Evaluator<Position>{


    @Override
    public int evaluate(Position position) {
        //TODO merge with evaluatePosition when refactoring is defintive
        return PositionEvaluator.evaluatePosition(position);
    }
    public static int evaluatePosition(Position position){
        int result = 0;
        int blackBishops = 0;
        int whiteBishops = 0;
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                Piece currentPiece = position.getPieceAt(rank, file);
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
            result += 150;
        }
        if (blackBishops >= 2){
            result -= 150;
        }
        return result;
    }
}
