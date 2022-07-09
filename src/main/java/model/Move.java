package model;

public class Move {
    private Coordinate startingSpace;
    private Coordinate targetSpace;
    public Move(Coordinate startingSpace, Coordinate targetSpace) {
        this.startingSpace = startingSpace;
        this.targetSpace = targetSpace;
    }
    public Coordinate getStartingSpace() {
        return startingSpace;
    }
    public Coordinate getTargetSpace() {
        return targetSpace;
    }
    @Override
    public String toString() {
        return startingSpace + " " + targetSpace;
    }

    public String toStringTraditional(){
        return startingSpace.toStringTraditional() + " " + targetSpace.toStringTraditional();
    }
}
