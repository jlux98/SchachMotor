import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.Position;
import movegenerator.MoveGenerator;
import uciservice.FenParser;

public class MoveGeneratorTest {

    private static Position startingPosition;
    private static Position blackCastlingBoard;
    private static Position whiteCastlingBoard;

    @BeforeAll
    public static void setup() {
       blackCastlingBoard = FenParser.parseFen("r3k2r/p6p/8/8/8/8/P6P/R3K2R b KQkq - 0 1");
       whiteCastlingBoard = FenParser.parseFen("r3k2r/p6p/8/8/8/8/P6P/R3K2R w KQkq - 0 1");
       startingPosition = FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR " +
            "w KQkq - 0 1");
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
    public void pawnCheckDetectionTest(){
        Position checkPosition = FenParser.parseFen("8/8/R2p2k1/8/8/8/8/K7 " +
            "w KQkq - 0 1");
        Position[] expectedPositions = null;
        assertEquals(expectedPositions,
            MoveGenerator.generatePossibleMovesPerPiece(checkPosition, 2, 2));   
    }

    @Test
    public void pawnStepGenerationTest(){
        // Pawn can either do a double step or a single step
        Position stepPosition = FenParser.parseFen("8/p6k/8/8/8/8/8/K7 " +
            "b KQkq - 0 1");
        List<Position> expectedPositions = new ArrayList<Position>();
        expectedPositions.add(FenParser.parseFen("8/7k/p7/8/8/8/8/K7 " +
            "w KQkq - 0 2"));
        expectedPositions.add(FenParser.parseFen("8/7k/8/p7/8/8/8/K7 " +
            "w KQkq a6 0 2"));
        Collections.sort(expectedPositions);
        List<Position>actualPositions = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(stepPosition, 1, 0));
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    public void pawnCaptureGenerationTest(){
                // Pawn should capture left and right
        Position capturePosition = FenParser.parseFen("8/8/1p5k/PPP5/8/8/8/7K " +
            "b KQkq - 0 1");
        List<Position> expectedPositions = new ArrayList<Position>();
        expectedPositions.add(FenParser.parseFen("8/8/7k/pPP5/8/8/8/7K " +
            "w KQkq - 0 2"));
        expectedPositions.add(FenParser.parseFen("8/8/7k/PPp5/8/8/8/7K " +
            "w KQkq - 0 2"));
        Collections.sort(expectedPositions);
        List<Position>actualPositions = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(capturePosition,2,1));
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    public void pawnPromotionGenerationTest(){
        // Pawn should promote to all 4 possible options
        Position promotionPosition = FenParser.parseFen("8/7k/8/8/8/8/p7/7K " +
        "b KQkq - 0 1");
        List<Position> expectedPositions = new ArrayList<Position>();
        expectedPositions.add(FenParser.parseFen("8/7k/8/8/8/8/8/b6K " +
        "w KQkq - 0 2"));
        expectedPositions.add(FenParser.parseFen("8/7k/8/8/8/8/8/n6K " +
        "w KQkq - 0 2"));
        expectedPositions.add(FenParser.parseFen("8/7k/8/8/8/8/8/q6K " +
        "w KQkq - 0 2"));
        expectedPositions.add(FenParser.parseFen("8/7k/8/8/8/8/8/r6K " +
        "w KQkq - 0 2"));
        Collections.sort(expectedPositions);
        List<Position> actualPositions = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(promotionPosition,6,0));
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    public void pawnEnPassantGenerationTest(){
        Position enPassantLeftPosition = FenParser.parseFen("8/1pP4k/1P6/8/8/8/8/7K " +
            "b KQkq c6 0 1");
        Position enPassantRightPosition = FenParser.parseFen("8/Pp5k/1P6/8/8/8/8/7K " +
            "b KQkq a6 0 1");
        List<Position> expectedPositionLeft = new ArrayList<Position>();
        expectedPositionLeft.add(FenParser.parseFen("8/7k/1Pp5/8/8/8/8/7K " +
            "w KQkq - 0 2"));
        List<Position> actualPositionsLeft = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(enPassantLeftPosition,1,1));
        assertEquals(expectedPositionLeft, actualPositionsLeft);
        List<Position> expectedPositionRight = new ArrayList<Position>();
        expectedPositionRight.add(FenParser.parseFen("8/7k/pP6/8/8/8/8/7K " +
            "w KQkq - 0 2"));
        List<Position> actualPositionsRight = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(enPassantRightPosition,1,1));
        assertEquals(expectedPositionRight, actualPositionsRight);
    }

    @Test
    public void knightGenerationTest(){
        Position knightPosition = FenParser.parseFen("7k/1n6/8/P7/8/8/8/7K b KQkq - 0 1");
        List<Position> expectedPositions = new ArrayList<Position>();
        expectedPositions.add(FenParser.parseFen("3n3k/8/8/P7/8/8/8/7K w KQkq - 1 2"));
        expectedPositions.add(FenParser.parseFen("7k/8/3n4/P7/8/8/8/7K w KQkq - 1 2"));
        expectedPositions.add(FenParser.parseFen("7k/8/8/n7/8/8/8/7K w KQkq - 0 2"));
        expectedPositions.add(FenParser.parseFen("7k/8/8/P1n5/8/8/8/7K w KQkq - 1 2"));
        List<Position> actualPositions = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(knightPosition,1,1));
        Collections.sort(expectedPositions);
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    public void knightCheckDetectionTest(){
        Position checkPosition1 = FenParser.parseFen("8/8/R2k2k1/8/8/8/8/K7 " +
            "b KQkq - 0 1");
        List<Position> expectedPositions1 = null;
        assertEquals(expectedPositions1,
            MoveGenerator.generatePossibleMovesPerPiece(checkPosition1, 2, 2));
        Position checkPosition2 = FenParser.parseFen("3n4/8/R5k1/8/8/8/8/K7 " +
            "b KQkq - 0 1");
        List<Position> expectedPositions2 = new ArrayList<Position>();
        expectedPositions2.add(FenParser.parseFen("8/8/R1n3k1/8/8/8/8/K7 w KQkq - 1 2"));
        expectedPositions2.add(FenParser.parseFen("8/8/R3n1k1/8/8/8/8/K7 w KQkq - 1 2"));
        List<Position> actualPositions2 = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(checkPosition2, 0, 3));
        Collections.sort(expectedPositions2);
        Collections.sort(actualPositions2);
        assertEquals(expectedPositions2, actualPositions2);
    }

    @Test
    public void kingStepGenerationTest(){
        Position kingPosition = FenParser.parseFen("7K/8/8/8/8/8/3P4/3k4 b - - 0 1");
        List<Position> expectedPositions = new ArrayList<>();
        expectedPositions.add(FenParser.parseFen("7K/8/8/8/8/8/3Pk3/8 w - - 1 2"));
        expectedPositions.add(FenParser.parseFen("7K/8/8/8/8/8/2kP4/8 w - - 1 2"));
        expectedPositions.add(FenParser.parseFen("7K/8/8/8/8/8/3k4/8 w - - 0 2"));
        expectedPositions.add(FenParser.parseFen("7K/8/8/8/8/8/3P4/2k5 w - - 1 2"));
        expectedPositions.add(FenParser.parseFen("7K/8/8/8/8/8/3P4/4k3 w - - 1 2"));
        List<Position> actualPositions = new ArrayList<>(
            MoveGenerator.generatePossibleMovesPerPiece(kingPosition, 7, 3));
        Collections.sort(expectedPositions);
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);        
    }

    @Test
    public void blackCastlingGenerationTest(){
        Position castlingPosition = FenParser.parseFen(
            "r3k2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        List<Position> expectedPosition = new ArrayList<>();
        expectedPosition.add(FenParser.parseFen(
            "r4k1r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2"));
        expectedPosition.add(FenParser.parseFen(
            "r2k3r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2"));
        expectedPosition.add(FenParser.parseFen(
            "2kr3r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2"));
        expectedPosition.add(FenParser.parseFen(
            "r4rk1/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2"));
        List<Position> actualPositions = new ArrayList<>(
            MoveGenerator.generatePossibleMovesPerPiece(castlingPosition,0,4));
        Collections.sort(expectedPosition);
        Collections.sort(actualPositions);
        assertEquals(expectedPosition, actualPositions);
    }
    
    @Test
    public void whiteCastlingGenerationTest(){
        Position castlingPosition = FenParser.parseFen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        List<Position> expectedPosition = new ArrayList<>();
        expectedPosition.add(FenParser.parseFen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R4RK1 b kq - 1 1"));
        expectedPosition.add(FenParser.parseFen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R4K1R b kq - 1 1"));
        expectedPosition.add(FenParser.parseFen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R2K3R b kq - 1 1"));
        expectedPosition.add(FenParser.parseFen(
           "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/2KR3R b kq - 1 1"));
        List<Position> actualPositions = new ArrayList<>(
            MoveGenerator.generatePossibleMovesPerPiece(castlingPosition,7,4));
        Collections.sort(expectedPosition);
        Collections.sort(actualPositions);
        assertEquals(expectedPosition, actualPositions);
    }

    /**
     * Compares the positions denoted as fen strings against the list of positions.
     * @param fenStrings
     * @param positions
     * @return true if and only if 
     */
    private void compareFenStringsToBoard(List<String> fenStrings, Set<Position> positions) {
        List<Position> calculatedBoards = new ArrayList<Position>(positions);
        List<Position> fenStringBoards = new ArrayList<Position>(fenStrings.size());
        for (String fen : fenStrings) {
            fenStringBoards.add(FenParser.parseFen(fen));

        }
        Collections.sort(fenStringBoards);
        Collections.sort(calculatedBoards);
        assertEquals(fenStringBoards, calculatedBoards);
    }

    @Test
    public void moveBishopTest() {
        Position bishopTestBoard = FenParser.parseFen("7K/8/1n6/4P3/3b4/8/8/7k b - - 0 1"); //bishop starts at d4
        Set<Position> followUpBoards = MoveGenerator.computeBishopMoves(bishopTestBoard, 4, 3);

        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());
        expectedfollowUpBoards.add("7K/8/1n6/2b1P3/8/8/8/7k w - - 1 2"); //move one square to upper left
        expectedfollowUpBoards.add("7K/8/1n6/4b3/8/8/8/7k w - - 0 2"); //capture pawn on e5 - one square to upper right
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/2b5/8/7k w - - 1 2"); //move to c3 - one square to bottom left
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/1b6/7k w - - 1 2"); //move to b2 - two squares to bottom left
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/8/b6k w - - 1 2"); //move to a1 - three squares to bottom left
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/4b3/8/7k w - - 1 2"); //move to e3 - one square to bottom right
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/5b2/7k w - - 1 2"); //move to f2 - two squares to bottom right
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/8/6bk w - - 1 2"); //move to g1 - three squares to bottom right

        compareFenStringsToBoard(expectedfollowUpBoards, followUpBoards);
    }

    @Test
    public void moveRookTest() {
        Position rookTestBoard = FenParser.parseFen("2n4K/8/8/8/2r1P3/8/8/7k b - - 0 1"); //rook starts at c4
        Set<Position> followUpBoards = MoveGenerator.computeRookMoves(rookTestBoard, 4, 2);

        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());
        expectedfollowUpBoards.add("2n4K/8/8/8/1r2P3/8/8/7k w - - 1 2"); //move to b4 - one square to the left
        expectedfollowUpBoards.add("2n4K/8/8/8/r3P3/8/8/7k w - - 1 2"); //move to a4 - two squares to the left
        expectedfollowUpBoards.add("2n4K/8/8/2r5/4P3/8/8/7k w - - 1 2"); //move to c5 - one square up
        expectedfollowUpBoards.add("2n4K/8/2r5/8/4P3/8/8/7k w - - 1 2"); //move to c6 - two squares up
        expectedfollowUpBoards.add("2n4K/2r5/8/8/4P3/8/8/7k w - - 1 2"); //move to c7 - 3 squares up
        expectedfollowUpBoards.add("2n4K/8/8/8/3rP3/8/8/7k w - - 1 2"); //move to d4 - one square to the right
        expectedfollowUpBoards.add("2n4K/8/8/8/4r3/8/8/7k w - - 0 2"); //capture pawn on e4 - two squares to the right
        expectedfollowUpBoards.add("2n4K/8/8/8/4P3/2r5/8/7k w - - 1 2"); //move to c3 - one square down
        expectedfollowUpBoards.add("2n4K/8/8/8/4P3/8/2r5/7k w - - 1 2"); //move to c2 - two squares down
        expectedfollowUpBoards.add("2n4K/8/8/8/4P3/8/8/2r4k w - - 1 2"); //move to c1 - three squares down

        compareFenStringsToBoard(expectedfollowUpBoards, followUpBoards);

    }

    @Test
    public void moveBlackKingsideRookTest() {
        Set<Position> followUpBoards = MoveGenerator.computeRookMoves(blackCastlingBoard, 0, 7);
        
        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());

        expectedfollowUpBoards.add("r3k1r1/p6p/8/8/8/8/P6P/R3K2R w KQq - 1 2"); //move to g8 - one square to the left
        expectedfollowUpBoards.add("r3kr2/p6p/8/8/8/8/P6P/R3K2R w KQq - 1 2"); //move to f8 - two squares to the left

        compareFenStringsToBoard(expectedfollowUpBoards, followUpBoards);
    }

    @Test
    public void moveBlackQueensideRookTest() {
        Set<Position> followUpBoards = MoveGenerator.computeRookMoves(blackCastlingBoard, 0, 0);
        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());

        expectedfollowUpBoards.add("1r2k2r/p6p/8/8/8/8/P6P/R3K2R w KQk - 1 2"); //move to b8 - one square to the right
        expectedfollowUpBoards.add("2r1k2r/p6p/8/8/8/8/P6P/R3K2R w KQk - 1 2"); //move to c8 - two squares to the right
        expectedfollowUpBoards.add("3rk2r/p6p/8/8/8/8/P6P/R3K2R w KQk - 1 2"); //move to d8 - three squares to the right

        compareFenStringsToBoard(expectedfollowUpBoards, followUpBoards);
        
    }

    @Test
    public void moveWhiteKingSideRookTest() {
        Set<Position> followUpBoards = MoveGenerator.computeRookMoves(whiteCastlingBoard, 7, 7);
        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());

        expectedfollowUpBoards.add("r3k2r/p6p/8/8/8/8/P6P/R3K1R1 b Qkq - 1 1"); //move to g1 - one square to the left
        expectedfollowUpBoards.add("r3k2r/p6p/8/8/8/8/P6P/R3KR2 b Qkq - 1 1"); //move to f1 - two squares to the left

        compareFenStringsToBoard(expectedfollowUpBoards, followUpBoards);
    }

    @Test
    public void moveWhiteQueensideRookTest() {
        Set<Position> followUpBoards = MoveGenerator.computeRookMoves(whiteCastlingBoard, 7, 0);
        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());

        expectedfollowUpBoards.add("r3k2r/p6p/8/8/8/8/P6P/1R2K2R b Kkq - 1 1"); //move to b1 - one square to the right
        expectedfollowUpBoards.add("r3k2r/p6p/8/8/8/8/P6P/2R1K2R b Kkq - 1 1"); //move to c1 - two squares to the right
        expectedfollowUpBoards.add("r3k2r/p6p/8/8/8/8/P6P/3RK2R b Kkq - 1 1"); //move to d1 - three squares to the right
        
        compareFenStringsToBoard(expectedfollowUpBoards, followUpBoards);
    }

    @Test
    public void moveQueenTest() {
        Position queenTestBoard = FenParser.parseFen("7K/8/2n5/8/3P4/2q5/8/7k b - - 0 1"); //queen starts at c3
        Set<Position> followUpBoards = MoveGenerator.computeQueenMoves(queenTestBoard, 5, 2);

        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/1q6/8/7k w - - 1 2"); //move to b3 - one square left
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/q7/8/7k w - - 1 2"); //move to a3 - two squares left
        expectedfollowUpBoards.add("7K/8/2n5/8/1q1P4/8/8/7k w - - 1 2"); //move to b4 - one square to upper left
        expectedfollowUpBoards.add("7K/8/2n5/q7/3P4/8/8/7k w - - 1 2"); //move to a5 - two squares to upper left
        expectedfollowUpBoards.add("7K/8/2n5/8/2qP4/8/8/7k w - - 1 2"); //move to c4 - one square up
        expectedfollowUpBoards.add("7K/8/2n5/2q5/3P4/8/8/7k w - - 1 2"); //move to c5 - two squares up
        expectedfollowUpBoards.add("7K/8/2n5/8/3q4/8/8/7k w - - 0 2"); //capture pawn on d4 - one square to upper right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/3q4/8/7k w - - 1 2"); //move to d3 - one square to the right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/4q3/8/7k w - - 1 2"); //move to e3 - two squares to the right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/5q2/8/7k w - - 1 2"); //move to f3 - three squares to the right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/6q1/8/7k w - - 1 2"); //move to g3 - four quares to the right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/7q/8/7k w - - 1 2"); //move to h3 - five squares to the right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/3q4/7k w - - 1 2"); //move to d2 - one square to bottom right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/8/4q2k w - - 1 2"); //move to e1 - two squares to bottom right
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/2q5/7k w - - 1 2"); //move to c2 - one square down
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/8/2q4k w - - 1 2"); //move to c1 - two squares down
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/1q6/7k w - - 1 2"); //move to b2 - one square to bottom left
        expectedfollowUpBoards.add("7K/8/2n5/8/3P4/8/8/q6k w - - 1 2"); //move to a1 - two squares to bottom left

        compareFenStringsToBoard(expectedfollowUpBoards, followUpBoards);
    }

    @Test
    /**
     * Tests if a piece does not attempt to leave board boundaries.
     * A black queen starts in the a8 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInA8CornerTest() {
        Position queenTestBoard = FenParser.parseFen("qr6/rr6/8/8/3Kk3/8/8/8 w - - 0 1"); //queen starts at a8
        Set<Position> followUpBoards = MoveGenerator.computeQueenMoves(queenTestBoard, 0, 0);
        List<String> emptyList = new ArrayList<String>();
        compareFenStringsToBoard(emptyList, followUpBoards);

    }

    @Test
    /**
     * Tests if a piece does not attempt to leave board boundaries.
     * A black queen starts in the h8 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInH8CornerTest() {
        Position queenTestBoard = FenParser.parseFen("6rq/6rr/8/8/3Kk3/8/8/8 w - - 0 1"); //queen starts at h8
        Set<Position> followUpBoards = MoveGenerator.computeQueenMoves(queenTestBoard, 0, 7);
        List<String> emptyList = new ArrayList<String>();
        compareFenStringsToBoard(emptyList, followUpBoards);

    }

    @Test
    /**
     * Tests if a piece does not attempt to leave board boundaries.
     * A black queen starts in the a1 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInA1CornerTest() {
        Position queenTestBoard = FenParser.parseFen("8/8/8/8/3Kk3/8/rr6/qr6 w - - 0 1"); //queen starts at a1
        Set<Position> followUpBoards = MoveGenerator.computeQueenMoves(queenTestBoard, 7, 0);
        List<String> emptyList = new ArrayList<String>();
        compareFenStringsToBoard(emptyList, followUpBoards);

    }

    @Test
    /**
     * Tests if a piece does not attempt to leave board boundaries.
     * A black queen starts in the h1 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInH1CornerTest() {
        Position queenTestBoard = FenParser.parseFen("8/8/8/8/3Kk3/8/6rr/6rq w - - 0 1"); //queen starts at h1
        Set<Position> followUpBoards = MoveGenerator.computeQueenMoves(queenTestBoard, 7, 7);
        List<String> emptyList = new ArrayList<String>();
        compareFenStringsToBoard(emptyList, followUpBoards);

    }

}
