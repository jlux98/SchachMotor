package model;

import static model.PieceEncoding.*;

public class Move {
    private Coordinate startingSpace;
    private Coordinate targetSpace;
    private byte promotedTo;

    public Move(Coordinate startingSpace, Coordinate targetSpace) {
        this.startingSpace = startingSpace;
        this.targetSpace = targetSpace;
        this.promotedTo = 0;
    }

    public Move(String moveString){

        if (moveString.matches("[a-h][1-8][a-h][1-8](B|N|Q|R|b|n|q|r)")){
            this.startingSpace = new Coordinate(moveString.substring(0,2));
            this.targetSpace = new Coordinate(moveString.substring(2,4));
            this.promotedTo = new Piece(moveString.charAt(4)).toByte();
            return;
        }

        if (moveString.matches("[a-h][1-8][a-h][1-8]")){
            this.startingSpace = new Coordinate(moveString.substring(0,2));
            this.targetSpace = new Coordinate(moveString.substring(2,4));
        } else {
            throw new IllegalArgumentException("Error: Move not correctly formatted");
        }
    }    
    
    public Move(Coordinate startingSpace, Coordinate targetSpace, byte promotedTo) {
        this.startingSpace = startingSpace;
        this.targetSpace = targetSpace;
        this.promotedTo = promotedTo;
    }

    /**
     * This constructor exists purely for compatibility with old tests
     * @param startingSpace
     * @param targetSpace
     * @param piece
     */
    public Move(Coordinate startingSpace, Coordinate targetSpace, Piece piece) {
        this(startingSpace, targetSpace, piece.toByte());
    }

    public Coordinate getStartingSpace() {
        return startingSpace;
    }

    public Coordinate getTargetSpace() {
        return targetSpace;
    }

    public byte getPromotedTo() {
        return promotedTo;
    }

    @Override
    public String toString() {
        String result = startingSpace.toString() + targetSpace.toString();
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
        String result = startingSpace.toStringAlgebraic() + targetSpace.toStringAlgebraic();
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
                    && this.getStartingSpace().equals(otherMove.getStartingSpace())
                    && this.getTargetSpace().equals(otherMove.getTargetSpace());
        }
        return false;
    }
}
