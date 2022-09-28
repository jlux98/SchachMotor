package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ByteBoard implements Board {
    private byte[][] spaces;
    private static final byte WHITE_BISHOP = 1;
    private static final byte WHITE_KING = 2;
    private static final byte WHITE_KNIGHT = 3;
    private static final byte WHITE_PAWN = 4;
    private static final byte WHITE_QUEEN = 5;
    private static final byte WHITE_ROOK = 6;
    // private static final byte BLACK_BISHOP = 7;
    private static final byte BLACK_KING = 8;
    // private static final byte BLACK_KNIGHT = 9;
    // private static final byte BLACK_PAWN = 10;
    // private static final byte BLACK_QUEEN = 11;
    // private static final byte BLACK_ROOK = 12;

    public ByteBoard(Piece[][] spaces) {
        byte[][] result = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                result[i][j] = pieceToByte(spaces[i][j]);
            }
        }
        this.spaces = result;
    }

    public ByteBoard(byte[][] spaces) {
        this.spaces = spaces;
    }

    /**
     * Instantiates an empty board.
     * @return a new instance
     */
    public static Board createEmpty() {
        return new ByteBoard(new byte[8][8]);
    }

    private byte pieceToByte(Piece piece) {
        if (piece == null) {
            return 0;
        }
        byte result = -1;
        switch (piece.getPieceType()) {
        case BISHOP:
            result = WHITE_BISHOP;
            break;
        case KING:
            result = WHITE_KING;
            break;
        case KNIGHT:
            result = WHITE_KNIGHT;
            break;
        case PAWN:
            result = WHITE_PAWN;
            break;
        case QUEEN:
            result = WHITE_QUEEN;
            break;
        case ROOK:
            result = WHITE_ROOK;
            break;
        default:
            break;
        }
        if (!piece.getIsWhite() && result != -1) {
            result += 6;
        }
        return result;
    }

    @Override
    public Piece getPieceAt(int rank, int file) {
        return byteToPiece(spaces[rank][file]);
    }

    private Piece byteToPiece(byte b) {
        if (b == 0) {
            return null;
        }
        char result = ' ';
        switch (b % 6) {
        case WHITE_BISHOP:
            result = 'B';
            break;
        case WHITE_KING:
            result = 'K';
            break;
        case WHITE_KNIGHT:
            result = 'N';
            break;
        case WHITE_PAWN:
            result = 'P';
            break;
        case WHITE_QUEEN:
            result = 'Q';
            break;
        case 0:
            result = 'R';
            break;
        }
        if (b > 6) {
            result = Character.toLowerCase(result);
        }
        return new Piece(result);
    }

    @Override
    public Coordinate getKingPosition(boolean isWhite) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                byte currentByte = spaces[rank][file];
                if (isWhite && currentByte != 0 &&
                    currentByte == WHITE_KING){
                    return new Coordinate(rank, file);
                }
                if (!isWhite && currentByte != 0 &&
                    currentByte == BLACK_KING){
                    return new Coordinate(rank, file);
                }
            }
        }
        return null;
    }

    public byte[][] copySpaces() {
        byte[][] copy = new byte[8][8];
        for (int index = 0; index < spaces.length; index++) {
            //copy the 8 inner arrays
            copy[index] = spaces[index].clone();
        }
        return copy;
    }

    @Override
    public String toString() {
        String[] spaceStrings = new String[8];
        for (int rank = 0; rank < 8; rank++) {
            String result = "";
            for (int file = 0; file < 8; file++) {
                Piece currentPiece = byteToPiece(spaces[rank][file]);
                if (currentPiece != null) {
                    result += currentPiece.toString();
                } else {
                    result += "0";
                }
            }
            spaceStrings[rank] = result;
        }
         return Arrays.toString(spaceStrings).replace(", ", ",\n").replace("[", "").replace("]", "");
         
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Board) {
            Board otherBoard = (Board) o;
            return this.toString().equals(otherBoard.toString());
        } else {
            return false;
        }
    }

    @Override
    public void setPieceAt(int rank, int file, Piece piece) {
        spaces[rank][file] = pieceToByte(piece);
        return;
    }

    @Override
    public List<Piece> getRank(int rank) {
        List<Piece> result = new ArrayList<>();
        for (int i = 0; i < spaces.length; i++) {
            result.add(byteToPiece(spaces[rank][i]));
        }
        return result;
    }

    @Override
    public Board copyBoard() {
        return new ByteBoard(copySpaces());
    }

    @Override
    public Piece getPieceAt(Coordinate space) {
        return getPieceAt(space.getRank(), space.getFile());
    }

    @Override
    public void setPieceAt(Coordinate space, Piece piece) {
        setPieceAt(space.getRank(), space.getFile(), piece);
    }

    @Override
    public byte getByteAt(Coordinate space) {
        return getByteAt(space.getRank(), space.getFile());
    }

    @Override
    public byte getByteAt(int rank, int file) {
        return spaces[rank][file];
    }

    @Override
    public void setByteAt(Coordinate space, byte b) {
        setByteAt(space.getRank(), space.getFile(), b);
    }

    @Override
    public void setByteAt(int rank, int file, byte b) {
        spaces[rank][file] = b;
    }
}
