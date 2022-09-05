package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An implementation of Board based on a two-dimensional array of Pieces representing the board.
 */
public class ArrayBoard implements Board {
    private Piece[][] spaces;

    public ArrayBoard(Piece[][] spaces) {
        this.spaces = spaces;
    }

    @Override
    public Piece getPieceAt(int rank, int file){
        return spaces[rank][file];
    }


    @Override
    public Coordinate getKingPosition(boolean isWhite){
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                Piece currentPiece = spaces[rank][file];
                if (isWhite && currentPiece != null &&
                    currentPiece.toString().equals("K")){
                    return new Coordinate(rank, file);
                }
                if (!isWhite && currentPiece != null &&
                    currentPiece.toString().equals("k")){
                    return new Coordinate(rank, file);
                }
            }
        }
        return null;
    }

    public Piece[][] getSpaces() {
        return spaces;
    }


    public Piece[][] copySpaces() {
        Piece[][] copy = new Piece[8][8];
        for (int index = 0; index < spaces.length; index++) {
            //copy the 8 inner arrays
            copy[index] = spaces[index].clone();
        }
        return copy;
    }

    @Override
    public String toString() {
        String[] spaceStrings = new String[8];
        for (int rank = 0; rank < 8; rank++) {
            String result = "";
            for (int file = 0; file < 8; file++) {
                Piece currentPiece = spaces[rank][file];
                if (currentPiece != null) {
                    result += currentPiece.toString();
                } else {
                    result += "0";
                }
            }
            spaceStrings[rank] = result;
        }
        return Arrays.toString(spaceStrings).replace(", ", ",\n");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Board) {
            Board otherBoard = (Board) o;
            return this.toString().equals(otherBoard.toString());
        } else {
            return false;
        }
    }

    @Override
    public void setPieceAt(int rank, int file, Piece piece) {
        spaces[rank][file] = piece;
        return;
    }

    @Override
    public List<Piece> getRank(int rank) {
        List<Piece> result = new ArrayList<>();
        for (int i = 0; i < spaces.length; i++) {
            result.add(spaces[rank][i]);
        }
        return result;
    }

    @Override
    public Board copyBoard() {
        return new ArrayBoard(copySpaces());
    }

}
