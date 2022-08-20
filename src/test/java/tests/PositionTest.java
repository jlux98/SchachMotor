package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Position;
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


    @BeforeAll
    public static void setup() {
        queenTestPosition = FenParser.parseFen("8/1q4k1/8/8/8/8/1Q4K1/8 " +
            "w KQkq - 0 1");
        rookTestPosition = FenParser.parseFen("8/1r4k1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
        bishopTestPosition = FenParser.parseFen("8/1b4k1/8/8/8/8/1Q4K1/8 " +
            "w KQkq - 0 1");  
        kingTestPosition = FenParser.parseFen("8/1k4R1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
        pawnAttackMapTestPosition = FenParser.parseFen("8/1p4k1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
        knightTestPosition = FenParser.parseFen("8/1n4k1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
    }

    @BeforeEach
    /**
     * Sets up the test position with every boolean set to false and every numberic
     * value set to the lowest possible value.
     * Do not make changes to the Position constructor invocation, it is mirrored in
     * some test methods.
     */
    public void setUpPosition() {
        this.testPosition = new Position(0, false, false, testSpaces, false, false, false, false, false, 0, 0, 0, 1);
    }

    @Test
    public void checkTestTest(){
        assertTrue(queenTestPosition.getWhiteInCheck());
        assertTrue(bishopTestPosition.getWhiteInCheck());
        assertTrue(rookTestPosition.getWhiteInCheck());
        assertFalse(knightTestPosition.getWhiteInCheck());
        assertFalse(pawnAttackMapTestPosition.getWhiteInCheck());
        assertFalse(kingTestPosition.getWhiteInCheck());
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
     * Returns a position that is equal to testPosition except for one square.
     * @param rank the rank of the square that should be changed
     * @param file the file of the square that should be changed
     * @param pieceCharacter the piece that should be placed on the square
     * @return
     */
    private Position generatePositionWithModififedSpaces(int rank, int file, char pieceCharacter) {
        Piece[][] modifiedSpaces = testPosition.copySpaces();
        modifiedSpaces[rank][file] = new Piece(pieceCharacter);
        return new Position(0, false, false, modifiedSpaces, false, false, false, false, false, 0, 0, 0, 1);
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
        Position constructedPosition = new Position(Integer.MAX_VALUE, true, false, testSpaces, true, true, false, false, true, 3, 5, 1297, 4289);
        assertEquals(Integer.MAX_VALUE, constructedPosition.getPointValue());
        assertTrue(constructedPosition.getWhiteInCheck());
        assertFalse(constructedPosition.getBlackInCheck());
        assertTrue(testSpaces == constructedPosition.getSpaces());
        assertTrue(constructedPosition.getWhiteNextMove());
        assertTrue(constructedPosition.getWhiteCastlingKingside());
        assertFalse(constructedPosition.getWhiteCastlingQueenside());
        assertFalse(constructedPosition.getBlackCastlingKingside());
        assertTrue(constructedPosition.getBlackCastlingQueenside());
        assertEquals(3, constructedPosition.getEnPassantTargetRank());
        assertEquals(5, constructedPosition.getEnPassantTargetFile());
        assertEquals(1297, constructedPosition.getHalfMoves());
        assertEquals(4289, constructedPosition.getFullMoves());


    }

    @Test
    public void followUpPositionNoCheckTest() {
        Position base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Piece[][] copiedSpaces = base.copySpaces();
        Position followUpPosition = base.generateFollowUpPosition(copiedSpaces, false);
        assertFalse(followUpPosition.getBlackInCheck());
        assertFalse(followUpPosition.getWhiteInCheck());
    }

    @Test
    public void followUpPositionWhiteInCheckTest() {
        Position base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Piece[][] copiedSpaces = base.copySpaces();
        copiedSpaces[1][2] = new Piece('n');// white in check
        Position followUpPosition = base.generateFollowUpPosition(copiedSpaces, false);
        assertFalse(followUpPosition.getBlackInCheck());
        assertTrue(followUpPosition.getWhiteInCheck());
    }

    @Test
    public void followUpPositionBlackInCheckTest() {
        Position base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Piece[][] copiedSpaces = base.copySpaces();
        copiedSpaces[3][0] = new Piece('R');// black in check
        Position followUpPosition = base.generateFollowUpPosition(copiedSpaces, false);
        assertTrue(followUpPosition.getBlackInCheck());
        assertFalse(followUpPosition.getWhiteInCheck());
    }

    @Test
    public void followUpPositionBothInCheckTest() {
        Position base = FenParser.parseFen("8/8/4K3/5r2/8/3B4/8/k7 w - - 0 1"); // no check
        Piece[][] copiedSpaces = base.copySpaces();
        copiedSpaces[3][0] = new Piece('R'); // black in check
        copiedSpaces[1][2] = new Piece('n'); // white in check
        Position followUpPosition = base.generateFollowUpPosition(copiedSpaces, false);
        assertTrue(followUpPosition.getBlackInCheck());
        assertTrue(followUpPosition.getWhiteInCheck());
    }

    @Test
    public void copySpacesTest() {
        Position position = new Position(false, false, testSpaces, false, false, false, false, false, 0, 0, 0, 1);
        Piece[][] copy = position.copySpaces();
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
    public void generateFollowUpPositionForBlack() {
        //white next move == false
        Piece[][] copiedSpaces = testPosition.copySpaces();
        Position followUpPosition = testPosition.generateFollowUpPosition(copiedSpaces, false);

        //check active player swap etc
        assertNoEnPassantTargetSquare(followUpPosition);
        assertEquals(!testPosition.getWhiteNextMove(), followUpPosition.getWhiteNextMove());
        assertTrue(followUpPosition.getSpaces() == copiedSpaces);
        assertEquals(testPosition.getHalfMoves() + 1, followUpPosition.getHalfMoves());
        assertEquals(testPosition.getFullMoves() + 1, followUpPosition.getFullMoves());
    }

    @Test
    public void generateFollowUpPositionForWhite() {
        //white next move == true
        Position basePosition = new Position(0, false, false, testSpaces, true, false, false, false, false, 0, 0, 0, 1);
        Piece[][] copiedSpaces = basePosition.copySpaces();
        Position followUpPosition = basePosition.generateFollowUpPosition(copiedSpaces, false);

        //check active player swap etc
        assertNoEnPassantTargetSquare(followUpPosition);
        assertEquals(!basePosition.getWhiteNextMove(), followUpPosition.getWhiteNextMove());
        assertTrue(followUpPosition.getSpaces() == copiedSpaces);
        assertEquals(basePosition.getHalfMoves() + 1, followUpPosition.getHalfMoves());
        assertEquals(basePosition.getFullMoves() + 0, followUpPosition.getFullMoves());
    }

    @Test
    public void generateFollowUpPositionForBlackWithHalfMoveResetTest() {
        //white next move == false
        Piece[][] copiedSpaces = testPosition.copySpaces();
        Position followUpPosition = testPosition.generateFollowUpPosition(copiedSpaces, true);

        //check active player swap etc
        assertNoEnPassantTargetSquare(followUpPosition);
        assertEquals(!testPosition.getWhiteNextMove(), followUpPosition.getWhiteNextMove());
        assertTrue(followUpPosition.getSpaces() == copiedSpaces);
        assertEquals(0, followUpPosition.getHalfMoves());
        assertEquals(testPosition.getFullMoves() + 1, followUpPosition.getFullMoves());
    }

    @Test
    public void generateFollowUpPositionWithEnPassantTargetSquare() {
        //white next move == false
        Piece[][] copiedSpaces = testPosition.copySpaces();
        Position followUpPosition = testPosition.generateFollowUpPosition(copiedSpaces, 4,7, true);

        //check active player swap etc
        assertEquals(4, followUpPosition.getEnPassantTargetRank());
        assertEquals(7, followUpPosition.getEnPassantTargetFile());
        assertEquals(!testPosition.getWhiteNextMove(), followUpPosition.getWhiteNextMove());
        assertTrue(followUpPosition.getSpaces() == copiedSpaces);
        assertEquals(0, followUpPosition.getHalfMoves());
        assertEquals(testPosition.getFullMoves() + 1, followUpPosition.getFullMoves());
    }

    @Test
    public void generateFollowUpPositionAllParameters() {
                //white next move == false
                Piece[][] copiedSpaces = testPosition.copySpaces();
                Position followUpPosition = testPosition.generateFollowUpPosition(copiedSpaces, 6, 1, false, true, false, true, false);
        
                //check active player swap etc
                assertEquals(6, followUpPosition.getEnPassantTargetRank());
                assertEquals(1, followUpPosition.getEnPassantTargetFile());
                assertEquals(!testPosition.getWhiteNextMove(), followUpPosition.getWhiteNextMove());
                assertTrue(followUpPosition.getSpaces() == copiedSpaces);
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
    public void notEqualsPointValuePositionTest() {
        Position clonedPosition = testPosition.clone();
        clonedPosition.setValue(1);
        assertFalse(testPosition.equals(clonedPosition));
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
    public void notEqualsSpacesTest() {
        Position comparedPosition = generatePositionWithModififedSpaces(4, 7, 'b');
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsA8SpaceTest() {
        Position comparedPosition = generatePositionWithModififedSpaces(0, 0, 'b');
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsH1SpaceTest() {
        Position comparedPosition = generatePositionWithModififedSpaces(7, 7, 'N');
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsA5SpaceTest() {
        Position comparedPosition = generatePositionWithModififedSpaces(0, 3, 'N');
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualswhiteNextMove() {
        Position comparedPosition = new Position(0, false, false, testPosition.copySpaces(), true, false, false, false, false, 0, 0,
                0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsWhiteCastlingKingside() {
        Position comparedPosition = new Position(0, false, false, testPosition.copySpaces(), false, true, false, false, false, 0, 0,
                0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsWhiteCastlingQueenside() {
        Position comparedPosition = new Position(0, false, false, testPosition.copySpaces(), false, false, true, false, false, 0, 0,
                0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsBlackCastlingKingside() {
        Position comparedPosition = new Position(0, false, false, testPosition.copySpaces(), false, false, false, true, false, 0, 0,
                0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsBlackCastlingQueenside() {
        Position comparedPosition = new Position(0, false, false, testPosition.copySpaces(), false, false, false, false, true, 0, 0,
                0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsEnPassantTargetRank() {
        Position comparedPosition = new Position(0, false, false, testPosition.copySpaces(), false, false, false, false, false, 1,
                0, 0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsEnPassantTargetFile() {
        Position comparedPosition = new Position(0, false, false, testPosition.copySpaces(), false, false, false, false, false, 0,
                1, 0, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsHalfMoves() {
        Position comparedPosition = new Position(0, false, false, testPosition.copySpaces(), false, false, false, false, false, 0,
                0, 1, 1);
        assertFalse(testPosition.equals(comparedPosition));
    }

    @Test
    public void notEqualsFullMoves() {
        Position comparedPosition = new Position(0, false, false, testPosition.copySpaces(), false, false, false, false, false, 0,
                0, 0, 2);
        assertFalse(testPosition.equals(comparedPosition));
    }

    
}
