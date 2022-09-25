package tests;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import data.MoveGeneratorData;
import helper.Mirror;
import helper.MoveGeneratorHelper;
import helper.PositionHelper;
import model.Position;
import movegenerator.MoveGenerator;
import uciservice.FenParser;

import static movegenerator.MoveGenerator.EMPTY_SQUARE;
import static movegenerator.MoveGenerator.WHITE_BISHOP;
import static movegenerator.MoveGenerator.WHITE_KING;
import static movegenerator.MoveGenerator.WHITE_KNIGHT;
import static movegenerator.MoveGenerator.WHITE_PAWN;
import static movegenerator.MoveGenerator.WHITE_QUEEN;
import static movegenerator.MoveGenerator.WHITE_ROOK;
import static movegenerator.MoveGenerator.BLACK_BISHOP;
import static movegenerator.MoveGenerator.BLACK_KING;
import static movegenerator.MoveGenerator.BLACK_KNIGHT;
import static movegenerator.MoveGenerator.BLACK_PAWN;
import static movegenerator.MoveGenerator.BLACK_QUEEN;
import static movegenerator.MoveGenerator.BLACK_ROOK;

public class MoveGeneratorTest {

    private static Position blackCastlingPosition;
    private static Position whiteCastlingPosition;
    
    /**
     * Shared empty list instance. Never add anything to this list!
     */
    private static final List<Position> EMPTY_LIST = new ArrayList<Position>(0);
    

    @BeforeAll
    public static void setup() {
        blackCastlingPosition = FenParser.parseFen("r3k2r/p6p/8/8/8/8/P6P/R3K2R b KQkq - 0 1");
        whiteCastlingPosition = FenParser.parseFen("r3k2r/p6p/8/8/8/8/P6P/R3K2R w KQkq - 0 1");
   }

    @Test
    public void pawnCheckDetectionTest() {
        Position checkPosition = FenParser.parseFen("8/8/R2p2k1/8/8/8/8/K7 b - - 0 1");
        List<Position> expectedPositions = new ArrayList<>();
        assertEquals(expectedPositions,
            MoveGenerator.generatePossibleMovesPerPiece(checkPosition, 2, 2));   
    }

    @Test
    public void pawnStepGenerationTestDoubleStepAllowed(){
        // Pawn can either do a double step or a single step

        Position stepPositionWhite = FenParser.parseFen ("8/7k/8/8/8/8/P7/K7 " +
            "w - - 0 1");
        List<String> expectedPositionsWhite = new ArrayList<>();
        expectedPositionsWhite.add("8/7k/8/8/8/P7/8/K7 " +
            "b - - 0 1");
        expectedPositionsWhite.add("8/7k/8/8/P7/8/8/K7 " +
            "b - a3 0 1");
        Collections.sort(expectedPositionsWhite);
        List<Position>actualPositionsWhite = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(stepPositionWhite, 6, 0));
        Collections.sort(actualPositionsWhite);
        MoveGeneratorHelper.compareFenStringsToPosition(expectedPositionsWhite, actualPositionsWhite);

        Position stepPositionBlack = FenParser.parseFen("8/p6k/8/8/8/8/8/K7 " +
            "b - - 0 1");
        List<String> expectedPositionsBlack = new ArrayList<>();
        expectedPositionsBlack.add("8/7k/p7/8/8/8/8/K7 " +
            "w - - 0 2");
        expectedPositionsBlack.add("8/7k/8/p7/8/8/8/K7 " +
            "w - a6 0 2");
        Collections.sort(expectedPositionsBlack);
        List<Position>actualPositionsBlack = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(stepPositionBlack, 1, 0));
        Collections.sort(actualPositionsBlack);
        MoveGeneratorHelper.compareFenStringsToPosition(expectedPositionsBlack, actualPositionsBlack);
    }

    @Test
    public void pawnStepGenerationTestDoubleStepNotAllowed(){
        // Pawn should only do a single step

        Position stepPositionWhite = FenParser.parseFen ("8/7k/8/8/8/P7/8/K7 " +
            "w - - 0 1");
        List<String> expectedPositionsWhite = new ArrayList<>();
        expectedPositionsWhite.add("8/7k/8/8/P7/8/8/K7 " +
            "b - - 0 1");
        Collections.sort(expectedPositionsWhite);
        List<Position>actualPositionsWhite = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(stepPositionWhite, 5, 0));
        Collections.sort(actualPositionsWhite);
        MoveGeneratorHelper.compareFenStringsToPosition(expectedPositionsWhite, actualPositionsWhite);

        Position stepPosition = FenParser.parseFen("8/7k/p7/8/8/8/8/K7 " +
            "b - - 0 1");
        List<String> expectedPositions = new ArrayList<>();
        expectedPositions.add("8/7k/8/p7/8/8/8/K7 " +
            "w - - 0 2");
        Collections.sort(expectedPositions);
        List<Position>actualPositions = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(stepPosition, 2, 0));
        Collections.sort(actualPositions);
        MoveGeneratorHelper.compareFenStringsToPosition(expectedPositions, actualPositions);
    }

    @Test
    public void pawnCaptureGenerationTest() {
        // Pawn should capture left and right
        Position capturePosition = FenParser.parseFen("8/8/1p5k/PPP5/8/8/8/7K " +
            "b KQkq - 0 1");
        List<String> expectedPositions = new ArrayList<>();
        expectedPositions.add("8/8/7k/pPP5/8/8/8/7K " +
            "w KQkq - 0 2");
        expectedPositions.add("8/8/7k/PPp5/8/8/8/7K " +
            "w KQkq - 0 2");
        Collections.sort(expectedPositions);
        List<Position>actualPositions = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(capturePosition,2,1));
        Collections.sort(actualPositions);
        MoveGeneratorHelper.compareFenStringsToPosition(expectedPositions, actualPositions);
    }

    @Test
    public void pawnPromotionGenerationTest() {
        // Pawn should promote to all 4 possible options
        Position promotionPosition = FenParser.parseFen("8/7k/8/8/8/8/p7/7K " +
        "b KQkq - 0 1");
        List<String> expectedPositions = new ArrayList<>();
        expectedPositions.add("8/7k/8/8/8/8/8/b6K " +
        "w KQkq - 0 2");
        expectedPositions.add("8/7k/8/8/8/8/8/n6K " +
        "w KQkq - 0 2");
        expectedPositions.add("8/7k/8/8/8/8/8/q6K " +
        "w KQkq - 0 2");
        expectedPositions.add("8/7k/8/8/8/8/8/r6K " +
        "w KQkq - 0 2");
        Collections.sort(expectedPositions);
        List<Position> actualPositions = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(promotionPosition,6,0));
        Collections.sort(actualPositions);
        MoveGeneratorHelper.compareFenStringsToPosition(expectedPositions, actualPositions);
    }

    @Test
    public void pawnEnPassantGenerationTest() {
        Position enPassantLeftPosition = FenParser.parseFen("8/1pP4k/1P6/8/8/8/8/7K " +
            "b KQkq c6 0 1");
        Position enPassantRightPosition = FenParser.parseFen("8/Pp5k/1P6/8/8/8/8/7K " +
            "b KQkq a6 0 1");
        List<String> expectedPositionLeft = new ArrayList<>();
        expectedPositionLeft.add("8/7k/1Pp5/8/8/8/8/7K " +
            "w KQkq - 0 2");
        List<Position> actualPositionsLeft = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(enPassantLeftPosition,1,1));
        MoveGeneratorHelper.compareFenStringsToPosition(expectedPositionLeft, actualPositionsLeft);
        List<String> expectedPositionRight = new ArrayList<>();
        expectedPositionRight.add("8/7k/pP6/8/8/8/8/7K " +
            "w KQkq - 0 2");
        List<Position> actualPositionsRight = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(enPassantRightPosition,1,1));
        MoveGeneratorHelper.compareFenStringsToPosition(expectedPositionRight, actualPositionsRight);
    }
    
    @Test
    public void knightGenerationTest() {
        Position knightPosition = FenParser.parseFen("7k/1n6/8/P7/8/8/8/7K b KQkq - 0 1");
        List<String> expectedPositions = new ArrayList<String>(4);
        expectedPositions.add("3n3k/8/8/P7/8/8/8/7K w KQkq - 1 2");
        expectedPositions.add("7k/8/3n4/P7/8/8/8/7K w KQkq - 1 2");
        expectedPositions.add("7k/8/8/n7/8/8/8/7K w KQkq - 0 2");
        expectedPositions.add("7k/8/8/P1n5/8/8/8/7K w KQkq - 1 2");

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(knightPosition, BLACK_KNIGHT, 1, 1, 
        expectedPositions);
    }

    @Test
    public void knightCheckDetectionTest() {
        Position knightCannotMovePosition = FenParser.parseFen("8/8/R2n2k1/8/8/8/8/K7 b - - 0 1");
        MoveGeneratorHelper.verifyPieceMoveGeneration(knightCannotMovePosition, BLACK_KNIGHT, 2, 3, EMPTY_LIST);

        Position knightMustMovePosition = FenParser.parseFen("3n4/8/R5k1/8/8/8/8/K7 " + "b KQkq - 0 1");
        List<String> expectedMustMovePositions = new ArrayList<String>(2);
        expectedMustMovePositions.add("8/8/R1n3k1/8/8/8/8/K7 w KQkq - 1 2");
        expectedMustMovePositions.add("8/8/R3n1k1/8/8/8/8/K7 w KQkq - 1 2");

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(knightMustMovePosition, BLACK_KNIGHT, 0, 3,
                expectedMustMovePositions);
    }

    @Test
    public void kingStepGenerationTest() {
        Position kingPosition = FenParser.parseFen("7K/8/8/8/8/8/3P4/3k4 b - - 0 1");
        List<String> expectedPositions = new ArrayList<>(5);
        expectedPositions.add("7K/8/8/8/8/8/3Pk3/8 w - - 1 2");
        expectedPositions.add("7K/8/8/8/8/8/2kP4/8 w - - 1 2");
        expectedPositions.add("7K/8/8/8/8/8/3k4/8 w - - 0 2");
        expectedPositions.add("7K/8/8/8/8/8/3P4/2k5 w - - 1 2");
        expectedPositions.add("7K/8/8/8/8/8/3P4/4k3 w - - 1 2");
        
        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(kingPosition, BLACK_KING, 7, 3, expectedPositions);
    }

    @Test
    public void blackCastlingGenerationTest() {
        Position castlingPosition = FenParser.parseFen(
            "r3k2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        List<String> expectedPosition = new ArrayList<>(4);
        expectedPosition.add("r4k1r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2");
        expectedPosition.add("r2k3r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2");
        expectedPosition.add("2kr3r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2");
        expectedPosition.add("r4rk1/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2");

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(castlingPosition, BLACK_KING, 0, 4, expectedPosition);
    }

    @Test
    public void whiteCastlingGenerationTest() {
        Position castlingPosition = FenParser.parseFen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        List<String> expectedPosition = new ArrayList<>(4);

        expectedPosition.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R4RK1 b kq - 1 1");
        expectedPosition.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R4K1R b kq - 1 1");
        expectedPosition.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R2K3R b kq - 1 1");
        expectedPosition.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/2KR3R b kq - 1 1");

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(castlingPosition, WHITE_KING, 7, 4, expectedPosition);
    }



    @Test
    public void moveBishopTest() {
        Position bishopTestPosition = FenParser.parseFen("7K/8/1n6/4P3/3b4/8/8/7k b - - 0 1"); //bishop starts at d4

        List<String> expectedfollowUpPositions = new ArrayList<String>(8);
        expectedfollowUpPositions.add("7K/8/1n6/2b1P3/8/8/8/7k w - - 1 2"); //move one square to upper left
        expectedfollowUpPositions.add("7K/8/1n6/4b3/8/8/8/7k w - - 0 2"); //capture pawn on e5 - one square to upper right
        expectedfollowUpPositions.add("7K/8/1n6/4P3/8/2b5/8/7k w - - 1 2"); //move to c3 - one square to bottom left
        expectedfollowUpPositions.add("7K/8/1n6/4P3/8/8/1b6/7k w - - 1 2"); //move to b2 - two squares to bottom left
        expectedfollowUpPositions.add("7K/8/1n6/4P3/8/8/8/b6k w - - 1 2"); //move to a1 - three squares to bottom left
        expectedfollowUpPositions.add("7K/8/1n6/4P3/8/4b3/8/7k w - - 1 2"); //move to e3 - one square to bottom right
        expectedfollowUpPositions.add("7K/8/1n6/4P3/8/8/5b2/7k w - - 1 2"); //move to f2 - two squares to bottom right
        expectedfollowUpPositions.add("7K/8/1n6/4P3/8/8/8/6bk w - - 1 2"); //move to g1 - three squares to bottom right

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(bishopTestPosition, BLACK_BISHOP, 4, 3, expectedfollowUpPositions);
    }

    @Test
    public void moveRookTest() {
        Position rookTestPosition = FenParser.parseFen("2n4K/8/8/8/2r1P3/8/8/7k b - - 0 1"); //rook starts at c4

        List<String> expectedfollowUpPositions = new ArrayList<String>(10);
        expectedfollowUpPositions.add("2n4K/8/8/8/1r2P3/8/8/7k w - - 1 2"); //move to b4 - one square to the left
        expectedfollowUpPositions.add("2n4K/8/8/8/r3P3/8/8/7k w - - 1 2"); //move to a4 - two squares to the left
        expectedfollowUpPositions.add("2n4K/8/8/2r5/4P3/8/8/7k w - - 1 2"); //move to c5 - one square up
        expectedfollowUpPositions.add("2n4K/8/2r5/8/4P3/8/8/7k w - - 1 2"); //move to c6 - two squares up
        expectedfollowUpPositions.add("2n4K/2r5/8/8/4P3/8/8/7k w - - 1 2"); //move to c7 - 3 squares up
        expectedfollowUpPositions.add("2n4K/8/8/8/3rP3/8/8/7k w - - 1 2"); //move to d4 - one square to the right
        expectedfollowUpPositions.add("2n4K/8/8/8/4r3/8/8/7k w - - 0 2"); //capture pawn on e4 - two squares to the right
        expectedfollowUpPositions.add("2n4K/8/8/8/4P3/2r5/8/7k w - - 1 2"); //move to c3 - one square down
        expectedfollowUpPositions.add("2n4K/8/8/8/4P3/8/2r5/7k w - - 1 2"); //move to c2 - two squares down
        expectedfollowUpPositions.add("2n4K/8/8/8/4P3/8/8/2r4k w - - 1 2"); //move to c1 - three squares down

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(rookTestPosition, BLACK_ROOK, 4, 2, expectedfollowUpPositions);

    }

    @Test
    public void moveBlackKingsideRookTest() {
        List<String> expectedfollowUpPositions = new ArrayList<String>(2);
        expectedfollowUpPositions.add("r3k1r1/p6p/8/8/8/8/P6P/R3K2R w KQq - 1 2"); //move to g8 - one square to the left
        expectedfollowUpPositions.add("r3kr2/p6p/8/8/8/8/P6P/R3K2R w KQq - 1 2"); //move to f8 - two squares to the left

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(blackCastlingPosition, BLACK_ROOK, 0, 7, expectedfollowUpPositions);
    }

    @Test
    public void moveBlackQueensideRookTest() {
        List<String> expectedfollowUpPositions = new ArrayList<String>(3);
        expectedfollowUpPositions.add("1r2k2r/p6p/8/8/8/8/P6P/R3K2R w KQk - 1 2"); //move to b8 - one square to the right
        expectedfollowUpPositions.add("2r1k2r/p6p/8/8/8/8/P6P/R3K2R w KQk - 1 2"); //move to c8 - two squares to the right
        expectedfollowUpPositions.add("3rk2r/p6p/8/8/8/8/P6P/R3K2R w KQk - 1 2"); //move to d8 - three squares to the right

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(blackCastlingPosition, BLACK_ROOK, 0, 0, expectedfollowUpPositions);

    }

    @Test
    public void moveWhiteKingSideRookTest() {
        List<String> expectedfollowUpPositions = new ArrayList<String>(2);
        expectedfollowUpPositions.add("r3k2r/p6p/8/8/8/8/P6P/R3K1R1 b Qkq - 1 1"); //move to g1 - one square to the left
        expectedfollowUpPositions.add("r3k2r/p6p/8/8/8/8/P6P/R3KR2 b Qkq - 1 1"); //move to f1 - two squares to the left

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(whiteCastlingPosition, WHITE_ROOK, 7, 7, expectedfollowUpPositions);
    }

    @Test
    public void moveWhiteQueensideRookTest() {
        List<String> expectedfollowUpPositions = new ArrayList<String>(3);

        expectedfollowUpPositions.add("r3k2r/p6p/8/8/8/8/P6P/1R2K2R b Kkq - 1 1"); //move to b1 - one square to the right
        expectedfollowUpPositions.add("r3k2r/p6p/8/8/8/8/P6P/2R1K2R b Kkq - 1 1"); //move to c1 - two squares to the right
        expectedfollowUpPositions.add("r3k2r/p6p/8/8/8/8/P6P/3RK2R b Kkq - 1 1"); //move to d1 - three squares to the right

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(whiteCastlingPosition, WHITE_ROOK, 7, 0, expectedfollowUpPositions);
    }

    @Test
    public void moveQueenTest() {
        Position queenTestPosition = FenParser.parseFen("7K/8/2n5/8/3P4/2q5/8/7k b - - 0 1"); //queen starts at c3

        List<String> expectedfollowUpPositions = new ArrayList<String>(18);
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/1q6/8/7k w - - 1 2"); //move to b3 - one square left
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/q7/8/7k w - - 1 2"); //move to a3 - two squares left
        expectedfollowUpPositions.add("7K/8/2n5/8/1q1P4/8/8/7k w - - 1 2"); //move to b4 - one square to upper left
        expectedfollowUpPositions.add("7K/8/2n5/q7/3P4/8/8/7k w - - 1 2"); //move to a5 - two squares to upper left
        expectedfollowUpPositions.add("7K/8/2n5/8/2qP4/8/8/7k w - - 1 2"); //move to c4 - one square up
        expectedfollowUpPositions.add("7K/8/2n5/2q5/3P4/8/8/7k w - - 1 2"); //move to c5 - two squares up
        expectedfollowUpPositions.add("7K/8/2n5/8/3q4/8/8/7k w - - 0 2"); //capture pawn on d4 - one square to upper right
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/3q4/8/7k w - - 1 2"); //move to d3 - one square to the right
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/4q3/8/7k w - - 1 2"); //move to e3 - two squares to the right
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/5q2/8/7k w - - 1 2"); //move to f3 - three squares to the right
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/6q1/8/7k w - - 1 2"); //move to g3 - four quares to the right
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/7q/8/7k w - - 1 2"); //move to h3 - five squares to the right
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/8/3q4/7k w - - 1 2"); //move to d2 - one square to bottom right
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/8/8/4q2k w - - 1 2"); //move to e1 - two squares to bottom right
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/8/2q5/7k w - - 1 2"); //move to c2 - one square down
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/8/8/2q4k w - - 1 2"); //move to c1 - two squares down
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/8/1q6/7k w - - 1 2"); //move to b2 - one square to bottom left
        expectedfollowUpPositions.add("7K/8/2n5/8/3P4/8/8/q6k w - - 1 2"); //move to a1 - two squares to bottom left

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(queenTestPosition, BLACK_QUEEN, 5, 2,
                expectedfollowUpPositions);
    }

    /**
     * Tests the generation for multiple piece types at once using {@link MoveGenerator#generatePossibleMoves(Position)}.
     */
    @Test
    public void generatePossibleMovesTest() {
        //test
        Position allBlackPiecesPosition = FenParser.parseFen(MoveGeneratorData.allBlackPiecesFen);

        Position[] actualFollowUpPositionsArray = MoveGenerator.generatePossibleMoves(allBlackPiecesPosition);
        //copy array data into list
        List<Position> actualFollowUpPositions = new ArrayList<Position>();
        Collections.addAll(actualFollowUpPositions, actualFollowUpPositionsArray);
        
        MoveGeneratorHelper.compareFenStringsToPosition(MoveGeneratorData.getExpectedAllBlackPiecesFenFollowUpMoves(), actualFollowUpPositions);

        //mirrored test
        Position mirroredAllBlackPiecesPosition = Mirror.mirrorPosition(allBlackPiecesPosition);

        Position[] mirroredActualFollowUpPositionsArray = MoveGenerator.generatePossibleMoves(mirroredAllBlackPiecesPosition);
        //copy array data into list
        List<Position> mirroredActualFollowUpPositions = new ArrayList<Position>();
        Collections.addAll(mirroredActualFollowUpPositions, mirroredActualFollowUpPositionsArray);

         MoveGeneratorHelper.mirrorFenStringsAndCompareToPosition(MoveGeneratorData.getExpectedAllBlackPiecesFenFollowUpMoves(), mirroredActualFollowUpPositions);
    }

    @Test
    /**
     * Tests if a sliding piece does not attempt to leave board boundaries.
     * A black queen starts in the a8 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInA8CornerTest() {
        Position queenTestPosition = FenParser.parseFen("qr6/rr6/8/8/3Kk3/8/8/8 w - - 0 1"); //queen starts at a8
        MoveGeneratorHelper.verifyPieceMoveGeneration(queenTestPosition, BLACK_QUEEN, 0, 0, EMPTY_LIST);
    }

    @Test
    /**
     * Tests if a sliding piece does not attempt to leave board boundaries.
     * A black queen starts in the h8 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInH8CornerTest() {
        Position queenTestPosition = FenParser.parseFen("6rq/6rr/8/8/3Kk3/8/8/8 w - - 0 1"); //queen starts at h8
        MoveGeneratorHelper.verifyPieceMoveGeneration(queenTestPosition, BLACK_QUEEN, 0, 7, EMPTY_LIST);
    }

    @Test
    /**
     * Tests if a sliding piece does not attempt to leave board boundaries.
     * A black queen starts in the a1 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInA1CornerTest() {
        Position queenTestPosition = FenParser.parseFen("8/8/8/8/3Kk3/8/rr6/qr6 w - - 0 1"); //queen starts at a1
        MoveGeneratorHelper.verifyPieceMoveGeneration(queenTestPosition, BLACK_QUEEN, 7, 0, EMPTY_LIST);
    }

    @Test
    /**
     * Tests if a sliding piece does not attempt to leave board boundaries.
     * A black queen starts in the h1 corner surrounded by black rooks. No moves are possible.
     */
    public void pieceInH1CornerTest() {
        Position queenTestPosition = FenParser.parseFen("8/8/8/8/3Kk3/8/6rr/6rq w - - 0 1"); //queen starts at h1
        MoveGeneratorHelper.verifyPieceMoveGeneration(queenTestPosition, BLACK_QUEEN, 7, 7, EMPTY_LIST);

    }

    @Test
    public void arrayIndexOutOfBoundsBugTest() {
        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/PPPPPPPP/6n1/8/8/8/8/2k5 w - - 0 1");
        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/P4PPP/6n1/8/8/8/8/2k5 w - - 0 1");
        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/5PPP/6n1/8/8/8/8/2k5 w - - 0 1");
        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/5P1P/6n1/8/8/8/8/2k5 w - - 0 1");
        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/7P/6n1/8/8/8/8/2k5 w - - 0 1");

        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/7P/8/8/8/8/8/2k5 w - - 0 1"));
        Position promotion = FenParser.parseFen("2K5/4P3/8/8/8/8/8/2k5 w - - 0 1");
        Position captureAndPromotion = FenParser.parseFen("k7/8/8/8/8/8/5p2/K3N3 b - - 1 25");
        Position blockedByPieceOnLastRank = FenParser.parseFen("k7/8/8/8/8/8/5p2/K4N2 b - - 1 25");

        MoveGenerator.generatePossibleMoves(promotion);
        MoveGenerator.generatePossibleMoves(captureAndPromotion);
        MoveGenerator.generatePossibleMoves(blockedByPieceOnLastRank);

        
        MoveGenerator.generatePossibleMoves(Mirror.mirrorPosition(promotion));
        MoveGenerator.generatePossibleMoves(Mirror.mirrorPosition(captureAndPromotion));
        MoveGenerator.generatePossibleMoves(Mirror.mirrorPosition(blockedByPieceOnLastRank));
    }

    @Test
    public void whiteStalemateByRooks() {
        PositionHelper.verifyStaleMate("k7/8/8/1r6/8/8/4r3/K7 w - - 0 1");
    }

    @Test
    public void blackStaleMateByRooks() {
        PositionHelper.verifyStaleMate("k7/4R3/8/1R6/8/8/8/K7 b - - 0 1");
    }

    @Test
    public void whiteCheckMateByRooksTest() {
        PositionHelper.verifyCheckMate("8/3K4/8/2rrr3/8/8/8/3k4 w - - 0 1", true);
    }

    @Test
    public void blackCheckMateByRooksTest() {
        PositionHelper.verifyCheckMate("8/3k4/8/2RRR3/8/8/8/3K4 b - - 0 1", false);
    }

    @Test
    public void whiteStaleMateByKnightsTest() {
        PositionHelper.verifyStaleMate("1n3n2/8/n2K2n1/8/1n3n2/8/8/k7 w - - 0 1");
    }

    @Test
    public void blackStaleMateByKnightsTest() {
        PositionHelper.verifyStaleMate("1N3N2/8/N2k2N1/8/1N3N2/8/8/K7 b - - 0 1");
    }

    @Test
    public void whiteCheckMateByKnightsTest() {
        PositionHelper.verifyCheckMate("1n3n2/8/n2K2n1/8/1n2nn2/8/8/k7 w - - 0 1", true);
    }

    @Test
    public void blackCheckMateByKnightsTest() {
        PositionHelper.verifyCheckMate("1N3N2/8/N2k2N1/8/1N2NN2/8/8/K7 b - - 0 1", false);
    }

    // @Test
    // public void whiteStaleMateByBishopTest() {
    //     PositionHelper.verifyStaleMate("8/8/3K4/b7/6bb/2b2b2/5b2/k7 w - - 0 1");
    // }

    // @Test
    // public void blackStaleMateByBishopTest() {
    //     // FIXME: This is not a stalemate
    //     PositionHelper.verifyStaleMate("8/8/3k4/B7/6B1/2B2B2/8/K7 b - - 0 1");
    // }

    // @Test
    // public void whiteCheckMateByBishopTest() {
    //     // FIXME: This is not a checkmate
    //     PositionHelper.verifyCheckMate("8/8/3K4/b7/5bb1/2b2b2/8/k7 w - - 0 1", true);
    // }

    // @Test
    // public void blackCheckMateByBishopTest() {
    //     // FIXME: This is not a checkmate
    //     PositionHelper.verifyCheckMate("8/8/3k4/B7/5BB1/2B2B2/8/K7 b - - 0 1", false);
    // }

    // @Test
    // public void whiteStaleMateByQueenTest() {
    //     // FIXME: This is not a stalemate
    //     PositionHelper.verifyStaleMate("8/1q6/3K4/8/4q3/8/8/k7 w - - 0 1");
    // }

    // @Test
    // public void blackStaleMateByQueenTest() {
    //     // FIXME: This is not a stalemate
    //     PositionHelper.verifyStaleMate("8/1Q6/3k4/8/4Q3/8/8/K7 b - - 0 1");
    // }

    @Test
    public void whiteCheckMateByQueenTest() {
        PositionHelper.verifyCheckMate("8/1q2q3/3K4/8/4q3/8/8/k7 w - - 0 1", true);
    }

    @Test
    public void blackCheckMateByQueenTest() {
        PositionHelper.verifyCheckMate("8/1Q2Q3/3k4/8/4Q3/8/8/K7 b - - 0 1", false);
    }

    // @Test
    // public void whiteStaleMateByPawnTest() {
    //     // FIXME: This is not a stalemate
    //     PositionHelper.verifyStaleMate("3K4/1p1p1p2/2ppp3/8/8/8/8/k7 w - - 0 1");
    // }

    @Test
    public void blackStaleMateByPawnTest() {
        PositionHelper.verifyStaleMate("3k4/1P1P1P2/2PPP3/8/8/8/8/K7 b - - 0 1");
    }

    // @Test
    // public void whiteCheckMateByPawnTest() {
    //     // FIXME: This is not a checkmate
    //     PositionHelper.verifyCheckMate("3K4/1p1ppp2/2ppp3/8/8/8/8/k7 w - - 0 1", true);
    // }

    @Test
    public void blackCheckMateByPawnTest() {
        PositionHelper.verifyCheckMate("3k4/1P1PPP2/2PPP3/8/8/8/8/K7 b - - 0 1", false);
    }

    @Test
    public void whiteStaleMateByKingTest() {
        PositionHelper.verifyStaleMate("K7/P1k5/8/8/8/8/8/8 w - - 0 1");
    }

    @Test
    public void blackStaleMateByKingTest() {
        PositionHelper.verifyStaleMate("8/8/8/8/8/8/p7/k1K5 b - - 0 1");
    }

    @Test
    public void algebraicNotationTest() {
        Position position1 = FenParser.parseFen("k7/8/5pr1/5P2/8/8/8/K7 w - - 0 1");
        String expected1 = "f5g6";
        List<Position> actualPositions1 = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(position1,3,5));
        String actual1 = actualPositions1.get(0).getMove().toStringAlgebraic();
        assertEquals(expected1, actual1);
        Position position2 = FenParser.parseFen("k7/8/8/8/3p4/3PB3/8/K7 b - - 0 1");
        String expected2 = "d4e3";
        List<Position> actualPositions2 = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(position2,4,3));
        String actual2 = actualPositions2.get(0).getMove().toStringAlgebraic();
        assertEquals(expected2, actual2);
    }

    @Test
    public void pawnCaptureIndexOutOfBoundsBugBlackTest() {
        Position pawnPosition = FenParser.parseFen("k7/8/8/8/8/8/5p2/K5N1 b - - 1 25");

        List<String> expectedfollowUpPositions = new ArrayList<String>(8);

        //move to f1
        expectedfollowUpPositions.add("k7/8/8/8/8/8/8/K4qN1 w - - 0 26"); //move to f1, promote to queen
        expectedfollowUpPositions.add("k7/8/8/8/8/8/8/K4rN1 w - - 0 26"); //move to f1, promote to rook
        expectedfollowUpPositions.add("k7/8/8/8/8/8/8/K4bN1 w - - 0 26"); //move to f1, promote to bishop
        expectedfollowUpPositions.add("k7/8/8/8/8/8/8/K4nN1 w - - 0 26"); //move to f1, promote to knight

        //capture on g1
        expectedfollowUpPositions.add("k7/8/8/8/8/8/8/K5q1 w - - 0 26"); //capture on g1, promote to queen
        expectedfollowUpPositions.add("k7/8/8/8/8/8/8/K5r1 w - - 0 26"); //capture on g1, promote to rook
        expectedfollowUpPositions.add("k7/8/8/8/8/8/8/K5b1 w - - 0 26"); //capture on g1, promote to bishop
        expectedfollowUpPositions.add("k7/8/8/8/8/8/8/K5n1 w - - 0 26"); //capture on g1, promote to knight

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(pawnPosition, BLACK_PAWN, 6, 5, expectedfollowUpPositions);
    }

    @Test
    public void pawnCaptureIndexOutOfBoundsBugWhiteTest() {
        Position pawnPosition = FenParser.parseFen("k5n1/5P2/8/8/8/8/8/K7 w - - 1 25");
        List<String> expectedfollowUpPositions = new ArrayList<String>(8);

        //move to f8
        expectedfollowUpPositions.add("k4Qn1/8/8/8/8/8/8/K7 b - - 0 25"); //move to f8, promote to queen
        expectedfollowUpPositions.add("k4Rn1/8/8/8/8/8/8/K7 b - - 0 25"); //move to f8, promote to rook
        expectedfollowUpPositions.add("k4Bn1/8/8/8/8/8/8/K7 b - - 0 25"); //move to f8, promote to bishop
        expectedfollowUpPositions.add("k4Nn1/8/8/8/8/8/8/K7 b - - 0 25"); //move to f8, promote to knight

        //capture on g1
        expectedfollowUpPositions.add("k5Q1/8/8/8/8/8/8/K7 b - - 0 25"); //capture on g8, promote to queen
        expectedfollowUpPositions.add("k5R1/8/8/8/8/8/8/K7 b - - 0 25"); //capture on g8, promote to rook
        expectedfollowUpPositions.add("k5B1/8/8/8/8/8/8/K7 b - - 0 25"); //capture on g8, promote to bishop
        expectedfollowUpPositions.add("k5N1/8/8/8/8/8/8/K7 b - - 0 25"); //capture on g8, promote to knight

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(pawnPosition, WHITE_PAWN, 1, 5, expectedfollowUpPositions);
    }

}
