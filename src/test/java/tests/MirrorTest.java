package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static model.PieceEncoding.*;

import org.junit.jupiter.api.Test;

import helper.Mirror;
import model.Board;
import model.ByteBoard;
import model.Coordinate;
import model.Move;
import model.Position;
import uciservice.FenParser;

public class MirrorTest {

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
        assertEquals(new Coordinate(0, 1), mirroredMove.getStartingSquare());
        assertEquals(new Coordinate(2, 2), mirroredMove.getTargetSquare());
    }

    @Test
    public void mirrorE8H8MoveTest() {
        Move move = new Move(new Coordinate(0, 4), new Coordinate(0, 7));
        Move mirroredMove = Mirror.mirrorMove(move);
        assertEquals(new Coordinate(7, 4), mirroredMove.getStartingSquare());
        assertEquals(new Coordinate(7, 7), mirroredMove.getTargetSquare());
    }

    @Test
    public void mirrorPromotionTest() {
        Move blackPromotion = new Move(new Coordinate(1, 6), new Coordinate(0, 6), BLACK_KNIGHT);
        Move expectedMirroredMove = new Move(new Coordinate(6, 6), new Coordinate(7, 6), WHITE_KNIGHT);
        Move mirroredMove = Mirror.mirrorMove(blackPromotion);
        assertEquals(expectedMirroredMove, mirroredMove);
        assertEquals(blackPromotion, Mirror.mirrorMove(mirroredMove));
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
    public void mirrorPositionOccupiedSquaresTest() {
        Position position = FenParser.parseFen("K6Q/3B1P2/8/8/8/8/p2npb2/r6k b - - 0 1");
        Position expected = FenParser.parseFen("R6K/P2NPB2/8/8/8/8/3b1p2/k6q w - - 0 1");
        Position mirrored = Mirror.mirrorPosition(position);
        assertEquals(expected, mirrored);
        assertEquals(position, Mirror.mirrorPosition(mirrored));
    }

    @Test
    public void mirrorPositionWhiteCastlingKingsideTest() {
        Position whiteCastlingKingside = FenParser.parseFen("4k3/8/8/8/8/8/8/4K2R w K - 0 1");
        Position expected = FenParser.parseFen("4k2r/8/8/8/8/8/8/4K3 b k - 0 1");
        assertEquals(expected, Mirror.mirrorPosition(whiteCastlingKingside));
    }

    @Test
    public void mirrorPositionWhiteCastlingQueensideTest() {
        Position whiteCastlingQueenSide = FenParser.parseFen("4k3/8/8/8/8/8/8/R3K3 w Q - 0 1");
        Position expected = FenParser.parseFen("r3k3/8/8/8/8/8/8/4K3 b q - 0 1");
        assertEquals(expected, Mirror.mirrorPosition(whiteCastlingQueenSide));
    }

    @Test
    public void mirrorPositionBlackCastlingKingsideTest() {
        Position blackCastlingKingside = FenParser.parseFen("4k2r/8/8/8/8/8/8/4K3 w k - 0 1");
        Position expected = FenParser.parseFen("4k3/8/8/8/8/8/8/4K2R b K - 0 1");
        assertEquals(expected, Mirror.mirrorPosition(blackCastlingKingside));
    }

    @Test
    public void mirrorPositionBlackCastlingQueensideTest() {
        Position blackCastlingQueenside = FenParser.parseFen("r3k3/8/8/8/8/8/8/4K3 w q - 0 1");
        Position expected = FenParser.parseFen("4k3/8/8/8/8/8/8/R3K3 b Q - 0 1");
        assertEquals(expected, Mirror.mirrorPosition(blackCastlingQueenside));
    }

    @Test
    public void mirrorMixedCastlingRightsTest() {
        Position mixed = FenParser.parseFen("4k2r/8/8/8/8/8/8/R3K3 b Qk - 0 1");
        Position expected = FenParser.parseFen("r3k3/8/8/8/8/8/8/4K2R w Kq - 0 1");
        Position mirrored = Mirror.mirrorPosition(mixed);
        assertEquals(expected, mirrored);
        assertEquals(mixed, Mirror.mirrorPosition(mirrored));
    }

    @Test
    public void mirrorOneSideHasBothCastlingRightsTest() {
        Position onesided = FenParser.parseFen("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
        Position expected = FenParser.parseFen("r3k2r/8/8/8/8/8/8/4K3 b kq - 0 1");
        Position mirrored = Mirror.mirrorPosition(onesided);
        assertEquals(expected, mirrored);
        assertEquals(onesided, Mirror.mirrorPosition(mirrored));
    }

    @Test
    public void mirrorNoastlingRightsTest() {
        Position noCastling = FenParser.parseFen("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        Position expected = FenParser.parseFen("r3k2r/8/8/8/8/8/8/R3K2R b - - 0 1");
        Position mirrored = Mirror.mirrorPosition(noCastling);
        assertEquals(expected, mirrored);
        assertEquals(noCastling, Mirror.mirrorPosition(mirrored));
    }

    @Test
    public void mirrorCenteredPiecesTest() {
        Position centered = FenParser.parseFen("4k3/8/8/n2Q3P/P2r3b/8/8/4K3 b - - 0 1");
        Position expected = FenParser.parseFen("4k3/8/8/p2R3B/N2q3p/8/8/4K3 w - - 0 1");
        Position mirrored = Mirror.mirrorPosition(centered);
        assertEquals(expected, mirrored);
        assertEquals(centered, Mirror.mirrorPosition(mirrored));
    }

    @Test
    public void mirrorQueenCoveredByPawnTest() {
        Position position = FenParser.parseFen("4k3/2p5/3q4/8/8/8/3P4/2K5 b - - 0 1");
        Position expected = FenParser.parseFen("2k5/3p4/8/8/8/3Q4/2P5/4K3 w - - 0 1");
        Position mirrored = Mirror.mirrorPosition(position);
        assertEquals(expected, mirrored);
        assertEquals(position, Mirror.mirrorPosition(mirrored));
    }

    @Test
    public void mirrorPromotedPositionTest() {
        Position blackPromoted = FenParser.parseFen("q7/2K5/5B2/8/8/2p2r2/k7/8 w - - 0 1");
        blackPromoted.setMove(new Move(new Coordinate(1, 0), new Coordinate(0, 0), BLACK_QUEEN));

        Position expectedMirroredPosition = FenParser.parseFen("8/K7/2P2R2/8/8/5b2/2k5/Q7 b - - 0 1");
        expectedMirroredPosition.setMove(new Move(new Coordinate(6, 0), new Coordinate(7, 0), WHITE_QUEEN));

        Position mirroredPosition = Mirror.mirrorPosition(blackPromoted);
        assertEquals(expectedMirroredPosition, mirroredPosition);
        assertEquals(blackPromoted, Mirror.mirrorPosition(mirroredPosition));    
        //compare moves (not part of Position.equals())
        assertEquals(expectedMirroredPosition.getMove(), mirroredPosition.getMove());    
        assertEquals(blackPromoted.getMove(), Mirror.mirrorPosition(mirroredPosition).getMove());
    }
}
