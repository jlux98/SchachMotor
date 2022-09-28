package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import data.MoveGeneratorData;
import gametree.ComputeChildrenException;
import gametree.GameNode;
import gametree.Node;
import helper.Mirror;
import helper.MoveGeneratorHelper;
import helper.PositionHelper;
import model.Position;
import movegenerator.MoveGenerator;
import positionevaluator.PositionEvaluator;
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
        Position checkPosition = FenParser.parseFen("8/8/R2p2k1/8/8/8/8/K7 " + "w KQkq - 0 1");
        Position[] expectedPositions = null;
        assertEquals(expectedPositions, MoveGenerator.generatePossibleMovesPerPiece(checkPosition, 2, 2));
    }

    @Test
    public void pawnStepGenerationTestDoubleStepAllowed() {
        // Pawn can either do a double step or a single step

        Position stepPositionWhite = FenParser.parseFen ("8/7k/8/8/8/8/P7/K7 " +
            "w - - 0 1");
        List<Position> expectedPositionsWhite = new ArrayList<Position>();
        expectedPositionsWhite.add(FenParser.parseFen   ("8/7k/8/8/8/P7/8/K7 " +
            "b - - 0 1"));
        expectedPositionsWhite.add(FenParser.parseFen   ("8/7k/8/8/P7/8/8/K7 " +
            "b - a3 0 1"));
        Collections.sort(expectedPositionsWhite);
        List<Position>actualPositionsWhite = new ArrayList<Position>(
                MoveGenerator.generatePossibleMovesPerPiece(stepPositionWhite, 6, 0));
        Collections.sort(actualPositionsWhite);
        assertEquals(expectedPositionsWhite, actualPositionsWhite);

        Position stepPositionBlack = FenParser.parseFen("8/p6k/8/8/8/8/8/K7 " +
            "b - - 0 1");
        List<Position> expectedPositionsBlack = new ArrayList<Position>();
        expectedPositionsBlack.add(FenParser.parseFen("8/7k/p7/8/8/8/8/K7 " +
            "w - - 0 2"));
        expectedPositionsBlack.add(FenParser.parseFen("8/7k/8/p7/8/8/8/K7 " +
            "w - a6 0 2"));
        Collections.sort(expectedPositionsBlack);
        List<Position>actualPositionsBlack = new ArrayList<Position>(
                MoveGenerator.generatePossibleMovesPerPiece(stepPositionBlack, 1, 0));
        Collections.sort(actualPositionsBlack);
        assertEquals(expectedPositionsBlack, actualPositionsBlack);
    }

    @Test
    public void pawnStepGenerationTestDoubleStepNotAllowed() {
        // Pawn should only do a single step

        Position stepPositionWhite = FenParser.parseFen ("8/7k/8/8/8/P7/8/K7 " +
            "w - - 0 1");
        List<Position> expectedPositionsWhite = new ArrayList<Position>();
        expectedPositionsWhite.add(FenParser.parseFen   ("8/7k/8/8/P7/8/8/K7 " +
            "b - - 0 1"));
        Collections.sort(expectedPositionsWhite);
        List<Position>actualPositionsWhite = new ArrayList<Position>(
                MoveGenerator.generatePossibleMovesPerPiece(stepPositionWhite, 5, 0));
        Collections.sort(actualPositionsWhite);
        assertEquals(expectedPositionsWhite, actualPositionsWhite);

        Position stepPosition = FenParser.parseFen("8/7k/p7/8/8/8/8/K7 " +
            "b - - 0 1");
        List<Position> expectedPositions = new ArrayList<Position>();
        expectedPositions.add(FenParser.parseFen("8/7k/8/p7/8/8/8/K7 " +
            "w - - 0 2"));
        Collections.sort(expectedPositions);
        List<Position>actualPositions = new ArrayList<Position>(
            MoveGenerator.generatePossibleMovesPerPiece(stepPosition, 2, 0));
        Collections.sort(actualPositions);
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    public void pawnCaptureGenerationTest() {
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
    public void pawnPromotionGenerationTest() {
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
    public void pawnEnPassantGenerationTest() {
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

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(bishopTestPosition, BLACK_BISHOP, 4, 3,
                expectedfollowUpPositions);
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

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(blackCastlingPosition, BLACK_ROOK, 0, 7,
                expectedfollowUpPositions);
    }

    @Test
    public void moveBlackQueensideRookTest() {
        List<String> expectedfollowUpPositions = new ArrayList<String>(3);
        expectedfollowUpPositions.add("1r2k2r/p6p/8/8/8/8/P6P/R3K2R w KQk - 1 2"); //move to b8 - one square to the right
        expectedfollowUpPositions.add("2r1k2r/p6p/8/8/8/8/P6P/R3K2R w KQk - 1 2"); //move to c8 - two squares to the right
        expectedfollowUpPositions.add("3rk2r/p6p/8/8/8/8/P6P/R3K2R w KQk - 1 2"); //move to d8 - three squares to the right

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(blackCastlingPosition, BLACK_ROOK, 0, 0,
                expectedfollowUpPositions);

    }

    @Test
    public void moveWhiteKingSideRookTest() {
        List<String> expectedfollowUpPositions = new ArrayList<String>(2);
        expectedfollowUpPositions.add("r3k2r/p6p/8/8/8/8/P6P/R3K1R1 b Qkq - 1 1"); //move to g1 - one square to the left
        expectedfollowUpPositions.add("r3k2r/p6p/8/8/8/8/P6P/R3KR2 b Qkq - 1 1"); //move to f1 - two squares to the left

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(whiteCastlingPosition, WHITE_ROOK, 7, 7,
                expectedfollowUpPositions);
    }

    @Test
    public void moveWhiteQueensideRookTest() {
        List<String> expectedfollowUpPositions = new ArrayList<String>(3);

        expectedfollowUpPositions.add("r3k2r/p6p/8/8/8/8/P6P/1R2K2R b Kkq - 1 1"); //move to b1 - one square to the right
        expectedfollowUpPositions.add("r3k2r/p6p/8/8/8/8/P6P/2R1K2R b Kkq - 1 1"); //move to c1 - two squares to the right
        expectedfollowUpPositions.add("r3k2r/p6p/8/8/8/8/P6P/3RK2R b Kkq - 1 1"); //move to d1 - three squares to the right

        MoveGeneratorHelper.verifyPieceMoveGenerationWithFenStrings(whiteCastlingPosition, WHITE_ROOK, 7, 0,
                expectedfollowUpPositions);
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

        MoveGeneratorHelper.compareFenStringsToPosition(MoveGeneratorData.getExpectedAllBlackPiecesFenFollowUpMoves(),
                actualFollowUpPositions);

        //mirrored test
        Position mirroredAllBlackPiecesPosition = Mirror.mirrorPosition(allBlackPiecesPosition);

        Position[] mirroredActualFollowUpPositionsArray = MoveGenerator.generatePossibleMoves(mirroredAllBlackPiecesPosition);
        //copy array data into list
        List<Position> mirroredActualFollowUpPositions = new ArrayList<Position>();
        Collections.addAll(mirroredActualFollowUpPositions, mirroredActualFollowUpPositionsArray);

        MoveGeneratorHelper.mirrorFenStringsAndCompareToPosition(MoveGeneratorData.getExpectedAllBlackPiecesFenFollowUpMoves(),
                mirroredActualFollowUpPositions);
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
        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/PPPPPPPP/6n1/8/8/8/8/2k5 w - - 0 1"));
        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/P4PPP/6n1/8/8/8/8/2k5 w - - 0 1"));
        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/5PPP/6n1/8/8/8/8/2k5 w - - 0 1"));
        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/5P1P/6n1/8/8/8/8/2k5 w - - 0 1"));
        //MoveGenerator.generatePossibleMoves(FenParser.parseFen("2K5/7P/6n1/8/8/8/8/2k5 w - - 0 1"));

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

    @Test
    public void mirrorBlackFirstMove() {
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("r1bqkbnr/pppppppp/2n5/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 1 2");
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("r1bqkbnr/pppppppp/2n5/8/8/1P6/P1PPPPPP/RNBQKBNR b KQkq - 0 2");
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("1rbqkbnr/pppppppp/2n5/8/8/1P6/P1PPPPPP/RNBQKBNR w KQk - 1 3");
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("1rbqkbnr/pppppppp/2n5/8/8/1P6/PBPPPPPP/RN1QKBNR b KQk - 2 3");
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("r1bqkbnr/pppppppp/2n5/8/8/1P6/PBPPPPPP/RN1QKBNR w KQk - 3 4");
    }

    @Test
    public void mirrorWhiteFirstMove() {
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("rnbqkbnr/1ppppppp/p7/8/8/P7/1PPPPPPP/RNBQKBNR w KQkq - 0 2");
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("rnbqkbnr/1ppppppp/p7/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 0 2");
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("1nbqkbnr/rppppppp/p7/8/P7/8/1PPPPPPP/RNBQKBNR w KQk - 1 3");
        MoveGeneratorHelper.compareWhiteAndBlackMoveGeneration("1nbqkbnr/rppppppp/p7/P7/8/8/1PPPPPPP/RNBQKBNR b KQk - 0 3");
    }

    @Test
    public void moveGenerationStartPositionTest() {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "a2a3", "b2b3", "c2c3",
                "d2d3", "e2e3", "f2f3", "g2g3", "h2h3", "a2a4", "b2b4", "c2c4", "d2d4", "e2e4", "f2f4", "g2g4", "h2h4", "b1a3",
                "b1c3", "g1f3", "g1h3");
    }

    @Test
    public void moveGenerationMove2Test() {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", "a7a6", "b7b6",
                "c7c6", "d7d6", "e7e6", "f7f6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "d7d5", "e7e5", "f7f5", "g7g5", "h7h5",
                "b8a6", "b8c6", "g8f6", "g8h6");
    }

    @Test
    public void moveGenerationMove3Test() {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2", "a2a3", "b2b3",
                "c2c3", "d2d3", "f2f3", "g2g3", "h2h3", "a2a4", "b2b4", "c2c4", "d2d4", "f2f4", "g2g4", "h2h4", "b1a3", "b1c3",
                "g1e2", "g1f3", "g1h3", "f1e2", "f1d3", "f1c4", "f1b5", "f1a6", "d1e2", "d1f3", "d1g4", "d1h5", "e1e2");
    }

    @Test
    public void moveGenerationMove4Test() {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2", "a7a6", "b7b6",
                "c7c6", "d7d6", "f7f6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "d7d5", "f7f5", "g7g5", "h7h5", "b8a6", "b8c6",
                "g8f6", "g8h6", "g8e7", "f8a3", "f8b4", "f8c5", "f8d6", "f8e7", "d8h4", "d8g5", "d8f6", "d8e7", "e8e7");
    }

    @Test
    public void moveGenerationMove5Test() {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkb1r/pppp1ppp/5n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3", "a2a3", "b2b3",
                "c2c3", "d2d3", "g2g3", "h2h3", "a2a4", "b2b4", "c2c4", "d2d4", "g2g4", "h2h4", "b1a3", "b1c3", "f3g1", "f3d4",
                "f3h4", "f3e5", "f3g5", "f1e2", "f1d3", "f1c4", "f1b5", "f1a6", "h1g1", "d1e2", "e1e2");
    }

    @Test
    public void moveGenerationMove6Test() {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkb1r/pppp1ppp/5n2/4p3/3PP3/5N2/PPP2PPP/RNBQKB1R b KQkq - 0 3", "a7a6", "b7b6",
                "c7c6", "d7d6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "d7d5", "g7g5", "h7h5", "e5d4", "f6e4", "f6g4", "f6d5",
                "f6h5", "f6g8", "b8a6", "b8c6", "f8a3", "f8b4", "f8c5", "f8d6", "f8e7", "h8g8", "d8e7", "e8e7");

    }

    @Test
    public void moveGenerationMove7Test() {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkb1r/pppp1ppp/8/4p3/3Pn3/5N2/PPP2PPP/RNBQKB1R w KQkq - 0 4",

                "a2a3", "b2b3", "c2c3", "g2g3", "h2h3", "d4d5", "a2a4", "b2b4", "c2c4", "g2g4", "h2h4", "d4e5", "b1d2", "b1a3",
                "b1c3", "f3g1", "f3d2", "f3h4", "f3e5", "f3g5", "c1d2", "c1e3", "c1f4", "c1g5", "c1h6", "f1e2", "f1d3", "f1c4",
                "f1b5", "f1a6", "h1g1", "d1d2", "d1e2", "d1d3", "e1e2");

    }

    @Test
    public void moveGEnerationMove8Test() {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkb1r/pppp1ppp/8/4N3/3Pn3/8/PPP2PPP/RNBQKB1R b KQkq - 0 4", "a7a6", "b7b6",
                "c7c6", "d7d6", "f7f6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "d7d5", "f7f5", "g7g5", "h7h5", "e4d2", "e4f2",
                "e4c3", "e4g3", "e4c5", "e4g5", "e4d6", "e4f6", "b8a6", "b8c6", "f8a3", "f8b4", "f8c5", "f8d6", "f8e7", "h8g8",
                "d8h4", "d8g5", "d8f6", "d8e7", "e8e7");
    }

    @Test
    public void moveGenerationMove9Test() {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkb1r/ppp2ppp/8/3pN3/3Pn3/8/PPP2PPP/RNBQKB1R w KQkq - 0 5", "a2a3", "b2b3",
                "c2c3", "f2f3", "g2g3", "h2h3", "a2a4", "b2b4", "c2c4", "f2f4", "g2g4", "h2h4", "b1d2", "b1a3", "b1c3", "e5d3",
                "e5f3", "e5c4", "e5g4", "e5c6", "e5g6", "e5d7", "e5f7", "c1d2", "c1e3", "c1f4", "c1g5", "c1h6", "f1e2", "f1d3",
                "f1c4", "f1b5", "f1a6", "h1g1", "d1d2", "d1e2", "d1d3", "d1f3", "d1g4", "d1h5", "e1e2");
    }

    @Test
    public void moveGenerationMove10Test() {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkb1r/ppp2ppp/8/3pN3/3Pn3/3B4/PPP2PPP/RNBQK2R b KQkq - 1 5", "a7a6", "b7b6",
                "c7c6", "f7f6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "f7f5", "g7g5", "h7h5", "e4d2", "e4f2", "e4c3", "e4g3",
                "e4c5", "e4g5", "e4d6", "e4f6", "b8a6", "b8c6", "b8d7", "c8h3", "c8g4", "c8f5", "c8e6", "c8d7", "f8a3", "f8b4",
                "f8c5", "f8d6", "f8e7", "h8g8", "d8h4", "d8g5", "d8d6", "d8f6", "d8d7", "d8e7", "e8e7"

        );
    }

    @Test
    public void moveGenerationMove11Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r1bqkb1r/pppn1ppp/8/3pN3/3Pn3/3B4/PPP2PPP/RNBQK2R w KQkq - 2 6", "a2a3", "b2b3",
                "c2c3", "f2f3", "g2g3", "h2h3", "a2a4", "b2b4", "c2c4", "f2f4", "g2g4", "h2h4", "b1d2", "b1a3", "b1c3", "e5f3",
                "e5c4", "e5g4", "e5c6", "e5g6", "e5d7", "e5f7", "c1d2", "c1e3", "c1f4", "c1g5", "c1h6", "d3f1", "d3e2", "d3c4",
                "d3e4", "d3b5", "d3a6", "h1f1", "h1g1", "d1d2", "d1e2", "d1f3", "d1g4", "d1h5", "e1f1", "e1g1", "e1e2"

        );
    }

    @Test
    public void moveGenerationMoveT11Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r1bqkb1r/pppN1ppp/8/3p4/3Pn3/3B4/PPP2PPP/RNBQK2R b KQkq - 0 6", "a7a6", "b7b6",
                "c7c6", "f7f6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "f7f5", "g7g5", "h7h5", "e4d2", "e4f2", "e4c3", "e4g3",
                "e4c5", "e4g5", "e4d6", "e4f6", "c8d7", "f8a3", "f8b4", "f8c5", "f8d6", "f8e7", "a8b8", "h8g8", "d8h4", "d8g5",
                "d8f6", "d8d7", "d8e7", "e8d7", "e8e7");
    }

    @Test
    public void moveGenerationMove12Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2qkb1r/pppb1ppp/8/3p4/3Pn3/3B4/PPP2PPP/RNBQK2R w KQkq - 0 7", "a2a3", "b2b3",
                "c2c3", "f2f3", "g2g3", "h2h3", "a2a4", "b2b4", "c2c4", "f2f4", "g2g4", "h2h4", "b1d2", "b1a3", "b1c3", "c1d2",
                "c1e3", "c1f4", "c1g5", "c1h6", "d3f1", "d3e2", "d3c4", "d3e4", "d3b5", "d3a6", "h1f1", "h1g1", "d1d2", "d1e2",
                "d1f3", "d1g4", "d1h5", "e1f1", "e1g1", "e1e2");
        //e1g1 is white castling kingside
    }

    @Test
    public void moveGenerationMove13Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2qkb1r/pppb1ppp/8/3p4/3Pn3/3B4/PPP2PPP/RNBQ1RK1 b kq - 1 7", "a7a6", "b7b6",
                "c7c6", "f7f6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "f7f5", "g7g5", "h7h5", "e4d2", "e4f2", "e4c3", "e4g3",
                "e4c5", "e4g5", "e4d6", "e4f6", "d7h3", "d7a4", "d7g4", "d7b5", "d7f5", "d7c6", "d7e6", "d7c8", "f8a3", "f8b4",
                "f8c5", "f8d6", "f8e7", "a8b8", "a8c8", "h8g8", "d8h4", "d8g5", "d8f6", "d8e7", "d8b8", "d8c8", "e8e7");
    }

    @Test
    public void moveGenerationMove14Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2qkb1r/pppb1ppp/5n2/3p4/3P4/3B4/PPP2PPP/RNBQ1RK1 w kq - 2 8", "a2a3", "b2b3",
                "c2c3", "f2f3", "g2g3", "h2h3", "a2a4", "b2b4", "c2c4", "f2f4", "g2g4", "h2h4", "b1d2", "b1a3", "b1c3", "c1d2",
                "c1e3", "c1f4", "c1g5", "c1h6", "d3e2", "d3c4", "d3e4", "d3b5", "d3f5", "d3a6", "d3g6", "d3h7", "f1e1", "d1e1",
                "d1d2", "d1e2", "d1f3", "d1g4", "d1h5", "g1h1");
    }

    @Test
    public void moveGenerationMove15Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2qkb1r/pppb1ppp/5n2/3p4/2PP4/3B4/PP3PPP/RNBQ1RK1 b kq - 0 8", "a7a6", "b7b6",
                "c7c6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "g7g5", "h7h5", "d5c4", "f6e4", "f6g4", "f6h5", "f6g8", "d7h3",
                "d7a4", "d7g4", "d7b5", "d7f5", "d7c6", "d7e6", "d7c8", "f8a3", "f8b4", "f8c5", "f8d6", "f8e7", "a8b8", "a8c8",
                "h8g8", "d8e7", "d8b8", "d8c8", "e8e7");
    }

    @Test
    public void moveGenerationMove16Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2qk2r/pppbbppp/5n2/3p4/2PP4/3B4/PP3PPP/RNBQ1RK1 w kq - 1 9", "a2a3", "b2b3",
                "f2f3", "g2g3", "h2h3", "c4c5", "a2a4", "b2b4", "f2f4", "g2g4", "h2h4", "c4d5", "b1d2", "b1a3", "b1c3", "c1d2",
                "c1e3", "c1f4", "c1g5", "c1h6", "d3c2", "d3e2", "d3e4", "d3f5", "d3g6", "d3h7", "f1e1", "d1e1", "d1c2", "d1d2",
                "d1e2", "d1b3", "d1f3", "d1a4", "d1g4", "d1h5", "g1h1");
    }

    @Test
    public void moveGenerationMove17Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2qk2r/pppbbppp/5n2/3p4/2PP4/2NB4/PP3PPP/R1BQ1RK1 b kq - 2 9", "a7a6", "b7b6",
                "c7c6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "g7g5", "h7h5", "d5c4", "f6e4", "f6g4", "f6h5", "f6g8", "d7h3",
                "d7a4", "d7g4", "d7b5", "d7f5", "d7c6", "d7e6", "d7c8", "e7a3", "e7b4", "e7c5", "e7d6", "e7f8", "a8b8", "a8c8",
                "h8f8", "h8g8", "d8b8", "d8c8", "e8f8", "e8g8");
    }

    @Test
    public void moveGenerationMove18Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2qk2r/pppbbppp/5n2/8/2pP4/2NB4/PP3PPP/R1BQ1RK1 w kq - 0 10", "a2a3", "b2b3",
                "f2f3", "g2g3", "h2h3", "d4d5", "a2a4", "b2b4", "f2f4", "g2g4", "h2h4", "c3b1", "c3e2", "c3a4", "c3e4", "c3b5",
                "c3d5", "c1d2", "c1e3", "c1f4", "c1g5", "c1h6", "d3b1", "d3c2", "d3e2", "d3c4", "d3e4", "d3f5", "d3g6", "d3h7",
                "a1b1", "f1e1", "d1e1", "d1c2", "d1d2", "d1e2", "d1b3", "d1f3", "d1a4", "d1g4", "d1h5", "g1h1");
    }

    @Test
    public void moveGenerationMove19Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2qk2r/pppbbppp/5n2/8/2BP4/2N5/PP3PPP/R1BQ1RK1 b kq - 0 10", "a7a6", "b7b6",
                "c7c6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "g7g5", "h7h5", "f6e4", "f6g4", "f6d5", "f6h5", "f6g8", "d7h3",
                "d7a4", "d7g4", "d7b5", "d7f5", "d7c6", "d7e6", "d7c8", "e7a3", "e7b4", "e7c5", "e7d6", "e7f8", "a8b8", "a8c8",
                "h8f8", "h8g8", "d8b8", "d8c8", "e8f8", "e8g8");
        //e8g8 is black castling kingside
    }

    @Test
    public void moveGenerationMove20Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2q1rk1/pppbbppp/5n2/8/2BP4/2N5/PP3PPP/R1BQ1RK1 w - - 1 11", "a2a3", "b2b3",
                "f2f3", "g2g3", "h2h3", "d4d5", "a2a4", "b2b4", "f2f4", "g2g4", "h2h4", "c3b1", "c3e2", "c3a4", "c3e4", "c3b5",
                "c3d5", "c1d2", "c1e3", "c1f4", "c1g5", "c1h6", "c4e2", "c4b3", "c4d3", "c4b5", "c4d5", "c4a6", "c4e6", "c4f7",
                "a1b1", "f1e1", "d1e1", "d1c2", "d1d2", "d1e2", "d1b3", "d1d3", "d1f3", "d1a4", "d1g4", "d1h5", "g1h1");
    }

    @Test
    public void moveGenerationMove21Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2q1rk1/pppbbppp/5n2/8/2BP4/2N5/PP3PPP/R1BQR1K1 b - - 2 11", "a7a6", "b7b6",
                "c7c6", "g7g6", "h7h6", "a7a5", "b7b5", "c7c5", "g7g5", "h7h5", "f6e4", "f6g4", "f6d5", "f6h5", "f6e8", "d7h3",
                "d7a4", "d7g4", "d7b5", "d7f5", "d7c6", "d7e6", "d7c8", "d7e8", "e7a3", "e7b4", "e7c5", "e7d6", "a8b8", "a8c8",
                "f8e8", "d8b8", "d8c8", "d8e8", "g8h8");
    }

    @Test
    public void moveGenerationMove22Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2q1rk1/pp1bbppp/2p2n2/8/2BP4/2N5/PP3PPP/R1BQR1K1 w - - 0 12", "a2a3", "b2b3",
                "f2f3", "g2g3", "h2h3", "d4d5", "a2a4", "b2b4", "f2f4", "g2g4", "h2h4", "c3b1", "c3e2", "c3a4", "c3e4", "c3b5",
                "c3d5", "c1d2", "c1e3", "c1f4", "c1g5", "c1h6", "c4f1", "c4e2", "c4b3", "c4d3", "c4b5", "c4d5", "c4a6", "c4e6",
                "c4f7", "a1b1", "e1f1", "e1e2", "e1e3", "e1e4", "e1e5", "e1e6", "e1e7", "d1c2", "d1d2", "d1e2", "d1b3", "d1d3",
                "d1f3", "d1a4", "d1g4", "d1h5", "g1f1", "g1h1");
    }

    @Test
    public void moveGenerationMove23Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2q1rk1/pp1bbppp/2p2n2/8/2BP4/2N4P/PP3PP1/R1BQR1K1 b - - 0 12", "c6c5", "a7a6", "b7b6", "g7g6", "h7h6", "a7a5", "b7b5", "g7g5", "h7h5",
                "f6e4", "f6g4", "f6d5", "f6h5", "f6e8", "d7h3", "d7g4", "d7f5", "d7e6", "d7c8", "d7e8", "e7a3", "e7b4", "e7c5",
                "e7d6", "a8b8", "a8c8", "f8e8", "d8a5", "d8b6", "d8c7", "d8b8", "d8c8", "d8e8", "g8h8");
    }

    @Test
    public void moveGenerationMove24Test() {
        MoveGeneratorHelper.verifyMoveGeneration("r2q1rk1/pp1bbppp/2p2n2/8/2BP4/2N4P/PP3PP1/R1BQR1K1 b - - 0 12", "a2a3", "b2b3",
                "f2f3", "g2g3", "h3h4", "d4d5", "a2a4", "b2b4", "f2f4", "g2g4", "c3b1", "c3e2", "c3a4", "c3e4", "c3b5", "c3d5",
                "c1d2", "c1e3", "c1f4", "c1g5", "c1h6", "c4f1", "c4e2", "c4b3", "c4d3", "c4b5", "c4d5", "c4e6", "c4f7", "a1b1",
                "e1f1", "e1e2", "e1e3", "e1e4", "e1e5", "e1e6", "e1e7", "d1c2", "d1d2", "d1e2", "d1b3", "d1d3", "d1f3", "d1a4",
                "d1g4", "d1h5", "g1f1", "g1h1", "g1h2");
    }

    @Test
    public void chaosPositionTest() {
        MoveGeneratorHelper.verifyMoveGeneration("qb1rb3/1n6/2pkppn1/8/N1p1Q2r/P1P2P1P/1R1B2RK/1B3N2 b - - 0 1", "c6c5", "e6e5",
                "f6f5", "g6f4", "g6e5", "g6e7", "g6f8", "g6h8", "b7a5", "b7c5", "b8a7", "b8c7", "e8d7", "e8f7", "h4h3", "h4e4",
                "h4f4", "h4g4", "h4h5", "h4h6", "h4h7", "h4h8", "d8d7", "d8c8", "a8a4", "a8a5", "a8a6", "a8a7", "d6e7", "d6d7",
                "d6c7");
    }

    @Test
    public void onePossibleMoveTest() {
        MoveGeneratorHelper.verifyMoveGeneration("1qRk4/3r4/8/2Q3K1/8/8/8/8 b - - 1 1", "b8c8");
    }

    @Test
    public void blackSeesBishopMoveTest() throws ComputeChildrenException {
        MoveGeneratorHelper.verifyMoveGeneration("rnbqkbnr/p1pppppp/1p6/8/8/N7/PPPPPPPP/R1BQKBNR b Kkq - 0 1", "b6b5", "a7a6",
                "c7c6", "d7d6", "e7e6", "f7f6", "g7g6", "h7h6", "a7a5", "c7c5", "d7d5", "e7e5", "f7f5", "g7g5", "h7h5", "b8a6",
                "b8c6", "g8f6", "g8h6", "c8a6", "c8b7");

        GameNode node = new GameNode(FenParser.parseFen("rnbqkbnr/p1pppppp/1p6/8/8/N7/PPPPPPPP/R1BQKBNR b Kkq - 0 1"));
        List<? extends Node<Position>> children = node.queryChildren();
        Position bishopMove = null;
        for (Node<Position> child : children) {
            if (((GameNode) child).getRepresentedMove().toStringAlgebraic().equals("c8b7")) {
                bishopMove = child.getContent();
                System.out.println(child.getContent());
                System.out.println(child.getValue());
            }
        }
        assertEquals(0, PositionEvaluator.evaluatePosition(bishopMove, false, 0));
        assertNotNull(bishopMove);
    }

    @Test
    public void blackSeesCaptureTest() throws ComputeChildrenException {
        MoveGeneratorHelper.verifyMoveGeneration("rn1qkbnr/pbpppppp/1p6/8/8/N7/PPPPPPPP/1RBQKBNR b Kkq - 0 1", "b6b5", "a7a6",
                "c7c6", "d7d6", "e7e6", "f7f6", "g7g6", "h7h6", "a7a5", "c7c5", "d7d5", "e7e5", "f7f5", "g7g5", "h7h5", "b8a6",
                "b8c6", "g8f6", "g8h6", "b7g2", "b7f3", "b7e4", "b7d5", "b7a6", "b7c6", "b7c8", "d8c8");

                GameNode node = new GameNode(FenParser.parseFen("rn1qkbnr/pbpppppp/1p6/8/8/N7/PPPPPP1P/1RBQKBNR b Kkq - 0 1"));
                List<? extends Node<Position>> children = node.queryChildren();
                Position bishopCapture = null;
                for (Node<Position> child : children) {
                    if (((GameNode) child).getRepresentedMove().toStringAlgebraic().equals("b7g2")) {
                        bishopCapture = child.getContent();
                        System.out.println(child.getContent());
                        System.out.println(child.getValue());
                    }
                }
                assertEquals(-100, PositionEvaluator.evaluatePosition(bishopCapture, false, 0));
                assertNotNull(bishopCapture);                
    }

    @Test
    public void Test() {
        Position pos1 = FenParser.parseFen("1q1k4/2Rr4/8/2Q3K1/8/8/8/8 b - - 1 1");
        Position pos2 = FenParser.parseFen("2qk4/3r4/8/2Q3K1/8/8/8/8 w - - 1 1");
        System.out.println(PositionEvaluator.evaluatePosition(pos1, false, -1));
        System.out.println(PositionEvaluator.evaluatePosition(pos2, false, -1));
    }

    public static void main(String[] args) {
        new MoveGeneratorTest().Test();
    }

}
