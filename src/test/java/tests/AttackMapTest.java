package tests;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.Position;
import movegenerator.AttackMapGenerator;
import uciservice.FenParser;

public class AttackMapTest {

    private static Position emptyPosition;
    private static Position queenTestPosition;
    private static Position rookTestPosition;
    private static Position bishopTestPosition;
    private static Position pawnAttackMapTestPosition;
    private static Position kingTestPosition;
    private static Position knightTestPosition;

    @BeforeAll
    public static void initialize(){
        emptyPosition = FenParser.parseFen("k7/8/8/8/8/8/8/7K " +
            "w KQkq - 0 1");
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
            AttackMapGenerator.paintRayAttack(emptyPosition.getBoard(), 
            new boolean[8][8], 1,1,1,1);
        boolean[][] attackMapActualHorizontal =
            AttackMapGenerator.paintRayAttack(emptyPosition.getBoard(),
            new boolean[8][8], 1,1,0,1);
        boolean[][] attackMapActualVertical =
            AttackMapGenerator.paintRayAttack(emptyPosition.getBoard(),
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
            AttackMapGenerator.paintRayAttack(queenTestPosition.getBoard(), 
            new boolean[8][8], 2,2,1,1);
        boolean[][] attackMapActualHorizontal =
            AttackMapGenerator.paintRayAttack(queenTestPosition.getBoard(), 
            new boolean[8][8], 1,2,0,1);
        boolean[][] attackMapActualVertical =
            AttackMapGenerator.paintRayAttack(queenTestPosition.getBoard(), 
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
            AttackMapGenerator.computeChecks(bishopTestPosition.getBoard(),
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
            AttackMapGenerator.computeChecks(queenTestPosition.getBoard(), false);
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
            AttackMapGenerator.computeChecks(kingTestPosition.getBoard(),false);
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
            AttackMapGenerator.computeChecks(pawnAttackMapTestPosition.getBoard(), false);
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
            AttackMapGenerator.computeChecks(knightTestPosition.getBoard(),false);
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                assertEquals(
                    attackMapExpected[rank][file], attackMapActual[rank][file],
                    "Error at ["+rank+"]["+file+"]");
            }
        }
    }
}
