package classes;

import java.util.Comparator;

import model.Position;

/**
 * Compares moves by comparing their algebraic string representations.
 */
public class MoveComparator implements Comparator<Position> {

    @Override
    public int compare(Position pos1, Position pos2) {
        return (pos1.getMove().toStringAlgebraic().compareTo(pos2.getMove().toStringAlgebraic()));
    }

}
