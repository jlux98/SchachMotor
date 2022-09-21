package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

import org.junit.jupiter.api.Test;

import helper.Mirror;
import model.Board;
import model.ByteBoard;
import model.Coordinate;
import model.Move;
import model.Position;
import uciservice.FenParser;

public class IndexMirroringTest {

    //a8 = (0, 0);
    //h8 = (0, 7);
    //a1 = (7, 0);
    //h1 = (7, 7);

    @Test
    public void mirrorA8Test() {
        Board board = ByteBoard.createEmpty();
        //write to a8
        board.setByteAt(0, 0, BLACK_KNIGHT);
        //access a1 mirrored
        assertEquals(BLACK_KNIGHT, Mirror.mirroredGetByteAt(7, 0, board));
    }

    @Test
    public void mirrorH8Test() {
        Board board = ByteBoard.createEmpty();
        //write to h8
        board.setByteAt(0, 7, WHITE_BISHOP);
        //access h1 mirrored
        assertEquals(WHITE_BISHOP, Mirror.mirroredGetByteAt(7, 7, board));
    }

    @Test
    public void mirrorA1Test() {
        Board board = ByteBoard.createEmpty();
        //write to a1
        board.setByteAt(7, 0, BLACK_QUEEN);
        //access a8 mirrored
        assertEquals(BLACK_QUEEN, Mirror.mirroredGetByteAt(0, 0, board));
    }

    @Test
    public void mirrorH1Test() {
        Board board = ByteBoard.createEmpty();
        //write to h1
        board.setByteAt(7, 7, WHITE_PAWN);
        //access h8 mirrored
        assertEquals(WHITE_PAWN, Mirror.mirroredGetByteAt(0, 7, board));
    }

    @Test
    public void mirrorE2Test() {
        Board board = ByteBoard.createEmpty();
        //write to h1
        board.setByteAt(6, 4, BLACK_PAWN);
        //access h8 mirrored
        assertEquals(BLACK_PAWN, Mirror.mirroredGetByteAt(1, 4, board));
    }

    @Test
    public void mirrorStartingPositionTest() {
        Position startingPosition = FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Position mirroredStartingPosition = Mirror.mirrorPosition(startingPosition);
        Position startingPositionButBlacksTurn = FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        assertEquals(startingPositionButBlacksTurn, mirroredStartingPosition);
    }

    @Test
    public void mirrorStartingPositionTwiceTest() {
        Position startingPosition = FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Position mirroredStartingPosition = Mirror.mirrorPosition(startingPosition);
        Position twiceMirroredStartingPosition = Mirror.mirrorPosition(mirroredStartingPosition);
        assertEquals(startingPosition, twiceMirroredStartingPosition);
    }

    @Test
    public void mirrorWhitePiecesTest() {
        Position whitePieces = FenParser.parseFen("7B/1R6/5K2/2N5/8/4QP2/8/k7 w - - 0 1");
        Position mirroredPosition = Mirror.mirrorPosition(whitePieces);
        Position expectedPosition = FenParser.parseFen("K7/8/4qp2/8/2n5/5k2/1r6/7b b - - 0 1");
        assertEquals(expectedPosition, mirroredPosition);
    }

    @Test
    public void mirrorBlackPiecesTest() {
        Position blackPieces = FenParser.parseFen("K7/8/4qp2/8/2n5/5k2/1r6/7b b - - 0 1");
        Position mirroredPosition = Mirror.mirrorPosition(blackPieces);        
        Position expectedPosition = FenParser.parseFen("7B/1R6/5K2/2N5/8/4QP2/8/k7 w - - 0 1");
        assertEquals(expectedPosition, mirroredPosition);
    }

    @Test
    public void mirrorH8CoordinateTest() {
        Coordinate mirrored = Mirror.mirrorCoordinate(new Coordinate(0, 7));
        assertEquals(7, mirrored.getRank());
        assertEquals(7, mirrored.getFile());
    }

    @Test
    public void mirrorA1CoordinateTest() {
        Coordinate mirrored = Mirror.mirrorCoordinate(new Coordinate(07, 0));
        assertEquals(0, mirrored.getRank());
        assertEquals(0, mirrored.getFile());
    }

    @Test
    public void mirrorH1CoordinateTest() {
        Coordinate mirrored = Mirror.mirrorCoordinate(new Coordinate(7, 7));
        assertEquals(0, mirrored.getRank());
        assertEquals(7, mirrored.getFile());
    }

    @Test
    public void mirrorA8CoordinateTest() {
        Coordinate mirrored = Mirror.mirrorCoordinate(new Coordinate(0, 0));
        assertEquals(7, mirrored.getRank());
        assertEquals(0, mirrored.getFile());
    }

    @Test
    public void mirrorC6CoordinateTest() {
        Coordinate mirrored = Mirror.mirrorCoordinate(new Coordinate(2, 2));
        assertEquals(5, mirrored.getRank());
        assertEquals(2, mirrored.getFile());
    }

    @Test
    public void mirrorH2CoordinateTest() {
        Coordinate mirrored = Mirror.mirrorCoordinate(new Coordinate(6, 7));
        assertEquals(1, mirrored.getRank());
        assertEquals(7, mirrored.getFile());
    }

    @Test
    public void mirrorB1C3MoveTest() {
        Move move = new Move(new Coordinate(7, 1), new Coordinate(5, 2));
        Move mirroredMove = Mirror.mirrorMove(move);
        assertEquals(new Coordinate(0, 1), mirroredMove.getStartingSpace());
        assertEquals(new Coordinate(2, 2), mirroredMove.getTargetSpace());
    }

    @Test
    public void mirrorE8H8MoveTest() {
        Move move = new Move(new Coordinate(0, 4), new Coordinate(0, 7));
        Move mirroredMove = Mirror.mirrorMove(move);
        assertEquals(new Coordinate(7, 4), mirroredMove.getStartingSpace());
        assertEquals(new Coordinate(7, 7), mirroredMove.getTargetSpace());
    }

    @Test
    public void changeColorTest() {

        assertEquals(EMPTY_SQUARE, Mirror.changeColor(EMPTY_SQUARE));

        assertEquals(BLACK_BISHOP, Mirror.changeColor(WHITE_BISHOP));
        assertEquals(BLACK_KING, Mirror.changeColor(WHITE_KING));
        assertEquals(BLACK_KNIGHT, Mirror.changeColor(WHITE_KNIGHT));
        assertEquals(BLACK_PAWN, Mirror.changeColor(WHITE_PAWN));
        assertEquals(BLACK_QUEEN, Mirror.changeColor(WHITE_QUEEN));
        assertEquals(BLACK_ROOK, Mirror.changeColor(WHITE_ROOK));

        assertEquals(WHITE_BISHOP, Mirror.changeColor(BLACK_BISHOP));
        assertEquals(WHITE_KING, Mirror.changeColor(BLACK_KING));
        assertEquals(WHITE_KNIGHT, Mirror.changeColor(BLACK_KNIGHT));
        assertEquals(WHITE_PAWN, Mirror.changeColor(BLACK_PAWN));
        assertEquals(WHITE_QUEEN, Mirror.changeColor(BLACK_QUEEN));
        assertEquals(WHITE_ROOK, Mirror.changeColor(BLACK_ROOK));
    }
}
