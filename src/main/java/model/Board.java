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

    /**
    * Like {@link #Board(int , boolean , boolean , Piece[][] , boolean , boolean , boolean , boolean , boolean , int , int , int , int)}
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
        computeChecks();
    }

    private void computeChecks() {
        //TODO: Implement a method for autonomously tracking which side is in check
    }

    /**
     * Like {@link #Board(int , boolean , boolean , Piece[][] , boolean , boolean , boolean , boolean , boolean , int , int , int , int)}
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

    /**
     * Generates a follow-up board without en passant target square.
     * Same as {@link Board#generateFollowUpBoard(Piece[][], int, int) generateFollowUpBoard(Board, Piece[][], -1, -1)}.
     * @param previousBoard
     * @param newPosition
     * @return
     */
    public Board generateFollowUpBoard(Piece[][] newPosition) {
        return generateFollowUpBoard(newPosition, -1, -1);
    }

    /**
     * Generates a follow-up board to this board with the specified parameters.
     * Sets check flags according to the attack maps.
     * Castling right flags are copied from this board.
     * <br><br>
     * <b>Note:</b>
     * This method is not suitable to generate follow-up boards for rooks and kings since castling rights are copied.
     * Use {@link #generateFollowUpBoard(Piece[][], int, int, boolean, boolean, boolean, boolean)}  instead.
     * @param newPosition the piece's  new position 
     * @param newEnPassantTargetRank rank of the en passant target square
     * @param newEnPassantTargetFile file of the en passant target square
     * @return a follow-up board to this board  
     */
    public Board generateFollowUpBoard(Piece[][] newPosition, int newEnPassantTargetRank, int newEnPassantTargetFile) {

        //use getters over direct field access so additional code can be run if required at a later time
        boolean newWhiteCastlingKingside = this.getWhiteCastlingKingside();
        boolean newWhiteCastlingQueenside = this.getWhiteCastlingQueenside();
        boolean newBlackCastlingKingside = this.getBlackCastlingKingside();
        boolean newBlackCastlingQueenside = this.getBlackCastlingQueenside();

        return generateFollowUpBoard(newPosition, newEnPassantTargetRank, newEnPassantTargetFile, newWhiteCastlingKingside,
                newWhiteCastlingQueenside, newBlackCastlingKingside, newBlackCastlingQueenside);
    }

    /**
    * Generates a follow-up board to this board with the specified parameters.
    * Sets check flags according to the attack maps.
    * <br><br>  
    * Castling flags represent permanent loss of castling ability, 
    * not temporary inability to castle e.g. caused by check or a piece placed in between rook and king.
    * 
    * @param newPosition the piece's  new position 
    * @param newEnPassantTargetRank rank of the en passant target square
    * @param enPassantTargetFileboolean file of the en passant target square
    * @param newWhiteCastlingKingside whether white may castle kingside
    * @param newWhiteCastlingQueenside whether white may castle queenside
    * @param newBlackCastlingKingside whether black may castle kingside
    * @param newBlackCastlingQueenside whether black may castle queenside
    * @return a follow-up board to this board 
    */
    public Board generateFollowUpBoard(Piece[][] newPosition, int newEnPassantTargetRank, int newEnPassantTargetFile,
            boolean newWhiteCastlingKingside, boolean newWhiteCastlingQueenside, boolean newBlackCastlingKingside,
            boolean newBlackCastlingQueenside) {

        //arguments start with "new" to prevent shadowing of / name-clashing with the surrounding board's attributes
        //such shadowing should be avoided since arguments (e.g. whiteCastlingKingSide) could be missing and the value would be read
        //from the corresponding attribute, rather than resulting in an error

        int fullMoveCount = this.getFullMoves();
        if (this.getWhiteNextMove()) {
            //the board to be generated is white's turn. since black just moved, increment fullMoveCounter
            fullMoveCount += 1;
        }

        //TODO set check flags
        boolean newWhiteInCheck = false;
        boolean newBlackInCheck = false;

        return new Board(newWhiteInCheck, newBlackInCheck, newPosition, !this.getWhiteNextMove(), newWhiteCastlingKingside,
                newWhiteCastlingQueenside, newBlackCastlingKingside, newBlackCastlingQueenside, newEnPassantTargetRank,
                newEnPassantTargetFile, this.getHalfMoves() + 1, fullMoveCount);
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
        for (int i = 0; i < 8; i++) {
            String result = "";
            for (int j = 0; j < 8; j++){
                /*  Ordering: it is more intuive for code to write [x][y] for coordinates
                    but this leads to the toString depicting the board on its
                    side without swapping i and j here */
                Piece currentPiece = inputSpaces[j][i]; 
                if (currentPiece != null){
                    result += currentPiece.toString();
                } else {
                    result += "0";
                }
            }
            spaceStrings[i] = result;
        }
        return Arrays.toString(spaceStrings);
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
}
