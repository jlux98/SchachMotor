package movegenerator;

import model.Board;
import model.PieceType;
import static model.PieceEncoding.*;

public abstract class AttackMapGenerator {

    public static boolean[] computeChecks(Board squares, boolean isWhite) {
        boolean[] result = new boolean[64];
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                byte currentPiece = squares.getByteAt(rank, file);
                if (currentPiece != EMPTY_SQUARE && isBytePieceWhite(currentPiece) == isWhite){
                    result = paintAttackBoard(squares, result, rank, file, getBytePieceType(currentPiece),isWhite);
                }
            }
        }
        return result;
    }

    private static boolean[] paintAttackBoard(Board squares, boolean[] result, int rank, int file,
        PieceType type, boolean isWhite) {
        switch(type){
            case BISHOP:
                return paintBishopAttacks(squares, result, rank, file);
            case KING:
                return paintKingAttacks(squares, result, rank, file);
            case KNIGHT:
                return paintKnightAttacks(squares, result, rank, file);
            case PAWN:
                return paintPawnAttacks(squares, result, rank, file, isWhite);
            case QUEEN:
                return paintQueenAttacks(squares, result, rank, file);
            case ROOK:
                return paintRookAttacks(squares, result, rank, file);
            default:
                return result;

        }
    }

    private static boolean[] paintRookAttacks(Board squares, boolean[] result, int rank, int file) {
        // northern attack vector
        result = paintRayAttack(squares, result, rank-1, file, -1, 0);
        // eastern attack vector
        result = paintRayAttack(squares, result, rank, file+1, 0, 1);
        // southern attack vector
        result = paintRayAttack(squares, result, rank+1, file, 1, 0);
        // western attack vector
        result = paintRayAttack(squares, result, rank, file-1, 0, -1);
        return result;
    }

    private static boolean[] paintQueenAttacks(Board squares, boolean[] result, int rank, int file) {
        result = paintBishopAttacks(squares, result, rank, file);
        result = paintRookAttacks(squares, result, rank, file);
        return result;
    }

    private static boolean[] paintPawnAttacks(Board squares, boolean[] result, int rank, int file, boolean color) {
        int sign = 0;
        if (color){
            sign = -1;
        } else {
            sign = 1;
        }
        result = paintRayAttack(squares, result, rank+(sign*1), file+1, 0, 0);
        result = paintRayAttack(squares, result, rank+(sign*1), file-1, 0, 0);
        return result;
    }

    private static boolean[] paintKnightAttacks(Board squares, boolean[] result, int rank, int file) {
        result = paintRayAttack(squares, result, rank-2, file-1, 0, 0);
        result = paintRayAttack(squares, result, rank-2, file+1, 0, 0);
        result = paintRayAttack(squares, result, rank-1, file+2, 0, 0);
        result = paintRayAttack(squares, result, rank+1, file+2, 0, 0);
        result = paintRayAttack(squares, result, rank+2, file+1, 0, 0);
        result = paintRayAttack(squares, result, rank+2, file-1, 0, 0);
        result = paintRayAttack(squares, result, rank+1, file-2, 0, 0);
        result = paintRayAttack(squares, result, rank-1, file-2, 0, 0);
        return result;
    }

    /**
     * A method that gets a starting square and marks the line eminating from
     * that square defined by the slope arguments in a 2d-Array
     * @param result the array in which to mark a line
     * @param targetRank the horizontal coordinate of the starting square
     * @param targetFile the vertical coordinate of the starting square
     * @param rankSlope the vertical variance of the line
     * @param fileSlope the horizontal variance of the line
     * @return the array with the line marked
     */
    public static boolean[] paintRayAttack (Board squares, boolean[] result, int targetRank, int targetFile, int rankSlope, int fileSlope) {
        boolean collision = false;
        if (targetRank < 0 ||
            targetRank > 7 ||
            targetFile < 0 ||
            targetFile > 7) {
            return result;
        }
        if (rankSlope == 0 && fileSlope == 0) {
            result[targetRank*8+targetFile] = true;
            return result;
        }
        while(isInbounds(targetRank) && isInbounds(targetFile) && (!collision)){
            result[targetRank*8+targetFile] = true;
            if (squares.getByteAt(targetRank, targetFile) != EMPTY_SQUARE){
                collision = true;
                break;
            }
            targetRank += rankSlope;
            targetFile += fileSlope;
        }
        return result;
    }

    private static boolean[] paintBishopAttacks(Board squares, boolean[] result, int rank, int file) {
        // Northeastern attack vector
        result = paintRayAttack(squares, result, rank-1, file+1, -1, 1);
        // Southeastern attack vector
        result = paintRayAttack(squares, result, rank+1, file+1, 1, 1);
        // Southwestern attack vector
        result = paintRayAttack(squares, result, rank+1, file-1, 1, -1);
        // Northwestern attack vector
        result = paintRayAttack(squares, result, rank-1, file-1, -1, -1);
        return result;
    }

    private static boolean[] paintKingAttacks(Board squares, boolean[] result, int rank, int file) {
         // northern attack vector
         result = paintRayAttack(squares, result, rank-1, file, 0, 0);
         // eastern attack vector
         result = paintRayAttack(squares, result, rank, file+1, 0, 0);
         // southern attack vector
         result = paintRayAttack(squares, result, rank+1, file, 0, 0);
         // western attack vector
         result = paintRayAttack(squares, result, rank, file-1, 0, 0);
         // Northeastern attack vector
        result = paintRayAttack(squares, result, rank-1, file+1, 0, 0);
        // Southeastern attack vector
        result = paintRayAttack(squares, result, rank+1, file+1, 0, 0);
        // Southwestern attack vector
        result = paintRayAttack(squares, result, rank+1, file-1, 0, 0);
        // Northwestern attack vector
        result = paintRayAttack(squares, result, rank-1, file-1, 0, 0);
         return result;
    }

    // -------------------------ByteEncoded------------------------------------

    public static byte[] computeChecksByteEncoded(Board squares, boolean isWhite) {
        byte[] result = new byte[8];
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                byte currentPiece = squares.getByteAt(rank, file);
                if (currentPiece != EMPTY_SQUARE && isBytePieceWhite(currentPiece) == isWhite){
                    result = paintAttackBoardByteEncoded(squares, result, rank, file, getBytePieceType(currentPiece),isWhite);
                }
            }
        }
        return result;
    }

    private static byte[] paintAttackBoardByteEncoded(Board squares, byte[] result, int rank, int file,
        PieceType type, boolean isWhite) {
        switch(type){
            case BISHOP:
                return paintBishopAttacksByteEncoded(squares, result, rank, file);
            case KING:
                return paintKingAttacksByteEncoded(squares, result, rank, file);
            case KNIGHT:
                return paintKnightAttacksByteEncoded(squares, result, rank, file);
            case PAWN:
                return paintPawnAttacksByteEncoded(squares, result, rank, file, isWhite);
            case QUEEN:
                return paintQueenAttacksByteEncoded(squares, result, rank, file);
            case ROOK:
                return paintRookAttacksByteEncoded(squares, result, rank, file);
            default:
                return result;

        }
    }

    private static byte[] paintRookAttacksByteEncoded(Board squares, byte[] result, int rank, int file) {
        // northern attack vector
        result = paintRayAttack(squares, result, rank-1, file, -1, 0);
        // eastern attack vector
        result = paintRayAttack(squares, result, rank, file+1, 0, 1);
        // southern attack vector
        result = paintRayAttack(squares, result, rank+1, file, 1, 0);
        // western attack vector
        result = paintRayAttack(squares, result, rank, file-1, 0, -1);
        return result;
    }

    private static byte[] paintQueenAttacksByteEncoded(Board squares, byte[] result, int rank, int file) {
        result = paintBishopAttacksByteEncoded(squares, result, rank, file);
        result = paintRookAttacksByteEncoded(squares, result, rank, file);
        return result;
    }

    private static byte[] paintPawnAttacksByteEncoded(Board squares, byte[] result, int rank, int file, boolean color) {
        int sign = 0;
        if (color){
            sign = -1;
        } else {
            sign = 1;
        }
        result = paintRayAttack(squares, result, rank+(sign*1), file+1, 0, 0);
        result = paintRayAttack(squares, result, rank+(sign*1), file-1, 0, 0);
        return result;
    }

    private static byte[] paintKnightAttacksByteEncoded(Board squares, byte[] result, int rank, int file) {
        result = paintRayAttack(squares, result, rank-2, file-1, 0, 0);
        result = paintRayAttack(squares, result, rank-2, file+1, 0, 0);
        result = paintRayAttack(squares, result, rank-1, file+2, 0, 0);
        result = paintRayAttack(squares, result, rank+1, file+2, 0, 0);
        result = paintRayAttack(squares, result, rank+2, file+1, 0, 0);
        result = paintRayAttack(squares, result, rank+2, file-1, 0, 0);
        result = paintRayAttack(squares, result, rank+1, file-2, 0, 0);
        result = paintRayAttack(squares, result, rank-1, file-2, 0, 0);
        return result;
    }

    /**
     * A method that gets a starting square and marks the line eminating from
     * that square defined by the slope arguments in a 2d-Array
     * @param result the array in which to mark a line
     * @param targetRank the horizontal coordinate of the starting square
     * @param targetFile the vertical coordinate of the starting square
     * @param rankSlope the vertical variance of the line
     * @param fileSlope the horizontal variance of the line
     * @return the array with the line marked
     */
    public static byte[] paintRayAttack (Board square, byte[] result, int targetRank, int targetFile, int rankSlope, int fileSlope) {
        boolean collision = false;
        if (targetRank < 0 ||
            targetRank > 7 ||
            targetFile < 0 ||
            targetFile > 7) {
            return result;
        }
        if (rankSlope == 0 && fileSlope == 0) {
            setBitToTrue(targetRank, targetFile,result);
            return result;
        }
        while(isInbounds(targetRank) && isInbounds(targetFile) && (!collision)){
            setBitToTrue(targetRank, targetFile, result);
            if (square.getByteAt(targetRank, targetFile) != EMPTY_SQUARE){
                collision = true;
                break;
            }
            targetRank += rankSlope;
            targetFile += fileSlope;
        }
        return result;
    }

    private static void setBitToTrue(int rank, int file, byte[] result){
        int mask = (1 << 7-file);
        result[rank] = (byte) ((result[rank] | mask) & 0b11111111);
    }

    public static boolean getBoolFromByte(int rank, int file, byte[] bytes){
        int mask = (1 << 7-file);
        byte result = (byte) ((bytes[rank] & mask) & 0b11111111);
        return result != 0;
    }

    private static boolean isInbounds (int coordinate){
        return (coordinate >= 0 && coordinate <= 7);
    }

    private static byte[] paintBishopAttacksByteEncoded(Board squares, byte[] result, int rank, int file) {
        // Northeastern attack vector
        result = paintRayAttack(squares, result, rank-1, file+1, -1, 1);
        // Southeastern attack vector
        result = paintRayAttack(squares, result, rank+1, file+1, 1, 1);
        // Southwestern attack vector
        result = paintRayAttack(squares, result, rank+1, file-1, 1, -1);
        // Northwestern attack vector
        result = paintRayAttack(squares, result, rank-1, file-1, -1, -1);
        return result;
    }

    private static byte[] paintKingAttacksByteEncoded(Board squares, byte[] result, int rank, int file) {
         // northern attack vector
         result = paintRayAttack(squares, result, rank-1, file, 0, 0);
         // eastern attack vector
         result = paintRayAttack(squares, result, rank, file+1, 0, 0);
         // southern attack vector
         result = paintRayAttack(squares, result, rank+1, file, 0, 0);
         // western attack vector
         result = paintRayAttack(squares, result, rank, file-1, 0, 0);
         // Northeastern attack vector
        result = paintRayAttack(squares, result, rank-1, file+1, 0, 0);
        // Southeastern attack vector
        result = paintRayAttack(squares, result, rank+1, file+1, 0, 0);
        // Southwestern attack vector
        result = paintRayAttack(squares, result, rank+1, file-1, 0, 0);
        // Northwestern attack vector
        result = paintRayAttack(squares, result, rank-1, file-1, 0, 0);
         return result;
    }
}
