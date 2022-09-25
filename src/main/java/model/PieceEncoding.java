package model;

public class PieceEncoding {
    public static final byte EMPTY_SQUARE           = 0;
    public static final byte WHITE_BISHOP           = 1;
    public static final byte WHITE_KING             = 2;
    public static final byte WHITE_KNIGHT           = 3;
    public static final byte WHITE_PAWN             = 4;
    public static final byte WHITE_QUEEN            = 5;
    public static final byte WHITE_ROOK             = 6;
    /** Pieces encoded whith a value lower than the threshold are white and 
     * encoded with a higher value than the threshold are black.
     * There is no piece encoded with the value of the threshold. */
    public static final byte EXCLUSIVE_THRESHOLD    = 7;
    public static final byte BLACK_BISHOP           = 8;
    public static final byte BLACK_KING             = 9;
    public static final byte BLACK_KNIGHT           = 10;
    public static final byte BLACK_PAWN             = 11;
    public static final byte BLACK_QUEEN            = 12;
    public static final byte BLACK_ROOK             = 13;
    /**The value added to a white piece to convert it to a black piece of equal
     * type. Subtract it from a black piece to convert it into a white piece
     * of equal type.*/
    public static final byte PIECE_OFFSET           = 7;
    /**The last byte value that represents a valid piece. Bytes with a higher
     * value do not represent valid PieceEncodings.*/
    public static final byte UPPER_LIMIT            = 13;
    /**The first byte value that represents a valid piece. Bytes with a lower
     * value do not represent valid PieceEncodings.*/
    public static final byte LOWER_LIMIT            = 1;

    public static byte switchBytePieceColor(byte piece){
        if (piece == 0){
            return 0;
        }
        if (!isBytePieceValid(piece)){
            throw new IllegalArgumentException("piece must be a value between 0 and "+UPPER_LIMIT);
        }
        if (isBytePieceWhite(piece)){
            return (byte) (piece + PIECE_OFFSET);
        } else {
            return (byte) (piece - PIECE_OFFSET);
        }
    }

    public static boolean isBytePieceWhite(byte piece){
        if (piece == EXCLUSIVE_THRESHOLD){
            throw new IllegalArgumentException("Cannot use this function with the exclusive threshold.");
        }
        if (piece < EXCLUSIVE_THRESHOLD){
            return true;
        } else { 
            return false;
        }
    }

    public static boolean isBytePieceValid(byte piece){
        return (piece >= LOWER_LIMIT && piece <= UPPER_LIMIT);
    }
}
