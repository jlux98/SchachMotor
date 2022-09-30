package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import model.Board;
import model.ByteBoard;
import model.Position;
import uciservice.FenParser;

import static model.PieceEncoding.*;

public class ByteBoardTest {
    @Test
    public void setByteAtTest(){
        Board testBoard = ByteBoard.createEmpty();
        testBoard.setByteAt(0,0, WHITE_KING);
        testBoard.setByteAt(7, 7, BLACK_KING);
        assertEquals("K7/8/8/8/8/8/8/7k", testBoard.toStringFen());
        testBoard.setByteAt(7,6, WHITE_KING);
        testBoard.setByteAt(0, 1, BLACK_KING);
        assertEquals("Kk6/8/8/8/8/8/8/6Kk", testBoard.toStringFen());
    }

    @Test
    public void blackKingTest(){
        Board testBoard = ByteBoard.createEmpty();
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                testBoard.setByteAt(rank, file, BLACK_ROOK);
            }
        }
        assertEquals(
            "rrrrrrrr/rrrrrrrr/rrrrrrrr/rrrrrrrr/rrrrrrrr/rrrrrrrr/rrrrrrrr/rrrrrrrr",
            testBoard.toStringFen());
    }

    @Test
    public void fullBoardTest(){
        String fen = "rnbqkbnr/RNBQKBNR/rnbqkbnr/RNBQKBNR/rnbqkbnr/RNBQKBNR/rnbqkbnr/RNBQKBNR w KQkq - 0 1";
        Position testPosition = FenParser.parseFen(fen);
        assertEquals(fen, testPosition.toStringFen());
    }

    @Test
    public void getByteAtTest(){
        Board testBoard = ByteBoard.createEmpty();
        testBoard.setByteAt(0,0, WHITE_KING);
        testBoard.setByteAt(7, 7, BLACK_KING);
        assertEquals(WHITE_KING, testBoard.getByteAt(0,0));
        assertEquals(BLACK_KING, testBoard.getByteAt(7,7));
        testBoard.setByteAt(7,6, BLACK_KING);
        testBoard.setByteAt(0,1, BLACK_KING);
        assertEquals(WHITE_KING, testBoard.getByteAt(0,0));
        assertEquals(BLACK_KING, testBoard.getByteAt(7,7));
        assertEquals(BLACK_KING, testBoard.getByteAt(7,6));
        assertEquals(BLACK_KING, testBoard.getByteAt(0,1));
    }
}
