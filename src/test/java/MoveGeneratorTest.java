import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import model.Board;
import movegenerator.MoveGenerator;
import uciservice.FenParser;

public class MoveGeneratorTest {
    
    private Board startingPosition;
    private Board emptyPosition;
    private Board queenTestPosition;
    private Board rookTestPosition;
    private Board bishopTestPosition;
    private Board pawnAttackMapTestPosition;
    private Board kingTestPosition;
    private Board knightTestPosition;

    @BeforeEach
    public void initialize(){
        startingPosition = fullParseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR " +
            "w KQkq - 0 1");
        emptyPosition = fullParseFen("k7/8/8/8/8/8/8/7K " +
            "w KQkq - 0 1");
        queenTestPosition = fullParseFen("8/1q4k1/8/8/8/8/1Q4K1/8 " +
            "w KQkq - 0 1");
        rookTestPosition = fullParseFen("8/1r4k1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
        bishopTestPosition = fullParseFen("8/1b4k1/8/8/8/8/1Q4K1/8 " +
            "w KQkq - 0 1");  
        kingTestPosition = fullParseFen("8/1k4R1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
        pawnAttackMapTestPosition = fullParseFen("8/1p4k1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
        knightTestPosition = fullParseFen("8/1n4k1/8/8/8/8/1K4Q1/8 " +
            "w KQkq - 0 1");
    }

    private Board fullParseFen(String fenString){
        FenParser fenParser = new FenParser(fenString);
        return fenParser.parseFen();
    }

    @Test
    public void parserTest(){
        String startingStringActual = startingPosition.toString();
        String startingStringExpected = """
            [rnbqkbnr,
            pppppppp,
            00000000,
            00000000,
            00000000,
            00000000,
            PPPPPPPP,
            RNBQKBNR]
            White Next Move
            White Castling: Kingside and Queenside
            Black Castling: Kingside and Queenside
            No En Passant possible
            Halfmove Clock: 0
            Fullmove Number: 1
                """;
        assertEquals(startingStringExpected, startingStringActual);;
    }

    @Test
    public void paintRayAttackFullBoardTest(){
        boolean[][] attackMapExpectedDiagonal = {
            {false, false, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, false, true, false, false, false, false, false},
            {false, false, false, true, false, false, false, false},
            {false, false, false, false, true, false, false, false},
            {false, false, false, false, false, true, false, false},
            {false, false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, true}
        };
        boolean[][] attackMapExpectedHorizontal = {
            {false, false, false, false, false, false, false, false},
            {false, true, true, true, true, true, true, true},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false}
        };
        boolean[][] attackMapExpectedVertical = {
            {false, false, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false}
        };

        boolean[][] attackMapActualDiagonal =
            emptyPosition.paintRayAttack(new boolean[8][8], 1,1,1,1);
        boolean[][] attackMapActualHorizontal =
            emptyPosition.paintRayAttack(new boolean[8][8], 1,1,0,1);
        boolean[][] attackMapActualVertical =
            emptyPosition.paintRayAttack(new boolean[8][8], 1,1,1,0);
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                assertEquals(
                    attackMapExpectedDiagonal[rank][file], attackMapActualDiagonal[rank][file],
                    "Diagonal error at ["+rank+"]["+file+"]");
                assertEquals(
                    attackMapExpectedHorizontal[rank][file], attackMapActualHorizontal[rank][file],
                    "Horizontal error at ["+rank+"]["+file+"]");
                assertEquals(
                    attackMapExpectedVertical[rank][file], attackMapActualVertical[rank][file],
                    "Vertical error at ["+rank+"]["+file+"]");
            }
        }
    }

    @Test
    public void paintRayAttackCollisionTest(){
        boolean[][] attackMapExpectedDiagonal = {
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, true, false, false, false, false, false},
            {false, false, false, true, false, false, false, false},
            {false, false, false, false, true, false, false, false},
            {false, false, false, false, false, true, false, false},
            {false, false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, false}
        };
        boolean[][] attackMapExpectedHorizontal = {
            {false, false, false, false, false, false, false, false},
            {false, false, true, true, true, true, true, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false}
        };
        boolean[][] attackMapExpectedVertical = {
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false}
        };

        boolean[][] attackMapActualDiagonal =
            queenTestPosition.paintRayAttack(new boolean[8][8], 2,2,1,1);
        boolean[][] attackMapActualHorizontal =
            queenTestPosition.paintRayAttack(new boolean[8][8], 1,2,0,1);
        boolean[][] attackMapActualVertical =
            queenTestPosition.paintRayAttack(new boolean[8][8], 2,1,1,0);
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                assertEquals(
                    attackMapExpectedDiagonal[rank][file], attackMapActualDiagonal[rank][file],
                    "Diagonal error at ["+rank+"]["+file+"]");
                assertEquals(
                    attackMapExpectedHorizontal[rank][file], attackMapActualHorizontal[rank][file],
                    "Horizontal error at ["+rank+"]["+file+"]");
                assertEquals(
                    attackMapExpectedVertical[rank][file], attackMapActualVertical[rank][file],
                    "Vertical error at ["+rank+"]["+file+"]");
            }
        }
    }

    @Test
    public void rookAttackMapTest(){
        boolean[][] attackMapExpected = {
            {false, true, false, false, false, true, true, true},
            {true, false/* rook */, true, true, true, true, true/* king */, true},
            {false, true, false, false, false, true, true, true},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false}
        };
        
        boolean[][] attackMapActual =
            rookTestPosition.getAttackedByBlack();
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                assertEquals(
                    attackMapExpected[rank][file], attackMapActual[rank][file],
                    "Error at ["+rank+"]["+file+"]");
            }
        }
    }

    @Test
    public void bishopAttackMapTest(){
        boolean[][] attackMapExpected = {
            {true, false, true, false, false, true, true, true},
            {false, false, false, false, false, true, false, true},
            {true, false, true, false, false, true, true, true},
            {false, false, false, true, false, false, false, false},
            {false, false, false, false, true, false, false, false},
            {false, false, false, false, false, true, false, false},
            {false, false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, false}
        };
        boolean[][] attackMapActual =
            bishopTestPosition.computeChecks(false);
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                assertEquals(
                    attackMapExpected[rank][file], attackMapActual[rank][file],
                    "Error at ["+rank+"]["+file+"]");
            }
        }
    }

    @Test
    public void queenAttackMapTest(){
        boolean[][] attackMapExpected = {
            {true, true, true, false, false, true, true, true},
            {true, false, true, true, true, true, true, true},
            {true, true, true, false, false, true, true, true},
            {false, true, false, true, false, false, false, false},
            {false, true, false, false, true, false, false, false},
            {false, true, false, false, false, true, false, false},
            {false, true, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, false}
        };
        boolean[][] attackMapActual =
            queenTestPosition.computeChecks(false);
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                assertEquals(
                    attackMapExpected[rank][file], attackMapActual[rank][file],
                    "Error at ["+rank+"]["+file+"]");
            }
        }
    }

    @Test
    public void kingAttackMapTest(){
        boolean[][] attackMapExpected = {
            {true, true, true, false, false, false, false, false},
            {true, false, true, false, false, false, false, false},
            {true, true, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false}
        };
        boolean[][] attackMapActual =
            kingTestPosition.computeChecks(false);
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                assertEquals(
                    attackMapExpected[rank][file], attackMapActual[rank][file],
                    "Error at ["+rank+"]["+file+"]");
            }
        }
    }

    @Test
    public void pawnAttackMapTest(){
        boolean[][] attackMapExpected = {
            {false, false, false, false, false, true, true, true},
            {false, false, false, false, false, true, false, true},
            {true, false, true, false, false, true, true, true},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false}
        };
        boolean[][] attackMapActual =
            pawnAttackMapTestPosition.computeChecks(false);
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                assertEquals(
                    attackMapExpected[rank][file], attackMapActual[rank][file],
                    "Error at ["+rank+"]["+file+"]");
            }
        }
    }

    @Test
    public void knightAttackMapTest(){
        boolean[][] attackMapExpected = {
            {false, false, false, true, false, true, true, true},
            {false, false, false, false, false, true, false, true},
            {false, false, false, true, false, true, true, true},
            {true, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false}
        };
        boolean[][] attackMapActual =
            knightTestPosition.computeChecks(false);
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                assertEquals(
                    attackMapExpected[rank][file], attackMapActual[rank][file],
                    "Error at ["+rank+"]["+file+"]");
            }
        }
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

    @Test
    public void pawnCheckDetectionTest(){
        Board checkPosition = fullParseFen("8/8/R2p2k1/8/8/8/8/K7 " +
            "w KQkq - 0 1");
        Board[] expectedPositions = null;
        assertEquals(expectedPositions,
            MoveGenerator.generatePossibleMovesPerPiece(checkPosition, 2, 2));   
    }

    @Test
    public void pawnStepGenerationTest(){
        // Pawn can either do a double step or a single step
        Board stepPosition = fullParseFen("8/p6k/8/8/8/8/8/K7 " +
            "w KQkq - 0 1");
        List<Board> expectedPositions = new ArrayList<Board>();
        expectedPositions.add(fullParseFen("8/7k/p7/8/8/8/8/K7 " +
            "b KQkq - 0 2"));
        expectedPositions.add(fullParseFen("8/7k/8/p7/8/8/8/K7 " +
            "b KQkq a6 0 2"));
        Collections.sort(expectedPositions);
        List<Board>actualPositions = new ArrayList<Board>(
            MoveGenerator.generatePossibleMovesPerPiece(stepPosition, 1, 0));
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    public void pawnCaptureGenerationTest(){
        // Pawn should capture left and right
        Board capturePosition = fullParseFen("8/8/1p5k/PPP5/8/8/8/7K " +
            "w KQkq - 0 1");
        List<Board> expectedPositions = new ArrayList<Board>();
        expectedPositions.add(fullParseFen("8/8/7k/pPP5/8/8/8/7K " +
            "b KQkq - 0 2"));
        expectedPositions.add(fullParseFen("8/8/7k/PPp5/8/8/8/7K " +
            "b KQkq - 0 2"));
        Collections.sort(expectedPositions);
        List<Board>actualPositions = new ArrayList<Board>(
            MoveGenerator.generatePossibleMovesPerPiece(capturePosition,2,1));
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    public void pawnPromotionGenerationTest(){
        // Pawn should promote to all 4 possible options
        Board promotionPosition = fullParseFen("8/7k/8/8/8/8/p7/7K " +
        "w KQkq - 0 1");
        List<Board> expectedPositions = new ArrayList<Board>();
        expectedPositions.add(fullParseFen("8/7k/8/8/8/8/8/b6K " +
        "b KQkq - 0 2"));
        expectedPositions.add(fullParseFen("8/7k/8/8/8/8/8/n6K " +
        "b KQkq - 0 2"));
        expectedPositions.add(fullParseFen("8/7k/8/8/8/8/8/q6K " +
        "b KQkq - 0 2"));
        expectedPositions.add(fullParseFen("8/7k/8/8/8/8/8/r6K " +
        "b KQkq - 0 2"));
        Collections.sort(expectedPositions);
        List<Board> actualPositions = new ArrayList<Board>(
            MoveGenerator.generatePossibleMovesPerPiece(promotionPosition,6,0));
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    public void pawnEnPassantGenerationTest(){
        Board enPassantLeftPosition = fullParseFen("8/1pP4k/1P6/8/8/8/8/7K " +
            "w KQkq c6 0 1");
        Board enPassantRightPosition = fullParseFen("8/Pp5k/1P6/8/8/8/8/7K " +
            "w KQkq a6 0 1");
        List<Board> expectedPositionLeft = new ArrayList<Board>();
        expectedPositionLeft.add(fullParseFen("8/7k/1Pp5/8/8/8/8/7K " +
        "b KQkq - 0 2"));
        List<Board> actualPositionsLeft = new ArrayList<Board>(
            MoveGenerator.generatePossibleMovesPerPiece(enPassantLeftPosition,1,1));
        assertEquals(expectedPositionLeft, actualPositionsLeft);
        List<Board> expectedPositionRight = new ArrayList<Board>();
        expectedPositionRight.add(fullParseFen("8/7k/pP6/8/8/8/8/7K " +
        "b KQkq - 0 2"));
        List<Board> actualPositionsRight = new ArrayList<Board>(
            MoveGenerator.generatePossibleMovesPerPiece(enPassantRightPosition,1,1));
        assertEquals(expectedPositionRight, actualPositionsRight);
    }
}