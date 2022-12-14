package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.Conductor;
import model.Position;
import model.ArrayBoard;
import model.Board;
import model.Piece;
import uciservice.FenParser;

public class PositionTest {

    /**
     * Position with every boolean set to false and every numberic value set to the
     * lowest possible value.
     */
    private Position testPosition;


    private static Position queenTestPosition;
    private static Position rookTestPosition;
    private static Position bishopTestPosition;
    private static Position pawnAttackMapTestPosition;
    private static Position kingTestPosition;
    private static Position knightTestPosition;

    @AfterEach
    public void cleanup(){
        Conductor.emptyPositionList();
    }

    @BeforeAll
    public static void setup() {
        queenTestPosition = FenParser.parseFen("8/1q4k1/8/8/8/8/1Q4K1/8 w KQkq - 0 1");
        rookTestPosition = FenParser.parseFen("8/1r4k1/8/8/8/8/1K4Q1/8 w KQkq - 0 1");
        bishopTestPosition = FenParser.parseFen("8/1b4k1/8/8/8/8/1Q4K1/8 " +
            "w KQkq - 0 1");  
        kingTestPosition = FenParser.parseFen("8/1k4R1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
        pawnAttackMapTestPosition = FenParser.parseFen("8/1p4k1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
        knightTestPosition = FenParser.parseFen("8/1n4k1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
        Conductor.emptyPositionList();
    }

    @BeforeEach
    /**
     * Sets up the test position with every boolean set to false and every numberic
     * value set to the lowest possible value.
     * Do not make changes to the Position constructor invocation, it is mirrored in
     * some test methods.
     */
    public void setUpPosition() {
        this.testPosition = new Position(false, false, testBoard, false, false, false, false, false, 0, 0, (byte)0, 1);
    }

    @Test
    public void checkTestTest(){
        assertTrue(queenTestPosition.getWhiteInCheck());
        assertTrue(bishopTestPosition.getWhiteInCheck());
        assertTrue(rookTestPosition.getWhiteInCheck());
        assertFalse(knightTestPosition.getWhiteInCheck());
        assertFalse(pawnAttackMapTestPosition.getWhiteInCheck());
        assertFalse(kingTestPosition.getWhiteInCheck());
        assertTrue(FenParser.parseFen("K5q1/8/8/8/8/8/8/k7 b - - 0 1").getWhiteInCheck());
    }


    /**
     * take care not to make changes to this array as it is shared between tests
     */
    private Piece[][] testSquares = {
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
        private Board testBoard = new ArrayBoard(testSquares);

    /**
     * Returns a position that is equal to testPosition except for one square.
     * @param rank the rank of the square that should be changed
     * @param file the file of the square that should be changed
     * @param pieceCharacter the piece that should be placed on the square
     * @return
     */
    private Position generatePositionWithModififedSquares(int rank, int file, char pieceCharacter) {
        Board modifiedSquares = testPosition.copyBoard();
        modifiedSquares.setPieceAt(rank, file, new Piece(pieceCharacter));
        return new Position(false, false, modifiedSquares, false, false, false, false, false, 0, 0, (byte)0, 1);
    }

    /**
     * Asserts that the position has en passant target rank and file of -1.
     */
    private void assertNoEnPassantTargetSquare(Position position) {
        assertEquals(-1, position.getEnPassantTargetRank());
        assertEquals(-1, position.getEnPassantTargetFile());
    }

    @Test
    public void constructorTest() {
        Position constructedPosition = new Position(true, false, testBoard, true, true, false, false, true, 3, 5, (byte)30, 4289);
        assertTrue(constructedPosition.getWhiteInCheck());
        assertFalse(constructedPosition.getBlackInCheck());
        assertTrue(testBoard == constructedPosition.getBoard());
        assertTrue(constructedPosition.getWhiteNextMove());
        assertTrue(constructedPosition.getWhiteCastlingKingside());
        assertFalse(constructedPosition.getWhiteCastlingQueenside());
        assertFalse(constructedPosition.getBlackCastlingKingside());
        assertTrue(constructedPosition.getBlackCastlingQueenside());
        assertEquals(3, constructedPosition.getEnPassantTargetRank());
        assertEquals(5, constructedPosition.getEnPassantTargetFile());
        assertEquals(30, constructedPosition.getHalfMoves());
        assertEquals(4289, constructedPosition.getFullMoves());
    }

    @Test
    public void followUpPositionNoCheckTest() {
        Position base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Board copiedSquares = base.copyBoard();
        Position followUpPosition = base.generateFollowUpPosition(copiedSquares, false);
        assertFalse(followUpPosition.getBlackInCheck());
        assertFalse(followUpPosition.getWhiteInCheck());
    }

    @Test
    public void followUpPositionWhiteInCheckTest() {
        Position base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Board copiedSquares = base.copyBoard();
        copiedSquares.setPieceAt(1, 2, new Piece('n'));// white in check
        Position followUpPosition = base.generateFollowUpPosition(copiedSquares, false);
        assertFalse(followUpPosition.getBlackInCheck());
        assertTrue(followUpPosition.getWhiteInCheck());
    }

    @Test
    public void followUpPositionBlackInCheckTest() {
        Position base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Board copiedSquares = base.copyBoard();
        copiedSquares.setPieceAt(3, 0, new Piece('R'));// black in check
        Position followUpPosition = base.generateFollowUpPosition(copiedSquares, false);
        assertTrue(followUpPosition.getBlackInCheck());
        assertFalse(followUpPosition.getWhiteInCheck());
    }

    @Test
    public void followUpPositionBothInCheckTest() {
        Position base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Board copiedSquares = base.copyBoard();
        copiedSquares.setPieceAt(3, 0, new Piece('R')); // black in check
        copiedSquares.setPieceAt(1, 2, new Piece('n')); // white in check
        Position followUpPosition = base.generateFollowUpPosition(copiedSquares, false);
        assertTrue(followUpPosition.getBlackInCheck());
        assertTrue(followUpPosition.getWhiteInCheck());
    }

    @Test
    public void copySquaresTest() {
        Position position = new Position(false, false, testBoard, false, false, false, false, false, 0, 0, (byte)0, 1);
        Board copy = position.copyBoard();
        // verify that a new array was created
        // pieces need not be new instances
        assertTrue(testBoard != copy);
        for (int i = 0; i < 8; i++) {
            assertTrue(testBoard.getRank(i) != copy.getRank(i));
            for (int j = 0; j < 8; j++) {
                assertEquals(testBoard.getPieceAt(i, j), copy.getPieceAt(i, j));
            }
        }
    }

    @Test
    public void generateFollowUpPositionForBlack() {
        //white next move == false
        Board copiedSquares = testPosition.copyBoard();
        Position followUpPosition = testPosition.generateFollowUpPosition(copiedSquares, false);

        //check active player swap etc
        assertNoEnPassantTargetSquare(followUpPosition);
        assertEquals(!testPosition.getWhiteNextMove(), followUpPosition.getWhiteNextMove());
        assertTrue(followUpPosition.getBoard() == copiedSquares);
        assertEquals(testPosition.getHalfMoves() + 1, followUpPosition.getHalfMoves());
        assertEquals(testPosition.getFullMoves() + 1, followUpPosition.getFullMoves());
    }

    @Test
    public void generateFollowUpPositionForWhite() {
        //white next move == true
        Position basePosition = new Position(false, false, testBoard, true, false, false, false, false, 0, 0, (byte)0, 1);
        Board copiedSquares = basePosition.copyBoard();
        Position followUpPosition = basePosition.generateFollowUpPosition(copiedSquares, false);

        //check active player swap etc
        assertNoEnPassantTargetSquare(followUpPosition);
        assertEquals(!basePosition.getWhiteNextMove(), followUpPosition.getWhiteNextMove());
        assertTrue(followUpPosition.getBoard() == copiedSquares);
        assertEquals(basePosition.getHalfMoves() + 1, followUpPosition.getHalfMoves());
        assertEquals(basePosition.getFullMoves() + 0, followUpPosition.getFullMoves());
    }

    @Test
    public void generateFollowUpPositionForBlackWithHalfMoveResetTest() {
        //white next move == false
        Board copiedSquares = testPosition.copyBoard();
        Position followUpPosition = testPosition.generateFollowUpPosition(copiedSquares, true);

        //check active player swap etc
        assertNoEnPassantTargetSquare(followUpPosition);
        assertEquals(!testPosition.getWhiteNextMove(), followUpPosition.getWhiteNextMove());
        assertTrue(followUpPosition.getBoard() == copiedSquares);
        assertEquals(0, followUpPosition.getHalfMoves());
        assertEquals(testPosition.getFullMoves() + 1, followUpPosition.getFullMoves());
    }

    @Test
    public void generateFollowUpPositionWithEnPassantTargetSquare() {
        //white next move == false
        Board copiedSquares = testPosition.copyBoard();
        Position followUpPosition = testPosition.generateFollowUpPosition(copiedSquares, 4,7, true);

        //check active player swap etc
        assertEquals(4, followUpPosition.getEnPassantTargetRank());
        assertEquals(7, followUpPosition.getEnPassantTargetFile());
        assertEquals(!testPosition.getWhiteNextMove(), followUpPosition.getWhiteNextMove());
        assertTrue(followUpPosition.getBoard() == copiedSquares);
        assertEquals(0, followUpPosition.getHalfMoves());
        assertEquals(testPosition.getFullMoves() + 1, followUpPosition.getFullMoves());
    }

    @Test
    public void generateFollowUpPositionAllParameters() {
                //white next move == false
                Board copiedSquares = testPosition.copyBoard();
                Position followUpPosition = testPosition.generateFollowUpPosition(copiedSquares, 6, 1, false, true, false, true, false);
        
                //check active player swap etc
                assertEquals(6, followUpPosition.getEnPassantTargetRank());
                assertEquals(1, followUpPosition.getEnPassantTargetFile());
                assertEquals(!testPosition.getWhiteNextMove(), followUpPosition.getWhiteNextMove());
                assertTrue(followUpPosition.getBoard() == copiedSquares);
                assertEquals(testPosition.getHalfMoves() + 1, followUpPosition.getHalfMoves());
                assertEquals(testPosition.getFullMoves() + 1, followUpPosition.getFullMoves());
                assertFalse(followUpPosition.getWhiteCastlingKingside());
                assertTrue(followUpPosition.getWhiteCastlingQueenside());
                assertFalse(followUpPosition.getBlackCastlingKingside());
                assertTrue(followUpPosition.getBlackCastlingQueenside());
    }

    @Test
    public void equalsToSelf() {
        assertEquals(testPosition, testPosition);
    }

    @Test
    public void cloneReturnsEqualPositionTest() {
        Position clonedPosition = testPosition.clone();
        //cloned position needs to be a new instance
        assertTrue(clonedPosition != testPosition);
        assertEquals(testPosition, clonedPosition);
    }

    @Test
    public void notEqualsWhiteInCheckPositionTest() {
        Position clonedPosition = testPosition.clone();
        clonedPosition.setWhiteInCheck(true);
        assertFalse(testPosition.equals(clonedPosition));
    }

    @Test
    public void notEqualsBlackInCheckPositionTest() {
        Position clonedPosition = testPosition.clone();
        clonedPosition.setBlackInCheck(true);
        assertFalse(testPosition.equals(clonedPosition));
    }

    @Test
    public void notEqualsSquaresTest() {
        Position comparedPosition = generatePositionWithModififedSquares(4, 7, 'b');
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsA8SquareTest() {
        Position comparedPosition = generatePositionWithModififedSquares(0, 0, 'b');
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsH1SquareTest() {
        Position comparedPosition = generatePositionWithModififedSquares(7, 7, 'N');
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsA5SquareTest() {
        Position comparedPosition = generatePositionWithModififedSquares(0, 3, 'N');
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualswhiteNextMove() {
        Position comparedPosition = new Position(false, false, testPosition.copyBoard(), true, false, false, false, false, 0, 0,
        (byte)0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsWhiteCastlingKingside() {
        Position comparedPosition = new Position(false, false, testPosition.copyBoard(), false, true, false, false, false, 0, 0,
        (byte) 0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsWhiteCastlingQueenside() {
        Position comparedPosition = new Position(false, false, testPosition.copyBoard(), false, false, true, false, false, 0, 0,
        (byte)0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsBlackCastlingKingside() {
        Position comparedPosition = new Position(false, false, testPosition.copyBoard(), false, false, false, true, false, 0, 0,
        (byte) 0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsBlackCastlingQueenside() {
        Position comparedPosition = new Position(false, false, testPosition.copyBoard(), false, false, false, false, true, 0, 0,
        (byte)0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsEnPassantTargetRank() {
        Position comparedPosition = new Position(false, false, testPosition.copyBoard(), false, false, false, false, false, 1,
                0, (byte)0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsEnPassantTargetFile() {
        Position comparedPosition = new Position(false, false, testPosition.copyBoard(), false, false, false, false, false, 0,
                1, (byte)0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsHalfMoves() {
        Position comparedPosition = new Position(false, false, testPosition.copyBoard(), false, false, false, false, false, 0,
                0, (byte)1, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsFullMoves() {
        Position comparedPosition = new Position(false, false, testPosition.copyBoard(), false, false, false, false, false, 0,
                0, (byte)0, 2);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void threefoldRepetitionTestTrue(){
        Position testPos = FenParser.parseFen("r1bqkbnr/pppppppp/n7/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 5 3");
        Conductor.appendPosition(FenParser.parseFen("r1bqkbnr/pppppppp/n7/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 1 1").toStringLight());
        Conductor.appendPosition(FenParser.parseFen("r1bqkbnr/pppppppp/8/2n3N1/8/8/PPPPPPPP/RNBQKB1R b KQkq - 2 1").toStringLight());
        Conductor.appendPosition(FenParser.parseFen("r1bqkbnr/pppppppp/n7/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 3 2").toStringLight());
        Conductor.appendPosition(FenParser.parseFen("r1bqkbnr/pppppppp/8/2n3N1/8/8/PPPPPPPP/RNBQKB1R b KQkq - 4 2").toStringLight());
        Conductor.appendPosition(testPos.toStringLight());
        assertTrue(testPos.isDraw());
    }

    @Test
    public void threefoldRepetitionTestFalse(){
        Position testPos = FenParser.parseFen("r1bqkbnr/pppppppp/4n3/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 5 3");
        Conductor.appendPosition(FenParser.parseFen("r1bqkbnr/pppppppp/n7/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 1 1").toStringLight());
        Conductor.appendPosition(FenParser.parseFen("r1bqkbnr/pppppppp/8/2n3N1/8/8/PPPPPPPP/RNBQKB1R b KQkq - 2 1").toStringLight());
        Conductor.appendPosition(FenParser.parseFen("r1bqkbnr/pppppppp/n7/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 3 2").toStringLight());
        Conductor.appendPosition(FenParser.parseFen("r1bqkbnr/pppppppp/8/2n3N1/8/8/PPPPPPPP/RNBQKB1R b KQkq - 4 2").toStringLight());
        Conductor.appendPosition(testPos.toStringLight());
        assertFalse(testPos.isDraw());
    }

    @Test
    public void hashTestTrue(){
        Position position1 = FenParser.parseFen("r1bqkbnr/pppppppp/4n3/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 5 3");
        Position position2 = FenParser.parseFen("r1bqkbnr/pppppppp/4n3/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 5 3");
        Position position3 = FenParser.parseFen("r1bqkbnr/pppppppp/4n3/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 5 3");
        assertEquals(position1.hashCode(), position2.hashCode());
        assertEquals(position2.hashCode(), position3.hashCode());
    }

    @Test
    public void hashTestFalse(){
        Position position1 = FenParser.parseFen("r1bqkbnr/pppppppp/4n3/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 5 3");
        Position position2 = FenParser.parseFen("r1bqkbnr/pppppppp/4n3/8/8/5N2/PPPPPPPP/RNBQKB1R w - - 5 3");
        Position position3 = FenParser.parseFen("r1bqkbnr/pppppppp/4n3/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 5 3");
        Position position4 = FenParser.parseFen("r1bqkbnr/pppppppp/3n4/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 5 3");
        assertNotEquals(position1.hashCode(), position2.hashCode());
        assertNotEquals(position1.hashCode(), position3.hashCode());
        assertNotEquals(position1.hashCode(), position4.hashCode());
    }

    @Test
    public void fiftyMovesTestTrue(){
        Position testPos = FenParser.parseFen("r1bqkbnr/pppppppp/4n3/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 100 50");
        assertTrue(testPos.isDraw());
    }

    @Test
    public void fiftyMovesTestFalse(){
        Position testPos = FenParser.parseFen("r1bqkbnr/pppppppp/4n3/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 88 50");
        assertFalse(testPos.isDraw());
    }

    @Test
    public void toStringFenTest(){
        String startpos = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String benchmark1 = "rn2kb1r/pppppppp/1b4n1/4q3/2Q3B1/3N4/PPPPPPPP/RNB1K2R w KQkq - 0 1";
        String benchmark2 = "rnb1kbnr/ppp1pppp/8/8/1PP5/2N5/Pq1PBPPP/3QK1NR b Kkq - 0 1";
        assertEquals(startpos, FenParser.parseFen(startpos).toStringFen());
        assertEquals(benchmark1, FenParser.parseFen(benchmark1).toStringFen());
        assertEquals(benchmark2, FenParser.parseFen(benchmark2).toStringFen());
    }

    @Test
    public void illegalFullMoveCountTest() {
        assertThrows(IllegalArgumentException.class, () -> new Position(false, false, testBoard, false, false, false, false, false, 0, 0, (byte)3, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(false, false, testBoard, false, false, false, false, false, 0, 0, (byte)3, -1));
        assertThrows(IllegalArgumentException.class, () -> new Position(false, false, testBoard, false, false, false, false, false, 0, 0, (byte)3, Integer.MIN_VALUE));
        assertThrows(IllegalArgumentException.class, () ->testPosition.clone().setFullMoveCount(0));
        assertThrows(IllegalArgumentException.class, () ->testPosition.clone().setFullMoveCount(-1));
        assertThrows(IllegalArgumentException.class, () ->testPosition.clone().setFullMoveCount(Integer.MIN_VALUE));
    }

    @Test
    public void illegalHalfMoveCountTest() {
        assertThrows(IllegalArgumentException.class, () -> new Position(false, false, testBoard, false, false, false, false, false, 0, 0, (byte)-1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Position(false, false, testBoard, false, false, false, false, false, 0, 0, (byte)101, 1));
        assertThrows(IllegalArgumentException.class, () -> new Position(false, false, testBoard, false, false, false, false, false, 0, 0, Byte.MAX_VALUE, 1));
        assertThrows(IllegalArgumentException.class, () -> new Position(false, false, testBoard, false, false, false, false, false, 0, 0, Byte.MIN_VALUE, 1));
    }
}
