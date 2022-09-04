package model;

public class Move {
    private Coordinate startingSpace;
    private Coordinate targetSpace;
    private Piece promotedTo;
    public Move(Coordinate startingSpace, Coordinate targetSpace) {
        this.startingSpace = startingSpace;
        this.targetSpace = targetSpace;
        this.promotedTo = null;
    }

    

    public Move(Coordinate startingSpace, Coordinate targetSpace, Piece promotedTo) {
        this.startingSpace = startingSpace;
        this.targetSpace = targetSpace;
        this.promotedTo = promotedTo;
    }



    public Coordinate getStartingSpace() {
        return startingSpace;
    }
    public Coordinate getTargetSpace() {
        return targetSpace;
    }
    public Piece getPromotedTo() {
        return promotedTo;
    }
    
    @Override
    public String toString() {
        String result = startingSpace.toString() + targetSpace.toString();
        if (promotedTo != null) {
            result += promotedTo.toString();
        }
        return result;
    }

    public String toStringAlgebraic(){
        String result = startingSpace.toStringAlgebraic() + targetSpace.toStringAlgebraic();
        if (promotedTo != null) {
            result += promotedTo.toString();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Move) {
            Move otherMove = (Move) obj;
            return this.getStartingSpace().equals(otherMove.getStartingSpace())
                    && this.getTargetSpace().equals(otherMove.getTargetSpace())
                    && this.getPromotedTo().equals(otherMove.getPromotedTo());
        }
        return false;
    }
}
