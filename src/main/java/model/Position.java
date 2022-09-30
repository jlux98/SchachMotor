package model;

import java.util.HashMap;
import java.util.Timer;

import application.Conductor;
import movegenerator.AttackMapGenerator;
import movegenerator.MoveGenerator;
import utility.PerformanceData;
import utility.TimeUtility;

/**
 * Class representing the game state.
 * <br>
 * <br>
 * It stores the position of the pieces, the players' castling ability, whose
 * turn it is, if a player is in check
 * and if a pawn may be captured en passant
 * (= all of the information denoted by a fen string plus whether a player is in
 * check).
 * <br>
 * <br>
 * <b>Note:</b>
 * The array element at [0][0] represents the space a8, while [7][7] represents
 * h1.
 */
public class Position implements Comparable<Position>, Cloneable {

    /**
     * The element at the Board-space (0,0) represents the space a8, (0,7)
     * represents h8,
     * (7,0) represents a1 and (7,7) represents h1.
     */
    private Board board;
    private boolean whiteInCheck;
    private boolean blackInCheck;
    private boolean blackKingsideLeft;
    private boolean blackKingsideRight;
    private boolean blackQueensideLeft;
    private boolean blackQueensideRight;
    private boolean whiteKingsideLeft;
    private boolean whiteKingsideRight;
    private boolean whiteQueensideLeft;
    private boolean whiteQueensideRight;
    private boolean whiteNextMove;
    private boolean whiteCastlingKingside;
    private boolean whiteCastlingQueenside;
    private boolean blackCastlingKingside;
    private boolean blackCastlingQueenside;
    private Coordinate enPassantTargetSpace;
    private byte halfMovesSincePawnMoveOrCapture; //may not be greater than 100 at any time
    private int fullMoveCount;
    // private boolean[] attackedByWhite;
    // private boolean[] attackedByBlack;
    private Move generatedByMove;

    private static TimeUtility<Integer> attackMapTimer = new TimeUtility<Integer>();

    /**
    * Like {@link #Position(int , boolean , boolean , Piece[][] , boolean , boolean , boolean , boolean , boolean , int , int , int , int)}
    * but without requiring whiteInCheck and blackInCheck to be set.
    * These values are automatically set by computing attack maps.
    */
    public Position(Board spaces, boolean whiteNextMove, boolean whiteCastlingKingside, boolean whiteCastlingQueenside,
            boolean blackCastlingKingside, boolean blackCastlingQueenside, int enPassantTargetRank, int enPassantTargetFile,
            byte halfMoves, int fullMoves) {

        if (spaces == null) {
            throw new NullPointerException("spaces array may not be null");
        }
        this.board = spaces;

        setFullMoveCount(fullMoves);
        setHalfMoveCount(halfMoves);
        this.whiteNextMove = whiteNextMove;
        this.whiteCastlingKingside = whiteCastlingKingside;
        this.whiteCastlingQueenside = whiteCastlingQueenside;
        this.blackCastlingKingside = blackCastlingKingside;
        this.blackCastlingQueenside = blackCastlingQueenside;
        setEnPAssantTargetSpace(enPassantTargetRank, enPassantTargetFile);

        attackMapTimer.time(() -> computeChecks(spaces));
        PerformanceData.attackMapGenerationTime += attackMapTimer.getElapsedTime();
    }

 /**
     * Creates a new position with the specified values.
     * @param spaces the board holding the chess pieces
     * @param whiteNextMove whether the next turn is white's
     * @param whiteInCheck whether white is in checks
     * @param blackInCheck whether black is in check
     * @param whiteCastlingKingside whether white has the right to castle kingside
     * @param whiteCastlingQueenside whether white has the right to castle queenside
     * @param blackCastlingKingside whether black has the right to castle kingside
     * @param blackCastlingQueenside whether black has the right to castle queenside
     * @param enPassantTargetRank if a pawn performed a double-step in the last turn, the rank of the traversed space; -1 otherwise
     * @param enPassantTargetFile if a pawn performed a double-step in the last turn, the file of the traversed space; -1 otherwise
     * @param halfMoves the number of half moves since a piece was captured or a pawn was moved . Starts at 0.
     * @param fullMoves the number of full moves that have been played since the start of this game. Starts at 1.
     */
    public Position(boolean whiteInCheck, boolean blackInCheck, Board spaces, boolean whiteNextMove,
            boolean whiteCastlingKingside, boolean whiteCastlingQueenside, boolean blackCastlingKingside,
            boolean blackCastlingQueenside, int enPassantTargetRank, int enPassantTargetFile, byte halfMoves, int fullMoves) {
        this(spaces, whiteNextMove, whiteCastlingKingside, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside,
                enPassantTargetRank, enPassantTargetFile, halfMoves, fullMoves);
        this.whiteInCheck = whiteInCheck;
        this.blackInCheck = blackInCheck;
    }

    private void setEnPAssantTargetSpace(int enPassantTargetRank, int enPassantTargetFile) {
        if (enPassantTargetRank == -1 && enPassantTargetFile == -1) {
            this.enPassantTargetSpace = null;
        } else {
            this.enPassantTargetSpace = new Coordinate(enPassantTargetRank, enPassantTargetFile);
        }
    }

    private int computeChecks(Board spaces) {
        byte[] attackedByWhite = AttackMapGenerator.computeChecksByteEncoded(spaces, true);
        byte[] attackedByBlack = AttackMapGenerator.computeChecksByteEncoded(spaces, false);
        Coordinate whiteKing = getKingPosition(true);
        if (whiteKing != null) {
            this.whiteInCheck = AttackMapGenerator.getBoolFromByte(whiteKing.getRank(), whiteKing.getFile(),
                    attackedByBlack);
            // this.whiteInCheck = isSpaceAttackedByBlack(whiteKing);
        } else {
            /*
             * This is done to prevent a position without a king from generating
             * moves without while allowing such a position state to exist for the
             * sake of point evaluation
             */
            this.whiteInCheck = true;
        }
        Coordinate blackKing = getKingPosition(false);
        if (blackKing != null) {
            this.blackInCheck = AttackMapGenerator.getBoolFromByte(blackKing.getRank(), blackKing.getFile(),
                    attackedByWhite);
            // this.blackInCheck = isSpaceAttackedByWhite(blackKing);
        } else {
            /*
             * This is done to prevent a position without a king from generating
             * moves without while allowing such a position state to exist for the
             * sake of point evaluation
             */
            this.blackInCheck = true;
        }

        blackQueensideLeft  = AttackMapGenerator.getBoolFromByte(0, 2, attackedByWhite);
        blackQueensideRight = AttackMapGenerator.getBoolFromByte(0, 3, attackedByWhite);
        blackKingsideLeft   = AttackMapGenerator.getBoolFromByte(0, 5, attackedByWhite);
        blackKingsideRight  = AttackMapGenerator.getBoolFromByte(0, 6, attackedByWhite);
        whiteQueensideLeft  = AttackMapGenerator.getBoolFromByte(7, 2, attackedByBlack);
        whiteQueensideRight = AttackMapGenerator.getBoolFromByte(7, 3, attackedByBlack);
        whiteKingsideLeft   = AttackMapGenerator.getBoolFromByte(7, 5, attackedByBlack);
        whiteKingsideRight  = AttackMapGenerator.getBoolFromByte(7, 5, attackedByBlack);     
        
        return -1; //for timer compatability
    }

    /**
     * Copies the spaces array to facilitate generation of follow-up positions wtihout affecting this position.
     * <br><br>
     * <b>Note:</b> Because pieces are immutable the pieces themselves are not copied (the same piece instances are returned within the copied array).
     * @return a copy of the two dimensional array representing the chess pieces' positions.
     */
    public Board copyBoard() {
        return board.copyBoard();
    }

    /**
     * Generates a follow-up position without en passant target square.
     * Same as {@link Position#generateFollowUpPosition(Piece[][], int, int)
     * generateFollowUpPosition(Position, Piece[][], -1, -1)}.
     */
    public Position generateFollowUpPosition(Board newBoard, boolean captureOrPawnMove) {
        return generateFollowUpPosition(newBoard, -1, -1, captureOrPawnMove);
    }

    /**
     * Generates a follow-up position to this position with the specified
     * parameters.
     * Sets check flags according to the attack maps.
     * Castling right flags are copied from this position.
     * <br>
     * <br>
     * <b>Note:</b>
     * This method is not suitable to generate follow-up positions for rooks and
     * kings since castling rights are copied.
     * Use
     * {@link #generateFollowUpPosition(Piece[][], int, int, boolean, boolean, boolean, boolean)}
     * instead.
     * 
     * @param newPosition            the piece's new position
     * @param newEnPassantTargetRank rank of the en passant target square
     * @param newEnPassantTargetFile file of the en passant target square
     * @param captureOrPawnMove      whether a piece was captured or a pawn was
     *                               moved. if true, half move count is reset
     * @return a follow-up position to this position
     */
    public Position generateFollowUpPosition(Board newBoard, int newEnPassantTargetRank, int newEnPassantTargetFile,
            boolean captureOrPawnMove) {

        // use getters over direct field access so additional code can be run if
        // required at a later time
        boolean newWhiteCastlingKingside = this.getWhiteCastlingKingside();
        boolean newWhiteCastlingQueenside = this.getWhiteCastlingQueenside();
        boolean newBlackCastlingKingside = this.getBlackCastlingKingside();
        boolean newBlackCastlingQueenside = this.getBlackCastlingQueenside();

        return generateFollowUpPosition(newBoard, newEnPassantTargetRank, newEnPassantTargetFile,
                newWhiteCastlingKingside,
                newWhiteCastlingQueenside, newBlackCastlingKingside, newBlackCastlingQueenside, captureOrPawnMove);
    }

    /**
     * Generates a follow-up position to this position with the specified
     * parameters.
     * Sets check flags according to the attack maps.
     * <br>
     * <br>
     * Castling flags represent permanent loss of castling ability,
     * not temporary inability to castle e.g. caused by check or a piece placed in
     * between rook and king.
     * 
     * @param newBoard                   the piece's new position
     * @param newEnPassantTargetRank     rank of the en passant target square
     * @param enPassantTargetFileboolean file of the en passant target square
     * @param newWhiteCastlingKingside   whether white may castle kingside
     * @param newWhiteCastlingQueenside  whether white may castle queenside
     * @param newBlackCastlingKingside   whether black may castle kingside
     * @param newBlackCastlingQueenside  whether black may castle queenside
     * @param captureOrPawnMove          whether a piece was captured or a pawn was
     *                                   moved. if true, half move count is reset
     * @return a follow-up position to this position
     */
    public Position generateFollowUpPosition(Board newBoard, int newEnPassantTargetRank, int newEnPassantTargetFile,
            boolean newWhiteCastlingKingside, boolean newWhiteCastlingQueenside, boolean newBlackCastlingKingside,
            boolean newBlackCastlingQueenside, boolean captureOrPawnMove) {

        // arguments start with "new" to prevent shadowing of / name-clashing with the
        // surrounding position's attributes
        // such shadowing should be avoided since arguments (e.g. whiteCastlingKingSide)
        // could be missing and the value would be read
        // from the corresponding attribute, rather than resulting in an error

        int fullMoveCount = this.getFullMoves();
        if (!this.getWhiteNextMove()) {
            // the position following this one is black's turn
            // so the position being generated from this position represents the game's
            // state after black moved
            // -> increment fullMoveCounter
            fullMoveCount += 1;
        }

        byte halfMoveCount = this.getHalfMoves();
        if (captureOrPawnMove) {
            // reset half move count if a piece was captured or a pawn was moved
            halfMoveCount = 0;
        } else {
            // increment otherwise
            halfMoveCount += 1;
        }

        return new Position(newBoard, !this.getWhiteNextMove(), newWhiteCastlingKingside,
                newWhiteCastlingQueenside, newBlackCastlingKingside, newBlackCastlingQueenside, newEnPassantTargetRank,
                newEnPassantTargetFile, halfMoveCount, fullMoveCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position position = (Position) obj;
            return (blackCastlingKingside == position.getBlackCastlingKingside()) &&
                    (blackCastlingQueenside == position.getBlackCastlingQueenside()) &&
                    (blackInCheck == position.getBlackInCheck()) &&
                    (getEnPassantTargetFile() == position.getEnPassantTargetFile()) &&
                    (getEnPassantTargetRank() == position.getEnPassantTargetRank()) &&
                    (fullMoveCount == position.getFullMoves()) &&
                    (halfMovesSincePawnMoveOrCapture == position.getHalfMoves()) &&
                    (board.equals(position.getBoard())) &&
                    (whiteCastlingKingside == position.getWhiteCastlingKingside()) &&
                    (whiteCastlingQueenside == position.getWhiteCastlingQueenside()) &&
                    (whiteInCheck == position.getWhiteInCheck()) &&
                    (whiteNextMove == position.getWhiteNextMove());
        } else {
            return false;
        }
    }

    /**
     * Watered down version of equals that disregards among other fullmove count
     * and halfmove count, used for checking for Threefold Repetition.
     * 
     * @param obj
     * @return
     */
    public boolean equalsLight(Object obj) {
        if (obj instanceof Position) {
            Position position = (Position) obj;
            return (blackCastlingKingside == position.getBlackCastlingKingside()) &&
                    (blackCastlingQueenside == position.getBlackCastlingQueenside()) &&
                    (getEnPassantTargetFile() == position.getEnPassantTargetFile()) &&
                    (getEnPassantTargetRank() == position.getEnPassantTargetRank()) &&
                    (board.equals(position.getBoard())) &&
                    (whiteCastlingKingside == position.getWhiteCastlingKingside()) &&
                    (whiteCastlingQueenside == position.getWhiteCastlingQueenside()) &&
                    (whiteNextMove == position.getWhiteNextMove());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String result;
        result = board.toString() + "\n";
        result += "Generating Move: " + generatedByMove + "\n";
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
        if (getEnPassantTargetRank() == -1 || getEnPassantTargetFile() == -1) {
            result += "No En Passant possible\n";
        } else {
            result += "En Passant possible with target square [" +
                    getEnPassantTargetRank() + "][" + getEnPassantTargetFile() + "]\n";
        }
        result += "Halfmove Clock: " + halfMovesSincePawnMoveOrCapture + "\n";
        result += "Fullmove Number: " + fullMoveCount + "\n";
        result += "White in Check: " + whiteInCheck + "\n";
        result += "Black in Check: " + blackInCheck + "\n";
        return result;
    }

    /**
     * Clones this position.
     * While a new spaces array is created, the contained pieces are the same
     * instances.
     * Thus modifying the array is possible without affecting this position.
     * As pieces are immutable it is valid to use the same instances.
     */
    @Override
    public Position clone() {
        Board copiedSpaces = this.copyBoard();
        Position result = new Position(this.whiteInCheck, this.blackInCheck, copiedSpaces, this.whiteNextMove,
                this.whiteCastlingKingside, this.whiteCastlingQueenside, this.blackCastlingKingside, this.blackCastlingQueenside,
                this.getEnPassantTargetRank(), this.getEnPassantTargetFile(), this.halfMovesSincePawnMoveOrCapture, this.fullMoveCount);
        if (generatedByMove != null){
                result.setMove(generatedByMove.clone());
        }
        return result;
    }

    @Override
    public int compareTo(Position otherPosition) {
        return this.toString().compareTo(otherPosition.toString());
    }

    public String toStringLight() {
        String result = board.toString() + "\n";
        result += whiteNextMove + "\n";
        result += whiteCastlingKingside + "\n";
        result += whiteCastlingQueenside + "\n";
        result += blackCastlingKingside + "\n";
        result += blackCastlingQueenside + "\n";
        result += enPassantTargetSpace + "\n";
        return result;
    }


    public Position getFollowUpByMove(Move toApply) {
        Position[] followUps = MoveGenerator.generatePossibleMoves(
                this);
        for (int i = 0; i < followUps.length; i++) {
            if (followUps[i].generatedByMove.equals(toApply)) {
                return followUps[i];
            }
        }
        return null;
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

    public void setFullMoveCount(int fullMoveCount) {
        if (fullMoveCount < 1) {
            // full move counter starts at 1
            throw new IllegalArgumentException("full move count must be greater than 0");
        }
        this.fullMoveCount = fullMoveCount;
    }

    private void setHalfMoveCount(byte halfMoveCount) {
        if (halfMoveCount < 0 || halfMoveCount > 100) {
            throw new IllegalArgumentException("half move count must be between 0 and 100");
        }
        this.halfMovesSincePawnMoveOrCapture = halfMoveCount;
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

    public Coordinate getEnPassantTargetSpace() {
        return enPassantTargetSpace;
    }

    public int getEnPassantTargetRank() {
        if (enPassantTargetSpace == null) {
            return -1;
        }
        return enPassantTargetSpace.getRank();
    }

    public int getEnPassantTargetFile() {
        if (enPassantTargetSpace == null) {
            return -1;
        }
        return enPassantTargetSpace.getFile();
    }

    public byte getHalfMoves() {
        return halfMovesSincePawnMoveOrCapture;
    }

    public int getFullMoves() {
        return fullMoveCount;
    }

    /**
     * The Piece at (0,0) represents the space a8, (0,7) represents h8,
     * (7,0) represents a1 and (7,7) represents h1.
     */
    public Piece getPieceAt(int rank, int file) {
        return board.getPieceAt(rank, file);
    }

    public Piece getPieceAt(Coordinate space) {
        return board.getPieceAt(space);
    }
    // public boolean[][] getAttackedByWhite() {
    // return attackedByWhite;
    // }

    public boolean isKingsideAttacked(boolean isWhite) {
        if (isWhite) {
            return whiteKingsideLeft || whiteKingsideRight;
        } else {
            return blackKingsideLeft || blackKingsideRight;
        }
    }

    public boolean isQueensideAttacked(boolean isWhite) {
        if (isWhite) {
            return whiteQueensideLeft || whiteQueensideRight;
        } else {
            return blackQueensideLeft || blackQueensideRight;
        }
    }
    
    // public void setAttackedByBlack(boolean[][] attackedByBlack) {
    //     this.attackedByBlack = attackedByBlack;
    // }

    // public boolean isSpaceAttackedByWhite(int rank, int file){
    // return attackedByWhite[rank*8+file];
    // }

    // public boolean isSpaceAttackedByWhite(Coordinate space){
    // return isSpaceAttackedByWhite(space.getRank(), space.getFile());
    // }

    // public void setAttackedByWhite(boolean[] attackedByWhite) {
    // this.attackedByWhite = attackedByWhite;
    // }

    // public boolean isSpaceAttackedByBlack(int rank, int file){
    // return attackedByBlack[rank*8+file];
    // }

    // public boolean isSpaceAttackedByBlack(Coordinate space){
    // return isSpaceAttackedByBlack(space.getRank(), space.getFile());
    // }

    // public boolean[][] getAttackedByBlack() {
    // return attackedByBlack;
    // }

    // public boolean isSpaceAttacked(int rank, int file, boolean attackedByWhite){
    // if (attackedByWhite){
    // return isSpaceAttackedByWhite(rank, file);
    // } else {
    // return isSpaceAttackedByBlack(rank, file);
    // }
    // }

    // public void setAttackedByBlack(boolean[] attackedByBlack) {
    // this.attackedByBlack = attackedByBlack;
    // }

    public String toStringFen() {
        String result = board.toStringFen();
        if (whiteNextMove) {
            result += " w ";
        } else {
            result += " b ";
        }
        if (whiteCastlingKingside || whiteCastlingQueenside || blackCastlingKingside || blackCastlingQueenside) {
            if (whiteCastlingKingside) {
                result += "K";
            }
            if (whiteCastlingQueenside) {
                result += "Q";
            }
            if (blackCastlingKingside) {
                result += "k";
            }
            if (blackCastlingQueenside) {
                result += "q";
            }
        } else {
            result += "-";
        }
        if (enPassantTargetSpace != null) {
            result += " " + enPassantTargetSpace + " ";
        } else {
            result += " - ";
        }
        result += halfMovesSincePawnMoveOrCapture + " ";
        result += fullMoveCount;
        return result;
    }

    public Coordinate getKingPosition(boolean isWhite) {
        return board.getKingPosition(isWhite);
    }

    public Move getMove() {
        return generatedByMove;
    }

    public void setMove(Move generatedBy) {
        if (generatedBy == null) {
            throw new NullPointerException(
                    "the move this board was generated by may not be explicitly set to null");
        }
        this.generatedByMove = generatedBy;
    }

    public void setMove(int startingRank, int startingFile, int targetRank, int targetFile) {
        this.generatedByMove = new Move(new Coordinate(startingRank, startingFile),
                new Coordinate(targetRank, targetFile));
    }

    public void setMove(int startingRank, int startingFile, int targetRank, int targetFile, Byte promotedTo) {
        this.generatedByMove = new Move(new Coordinate(startingRank, startingFile),
                new Coordinate(targetRank, targetFile), promotedTo);
    }

    public void deleteMove() {
        this.generatedByMove = null;
    }

    public Board getBoard() {
        return this.board;
    }

    public byte getByteAt(int rank, int file) {
        return board.getByteAt(rank, file);
    }

    public boolean isDraw() {
        if (halfMovesSincePawnMoveOrCapture >= 100) {
            return true;
        }
        if (checkForThreefoldRepetition()) {
            return true;
        }
        return false;
    }

    private boolean checkForThreefoldRepetition() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < Conductor.getPastPositions().size(); i++) {
            String key = Conductor.getPastPositions().get(i).toStringLight();
            if (hashMap.get(key) == null) {
                hashMap.put(key, 1);
            } else {
                hashMap.put(key, hashMap.get(key) + 1);
            }
            if (hashMap.get(key) >= 3) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // return Objects.hash(
        // board, whiteNextMove, whiteCastlingKingside,
        // whiteCastlingQueenside, blackCastlingKingside,
        // blackCastlingQueenside, enPassantTargetSpace);
        return toStringLight().hashCode();
    }

}