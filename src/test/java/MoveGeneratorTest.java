import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Board;
import movegenerator.AttackMapGenerator;
import movegenerator.MoveGenerator;
import positionevaluator.PositionEvaluator;
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
            AttackMapGenerator.paintRayAttack(emptyPosition.getSpaces(), 
            new boolean[8][8], 1,1,1,1);
        boolean[][] attackMapActualHorizontal =
            AttackMapGenerator.paintRayAttack(emptyPosition.getSpaces(),
            new boolean[8][8], 1,1,0,1);
        boolean[][] attackMapActualVertical =
            AttackMapGenerator.paintRayAttack(emptyPosition.getSpaces(),
            new boolean[8][8], 1,1,1,0);
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
            AttackMapGenerator.paintRayAttack(queenTestPosition.getSpaces(), 
            new boolean[8][8], 2,2,1,1);
        boolean[][] attackMapActualHorizontal =
            AttackMapGenerator.paintRayAttack(queenTestPosition.getSpaces(), 
            new boolean[8][8], 1,2,0,1);
        boolean[][] attackMapActualVertical =
            AttackMapGenerator.paintRayAttack(queenTestPosition.getSpaces(), 
            new boolean[8][8], 2,1,1,0);
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
            AttackMapGenerator.computeChecks(bishopTestPosition.getSpaces(),
            false);
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
            AttackMapGenerator.computeChecks(queenTestPosition.getSpaces(), false);
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
            AttackMapGenerator.computeChecks(kingTestPosition.getSpaces(),false);
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
            AttackMapGenerator.computeChecks(pawnAttackMapTestPosition.getSpaces(), false);
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
            AttackMapGenerator.computeChecks(knightTestPosition.getSpaces(),false);
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
            "b KQkq - 0 1");
        List<Board> expectedPositions = new ArrayList<Board>();
        expectedPositions.add(fullParseFen("8/7k/p7/8/8/8/8/K7 " +
            "w KQkq - 0 2"));
        expectedPositions.add(fullParseFen("8/7k/8/p7/8/8/8/K7 " +
            "w KQkq a6 0 2"));
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
            "b KQkq - 0 1");
        List<Board> expectedPositions = new ArrayList<Board>();
        expectedPositions.add(fullParseFen("8/8/7k/pPP5/8/8/8/7K " +
            "w KQkq - 0 2"));
        expectedPositions.add(fullParseFen("8/8/7k/PPp5/8/8/8/7K " +
            "w KQkq - 0 2"));
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
        "b KQkq - 0 1");
        List<Board> expectedPositions = new ArrayList<Board>();
        expectedPositions.add(fullParseFen("8/7k/8/8/8/8/8/b6K " +
        "w KQkq - 0 2"));
        expectedPositions.add(fullParseFen("8/7k/8/8/8/8/8/n6K " +
        "w KQkq - 0 2"));
        expectedPositions.add(fullParseFen("8/7k/8/8/8/8/8/q6K " +
        "w KQkq - 0 2"));
        expectedPositions.add(fullParseFen("8/7k/8/8/8/8/8/r6K " +
        "w KQkq - 0 2"));
        Collections.sort(expectedPositions);
        List<Board> actualPositions = new ArrayList<Board>(
            MoveGenerator.generatePossibleMovesPerPiece(promotionPosition,6,0));
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    public void pawnEnPassantGenerationTest(){
        Board enPassantLeftPosition = fullParseFen("8/1pP4k/1P6/8/8/8/8/7K " +
            "b KQkq c6 0 1");
        Board enPassantRightPosition = fullParseFen("8/Pp5k/1P6/8/8/8/8/7K " +
            "b KQkq a6 0 1");
        List<Board> expectedPositionLeft = new ArrayList<Board>();
        expectedPositionLeft.add(fullParseFen("8/7k/1Pp5/8/8/8/8/7K " +
            "w KQkq - 0 2"));
        List<Board> actualPositionsLeft = new ArrayList<Board>(
            MoveGenerator.generatePossibleMovesPerPiece(enPassantLeftPosition,1,1));
        assertEquals(expectedPositionLeft, actualPositionsLeft);
        List<Board> expectedPositionRight = new ArrayList<Board>();
        expectedPositionRight.add(fullParseFen("8/7k/pP6/8/8/8/8/7K " +
            "w KQkq - 0 2"));
        List<Board> actualPositionsRight = new ArrayList<Board>(
            MoveGenerator.generatePossibleMovesPerPiece(enPassantRightPosition,1,1));
        assertEquals(expectedPositionRight, actualPositionsRight);
    }

    @Test
    public void knightGenerationTest(){
        Board knightPosition = fullParseFen("7k/1n6/8/P7/8/8/8/7K b KQkq - 0 1");
        List<Board> expectedPositions = new ArrayList<Board>();
        expectedPositions.add(fullParseFen("3n3k/8/8/P7/8/8/8/7K w KQkq - 1 2"));
        expectedPositions.add(fullParseFen("7k/8/3n4/P7/8/8/8/7K w KQkq - 1 2"));
        expectedPositions.add(fullParseFen("7k/8/8/n7/8/8/8/7K w KQkq - 0 2"));
        expectedPositions.add(fullParseFen("7k/8/8/P1n5/8/8/8/7K w KQkq - 1 2"));
        List<Board> actualPositions = new ArrayList<Board>(
            MoveGenerator.generatePossibleMovesPerPiece(knightPosition,1,1));
        Collections.sort(expectedPositions);
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    public void knightCheckDetectionTest(){
        Board checkPosition1 = fullParseFen("8/8/R2k2k1/8/8/8/8/K7 " +
            "b KQkq - 0 1");
        List<Board> expectedPositions1 = null;
        assertEquals(expectedPositions1,
            MoveGenerator.generatePossibleMovesPerPiece(checkPosition1, 2, 2));
        Board checkPosition2 = fullParseFen("3n4/8/R5k1/8/8/8/8/K7 " +
            "b KQkq - 0 1");
        List<Board> expectedPositions2 = new ArrayList<Board>();
        expectedPositions2.add(fullParseFen("8/8/R1n3k1/8/8/8/8/K7 w KQkq - 1 2"));
        expectedPositions2.add(fullParseFen("8/8/R3n1k1/8/8/8/8/K7 w KQkq - 1 2"));
        List<Board> actualPositions2 = new ArrayList<Board>(
            MoveGenerator.generatePossibleMovesPerPiece(checkPosition2, 0, 3));
        Collections.sort(expectedPositions2);
        Collections.sort(actualPositions2);
        assertEquals(expectedPositions2, actualPositions2);
    }

    @Test
    public void kingStepGenerationTest(){
        Board kingPosition = fullParseFen("7K/8/8/8/8/8/3P4/3k4 b - - 0 1");
        List<Board> expectedPositions = new ArrayList<>();
        expectedPositions.add(fullParseFen("7K/8/8/8/8/8/3Pk3/8 w - - 1 2"));
        expectedPositions.add(fullParseFen("7K/8/8/8/8/8/2kP4/8 w - - 1 2"));
        expectedPositions.add(fullParseFen("7K/8/8/8/8/8/3k4/8 w - - 0 2"));
        expectedPositions.add(fullParseFen("7K/8/8/8/8/8/3P4/2k5 w - - 1 2"));
        expectedPositions.add(fullParseFen("7K/8/8/8/8/8/3P4/4k3 w - - 1 2"));
        List<Board> actualPositions = new ArrayList<>(
            MoveGenerator.generatePossibleMovesPerPiece(kingPosition, 7, 3));
        Collections.sort(expectedPositions);
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);        
    }

    @Test
    public void blackCastlingGenerationTest(){
        Board castlingPosition = fullParseFen(
            "r3k2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        List<Board> expectedPosition = new ArrayList<>();
        expectedPosition.add(fullParseFen(
            "r4k1r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2"));
        expectedPosition.add(fullParseFen(
            "r2k3r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2"));
        expectedPosition.add(fullParseFen(
            "2kr3r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2"));
        expectedPosition.add(fullParseFen(
            "r4rk1/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2"));
        List<Board> actualPositions = new ArrayList<>(
            MoveGenerator.generatePossibleMovesPerPiece(castlingPosition,0,4));
        Collections.sort(expectedPosition);
        Collections.sort(actualPositions);
        assertEquals(expectedPosition, actualPositions);
    }
    
    @Test
    public void whiteCastlingGenerationTest(){
        Board castlingPosition = fullParseFen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        List<Board> expectedPosition = new ArrayList<>();
        expectedPosition.add(fullParseFen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R4RK1 b kq - 1 1"));
        expectedPosition.add(fullParseFen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R4K1R b kq - 1 1"));
        expectedPosition.add(fullParseFen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R2K3R b kq - 1 1"));
        expectedPosition.add(fullParseFen(
           "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/2KR3R b kq - 1 1"));
        List<Board> actualPositions = new ArrayList<>(
            MoveGenerator.generatePossibleMovesPerPiece(castlingPosition,7,4));
        Collections.sort(expectedPosition);
        Collections.sort(actualPositions);
        assertEquals(expectedPosition, actualPositions);
    }

    @Test
    public void positionEvaluatorTest(){
        assertEquals(0, PositionEvaluator.evaluatePosition(startingPosition));
        assertEquals(-41,PositionEvaluator.evaluatePosition(
            fullParseFen("rnbqkbnr/pppppppp/8/8/8/8/8/7K b kq - 1 1")));
        assertEquals(41,PositionEvaluator.evaluatePosition(
            fullParseFen("k7/8/8/8/8/8/PPPPPPPP/RNBQKBNR b kq - 1 1")));
        assertEquals(-36,PositionEvaluator.evaluatePosition(
            fullParseFen("rnbqk1nr/pppppppp/8/8/8/8/8/7K b kq - 1 1")));
        assertEquals(36,PositionEvaluator.evaluatePosition(
            fullParseFen("k7/8/8/8/8/8/PPPPPPPP/RN1QKBNR b kq - 1 1")));
    }
}