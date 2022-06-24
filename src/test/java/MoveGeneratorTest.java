import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Board;
import movegenerator.MoveGenerator;
import uciservice.FenParser;

public class MoveGeneratorTest {

    Board startingPosition;

    @BeforeEach
    public void initialize() {
        String startingFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR " + "w KQkq - 0 1";
        FenParser startingFenParser = new FenParser(startingFen);
        startingPosition = startingFenParser.parseFen();
    }

    @Test
    public void parserTest() {
        String startingString = startingPosition.toString();
        System.out.println(startingPosition);
    }

    /**
     * Compares the positions denoted as fen strings against the list of boards.
     * @param fenStrings
     * @param boards
     * @return true if and only if 
     */
    private void compareFenStringsToBoard(List<String> fenStrings, Set<Board> boards) {
        List<Board> calculatedBoards = new ArrayList<Board>(boards);
        List<Board> fenStringBoards = new ArrayList<Board>(fenStrings.size());
        for (String fen : fenStrings) {
            fenStringBoards.add(FenParser.parseFen(fen));

        }
        Collections.sort(fenStringBoards);
        Collections.sort(calculatedBoards);
        assertEquals(fenStringBoards, calculatedBoards);
    }

    @Test
    public void moveBishopTest() {
        Board bishopTestBoard = FenParser.parseFen("7K/8/1n6/4P3/3b4/8/8/7k b - - 0 1"); //bishop starts at d4
        Set<Board> followUpBoards = MoveGenerator.computeBishopMoves(bishopTestBoard, 4, 3);

        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());
        expectedfollowUpBoards.add("7K/8/1n6/2b1P3/8/8/8/7k w - - 0 1"); //move one square to upper left
        expectedfollowUpBoards.add("7K/8/1n6/4b3/8/8/8/7k w - - 0 1"); //capture pawn on e5 - one square to upper right
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/2b5/8/7k w - - 0 1"); //move to c3 - one square to bottom left
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/1b6/7k w - - 0 1"); //move to b2 - two squares to bottom left
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/8/b6k w - - 0 1"); //move to a1 - three squares to bottom left
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/4b3/8/7k w - - 0 1"); //move to e3 - one square to bottom right
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/5b2/7k w - - 0 1"); //move to f2 - two squares to bottom right
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/8/6bk w - - 0 1"); //move to g1 - three squares to bottom right

        compareFenStringsToBoard(expectedfollowUpBoards, followUpBoards);
    }

    @Test
    public void moveRookTest() {
        Board rookTestBoard = FenParser.parseFen("2n4K/8/8/8/2r1P3/8/8/7k w - - 0 1"); //rook starts at c4
        Set<Board> followUpBoards = MoveGenerator.computeRookMoves(rookTestBoard, 4, 2);

        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());
        expectedfollowUpBoards.add("2n4K/8/8/8/1r2P3/8/8/7k w - - 0 1"); //move to b4 - one square to the left
        expectedfollowUpBoards.add("2n4K/8/8/8/r3P3/8/8/7k w - - 0 1"); //move to a4 - two squares to the left
        expectedfollowUpBoards.add("2n4K/8/8/2r5/4P3/8/8/7k w - - 0 1"); //move to c5 - one square up
        expectedfollowUpBoards.add("2n4K/8/2r5/8/4P3/8/8/7k w - - 0 1"); //move to c6 - two squares up
        expectedfollowUpBoards.add("2n4K/2r5/8/8/4P3/8/8/7k w - - 0 1"); //move to c7 - 3 squares up
        expectedfollowUpBoards.add("2n4K/8/8/8/3rP3/8/8/7k w - - 0 1"); //move to d4 - one square to the right
        expectedfollowUpBoards.add("2n4K/8/8/8/4r3/8/8/7k w - - 0 1"); //capture pawn on e4 - two squares to the right
        expectedfollowUpBoards.add("2n4K/8/8/8/4P3/2r5/8/7k w - - 0 1"); //move to c3 - one square down
        expectedfollowUpBoards.add("2n4K/8/8/8/4P3/8/2r5/7k w - - 0 1"); //move to c2 - two squares down
        expectedfollowUpBoards.add("2n4K/8/8/8/4P3/8/8/2r4k w - - 0 1"); //move to c1 - three squares down

        compareFenStringsToBoard(expectedfollowUpBoards, followUpBoards);

    }

    @Test
    public void moveQueenTest() {
        Board queenTestBoard = FenParser.parseFen("7K/8/2n5/8/3P4/2q5/8/7k w - - 0 1"); //queen starts at c3
        Set<Board> followUpBoards = MoveGenerator.computeQueenMoves(queenTestBoard, 5, 2);

        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/1q6/8/7k w - - 0 1"); //move to b3 - one square left
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/q7/8/7k w - - 0 1"); //move to a3 - two squares left
        expectedfollowUpBoards.add("7K/8/2n5/8/1q1P4/8/8/7k w - - 0 1"); //move to b4 - one square to upper left
        expectedfollowUpBoards.add("7K/8/2n5/q7/3P4/8/8/7k w - - 0 1"); //move to a5 - two squares to upper left
        expectedfollowUpBoards.add("7K/8/2n5/8/2qP4/8/8/7k w - - 0 1"); //move to c4 - one square up
        expectedfollowUpBoards.add("7K/8/2n5/2q5/3P4/8/8/7k w - - 0 1"); //move to c5 - two squares up
        expectedfollowUpBoards.add("7K/8/2n5/8/3q4/8/8/7k w - - 0 1"); //capture pawn on d4 - one square to upper right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/3q4/8/7k w - - 0 1"); //move to d3 - one square to the right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/4q3/8/7k w - - 0 1"); //move to e3 - two squares to the right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/5q2/8/7k w - - 0 1"); //move to f3 - three squares to the right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/6q1/8/7k w - - 0 1"); //move to g3 - four quares to the right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/7q/8/7k w - - 0 1"); //move to h3 - five squares to the right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/3q4/7k w - - 0 1"); //move to d2 - one square to bottom right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/8/4q2k w - - 0 1"); //move to e1 - two squares to bottom right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/2q5/7k w - - 0 1"); //move to c2 - one square down
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/8/2q4k w - - 0 1"); //move to c1 - two squares down
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/1q6/7k w - - 0 1"); //move to b2 - one square to bottom left
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/8/q6k w - - 0 1"); //move to a1 - two squares to bottom left

        compareFenStringsToBoard(expectedfollowUpBoards, followUpBoards);
    }

    @Test
    /**
     * Tests if a piece does not attempt to leave board boundaries.
     * A black queen starts in the a8 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInA8CornerTest() {
        Board queenTestBoard = FenParser.parseFen("qr6/rr6/8/8/3Kk3/8/8/8 w - - 0 1"); //queen starts at a8
        System.out.println(queenTestBoard);
        Set<Board> followUpBoards = MoveGenerator.computeQueenMoves(queenTestBoard, 0, 0);
        List<String> emptyList = new ArrayList<String>();
        compareFenStringsToBoard(emptyList, followUpBoards);

    }

    @Test
    /**
     * Tests if a piece does not attempt to leave board boundaries.
     * A black queen starts in the h8 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInH8CornerTest() {
        Board queenTestBoard = FenParser.parseFen("6rq/6rr/8/8/3Kk3/8/8/8 w - - 0 1"); //queen starts at h8
        System.out.println(queenTestBoard);
        Set<Board> followUpBoards = MoveGenerator.computeQueenMoves(queenTestBoard, 0, 0);
        List<String> emptyList = new ArrayList<String>();
        compareFenStringsToBoard(emptyList, followUpBoards);

    }

    @Test
    /**
     * Tests if a piece does not attempt to leave board boundaries.
     * A black queen starts in the a1 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInA1CornerTest() {
        Board queenTestBoard = FenParser.parseFen("8/8/8/8/3Kk3/8/rr6/qr6 w - - 0 1"); //queen starts at a1
        System.out.println(queenTestBoard);
        Set<Board> followUpBoards = MoveGenerator.computeQueenMoves(queenTestBoard, 0, 0);
        List<String> emptyList = new ArrayList<String>();
        compareFenStringsToBoard(emptyList, followUpBoards);

    }

    @Test
    /**
     * Tests if a piece does not attempt to leave board boundaries.
     * A black queen starts in the h1 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInH1CornerTest() {
        Board queenTestBoard = FenParser.parseFen("8/8/8/8/3Kk3/8/6rr/6rq w - - 0 1"); //queen starts at h1
        System.out.println(queenTestBoard);
        Set<Board> followUpBoards = MoveGenerator.computeQueenMoves(queenTestBoard, 0, 0);
        List<String> emptyList = new ArrayList<String>();
        compareFenStringsToBoard(emptyList, followUpBoards);

    }

}
