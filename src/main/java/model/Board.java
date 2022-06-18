package model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Class representing the game state.
 * <br><br>
 * It stores the position of the pieces, the players' castling ability, whose turn it is, if a player is in check
 * and if a pawn may be captured en passant
 * (= all of the information denoted by a fen string plus whether a player is in check).
 * <br><br>
 * <b>Note:</b>
 * The array element at [0][0] represents the space a8, while [7][7] represents h1.
 */
public class Board {

    /**
     * The array element at [0][0] represents the space a8, [7][7] represents h1.
     */
    private Piece[][] spaces;
    private int pointValue;
    private boolean whiteInCheck;
    private boolean blackInCheck;
    private boolean whiteNextMove;
    private boolean whiteCastlingKingside;
    private boolean whiteCastlingQueenside;
    private boolean blackCastlingKingside;
    private boolean blackCastlingQueenside;
    private int enPassantTargetRank;
    private int enPassantTargetFile;
    private int halfMovesSincePawnMoveOrCapture;
    private int fullMoveCount;
    private boolean[][] attackedByWhite;
    private boolean[][] attackedByBlack;

    /**
    * Like {@link #Board(int, Optional[][], boolean, boolean, boolean, boolean, boolean, boolean, boolean, int, int)}
    * but without requiring point value and whiteInCheck / blackInCheck to be set.
    * These value may be set using the corresponding setter at a later time.
    */
    public Board(Piece[][] spaces, boolean whiteNextMove, boolean whiteCastlingKingside, boolean whiteCastlingQueenside,
            boolean blackCastlingKingside, boolean blackCastlingQueenside, int enPassantTargetRank, int enPassantTargetFile,
            int halfMoves, int fullMoves) {
       if (spaces == null) {
            throw new NullPointerException("spaces array may not be null");
        } 
        // en passant target coordiantes of -1 are used if no en passant captures are possbile
        /* if (enPassantTargetRank <= 0 || enPassantTargetFile <= 0) {
            throw new IllegalArgumentException("coordinates must be greater than 0");
        } */
        if (enPassantTargetRank > 8 || enPassantTargetFile > 8) {
            throw new IllegalArgumentException("coordinates must be less than 8");
        }
        if (halfMoves < 0) {
            throw new IllegalArgumentException("half move count must be greater than 0");
        }
        if (fullMoves < 1) {
            //full move counter starts at 1
            throw new IllegalArgumentException("full move count must be greater than 1");
        }
        this.spaces = spaces;
        this.whiteNextMove = whiteNextMove;
        this.whiteCastlingKingside = whiteCastlingKingside;
        this.whiteCastlingQueenside = whiteCastlingQueenside;
        this.blackCastlingKingside = blackCastlingKingside;
        this.blackCastlingQueenside = blackCastlingQueenside;
        this.enPassantTargetRank = enPassantTargetRank;
        this.enPassantTargetFile = enPassantTargetFile;
        this.halfMovesSincePawnMoveOrCapture = halfMoves;
        this.fullMoveCount = fullMoves;
        this.attackedByWhite = computeChecks(true);
        this.attackedByBlack = computeChecks(false);
        Coordinate whiteKing = getKingPosition(true);
        this.whiteInCheck = attackedByBlack[whiteKing.getRank()][whiteKing.getFile()];
        Coordinate blackKing = getKingPosition(false);
        this.blackInCheck = attackedByWhite[blackKing.getRank()][blackKing.getFile()];
    }

    public boolean[][] computeChecks(Boolean isWhite) {
        //TODO: Implement a method for autonomously tracking which side is in check
        boolean[][] result = new boolean[8][8];
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                Piece currentPiece = spaces[rank][file];
                if (currentPiece != null && currentPiece.getIsWhite() == isWhite){
                    result = paintAttackBoard(result, rank, file, currentPiece.getPieceType(),isWhite);
                }
            }
        }
        return result;
    }

    private boolean[][] paintAttackBoard(boolean[][] result, int rank, int file,
        PieceType type, boolean isWhite) {
        switch(type){
            case BISHOP:
                return paintBishopAttacks(result, rank, file);
            case KING:
                return paintKingAttacks(result, rank, file);
            case KNIGHT:
                return paintKnightAttacks(result, rank, file);
            case PAWN:
                return paintPawnAttacks(result, rank, file, isWhite);
            case QUEEN:
                return paintQueenAttacks(result, rank, file);
            case ROOK:
                return paintRookAttacks(result, rank, file);
            default:
                return result;

        }
    }

    private boolean[][] paintRookAttacks(boolean[][] result, int rank, int file) {
        // northern attack vector
        result = paintRayAttack(result, rank-1, file, -1, 0);
        // eastern attack vector
        result = paintRayAttack(result, rank, file+1, 0, 1);
        // southern attack vector
        result = paintRayAttack(result, rank+1, file, 1, 0);
        // western attack vector
        result = paintRayAttack(result, rank, file-1, 0, -1);
        return result;
    }

    private boolean[][] paintQueenAttacks(boolean[][] result, int rank, int file) {
        result = paintBishopAttacks(result, rank, file);
        result = paintRookAttacks(result, rank, file);
        return result;
    }

    private boolean[][] paintPawnAttacks(boolean[][] result, int rank, int file, boolean color) {
        int sign = 0;
        if (color){
            sign = -1;
        } else {
            sign = 1;
        }
        result = paintRayAttack(result, rank+(sign*1), file+1, 0, 0);
        result = paintRayAttack(result, rank+(sign*1), file-1, 0, 0);
        return result;
    }

    private boolean[][] paintKnightAttacks(boolean[][] result, int rank, int file) {
        result = paintRayAttack(result, rank-2, file-1, 0, 0);
        result = paintRayAttack(result, rank-2, file+1, 0, 0);
        result = paintRayAttack(result, rank-1, file+2, 0, 0);
        result = paintRayAttack(result, rank+1, file+2, 0, 0);
        result = paintRayAttack(result, rank+2, file+1, 0, 0);
        result = paintRayAttack(result, rank+2, file-1, 0, 0);
        result = paintRayAttack(result, rank+1, file-2, 0, 0);
        result = paintRayAttack(result, rank-1, file-2, 0, 0);
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
    public boolean[][] paintRayAttack (boolean[][] result, int targetRank, int targetFile, int rankSlope, int fileSlope) {
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
        for (int i = 0; (i > -1) && (i < 8) && (!collision); i++){
            for (int j = 0; (j > -1) && (j < 8) && (!collision); j++){
                if (i == targetRank &&
                    j == targetFile){
                    result[i][j] = true;
                    targetRank += rankSlope;
                    targetFile += fileSlope;
                    if (spaces[i][j] != null){
                        collision = true;
                        break;
                    }
                }
            }
        }    
        return result;
    }

    private boolean[][] paintBishopAttacks(boolean[][] result, int rank, int file) {
        // Northeastern attack vector
        result = paintRayAttack(result, rank-1, file+1, -1, 1);
        // Southeastern attack vector
        result = paintRayAttack(result, rank+1, file+1, 1, 1);
        // Southwestern attack vector
        result = paintRayAttack(result, rank+1, file-1, 1, -1);
        // Northwestern attack vector
        result = paintRayAttack(result, rank-1, file-1, -1, -1);
        return result;
    }

    private boolean[][] paintKingAttacks(boolean[][] result, int rank, int file) {
         // northern attack vector
         result = paintRayAttack(result, rank-1, file, 0, 0);
         // eastern attack vector
         result = paintRayAttack(result, rank, file+1, 0, 0);
         // southern attack vector
         result = paintRayAttack(result, rank+1, file, 0, 0);
         // western attack vector
         result = paintRayAttack(result, rank, file-1, 0, 0);
         // Northeastern attack vector
        result = paintRayAttack(result, rank-1, file+1, 0, 0);
        // Southeastern attack vector
        result = paintRayAttack(result, rank+1, file+1, 0, 0);
        // Southwestern attack vector
        result = paintRayAttack(result, rank+1, file-1, 0, 0);
        // Northwestern attack vector
        result = paintRayAttack(result, rank-1, file-1, 0, 0);
         return result;
    }

    /**
     * Like {@link #Board(int, Optional[][], boolean, boolean, boolean, boolean, boolean, boolean, boolean, int, int)}
     * but without requiring a point value to be set.
     * The value may be set using Board.setPointValue() at a later time.
     */
    public Board(boolean whiteInCheck, boolean blackInCheck, Piece[][] spaces, boolean whiteNextMove,
            boolean whiteCastlingKingside, boolean whiteCastlingQueenside, boolean blackCastlingKingside,
            boolean blackCastlingQueenside, int enPassantTargetRank, int enPassantTargetFile, int halfMoves, int fullMoves) {
        this(spaces, whiteNextMove, whiteCastlingKingside, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside,
                enPassantTargetRank, enPassantTargetFile, halfMoves, fullMoves);
        this.whiteInCheck = whiteInCheck;
        this.blackInCheck = blackInCheck;
    }

    /**
     * Creates a new board with the specified values.
     * @param pointValue the boards evaluated value
     * @param spaces two dimensional array holding the chess pieces, represents the chess position
     * @param whiteNextMove whether the enxt turn is white's
     * @param whiteInCheck whether white is in checks
     * @param blackInCheck whether black is in check
     * @param whiteCastlingKingside whether white can castle kingside
     * @param whiteCastlingQueenside whether white can castle queenside
     * @param blackCastlingKingside whether black can castle kingside
     * @param blackCastlingQueenside whether black can castle queenside
     * @param enPassantTargetRank if a pawn performed a double-step in the last turn, the rank of the traversed space; -1 otherwise
     * @param enPassantTargetFile if a pawn performed a double-step in the last turn, the file of the traversed space; -1 otherwise
     * @param halfMoves the number of half moves since a piece was captured or a pawn was moved 
     * @param fullMoves the number of full moves that have been played since the start of this game
     */
    public Board(int pointValue, boolean whiteInCheck, boolean blackInCheck, Piece[][] spaces, boolean whiteNextMove,
            boolean whiteCastlingKingside, boolean whiteCastlingQueenside, boolean blackCastlingKingside,
            boolean blackCastlingQueenside, int enPassantTargetRank, int enPassantTargetFile, int halfMoves, int fullMoves) {
        //value may be less than 0 (minimax / negamax)
        this(whiteInCheck, blackInCheck, spaces, whiteNextMove, whiteCastlingKingside, whiteCastlingQueenside,
                blackCastlingKingside, blackCastlingQueenside, enPassantTargetRank, enPassantTargetFile, halfMoves, fullMoves);
        this.pointValue = pointValue;
    }

    /**
     * Copies the spaces array to facilitate generation of follow-up boards wtihout affecting this board.
     * <br><br>
     * <b>Note:</b> the pieces themselves are not copied (the same piece instances are returned inside the copied array)
     * @return a copy of the two dimensional array representing the chess pieces' positions.
     */
    public Piece[][] copySpaces() {
        Piece[][] copy = new Piece[8][8];
        for (int index = 0; index < spaces.length; index++) {
            //copy the 8 inner arrays
            copy[index] = spaces[index].clone();
        }
        return copy;

    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (obj instanceof Board){
            Board board = (Board) obj;
            return  (blackCastlingKingside == board.getBlackCastlingKingside()) &&
                    (blackCastlingQueenside == board.getBlackCastlingQueenside()) &&
                    (blackInCheck == board.getBlackInCheck()) &&
                    (enPassantTargetFile == board.getEnPassantTargetFile()) &&
                    (enPassantTargetRank == board.getEnPassantTargetRank()) &&
                    (fullMoveCount == board.getFullMoves()) &&
                    (halfMovesSincePawnMoveOrCapture == board.getHalfMoves()) &&
                    (pointValue == board.getPointValue()) &&
                    (spaces.equals(board.getSpaces())) &&
                    (whiteCastlingKingside == board.getWhiteCastlingKingside()) &&
                    (whiteCastlingQueenside == board.getWhiteCastlingQueenside()) &&
                    (whiteInCheck == board.getWhiteInCheck()) &&
                    (whiteNextMove == board.getWhiteNextMove());
        } else {
        return false;
        }
    }

    public static String spacesToString(Piece[][] inputSpaces){
        String[] spaceStrings = new String [8];
        for (int rank = 0; rank < 8; rank++) {
            String result = "";
            for (int file = 0; file < 8; file++){
                /*  Ordering: it is more intuive for code to write [x][y] for coordinates
                    but this leads to the toString depicting the board on its
                    side without swapping i and j here */
                Piece currentPiece = inputSpaces[file][rank]; 
                if (currentPiece != null){
                    result += currentPiece.toString();
                } else {
                    result += "0";
                }
            }
            spaceStrings[rank] = result;
        }
        
        return Arrays.toString(spaceStrings).replace(", ", ",\n");
    }

    public boolean spacesEquals(Object o){
        if (o instanceof Piece[][]){
            Piece[][] otherSpaces = (Piece[][]) o;
            return spacesToString(spaces).equals(spacesToString(otherSpaces));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String result;
        result = spacesToString(spaces) + "\n";
        if (whiteNextMove) {
            result += "White Next Move\n";
        } else {
            result += "Black Next Move\n";
        }
        result += "White Castling: ";
        if (whiteCastlingKingside && whiteCastlingQueenside) {
            result += "Kingside and Queenside\n";
        } else {
            if (whiteCastlingKingside) {
                result += "Kingside\n";
            } else if (whiteCastlingQueenside) {
                result += "Queenside\n";
            } else {
                result += "none\n";
            }
        }
        result += "Black Castling: ";
        if (blackCastlingKingside && blackCastlingQueenside) {
            result += "Kingside and Queenside\n";
        } else {
            if (blackCastlingKingside) {
                result += "Kingside\n";
            } else if (blackCastlingQueenside) {
                result += "Queenside\n";
            } else {
                result += "none\n";
            }
        }
        if (enPassantTargetRank == -1 || enPassantTargetFile == -1) {
            result += "No En Passant possible\n";
        } else {
            result += "En Passant possible with target square [" +
                enPassantTargetRank + "][" + enPassantTargetFile + "]\n";
        }
        result += "Halfmove Clock: " + halfMovesSincePawnMoveOrCapture + "\n";
        result += "Fullmove Number: " + fullMoveCount + "\n";
        return result;
    }


    /**
     * Sets the point value of this board.
     * Required to allow for boards to be generated first and evaluated at a later time.
     * @param pointValue the board's value
     */
    public void setPointValue(int pointValue) {
        //pointvalue may be negative
        this.pointValue = pointValue;
    }

    public void setWhiteInCheck(boolean whiteInCheck) {
        this.whiteInCheck = whiteInCheck;
    }

    public void setBlackInCheck(boolean blackInCheck) {
        this.blackInCheck = blackInCheck;
    }

    /*
     **********************************
     * Getters
     **********************************
     */
    public int getPointValue() {
        return pointValue;
    };

    public Piece[][] getSpaces() {
        return spaces;
    }

    public boolean getWhitesTurn() {
        return whiteNextMove;
    }

    public boolean getWhiteInCheck() {
        return whiteInCheck;
    }

    public boolean getBlackInCheck() {
        return blackInCheck;
    }

    public boolean getWhiteNextMove() {
        return whiteNextMove;
    }

    public boolean getWhiteCastlingKingside() {
        return whiteCastlingKingside;
    }

    public boolean getWhiteCastlingQueenside() {
        return whiteCastlingQueenside;
    }

    public boolean getBlackCastlingKingside() {
        return blackCastlingKingside;
    }

    public boolean getBlackCastlingQueenside() {
        return blackCastlingQueenside;
    }

    public int getEnPassantTargetRank() {
        return enPassantTargetRank;
    }

    public int getEnPassantTargetFile() {
        return enPassantTargetFile;
    }

    public int getHalfMoves() {
        return halfMovesSincePawnMoveOrCapture;
    }

    public int getFullMoves() {
        return fullMoveCount;
    }

    public Piece getPieceAt(int rank, int file){
        return spaces[rank][file];
    }

    public boolean[][] getAttackedByWhite() {
        return attackedByWhite;
    }

    public void setAttackedByWhite(boolean[][] attackedByWhite) {
        this.attackedByWhite = attackedByWhite;
    }

    public boolean[][] getAttackedByBlack() {
        return attackedByBlack;
    }

    public void setAttackedByBlack(boolean[][] attackedByBlack) {
        this.attackedByBlack = attackedByBlack;
    }

    public Coordinate getKingPosition(boolean isWhite){
        for (int rank = 0; rank < 7; rank++){
            for (int file = 0; file < 7; file++){
                if (isWhite && spaces[rank][file].toString().equals("K")){
                    return new Coordinate(rank, file);
                }
                if (!isWhite && spaces[rank][file].toString().equals("k")){
                    return new Coordinate(rank, file);
                }
            }
        }
        return null;
    }
}