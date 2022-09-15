package movegenerator;

import java.util.List;

import model.Position;

public class BishopMoveGenerator {
    /**
     * Generates all legal moves for a specific bishop.
     * @param position the position for which a follow-up position should be generated
     * @param rank the rank of the bishop
     * @param file the file of the bishop
     * @return the positions that the generated moves result in
     */
    public static List<Position> computeBishopMoves(Position position, int rank, int file) {
        return MoveGenerator.computeDiagonalMoves(position, rank, file);
    }
}
