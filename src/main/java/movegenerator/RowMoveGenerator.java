package movegenerator;

import java.util.List;

import model.Position;

public class RowMoveGenerator implements Runnable{
    private Position position;
    private int rank;
    private List<Position> resultList;


    public RowMoveGenerator(Position position, int rank, List<Position> resultList) {
        this.position = position;
        this.rank = rank;
        this.resultList = resultList;
    }

    @Override
    public void run() {
        for (int file = 0; file < 8; file++){
            MoveGenerator.generatePossibleMovesPerPiece(position, rank, file, resultList);
        }
    }
    
}
