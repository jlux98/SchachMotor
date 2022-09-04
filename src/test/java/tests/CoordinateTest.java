package tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.Coordinate;

public class CoordinateTest {

    @Test
    public void notEqualsNullTest() {
        Coordinate coordinate = new Coordinate(1, 2);
        assertFalse(coordinate.equals(null));
    }

    @Test
    public void notEqualsRankDifferentTest() {
        Coordinate coordinate = new Coordinate(1, 2);
        Coordinate differentCoordinate = new Coordinate(2, 2);

        assertFalse(coordinate.equals(differentCoordinate));
        assertFalse(differentCoordinate.equals(coordinate));
    }

    @Test
    public void notEqualsFileDifferentTest() {
        Coordinate coordinate = new Coordinate(5, 3);
        Coordinate differentCoordinate = new Coordinate(5, 5);

        assertFalse(coordinate.equals(differentCoordinate));
        assertFalse(differentCoordinate.equals(coordinate));
    }

    @Test
    public void notEqualsRankAndFileSwappedTest() {
        Coordinate coordinate = new Coordinate(4, 7);
        Coordinate differentCoordinate = new Coordinate(7, 4);

        assertFalse(coordinate.equals(differentCoordinate));
        assertFalse(differentCoordinate.equals(coordinate));
    }

    @Test
    public void equalsTest1() {
        Coordinate coordinate = new Coordinate(5, 3);
        Coordinate differentCoordinate = new Coordinate(5, 3);

        assertTrue(coordinate.equals(differentCoordinate));
        assertTrue(differentCoordinate.equals(coordinate));
    }

    @Test
    public void equalsTest2() {
        Coordinate coordinate = new Coordinate(4, 7);
        Coordinate differentCoordinate = new Coordinate(4, 7);

        assertTrue(coordinate.equals(differentCoordinate));
        assertTrue(differentCoordinate.equals(coordinate));
    }
}
