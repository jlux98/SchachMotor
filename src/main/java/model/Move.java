package model;

import static model.PieceEncoding.*;

public class Move implements Cloneable {
    private Coordinate startingSquare;
    private Coordinate targetSquare;
    private byte promotedTo;

    public Move(Coordinate startingSquare, Coordinate targetSquare) {
        this.startingSquare = startingSquare;
        this.targetSquare = targetSquare;
        this.promotedTo = 0;
    }

    public Move(String moveString){

        if (moveString.matches("[a-h][1-8][a-h][1-8](B|N|Q|R|b|n|q|r)")){
            this.startingSquare = new Coordinate(moveString.substring(0,2));
            this.targetSquare = new Coordinate(moveString.substring(2,4));
            this.promotedTo = PieceEncoding.getBytePieceFromCharacter(moveString.charAt(4));
            return;
        }

        if (moveString.matches("[a-h][1-8][a-h][1-8]")){
            this.startingSquare = new Coordinate(moveString.substring(0,2));
            this.targetSquare = new Coordinate(moveString.substring(2,4));
        } else {
            throw new IllegalArgumentException("Error: Move not correctly formatted: " + moveString);
        }
    }    
    
    public Move(Coordinate startingSquare, Coordinate targetSquare, byte promotedTo) {
        this.startingSquare = startingSquare;
        this.targetSquare = targetSquare;
        this.promotedTo = promotedTo;
    }

    /**
     * This constructor exists purely for compatibility with old tests
     * @param startingSquare
     * @param targetSquare
     * @param piece
     */
    public Move(Coordinate startingSquare, Coordinate targetSquare, Piece piece) {
        this(startingSquare, targetSquare, piece.toByte());
    }

    public Coordinate getStartingSquare() {
        return startingSquare;
    }

    public Coordinate getTargetSquare() {
        return targetSquare;
    }

    public byte getPromotedTo() {
        return promotedTo;
    }

    @Override
    public String toString() {
        String result = startingSquare.toString() + targetSquare.toString();
        if (promotedTo != 0) {
            result += promotedToString();
        }
        return result;
    }

    private String promotedToString() {
        switch (promotedTo) {
            case BLACK_BISHOP:
            case WHITE_BISHOP:
                return "b";
            case BLACK_KING:
            case WHITE_KING:
                return "k";
            case BLACK_KNIGHT:
            case WHITE_KNIGHT:
                return "n";
            case BLACK_PAWN:
            case WHITE_PAWN:
                return "p";
            case BLACK_QUEEN:
            case WHITE_QUEEN:
                return "q";
            case BLACK_ROOK:
            case WHITE_ROOK:
                return "r";
            default:
                return null;
        }
    }

    public String toStringAlgebraic() {
        String result = startingSquare.toStringAlgebraic() + targetSquare.toStringAlgebraic();
        if (promotedTo != 0) {
            result += promotedToString();
        }
        return result;
    }

    
    /**
     * Compares the Piece stored as promotedTo in this move to the one stored in the passed move.
     * @param otherMove the move to compare to, may be null
     * @return true if this.getPromotedTo() equals otherMove.getPromotedTo()
     */
    private boolean promotedToEquals(Move otherMove) {
        return this.getPromotedTo() == otherMove.getPromotedTo();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Move) {
            Move otherMove = (Move) obj;
            return this.promotedToEquals(otherMove)
                    && this.getStartingSquare().equals(otherMove.getStartingSquare())
                    && this.getTargetSquare().equals(otherMove.getTargetSquare());
        }
        return false;
    }

    @Override
    public Move clone() {
        return new Move(toStringAlgebraic());
    }
}
