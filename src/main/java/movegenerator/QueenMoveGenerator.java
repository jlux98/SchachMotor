package movegenerator;

import java.util.ArrayList;
import java.util.List;

import model.Position;


public class QueenMoveGenerator extends PieceMoveGenerator{
    public QueenMoveGenerator(Position position, int rank, int file, List<Position> resultList) {
        super(position, rank, file, resultList);
    }

    @Override
    public List<Position> computePieceMoves(Position position, int rank, int file) {
        return computeQueenMoves(position, rank, file);
    }

    /**
    * Generates all legal moves for a specific queen.
    * @param position the position for which a follow-up position should be generated
    * @param rank the rank of the queen
    * @param file the file of the queen
    * @return the positions that the generated moves result in
    */
    public  static List<Position> computeQueenMoves(Position position, int rank, int file) {
        List<Position> moves = new ArrayList<Position>();
        moves.addAll(MoveGenerator.computeHorizontalAndVerticalMoves(position, rank, file, false));
        moves.addAll(MoveGenerator.computeDiagonalMoves(position, rank, file));
        return moves;
    }
}
