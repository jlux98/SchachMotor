package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.Board;
import model.Piece;
import model.PieceType;
import model.Position;
import uciservice.FenParseException;
import uciservice.FenParser;

public class FenParserTest {

    //starting position fen = rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1

    /**
     * Asserts that parsing the passed Fen-String throws a FenParseException.
     * @param fen
     */
    private static void assertParsingThrowsFenParseException(String fen) {
        assertThrows(FenParseException.class, () -> FenParser.parseFen(fen));
    }

    @Test
    public void parseStartingPositionTest() {
        Position position = FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        assertTrue(position.getBlackCastlingKingside());
        assertTrue(position.getBlackCastlingQueenside());
        assertTrue(position.getWhiteCastlingKingside());
        assertTrue(position.getWhiteCastlingQueenside());

        //check detection is not done by parser
        //the check flags should be false for any position
        //(even if there is check)
        assertFalse(position.getBlackInCheck());
        assertFalse(position.getWhiteInCheck());

        assertEquals(-1, position.getEnPassantTargetFile());
        assertEquals(-1, position.getEnPassantTargetRank());
        assertTrue(position.getWhitesTurn());
        assertEquals(1, position.getFullMoves());
        assertEquals(0, position.getHalfMoves()); 
    }

    @Test
    public void piecesInCornerTest() {
        //FIXME check board orientation
        Position position = FenParser.parseFen("k6N/8/8/8/8/8/8/K6n w - - 0 1");
        Board board = position.getBoard();
        Piece a8Piece = board.getPieceAt(0, 0);
        Piece h8Piece = board.getPieceAt(7, 0);
        Piece a1Piece = board.getPieceAt(0, 7);
        Piece h1Piece = board.getPieceAt(7, 7);

        assertFalse(a8Piece.getIsWhite());
        assertEquals(PieceType.KING, a8Piece.getPieceType());

        assertTrue(h8Piece.getIsWhite());
        assertEquals(PieceType.KNIGHT, h8Piece.getPieceType());

        assertTrue(a1Piece.getIsWhite());
        assertEquals(PieceType.KING, a1Piece.getPieceType());

        assertFalse(h1Piece.getIsWhite());
        assertEquals(PieceType.KNIGHT, h1Piece.getPieceType());
    }

    @Test
    public void targetSquareA8Test() {
        Position position = FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq a8 0 1");
        assertEquals(0, position.getEnPassantTargetRank());
        assertEquals(0, position.getEnPassantTargetFile());
    }

    @Test
    public void targetSquareH1Test() {
        Position position = FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq H1 0 1");
        assertEquals(7, position.getEnPassantTargetRank());
        assertEquals(7, position.getEnPassantTargetFile());
    }

    @Test
    public void noSideToMoveTest() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR KQkq - 0 1");
    }

    @Test
    public void illegalSideToMoveCharacterTest() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR x KQkq - 0 1");
    }

    @Test
    public void illegalPieceCharacterTest() {
        assertParsingThrowsFenParseException("rnbqkbnx/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @Test
    public void illegalCastlingRightsCharacterTest() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQxq - 0 1");
    }

    @Test
    public void illegalCastlingRightsTest() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkqq - 0 1");

    }

    @Test
    public void illegalTargetSquareI1Test() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq i1 0 1");
    }

    @Test
    public void illegalTargetSquareA9Test() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq a9 0 1");
    }

    @Test
    public void illegalTargetSquareCharacterTest() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq x 0 1");
    }

    @Test
    public void negativeHalfMoveCountTest() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - -1 1");
    }

    @Test
    public void negativeFullMoveCountTest() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 1 -1");
    }

    @Test
    public void illegalHalfMoveCountCharacterTest() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - x 1");
    }

    @Test
    public void illegalFullMoveCountCharacterTest() {
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 1 x");
    }

    @Test
    public void tooFewRanksTest() {
        String sevenRanksFen = "rnbqkbnr/pppppppp/8/8/8/PPPPPPPP/RNBBNR w KQkq - 0 1";
        assertParsingThrowsFenParseException(sevenRanksFen);
    }

    @Test
    public void tooManyRanksTest() {
        String nineRanksFen = "8/rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBBNR w KQkq - 0 1";
        assertParsingThrowsFenParseException(nineRanksFen);
    }

    @Test
    public void underfilledRankTest() {
        //only has 6 pieces and no whitespace in last rank = 2 undefined squares in that rank
        assertParsingThrowsFenParseException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBBNR w KQkq - 0 1");
    }
}
