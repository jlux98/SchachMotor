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

    private String getFileString(){
        switch (file) {
            case 0:
                return "a";
            case 1:
                return "b";
            case 2:
                return "c";
            case 3:
                return "d";
            case 4:
                return "e";
            case 5:
                return "f";
            case 6:
                return "g";
            case 7:
                return "h";
            default:
                return "";
        }
    }

    private String getRankString(){
        switch (rank) {
            case 0:
                return "8";
            case 1:
                return "7";
            case 2:
                return "6";
            case 3:
                return "5";
            case 4:
                return "4";
            case 5:
                return "3";
            case 6:
                return "2";
            case 7:
                return "1";
            default:
                return "";
        }
    }

    

    @Override
    public String toString() {
        return "[" + rank + "][" + file + "]";
    }

    public String toStringAlgebraic() {
        return getFileString() + getRankString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Coordinate) {
            Coordinate otherCoordinate = (Coordinate) obj;
            return this.getRank() == otherCoordinate.getRank()
                    && this.getFile() == otherCoordinate.getFile();
        }
        return false;
    }
}
