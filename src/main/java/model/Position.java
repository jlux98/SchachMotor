package model;

import movegenerator.AttackMapGenerator;
import positionevaluator.Evaluable;
import positionevaluator.PositionEvaluator;

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
public class Position implements Comparable<Position>, Cloneable, Evaluable {

    /**
     * The array element at [0][0] represents the space a8, [7][7] represents h1.
     */
    private Board board;
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
    private Move generatedByMove;

    /**
     * Stores whether this Position is marked as interesting as required by {@link Evaluable}.
     * This field does not affect equals(), compareTo(), toString() and is not copied by clone().
     */
    private boolean isInteresting;

    /**
    * Like {@link #Position(int , boolean , boolean , Piece[][] , boolean , boolean , boolean , boolean , boolean , int , int , int , int)}
    * but without requiring point value and whiteInCheck / blackInCheck to be set.
    * These value may be set using the corresponding setter at a later time.
    */
    public Position(Piece[][] spaces, boolean whiteNextMove, boolean whiteCastlingKingside, boolean whiteCastlingQueenside,
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
            throw new IllegalArgumentException("full move count must be greater than 0");
        }
        this.board = new ArrayBoard(spaces);
        this.isInteresting = false;
        this.whiteNextMove = whiteNextMove;
        this.whiteCastlingKingside = whiteCastlingKingside;
        this.whiteCastlingQueenside = whiteCastlingQueenside;
        this.blackCastlingKingside = blackCastlingKingside;
        this.blackCastlingQueenside = blackCastlingQueenside;
        this.enPassantTargetRank = enPassantTargetRank;
        this.enPassantTargetFile = enPassantTargetFile;
        this.halfMovesSincePawnMoveOrCapture = halfMoves;
        this.fullMoveCount = fullMoves;
        this.attackedByWhite = AttackMapGenerator.computeChecks(spaces, true);
        this.attackedByBlack = AttackMapGenerator.computeChecks(spaces, false);
        Coordinate whiteKing = getKingPosition(true);
        if (whiteKing != null) {
            this.whiteInCheck = attackedByBlack[whiteKing.getRank()][whiteKing.getFile()];
        } else {
            /*  This is done to prevent a position without a king from generating
                moves without while allowing such a position state to exist for the
                sake of point evaluation */
            this.whiteInCheck = true;
        }
        Coordinate blackKing = getKingPosition(false);
        if (blackKing != null) {
            this.blackInCheck = attackedByWhite[blackKing.getRank()][blackKing.getFile()];
        } else {
            /*  This is done to prevent a position without a king from generating
                moves without while allowing such a position state to exist for the
                sake of point evaluation */
            this.blackInCheck = true;
        }
    }

    /**
     * Like {@link #Position(int , boolean , boolean , Piece[][] , boolean , boolean , boolean , boolean , boolean , int , int , int , int)}
     * but without requiring a point value to be set.
     * The value may be set using Position.setPointValue() at a later time.
     */
    public Position(boolean whiteInCheck, boolean blackInCheck, Piece[][] spaces, boolean whiteNextMove,
            boolean whiteCastlingKingside, boolean whiteCastlingQueenside, boolean blackCastlingKingside,
            boolean blackCastlingQueenside, int enPassantTargetRank, int enPassantTargetFile, int halfMoves, int fullMoves) {
        this(spaces, whiteNextMove, whiteCastlingKingside, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside,
                enPassantTargetRank, enPassantTargetFile, halfMoves, fullMoves);
        this.whiteInCheck = whiteInCheck;
        this.blackInCheck = blackInCheck;
    }

    /**
     * Creates a new position with the specified values.
     * @param pointValue the positions evaluated value
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
     * @param halfMoves the number of half moves since a piece was captured or a pawn was moved . Starts at 0.
     * @param fullMoves the number of full moves that have been played since the start of this game. Starts at 1.
     */
    public Position(int pointValue, boolean whiteInCheck, boolean blackInCheck, Piece[][] spaces, boolean whiteNextMove,
            boolean whiteCastlingKingside, boolean whiteCastlingQueenside, boolean blackCastlingKingside,
            boolean blackCastlingQueenside, int enPassantTargetRank, int enPassantTargetFile, int halfMoves, int fullMoves) {
        //value may be less than 0 (minimax / negamax)
        this(whiteInCheck, blackInCheck, spaces, whiteNextMove, whiteCastlingKingside, whiteCastlingQueenside,
                blackCastlingKingside, blackCastlingQueenside, enPassantTargetRank, enPassantTargetFile, halfMoves, fullMoves);
        this.pointValue = pointValue;
    }

    /**
     * Copies the spaces array to facilitate generation of follow-up positions wtihout affecting this position.
     * <br><br>
     * <b>Note:</b> Because pieces are immutable the pieces themselves are not copied (the same piece instances are returned within the copied array).
     * @return a copy of the two dimensional array representing the chess pieces' positions.
     */
    public Piece[][] copySpaces() {
        return board.copySpaces();

    }

    /**
     * Generates a follow-up position without en passant target square.
     * Same as {@link Position#generateFollowUpPosition(Piece[][], int, int) generateFollowUpPosition(Position, Piece[][], -1, -1)}.
     */
    public Position generateFollowUpPosition(Piece[][] newPosition, boolean captureOrPawnMove) {
        return generateFollowUpPosition(newPosition, -1, -1, captureOrPawnMove);
    }

    /**
     * Generates a follow-up position to this position with the specified parameters.
     * Sets check flags according to the attack maps.
     * Castling right flags are copied from this position.
     * <br><br>
     * <b>Note:</b>
     * This method is not suitable to generate follow-up positions for rooks and kings since castling rights are copied.
     * Use {@link #generateFollowUpPosition(Piece[][], int, int, boolean, boolean, boolean, boolean)}  instead.
     * @param newPosition the piece's  new position 
     * @param newEnPassantTargetRank rank of the en passant target square
     * @param newEnPassantTargetFile file of the en passant target square
     * @param captureOrPawnMove whether a piece was captured or a pawn was moved. if true, half move count is reset
     * @return a follow-up position to this position  
     */
    public Position generateFollowUpPosition(Piece[][] newPosition, int newEnPassantTargetRank, int newEnPassantTargetFile,
            boolean captureOrPawnMove) {

        //use getters over direct field access so additional code can be run if required at a later time
        boolean newWhiteCastlingKingside = this.getWhiteCastlingKingside();
        boolean newWhiteCastlingQueenside = this.getWhiteCastlingQueenside();
        boolean newBlackCastlingKingside = this.getBlackCastlingKingside();
        boolean newBlackCastlingQueenside = this.getBlackCastlingQueenside();

        return generateFollowUpPosition(newPosition, newEnPassantTargetRank, newEnPassantTargetFile, newWhiteCastlingKingside,
                newWhiteCastlingQueenside, newBlackCastlingKingside, newBlackCastlingQueenside, captureOrPawnMove);
    }

    /**
    * Generates a follow-up position to this position with the specified parameters.
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
    * @param captureOrPawnMove whether a piece was captured or a pawn was moved. if true, half move count is reset
    * @return a follow-up position to this position 
    */
    public Position generateFollowUpPosition(Piece[][] newPosition, int newEnPassantTargetRank, int newEnPassantTargetFile,
            boolean newWhiteCastlingKingside, boolean newWhiteCastlingQueenside, boolean newBlackCastlingKingside,
            boolean newBlackCastlingQueenside, boolean captureOrPawnMove) {

        //arguments start with "new" to prevent shadowing of / name-clashing with the surrounding position's attributes
        //such shadowing should be avoided since arguments (e.g. whiteCastlingKingSide) could be missing and the value would be read
        //from the corresponding attribute, rather than resulting in an error

        int fullMoveCount = this.getFullMoves();
        if (!this.getWhiteNextMove()) {
            //the position following this one is black's turn
            //so the position being generated from this position represents the game's state after black moved
            //  -> increment fullMoveCounter
            fullMoveCount += 1;
        }

        int halfMoveCount = this.getHalfMoves();
        if (captureOrPawnMove) {
            //reset half move count if a piece was captured or a pawn was moved
            halfMoveCount = 0;
        } else {
            //increment otherwise
            halfMoveCount += 1;
        }

        return new Position(newPosition, !this.getWhiteNextMove(), newWhiteCastlingKingside,
                newWhiteCastlingQueenside, newBlackCastlingKingside, newBlackCastlingQueenside, newEnPassantTargetRank,
                newEnPassantTargetFile, halfMoveCount, fullMoveCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position){
            Position position = (Position) obj;
            return  (blackCastlingKingside == position.getBlackCastlingKingside()) &&
                    (blackCastlingQueenside == position.getBlackCastlingQueenside()) &&
                    (blackInCheck == position.getBlackInCheck()) &&
                    (enPassantTargetFile == position.getEnPassantTargetFile()) &&
                    (enPassantTargetRank == position.getEnPassantTargetRank()) &&
                    (fullMoveCount == position.getFullMoves()) &&
                    (halfMovesSincePawnMoveOrCapture == position.getHalfMoves()) &&
                    (pointValue == position.getPointValue()) &&
                    (board.equals(position.getBoard())) &&
                    (whiteCastlingKingside == position.getWhiteCastlingKingside()) &&
                    (whiteCastlingQueenside == position.getWhiteCastlingQueenside()) &&
                    (whiteInCheck == position.getWhiteInCheck()) &&
                    (whiteNextMove == position.getWhiteNextMove());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String result;
        result = board.toString() + "\n";
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
     * Clones this position.
     * While a new spaces array is created, the contained pieces are the same instances.
     * Thus modifying the array is possible without affecting this position.
     * As pieces are immutable it is valid to use the same instances.
     * <br><br>
     * The created Position will not be marked as interesting, even if this Position is.
     */
    @Override
    public Position clone() {
        Piece[][] copiedSpaces = this.copySpaces();
        return new Position(this.pointValue, this.whiteInCheck, this.blackInCheck, copiedSpaces, this.whiteNextMove,
                this.whiteCastlingKingside, this.whiteCastlingQueenside, this.blackCastlingKingside, this.blackCastlingQueenside,
                this.enPassantTargetRank, this.enPassantTargetFile, this.halfMovesSincePawnMoveOrCapture, this.fullMoveCount);
    }

    @Override
    public int compareTo(Position otherPosition) {
        return this.toString().compareTo(otherPosition.toString());
    }

    @Override
    public int evaluate() {
        this.isInteresting = false;
        this.pointValue = PositionEvaluator.evaluatePosition(this);
        return this.pointValue;
    }

    @Override
    public void setValue(int pointValue) {
        //pointvalue may be negative
        this.pointValue = pointValue;
    }

    @Override
    public int getValue() {
        return this.pointValue;
    }

    @Override
    public boolean isInteresting() {
        return isInteresting;
    }

    @Override
    public void markAsInteresting() {
        this.isInteresting = true;
    }

    /*
     **********************************
     * Getters and Setters
     **********************************
     */

    public void setWhiteInCheck(boolean whiteInCheck) {
        this.whiteInCheck = whiteInCheck;
    }

    public void setBlackInCheck(boolean blackInCheck) {
        this.blackInCheck = blackInCheck;
    }

    public int getPointValue() {
        return pointValue;
    };

    public Piece[][] getSpaces() {
        return board.getSpaces();
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

    public Piece getPieceAt(int rank, int file) {
        return board.getPieceAt(rank, file);
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

    public Coordinate getKingPosition(boolean isWhite) {
        return board.getKingPosition(isWhite);
    }

    public Move getMove() {
        return generatedByMove;
    }

    public void setMove(int startingRank, int startingFile, int targetRank, int targetFile) {
        this.generatedByMove = new Move(new Coordinate(startingRank, startingFile),
            new Coordinate(targetRank, targetFile));
    }

    public Board getBoard() {
        return this.board;
    }

}