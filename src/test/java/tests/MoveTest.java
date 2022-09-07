package tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.Coordinate;
import model.Move;
import model.Piece;
import model.PieceType;

public class MoveTest {
    @Test
    public void notEqualsNullTest() {
        Move move = new Move(new Coordinate(1, 2), new Coordinate(3, 4));
        assertFalse(move.equals(null));

    }

    @Test
    public void notEqualsChangedFirstCoordinateTest() {
        Move move = new Move(new Coordinate(4, 1), new Coordinate(0, 7));
        Move differentMove = new Move(new Coordinate(5, 1), new Coordinate(0, 7));

        assertFalse(move.equals(differentMove));
        assertFalse(differentMove.equals(move));
    }

    @Test
    public void notEqualsChangedSecondCoordinateTest() {
        Move move = new Move(new Coordinate(3, 2), new Coordinate(0, 2));
        Move differentMove = new Move(new Coordinate(3, 2), new Coordinate(1, 2));

        assertFalse(move.equals(differentMove));
        assertFalse(differentMove.equals(move));
    }

    @Test
    public void notEqualsFirstAndSecondCoordinateSwappedTest() {
        Move move = new Move(new Coordinate(3, 7), new Coordinate(0, 2));
        Move differentMove = new Move(new Coordinate(0, 2), new Coordinate(3, 7));

        assertFalse(move.equals(differentMove));
        assertFalse(differentMove.equals(move));
    }

    @Test
    public void notEqualsChangedPromotedToTest() {
        Move move = new Move(new Coordinate(0, 2), new Coordinate(0, 7), new Piece(PieceType.ROOK, false));
        Move differentMove = new Move(new Coordinate(0, 2), new Coordinate(0, 7), new Piece(PieceType.ROOK, true));

        assertFalse(move.equals(differentMove));
        assertFalse(differentMove.equals(move));
    }

    @Test
    public void notEqualsOneWithOneWithoutPromotedToTest() {
        Move move = new Move(new Coordinate(1, 2), new Coordinate(6, 5), new Piece(PieceType.ROOK, false));
        Move moveWithoutPromotedTo = new Move(new Coordinate(1, 2), new Coordinate(6, 5));

        assertFalse(move.equals(moveWithoutPromotedTo));
        assertFalse(moveWithoutPromotedTo.equals(move));
    }

    @Test
    public void equalsWithoutPromotedToTest() {
        Move move = new Move(new Coordinate(4, 1), new Coordinate(0, 7));
        Move equalMove = new Move(new Coordinate(4, 1), new Coordinate(0, 7));
        
        assertTrue(move.equals(equalMove));
        assertTrue(equalMove.equals(move));
    }

    @Test
    public void equalsWithPromotedToTest1() {
        Move move = new Move(new Coordinate(1, 2), new Coordinate(3, 4), new Piece(PieceType.BISHOP, true));
        Move equalMove = new Move(new Coordinate(1, 2), new Coordinate(3, 4), new Piece(PieceType.BISHOP, true));

        assertTrue(move.equals(equalMove));
        assertTrue(equalMove.equals(move));
    }
}
