
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Board;
import model.Piece;
import uciservice.FenParser;

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

    /**
     * Returns a board that is equal to testBoard except for one square.
     * @param rank the rank of the square that should be changed
     * @param file the file of the square that should be changed
     * @param pieceCharacter the piece that should be placed on the square
     * @return
     */
    private Board generateBoardWithModififedSpaces(int rank, int file, char pieceCharacter) {
        Piece[][] modifiedSpaces = testBoard.copySpaces();
        modifiedSpaces[rank][file] = new Piece(pieceCharacter);
        return new Board(0, false, false, modifiedSpaces, false, false, false, false, false, 0, 0, 0, 1);
    }

    /**
     * Asserts that the board has en passant target rank and file of -1.
     */
    private void assertNoEnPassantTargetSquare(Board board) {
        assertEquals(-1, board.getEnPassantTargetRank());
        assertEquals(-1, board.getEnPassantTargetFile());
    }

    @Test
    public void constructorTest() {
        Board constructedBoard = new Board(Integer.MAX_VALUE, true, false, testSpaces, true, true, false, false, true, 3, 5, 1297, 4289);
        assertEquals(Integer.MAX_VALUE, constructedBoard.getPointValue());
        assertTrue(constructedBoard.getWhiteInCheck());
        assertFalse(constructedBoard.getBlackInCheck());
        assertTrue(testSpaces == constructedBoard.getSpaces());
        assertTrue(constructedBoard.getWhiteNextMove());
        assertTrue(constructedBoard.getWhiteCastlingKingside());
        assertFalse(constructedBoard.getWhiteCastlingQueenside());
        assertFalse(constructedBoard.getBlackCastlingKingside());
        assertTrue(constructedBoard.getBlackCastlingQueenside());
        assertEquals(3, constructedBoard.getEnPassantTargetRank());
        assertEquals(5, constructedBoard.getEnPassantTargetFile());
        assertEquals(1297, constructedBoard.getHalfMoves());
        assertEquals(4289, constructedBoard.getFullMoves());


    }

    @Test
    public void followUpBoardNoCheckTest() {
        Board base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Piece[][] copiedSpaces = base.copySpaces();
        Board followUpBoard = base.generateFollowUpBoard(copiedSpaces, false);
        assertTrue(followUpBoard.getBlackInCheck());
        assertFalse(followUpBoard.getWhiteInCheck());
    }

    @Test
    public void followUpBoardWhiteInCheckTest() {
        Board base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Piece[][] copiedSpaces = base.copySpaces();
        copiedSpaces[1][2] = new Piece('n');// white in check
        Board followUpBoard = base.generateFollowUpBoard(copiedSpaces, false);
        assertFalse(followUpBoard.getBlackInCheck());
        assertTrue(followUpBoard.getWhiteInCheck());
    }

    @Test
    public void followUpBoardBlackInCheckTest() {
        Board base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Piece[][] copiedSpaces = base.copySpaces();
        copiedSpaces[3][0] = new Piece('R');// black in check
        Board followUpBoard = base.generateFollowUpBoard(copiedSpaces, false);
        assertTrue(followUpBoard.getBlackInCheck());
        assertFalse(followUpBoard.getWhiteInCheck());
    }

    @Test
    public void followUpBoardBothInCheckTest() {
        Board base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Piece[][] copiedSpaces = base.copySpaces();
        copiedSpaces[3][0] = new Piece('R'); // black in check
        copiedSpaces[1][2] = new Piece('n'); // white in check
        Board followUpBoard = base.generateFollowUpBoard(copiedSpaces, false);
        assertTrue(followUpBoard.getBlackInCheck());
        assertTrue(followUpBoard.getWhiteInCheck());
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
    public void generateFollowUpBoardForBlack() {
        //white next move == false
        Piece[][] copiedSpaces = testBoard.copySpaces();
        Board followUpBoard = testBoard.generateFollowUpBoard(copiedSpaces, false);

        //check active player swap etc
        assertNoEnPassantTargetSquare(followUpBoard);
        assertEquals(!testBoard.getWhiteNextMove(), followUpBoard.getWhiteNextMove());
        assertTrue(followUpBoard.getSpaces() == copiedSpaces);
        assertEquals(testBoard.getHalfMoves() + 1, followUpBoard.getHalfMoves());
        assertEquals(testBoard.getFullMoves() + 1, followUpBoard.getFullMoves());
    }

    @Test
    public void generateFollowUpBoardForWhite() {
        //white next move == true
        Board baseBoard = new Board(0, false, false, testSpaces, true, false, false, false, false, 0, 0, 0, 1);
        Piece[][] copiedSpaces = baseBoard.copySpaces();
        Board followUpBoard = baseBoard.generateFollowUpBoard(copiedSpaces, false);

        //check active player swap etc
        assertNoEnPassantTargetSquare(followUpBoard);
        assertEquals(!baseBoard.getWhiteNextMove(), followUpBoard.getWhiteNextMove());
        assertTrue(followUpBoard.getSpaces() == copiedSpaces);
        assertEquals(baseBoard.getHalfMoves() + 1, followUpBoard.getHalfMoves());
        assertEquals(baseBoard.getFullMoves() + 0, followUpBoard.getFullMoves());
    }

    @Test
    public void generateFollowUpBoardForBlackWithHalfMoveResetTest() {
        //white next move == false
        Piece[][] copiedSpaces = testBoard.copySpaces();
        Board followUpBoard = testBoard.generateFollowUpBoard(copiedSpaces, true);

        //check active player swap etc
        assertNoEnPassantTargetSquare(followUpBoard);
        assertEquals(!testBoard.getWhiteNextMove(), followUpBoard.getWhiteNextMove());
        assertTrue(followUpBoard.getSpaces() == copiedSpaces);
        assertEquals(0, followUpBoard.getHalfMoves());
        assertEquals(testBoard.getFullMoves() + 1, followUpBoard.getFullMoves());
    }

    @Test
    public void generateFollowUpBoardWithEnPassantTargetSquare() {
        //white next move == false
        Piece[][] copiedSpaces = testBoard.copySpaces();
        Board followUpBoard = testBoard.generateFollowUpBoard(copiedSpaces, 4,7, true);

        //check active player swap etc
        assertEquals(4, followUpBoard.getEnPassantTargetRank());
        assertEquals(7, followUpBoard.getEnPassantTargetFile());
        assertEquals(!testBoard.getWhiteNextMove(), followUpBoard.getWhiteNextMove());
        assertTrue(followUpBoard.getSpaces() == copiedSpaces);
        assertEquals(0, followUpBoard.getHalfMoves());
        assertEquals(testBoard.getFullMoves() + 1, followUpBoard.getFullMoves());
    }

    @Test
    public void generateFollowUpBoardAllParameters() {
                //white next move == false
                Piece[][] copiedSpaces = testBoard.copySpaces();
                Board followUpBoard = testBoard.generateFollowUpBoard(copiedSpaces, 6, 1, false, true, false, true, false);
        
                //check active player swap etc
                assertEquals(6, followUpBoard.getEnPassantTargetRank());
                assertEquals(1, followUpBoard.getEnPassantTargetFile());
                assertEquals(!testBoard.getWhiteNextMove(), followUpBoard.getWhiteNextMove());
                assertTrue(followUpBoard.getSpaces() == copiedSpaces);
                assertEquals(testBoard.getHalfMoves() + 1, followUpBoard.getHalfMoves());
                assertEquals(testBoard.getFullMoves() + 1, followUpBoard.getFullMoves());
                assertFalse(followUpBoard.getWhiteCastlingKingside());
                assertTrue(followUpBoard.getWhiteCastlingQueenside());
                assertFalse(followUpBoard.getBlackCastlingKingside());
                assertTrue(followUpBoard.getBlackCastlingQueenside());
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

    
}
