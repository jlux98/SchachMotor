package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static model.PieceEncoding.*;

public class ByteBoard implements Board {
    private byte[] spaces;
    private byte blackKingSpace = -1;
    private byte whiteKingSpace = -1;

    public ByteBoard(Piece[][] spaces) {
        byte[] result = new byte[64];
        this.spaces = result;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                setByteAt(i, j, pieceToByte(spaces[i][j]));
            }
        }
    }

    public ByteBoard(byte[] spaces) {
        this.spaces = spaces;
    }

    /**
     * Instantiates an empty board.
     * @return a new instance
     */
    public static Board createEmpty() {
        return new ByteBoard(new byte[64]);
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
            result += PIECE_OFFSET;
        }
        return result;
    }

    @Override
    public Piece getPieceAt(int rank, int file) {
        return byteToPiece(spaces[rank*8+file]);
    }

    private Piece byteToPiece(byte b) {
        if (b == 0) {
            return null;
        }
        char result = ' ';
        switch (b % PIECE_OFFSET) {
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
        case WHITE_ROOK:
            result = 'R';
            break;
        default:
        }
        if (b > EXCLUSIVE_THRESHOLD) {
            result = Character.toLowerCase(result);
        }
        return new Piece(result);
    }

    @Override
    public Coordinate getKingPosition(boolean isWhite) {
        if (isWhite && whiteKingSpace >= 0){
            if (spaces[whiteKingSpace] == WHITE_KING){
                return new Coordinate(whiteKingSpace/8, whiteKingSpace%8);
            }
        } else if (!isWhite && blackKingSpace >= 0){
            if (spaces[blackKingSpace] == BLACK_KING){
                return new Coordinate(blackKingSpace/8, blackKingSpace%8);
            }
        }
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                byte currentByte = spaces[rank*8+file];
                if (isWhite && currentByte != 0 &&
                    currentByte == WHITE_KING){
                    whiteKingSpace = (byte)(rank*8+file);
                    return new Coordinate(rank, file);
                }
                if (!isWhite && currentByte != 0 &&
                    currentByte == BLACK_KING){
                    blackKingSpace = (byte)(rank*8+file);
                    return new Coordinate(rank, file);
                }
            }
        }
        return null;
    }

    public byte[] copySpaces() {
        return spaces.clone();
    }

    @Override
    public String toString() {
        String[] spaceStrings = new String[8];
        for (int rank = 0; rank < 8; rank++) {
            String result = "";
            for (int file = 0; file < 8; file++) {
                byte currentPiece = getByteAt(rank, file);
                if (currentPiece != EMPTY_SQUARE) {
                    result += getCharacterFromBytePiece(currentPiece);
                } else {
                    result += "0";
                }
            }
            spaceStrings[rank] = result;
        }
        return Arrays.toString(spaceStrings).replace(", ", ",\n");
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
        setByteAt(rank, file, pieceToByte(piece));
        return;
    }

    @Override
    public List<Piece> getRank(int rank) {
        List<Piece> result = new ArrayList<>();
        for (int file = 0; file < spaces.length; file++) {
            result.add(byteToPiece(spaces[rank*8+file]));
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
        return spaces[rank*8+file];
    }

    @Override
    public void setByteAt(Coordinate space, byte b) {
        setByteAt(space.getRank(), space.getFile(), b);
    }

    @Override
    public void setByteAt(int rank, int file, byte b) {
        spaces[rank*8+file] = b;
        if (b == WHITE_KING){
            whiteKingSpace = (byte)(rank*8+file);
        } else if (b == BLACK_KING){
            whiteKingSpace = (byte)(rank*8+file);
        }
    }

    @Override
    public String toStringFen() {
        String result = "";
        for (int rank = 0; rank < 8; rank ++){
            int countEmpty = 0;
            for (int file = 0; file < 8; file++){
                if (getByteAt(rank, file) == 0){
                    countEmpty++;
                } else {
                    result += printEmpty(countEmpty);
                    countEmpty = 0;
                    result += getPieceAt(rank, file).toString();
                }
            }
            result += printEmpty(countEmpty);
            if (rank < 7){
                result += "/";
            }
        }
        return result;
    }

    private String printEmpty(int countEmpty) {
        if (countEmpty == 0){
            return "";
        } else {
            return "" + countEmpty;
        }
    }
}
