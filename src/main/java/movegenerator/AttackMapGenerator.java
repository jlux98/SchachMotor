package movegenerator;

import model.Board;
import model.PieceType;
import static model.PieceEncoding.*;

public abstract class AttackMapGenerator {
    public static boolean[][] computeChecks(Board spaces, boolean isWhite) {
        boolean[][] result = new boolean[8][8];
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

    private static boolean[][] paintAttackBoard(Board spaces, boolean[][] result, int rank, int file,
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

    private static boolean[][] paintRookAttacks(Board spaces, boolean[][] result, int rank, int file) {
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

    private static boolean[][] paintQueenAttacks(Board spaces, boolean[][] result, int rank, int file) {
        result = paintBishopAttacks(spaces, result, rank, file);
        result = paintRookAttacks(spaces, result, rank, file);
        return result;
    }

    private static boolean[][] paintPawnAttacks(Board spaces, boolean[][] result, int rank, int file, boolean color) {
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

    private static boolean[][] paintKnightAttacks(Board spaces, boolean[][] result, int rank, int file) {
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
    public static boolean[][] paintRayAttack (Board spaces, boolean[][] result, int targetRank, int targetFile, int rankSlope, int fileSlope) {
        boolean collision = false;
        if (targetRank < 0 ||
            targetRank > 7 ||
            targetFile < 0 ||
            targetFile > 7) {
            return result;
        }
        if (rankSlope == 0 && fileSlope == 0) {
            result[targetRank][targetFile] = true;
            return result;
        }
        while(isInbounds(targetRank) && isInbounds(targetFile) && (!collision)){
            result[targetRank][targetFile] = true;
            if (spaces.getPieceAt(targetRank, targetFile) != null){
                collision = true;
                break;
            }
            targetRank += rankSlope;
            targetFile += fileSlope;
        }
        return result;
    }

    private static boolean isInbounds (int coordinate){
        return (coordinate >= 0 && coordinate <= 7);
    }

    private static boolean[][] paintBishopAttacks(Board spaces, boolean[][] result, int rank, int file) {
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

    private static boolean[][] paintKingAttacks(Board spaces, boolean[][] result, int rank, int file) {
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
