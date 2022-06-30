
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Board;
import model.Piece;

public class BoardTest {

    /**
     * Board with every boolean set to false and every numberic value set to the
     * lowest possible value.
     */
    private Board testBoard;

    @BeforeEach
    /**
     * Sets up the test board with every boolean set to false and every numberic
     * value set to the lowest possible value.
     * Do not make changes to the Board constructor invocation, it is mirrored in
     * some test methods.
     */
    public void setUpBoard() {
        this.testBoard = new Board(0, false, false, testSpaces, false, false, false, false, false, 0, 0, 0, 1);
    }

    /**
     * take care not to make changes to this array as it is shared between tests
     */
    private Piece[][] testSpaces = {
            { new Piece('p'), new Piece('B'), new Piece('r'), new Piece('n'), new Piece('Q'), new Piece('k'),
                    new Piece('n'), new Piece('q') },
            { new Piece('Q'), new Piece('b'), new Piece('P'), new Piece('p'), new Piece('B'), new Piece('P'),
                    new Piece('r'), new Piece('R') },
            { new Piece('b'), new Piece('B'), new Piece('r'), new Piece('R'), new Piece('n'), new Piece('N'),
                    new Piece('p'), new Piece('P') },
            { new Piece('p'), new Piece('p'), new Piece('b'), new Piece('b'), null, new Piece('r'), new Piece('n'),
                    new Piece('P') },
            { null, new Piece('q'), new Piece('K'), new Piece('b'), null, new Piece('P'), new Piece('N'),
                    new Piece('p') },
            { new Piece('n'), null, null, new Piece('N'), null, new Piece('b'), new Piece('q'), null },
            { new Piece('r'), new Piece('N'), new Piece('P'), new Piece('P'), null, new Piece('R'), new Piece('r'),
                    new Piece('Q') },
            { new Piece('N'), new Piece('R'), null, new Piece('b'), null, new Piece('p'), new Piece('r'), null },
    };

    private Board generateBoardWithModififedSpaces(int rank, int file, char pieceCharacter) {
        Piece[][] modifiedSpaces = testBoard.copySpaces();
        modifiedSpaces[rank][file] = new Piece(pieceCharacter);
        return new Board(0, false, false, modifiedSpaces, false, false, false, false, false, 0, 0, 0, 1);
    }

    @Test
    public void copySpacesTest() {
        Board board = new Board(false, false, testSpaces, false, false, false, false, false, 0, 0, 0, 1);
        Piece[][] copy = board.copySpaces();
        // verify that a new array was created
        // pieces need not be new instances
        assertTrue(testSpaces != copy);
        for (int i = 0; i < 8; i++) {
            assertTrue(testSpaces[i] != copy[i]);
            for (int j = 0; j < 8; j++) {
                assertEquals(testSpaces[i][j], copy[i][j]);
            }
        }
    }

    @Test
    public void equalsToSelf() {
        assertEquals(testBoard, testBoard);
    }

    @Test
    public void cloneReturnsEqualBoardTest() {
        Board clonedBoard = testBoard.clone();
        //cloned board needs to be a new instance
        assertTrue(clonedBoard != testBoard);
        assertEquals(testBoard, clonedBoard);
    }

    @Test
    public void notEqualsPointValueBoardTest() {
        Board clonedBoard = testBoard.clone();
        clonedBoard.setPointValue(1);
        assertFalse(testBoard.equals(clonedBoard));
    }

    @Test
    public void notEqualsWhiteInCheckBoardTest() {
        Board clonedBoard = testBoard.clone();
        clonedBoard.setWhiteInCheck(true);
        assertFalse(testBoard.equals(clonedBoard));
    }

    @Test
    public void notEqualsBlackInCheckBoardTest() {
        Board clonedBoard = testBoard.clone();
        clonedBoard.setBlackInCheck(true);
        assertFalse(testBoard.equals(clonedBoard));
    }

    @Test
    public void notEqualsSpacesTest() {
        Board comparedBoard = generateBoardWithModififedSpaces(4, 7, 'b');
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsA8SpaceTest() {
        Board comparedBoard = generateBoardWithModififedSpaces(0, 0, 'b');
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsH1SpaceTest() {
        Board comparedBoard = generateBoardWithModififedSpaces(7, 7, 'N');
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsA5SpaceTest() {
        Board comparedBoard = generateBoardWithModififedSpaces(0, 3, 'N');
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualswhiteNextMove() {
        Board comparedBoard = new Board(0, false, false, testBoard.copySpaces(), true, false, false, false, false, 0, 0,
                0, 1);
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsWhiteCastlingKingside() {
        Board comparedBoard = new Board(0, false, false, testBoard.copySpaces(), false, true, false, false, false, 0, 0,
                0, 1);
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsWhiteCastlingQueenside() {
        Board comparedBoard = new Board(0, false, false, testBoard.copySpaces(), false, false, true, false, false, 0, 0,
                0, 1);
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsBlackCastlingKingside() {
        Board comparedBoard = new Board(0, false, false, testBoard.copySpaces(), false, false, false, true, false, 0, 0,
                0, 1);
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsBlackCastlingQueenside() {
        Board comparedBoard = new Board(0, false, false, testBoard.copySpaces(), false, false, false, false, true, 0, 0,
                0, 1);
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsEnPassantTargetRank() {
        Board comparedBoard = new Board(0, false, false, testBoard.copySpaces(), false, false, false, false, false, 1,
                0, 0, 1);
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsEnPassantTargetFile() {
        Board comparedBoard = new Board(0, false, false, testBoard.copySpaces(), false, false, false, false, false, 0,
                1, 0, 1);
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsHalfMoves() {
        Board comparedBoard = new Board(0, false, false, testBoard.copySpaces(), false, false, false, false, false, 0,
                0, 1, 1);
        assertFalse(testBoard.equals(comparedBoard));
    }

    @Test
    public void notEqualsFullMoves() {
        Board comparedBoard = new Board(0, false, false, testBoard.copySpaces(), false, false, false, false, false, 0,
                0, 0, 2);
        assertFalse(testBoard.equals(comparedBoard));
    }

    //test
}
