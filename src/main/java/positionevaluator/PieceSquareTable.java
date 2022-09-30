package positionevaluator;

import static model.PieceEncoding.*;

public class PieceSquareTable {
    private static int[] bishopTable = {
        -20,-10,-10,-10,-10,-10,-10,-20, // 8
        -10,  0,  0,  0,  0,  0,  0,-10, // 7
        -10,  0,  5, 10, 10,  5,  0,-10, // 6
        -10,  5,  5, 10, 10,  5,  5,-10, // 5
        -10,  0, 10, 10, 10, 10,  0,-10, // 4
        -10, 10, 10, 10, 10, 10, 10,-10, // 3
        -10,  5,  0,  0,  0,  0,  5,-10, // 2
        -20,-10,-10,-10,-10,-10,-10,-20  // 1
      // a   b   c   d   e   f   g   h
    };
    private static int[] kingTable = new int[64];
    private static int[] knightTable = {
        -50,-40,-30,-30,-30,-30,-40,-50, // 8
        -40,-20,  0,  0,  0,  0,-20,-40, // 7
        -30,  0, 10, 15, 15, 10,  0,-30, // 6
        -30,  5, 15, 20, 20, 15,  5,-30, // 5
        -30,  0, 15, 20, 20, 15,  0,-30, // 4
        -30,  5, 10, 15, 15, 10,  5,-30, // 3
        -40,-20,  0,  5,  5,  0,-20,-40, // 2
        -50,-40,-30,-30,-30,-30,-40,-50  // 1
      // a   b   c   d   e   f   g   h
    };
    private static int[] pawnTable = {
        0,  0,  0,  0,  0,  0,  0,  0,  // 8
        50, 50, 50, 50, 50, 50, 50, 50, // 7
        10, 10, 20, 30, 30, 20, 10, 10, // 6
        5,  5, 10, 25, 25, 10,  5,  5,  // 5
        0,  0,  0, 20, 20,  0,  0,  0,  // 4
        5, -5,-10,  0,  0,-10, -5,  5,  // 3
        5, 10, 10,-20,-20, 10, 10,  5,  // 2
        0,  0,  0,  0,  0,  0,  0,  0   // 1
      // a   b   c   d   e   f   g   h
    };
    private static int[] queenTable = {
        -20,-10,-10, -5, -5,-10,-10,-20, // 8
        -10,  0,  0,  0,  0,  0,  0,-10, // 7
        -10,  0,  5,  5,  5,  5,  0,-10, // 6
        -5,  0,  5,  5,  5,  5,  0, -5,  // 5
         0,  0,  5,  5,  5,  5,  0, -5,  // 4
        -10,  5,  5,  5,  5,  5,  0,-10, // 3
        -10,  0,  5,  0,  0,  0,  0,-10, // 2
        -20,-10,-10, -5, -5,-10,-10,-20  // 1
      // a   b   c   d   e   f   g   h
    };
    private static int[] rookTable = {
         0,  0,  0,  0,  0,  0,  0,  0, // 8
         5, 10, 10, 10, 10, 10, 10,  5, // 7
        -5,  0,  0,  0,  0,  0,  0, -5, // 6
        -5,  0,  0,  0,  0,  0,  0, -5, // 5
        -5,  0,  0,  0,  0,  0,  0, -5, // 4
        -5,  0,  0,  0,  0,  0,  0, -5, // 3
        -5,  0,  0,  0,  0,  0,  0, -5, // 2
         0,  0,  0,  5,  5,  0,  0,  0  // 1
      // a   b   c   d   e   f   g   h

    };

    public static int evaluatePiecePosition(int rank, int file, byte piece){
        int[] relevantTable = null;
        switch(getBytePieceType(piece)){
            case BISHOP:
                relevantTable = bishopTable;
                break;
            case KING:
                relevantTable = kingTable;
                break;
            case KNIGHT:
                relevantTable = knightTable;
                break;
            case PAWN:
                relevantTable = pawnTable;
                break;
            case QUEEN:
                relevantTable =  queenTable;
                break;
            case ROOK:
                relevantTable = rookTable;
                break;
            default:
                return 0;
        }
        if (!isBytePieceWhite(piece)){
            rank = 7-rank;
        }
        return relevantTable[rank*8+file];
    }
}
