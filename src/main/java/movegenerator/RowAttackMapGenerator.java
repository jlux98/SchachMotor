package movegenerator;

import model.Board;
import static model.PieceEncoding.*;

public class RowAttackMapGenerator implements Runnable{

    private Board spaces;
    private int rank;
    private byte[] tempResult;
    private boolean isWhite;


    public RowAttackMapGenerator(Board spaces, int rank, byte[] tempResult, boolean isWhite) {
        this.spaces = spaces;
        this.rank = rank;
        this.tempResult = tempResult;
        this.isWhite = isWhite;
    }

    @Override
    public void run() {
        for (int file = 0; file < 8; file++){
            byte currentPiece = spaces.getByteAt(rank, file);
            if (currentPiece != EMPTY_SQUARE && isBytePieceWhite(currentPiece) == isWhite){
                tempResult = AttackMapGenerator.paintAttackBoardByteEncoded(spaces, tempResult, rank, file, getBytePieceType(currentPiece),isWhite);
            }
        }
    }
}