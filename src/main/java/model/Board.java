package model;

import java.util.List;

/**
 * Classes implementing this interface store the position of chess pieces on the board.
 */
public interface Board {

    //toString

    /**
     * @param rank rank of the piece
     * @param file file of the piece
     * @return the piece at the specified rank and file
     */    
    /**
     * The Piece at (0,0) represents the square a8, (0,7) represents h8,
     * (7,0) represents a1 and (7,7) represents h1.
     */
    public abstract Piece getPieceAt(int rank, int file);

    public abstract Piece getPieceAt(Coordinate square);

    public abstract byte getByteAt(Coordinate square);
    public abstract byte getByteAt(int rank, int file);

    public abstract List<Piece> getRank(int rank);


    // for better modularity so we can implement BitBoards without much code change
    public abstract void setPieceAt(int rank, int file, Piece piece);

    public abstract void setByteAt(Coordinate square, byte b);
    public abstract void setByteAt(int rank, int file, byte b);
    public abstract void setPieceAt(Coordinate square, Piece piece);
    /**
     * Returns the position of the white/black king on this board.
     * Calls to getPieceAt(rank,file) with rank and file as returned by this method must return the corresponding king.
     * @param isWhite true - if the white king should be searched, false - if the black king should be searched
     * @return the position of the specified king on this board
     */
    public abstract Coordinate getKingPosition(boolean isWhite);

    public abstract Board copyBoard();

    public abstract String toStringFen();
    
}
