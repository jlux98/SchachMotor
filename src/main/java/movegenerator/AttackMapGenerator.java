package movegenerator;

import model.Board;
import model.PieceType;
import static model.PieceEncoding.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AttackMapGenerator {

    public static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);

    public static void shutDownThreads() {
        executor.shutdown();
    }

    public static boolean[] computeChecks(Board spaces, boolean isWhite) {
        boolean[] result = new boolean[64];
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                byte currentPiece = spaces.getByteAt(rank, file);
                if (currentPiece != EMPTY_SQUARE && isBytePieceWhite(currentPiece) == isWhite){
                    result = paintAttackBoard(spaces, result, rank, file, getBytePieceType(currentPiece),isWhite);
                }
            }
        }
        return result;
    }

    private static boolean[] paintAttackBoard(Board spaces, boolean[] result, int rank, int file,
        PieceType type, boolean isWhite) {
        switch(type){
            case BISHOP:
                return paintBishopAttacks(spaces, result, rank, file);
            case KING:
                return paintKingAttacks(spaces, result, rank, file);
            case KNIGHT:
                return paintKnightAttacks(spaces, result, rank, file);
            case PAWN:
                return paintPawnAttacks(spaces, result, rank, file, isWhite);
            case QUEEN:
                return paintQueenAttacks(spaces, result, rank, file);
            case ROOK:
                return paintRookAttacks(spaces, result, rank, file);
            default:
                return result;

        }
    }

    private static boolean[] paintRookAttacks(Board spaces, boolean[] result, int rank, int file) {
        // northern attack vector
        result = paintRayAttack(spaces, result, rank-1, file, -1, 0);
        // eastern attack vector
        result = paintRayAttack(spaces, result, rank, file+1, 0, 1);
        // southern attack vector
        result = paintRayAttack(spaces, result, rank+1, file, 1, 0);
        // western attack vector
        result = paintRayAttack(spaces, result, rank, file-1, 0, -1);
        return result;
    }

    private static boolean[] paintQueenAttacks(Board spaces, boolean[] result, int rank, int file) {
        result = paintBishopAttacks(spaces, result, rank, file);
        result = paintRookAttacks(spaces, result, rank, file);
        return result;
    }

    private static boolean[] paintPawnAttacks(Board spaces, boolean[] result, int rank, int file, boolean color) {
        int sign = 0;
        if (color){
            sign = -1;
        } else {
            sign = 1;
        }
        result = paintRayAttack(spaces, result, rank+(sign*1), file+1, 0, 0);
        result = paintRayAttack(spaces, result, rank+(sign*1), file-1, 0, 0);
        return result;
    }

    private static boolean[] paintKnightAttacks(Board spaces, boolean[] result, int rank, int file) {
        result = paintRayAttack(spaces, result, rank-2, file-1, 0, 0);
        result = paintRayAttack(spaces, result, rank-2, file+1, 0, 0);
        result = paintRayAttack(spaces, result, rank-1, file+2, 0, 0);
        result = paintRayAttack(spaces, result, rank+1, file+2, 0, 0);
        result = paintRayAttack(spaces, result, rank+2, file+1, 0, 0);
        result = paintRayAttack(spaces, result, rank+2, file-1, 0, 0);
        result = paintRayAttack(spaces, result, rank+1, file-2, 0, 0);
        result = paintRayAttack(spaces, result, rank-1, file-2, 0, 0);
        return result;
    }

    /**
     * A method that gets a starting space and marks the line eminating from
     * that space defined by the slope arguments in a 2d-Array
     * @param result the array in which to mark a line
     * @param targetRank the horizontal coordinate of the starting space
     * @param targetFile the vertical coordinate of the starting space
     * @param rankSlope the vertical variance of the line
     * @param fileSlope the horizontal variance of the line
     * @return the array with the line marked
     */
    public static boolean[] paintRayAttack (Board spaces, boolean[] result, int targetRank, int targetFile, int rankSlope, int fileSlope) {
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
            if (spaces.getByteAt(targetRank, targetFile) != EMPTY_SQUARE){
                collision = true;
                break;
            }
            targetRank += rankSlope;
            targetFile += fileSlope;
        }
        return result;
    }

    private static boolean[] paintBishopAttacks(Board spaces, boolean[] result, int rank, int file) {
        // Northeastern attack vector
        result = paintRayAttack(spaces, result, rank-1, file+1, -1, 1);
        // Southeastern attack vector
        result = paintRayAttack(spaces, result, rank+1, file+1, 1, 1);
        // Southwestern attack vector
        result = paintRayAttack(spaces, result, rank+1, file-1, 1, -1);
        // Northwestern attack vector
        result = paintRayAttack(spaces, result, rank-1, file-1, -1, -1);
        return result;
    }

    private static boolean[] paintKingAttacks(Board spaces, boolean[] result, int rank, int file) {
         // northern attack vector
         result = paintRayAttack(spaces, result, rank-1, file, 0, 0);
         // eastern attack vector
         result = paintRayAttack(spaces, result, rank, file+1, 0, 0);
         // southern attack vector
         result = paintRayAttack(spaces, result, rank+1, file, 0, 0);
         // western attack vector
         result = paintRayAttack(spaces, result, rank, file-1, 0, 0);
         // Northeastern attack vector
        result = paintRayAttack(spaces, result, rank-1, file+1, 0, 0);
        // Southeastern attack vector
        result = paintRayAttack(spaces, result, rank+1, file+1, 0, 0);
        // Southwestern attack vector
        result = paintRayAttack(spaces, result, rank+1, file-1, 0, 0);
        // Northwestern attack vector
        result = paintRayAttack(spaces, result, rank-1, file-1, 0, 0);
         return result;
    }

    // -------------------------ByteEncoded------------------------------------

    // public static byte[] computeChecksByteEncoded(Board spaces, boolean isWhite) {
    //     byte[] result = new byte[8];
    //     for (int rank = 0; rank < 8; rank++){
    //         for (int file = 0; file < 8; file++){
    //             byte currentPiece = spaces.getByteAt(rank, file);
    //             if (currentPiece != EMPTY_SQUARE && isBytePieceWhite(currentPiece) == isWhite){
    //                 result = paintAttackBoardByteEncoded(spaces, result, rank, file, getBytePieceType(currentPiece),isWhite);
    //             }
    //         }
    //     }
    //     return result;
    // }

    public static byte[] computeChecksByteEncoded(Board spaces, boolean isWhite) {
        byte[] result = new byte[8];
        List<Future<?>> futureList = new ArrayList<>();
        List<byte[]> tempResultList = new ArrayList<>();
        for (int rank = 0; rank < 8; rank++) {
            byte[] tempResults = new byte[8];
            Future<?> f = generateAttackMapsPerRow(spaces, rank, tempResults, isWhite);
            if (f != null) {
                futureList.add(f);
                tempResultList.add(tempResults);
            }
        }
        for (int i = 0; i < futureList.size(); i++){
            if (futureList.get(i) != null){
                try {
                    futureList.get(i).get();
                    addTempResultsToResult(result,tempResultList.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalArgumentException();
            }
        }

        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                byte currentPiece = spaces.getByteAt(rank, file);
                if (currentPiece != EMPTY_SQUARE && isBytePieceWhite(currentPiece) == isWhite){
                    result = paintAttackBoardByteEncoded(spaces, result, rank, file, getBytePieceType(currentPiece),isWhite);
                }
            }
        }
        return result;
    }

    private static void addTempResultsToResult(byte[] result, byte[] tempResult) {
        for (int rank = 0; rank < 8; rank++){
            // for (int file = 0; file < 8; file++){
            //     if (getBoolFromByte(rank, file, tempResult)){
            //         setBitToTrue(rank, file, result);
            //     }
            // }
            result[rank] = (byte) (result[rank] | tempResult[rank]);
        }
    }


    private static Future<?> generateAttackMapsPerRow(Board spaces, int rank, byte[] tempResults, boolean isWhite) {
        Future<?> f = executor.submit(new RowAttackMapGenerator(spaces, rank, tempResults, isWhite));
        return f;
    }

    public static byte[] paintAttackBoardByteEncoded(Board spaces, byte[] result, int rank, int file,
        PieceType type, boolean isWhite) {
        switch(type){
            case BISHOP:
                return paintBishopAttacksByteEncoded(spaces, result, rank, file);
            case KING:
                return paintKingAttacksByteEncoded(spaces, result, rank, file);
            case KNIGHT:
                return paintKnightAttacksByteEncoded(spaces, result, rank, file);
            case PAWN:
                return paintPawnAttacksByteEncoded(spaces, result, rank, file, isWhite);
            case QUEEN:
                return paintQueenAttacksByteEncoded(spaces, result, rank, file);
            case ROOK:
                return paintRookAttacksByteEncoded(spaces, result, rank, file);
            default:
                return result;

        }
    }

    private static byte[] paintRookAttacksByteEncoded(Board spaces, byte[] result, int rank, int file) {
        // northern attack vector
        result = paintRayAttack(spaces, result, rank-1, file, -1, 0);
        // eastern attack vector
        result = paintRayAttack(spaces, result, rank, file+1, 0, 1);
        // southern attack vector
        result = paintRayAttack(spaces, result, rank+1, file, 1, 0);
        // western attack vector
        result = paintRayAttack(spaces, result, rank, file-1, 0, -1);
        return result;
    }

    private static byte[] paintQueenAttacksByteEncoded(Board spaces, byte[] result, int rank, int file) {
        result = paintBishopAttacksByteEncoded(spaces, result, rank, file);
        result = paintRookAttacksByteEncoded(spaces, result, rank, file);
        return result;
    }

    private static byte[] paintPawnAttacksByteEncoded(Board spaces, byte[] result, int rank, int file, boolean color) {
        int sign = 0;
        if (color){
            sign = -1;
        } else {
            sign = 1;
        }
        result = paintRayAttack(spaces, result, rank+(sign*1), file+1, 0, 0);
        result = paintRayAttack(spaces, result, rank+(sign*1), file-1, 0, 0);
        return result;
    }

    private static byte[] paintKnightAttacksByteEncoded(Board spaces, byte[] result, int rank, int file) {
        result = paintRayAttack(spaces, result, rank-2, file-1, 0, 0);
        result = paintRayAttack(spaces, result, rank-2, file+1, 0, 0);
        result = paintRayAttack(spaces, result, rank-1, file+2, 0, 0);
        result = paintRayAttack(spaces, result, rank+1, file+2, 0, 0);
        result = paintRayAttack(spaces, result, rank+2, file+1, 0, 0);
        result = paintRayAttack(spaces, result, rank+2, file-1, 0, 0);
        result = paintRayAttack(spaces, result, rank+1, file-2, 0, 0);
        result = paintRayAttack(spaces, result, rank-1, file-2, 0, 0);
        return result;
    }

    /**
     * A method that gets a starting space and marks the line eminating from
     * that space defined by the slope arguments in a 2d-Array
     * @param result the array in which to mark a line
     * @param targetRank the horizontal coordinate of the starting space
     * @param targetFile the vertical coordinate of the starting space
     * @param rankSlope the vertical variance of the line
     * @param fileSlope the horizontal variance of the line
     * @return the array with the line marked
     */
    public static byte[] paintRayAttack (Board spaces, byte[] result, int targetRank, int targetFile, int rankSlope, int fileSlope) {
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
            if (spaces.getByteAt(targetRank, targetFile) != EMPTY_SQUARE){
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

    private static byte[] paintBishopAttacksByteEncoded(Board spaces, byte[] result, int rank, int file) {
        // Northeastern attack vector
        result = paintRayAttack(spaces, result, rank-1, file+1, -1, 1);
        // Southeastern attack vector
        result = paintRayAttack(spaces, result, rank+1, file+1, 1, 1);
        // Southwestern attack vector
        result = paintRayAttack(spaces, result, rank+1, file-1, 1, -1);
        // Northwestern attack vector
        result = paintRayAttack(spaces, result, rank-1, file-1, -1, -1);
        return result;
    }

    private static byte[] paintKingAttacksByteEncoded(Board spaces, byte[] result, int rank, int file) {
         // northern attack vector
         result = paintRayAttack(spaces, result, rank-1, file, 0, 0);
         // eastern attack vector
         result = paintRayAttack(spaces, result, rank, file+1, 0, 0);
         // southern attack vector
         result = paintRayAttack(spaces, result, rank+1, file, 0, 0);
         // western attack vector
         result = paintRayAttack(spaces, result, rank, file-1, 0, 0);
         // Northeastern attack vector
        result = paintRayAttack(spaces, result, rank-1, file+1, 0, 0);
        // Southeastern attack vector
        result = paintRayAttack(spaces, result, rank+1, file+1, 0, 0);
        // Southwestern attack vector
        result = paintRayAttack(spaces, result, rank+1, file-1, 0, 0);
        // Northwestern attack vector
        result = paintRayAttack(spaces, result, rank-1, file-1, 0, 0);
         return result;
    }
}
