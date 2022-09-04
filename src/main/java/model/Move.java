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

    public Move(String moveString){

        // TODO: write case for pawn promotion

        if (moveString.matches("[a-h][1-8][a-h][1-8](B|N|Q|R|b|n|q|r)")){
            this.startingSpace = new Coordinate(moveString.substring(0,2));
            this.targetSpace = new Coordinate(moveString.substring(2,4));
            this.promotedTo = new Piece(moveString.charAt(4));
            return;
        }

        if (moveString.matches("[a-h][1-8][a-h][1-8]")){
            this.startingSpace = new Coordinate(moveString.substring(0,2));
            this.targetSpace = new Coordinate(moveString.substring(2,4));
        } else {
            throw new IllegalArgumentException("Error: Move not correctly formatted");
        }
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Move other = (Move) obj;
        if (promotedTo == null) {
            if (other.promotedTo != null)
                return false;
        } else if (!promotedTo.equals(other.promotedTo))
            return false;
        if (startingSpace == null) {
            if (other.startingSpace != null)
                return false;
        } else if (!startingSpace.equals(other.startingSpace))
            return false;
        if (targetSpace == null) {
            if (other.targetSpace != null)
                return false;
        } else if (!targetSpace.equals(other.targetSpace))
            return false;
        return true;
    }

    
}
