package movegenerator;

import java.util.List;

import model.Position;

public class RookMoveGenerator {
        /**
    * Generates all legal moves for a specific rook.
    * @param position the position for which a follow-up position should be generated
    * @param rank the rank of the rook
    * @param file the file of the rook
    * @return the positions that the generated moves result in
    */
    public static List<Position> computeRookMoves(Position position, int rank, int file) {
        return MoveGenerator.computeHorizontalAndVerticalMoves(position, rank, file, true);
    }
}
