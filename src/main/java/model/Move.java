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

    public String toStringTraditional(){
        String result = startingSpace.toStringTraditional() + targetSpace.toStringTraditional();
        if (promotedTo != null) {
            result += promotedTo.toString();
        }
        return result;
    }
}
