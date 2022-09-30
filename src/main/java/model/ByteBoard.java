package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static model.PieceEncoding.*;

public class ByteBoard implements Board {
    private byte[] spaces;

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
        return byteToPiece(getByteAt(rank, file));
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
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                byte currentByte = getByteAt(rank,file);
                if (isWhite && currentByte != 0 &&
                    currentByte == WHITE_KING){
                    // whiteKingSpace = (byte)(rank*8+file);
                    return new Coordinate(rank, file);
                }
                if (!isWhite && currentByte != 0 &&
                    currentByte == BLACK_KING){
                    // blackKingSpace = (byte)(rank*8+file);
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
        StringBuilder spaceStrings = new StringBuilder();
        spaceStrings.append('\n');
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                byte currentPiece = getByteAt(rank, file);
                if (currentPiece != EMPTY_SQUARE) {
                    spaceStrings.append(getCharacterFromBytePiece(currentPiece));
                } else {
                    spaceStrings.append('0');
                }
            }
            spaceStrings.append('\n');
        }
        return spaceStrings.toString();
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
            result.add(byteToPiece(getByteAt(rank, file)));
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
        boolean leftBits = file % 2 == 0;
        byte piece = spaces[rank*8+file/2];
        if (leftBits){
            piece = (byte) (piece >>> 4);
        } 
        piece = (byte) (piece & (byte) 0b00001111);
        return piece;
    }

    @Override
    public void setByteAt(Coordinate space, byte b) {
        setByteAt(space.getRank(), space.getFile(), b);
    }

    @Override
    public void setByteAt(int rank, int file, byte b) {

        boolean leftBits = file % 2 == 0;
        
        byte piece = b;
        byte resetMask;
        // reset current value at that space
        if (leftBits){
            resetMask = (byte) 0b00001111;
            piece = (byte) (piece << 4);
        } else {
            resetMask = (byte) 0b11110000;
        }
        spaces[rank*8+file/2] = (byte)(spaces[rank*8+file/2] & resetMask);
        spaces[rank*8+file/2] = (byte)(spaces[rank*8+file/2] | piece);

        // if (piece == WHITE_KING){
        //     whiteKingSpace = (byte)(rank*8+file/2);
        // } else if (piece == BLACK_KING){
        //     whiteKingSpace = (byte)(rank*8+file/2);
        // }
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
                    result += byteToPiece(getByteAt(rank, file));
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
