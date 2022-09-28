package positionevaluator;

import model.Position;

import model.PieceType;
import static model.PieceEncoding.*;

public class PositionEvaluator implements Evaluator<Position> {

    @Override
    public int evaluate(Position position, boolean isNaturalLeaf, int depth) {
        //TODO merge with evaluatePosition when refactoring is defintive
        return PositionEvaluator.evaluatePosition(position);
    }

    public static int evaluateLeafPosition(Position position, int depth) {
        if (position.isDraw()) {
            return 0;
        }
        if (position.getWhiteNextMove() && position.getWhiteInCheck()) {
            // white is mated, loses the value of a king
            // multiplying with depth to favor early mates
            return -(depth * PieceType.KING.getPointValue());
        } else if (!position.getWhiteNextMove() && position.getBlackInCheck()) {
            // black is mated, loses the value of a king
            // multiplying with depth to favor early mates
            return (depth * PieceType.KING.getPointValue());
        } else {
            return 0;
        }
    }

    public static int evaluatePosition(Position position) {
        int result = 0;
        int blackBishops = 0;
        int whiteBishops = 0;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                byte currentPiece = position.getByteAt(rank, file);
                if (currentPiece == 0) {
                    continue;
                }
                int sign = 0;
                if (isBytePieceWhite(currentPiece)) {
                    if (getBytePieceType(currentPiece) == PieceType.BISHOP) {
                        whiteBishops++;
                    }
                    sign = 1;
                } else {
                    if (getBytePieceType(currentPiece) == PieceType.BISHOP) {
                        blackBishops++;
                    }
                    sign = -1;
                }
                result += sign * getBytePieceType(currentPiece).getPointValue();
                result += sign * PieceSquareTable.evaluatePiecePosition(rank, file, currentPiece);
            }
        }
        if (whiteBishops >= 2) {
            result += 150;
        }
        if (blackBishops >= 2) {
            result -= 150;
        }
        return result;
    }
}
