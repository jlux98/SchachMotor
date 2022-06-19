package model;

public class Coordinate {
    private int rank;
    private int file;

    public Coordinate(int rank, int file){
        if (rank < 0 || rank > 7) {
            throw new IllegalArgumentException("Error: Rank out of bounds!");
        }
        if (file < 0 || file > 7) {
            throw new IllegalArgumentException("Error: File out of bounds!");
        }
        this.rank = rank;
        this.file = file;
    }

    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }

}
