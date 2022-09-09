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
    //TODO might be worth throwing an exception if the square is empty (instead of returning null)?
    public abstract Piece getPieceAt(int rank, int file);

    public abstract Piece getPieceAt(Coordinate space);

    public abstract byte getByteAt(Coordinate space);
    public abstract byte getByteAt(int rank, int file);

    public abstract List<Piece> getRank(int rank);


    // for better modularity so we can implement BitBoards without much code change
    public abstract void setPieceAt(int rank, int file, Piece piece);

    public abstract void setByteAt(Coordinate space, byte b);
    public abstract void setByteAt(int rank, int file, byte b);
    public abstract void setPieceAt(Coordinate space, Piece piece);
    /**
     * Returns the position of the white/black king on this board.
     * Calls to getPieceAt(rank,file) with rank and file as returned by this method must return the corresponding king.
     * @param isWhite true - if the white king should be searched, false - if the black king should be searched
     * @return the position of the specified king on this board
     */
    public abstract Coordinate getKingPosition(boolean isWhite);

    // public abstract Piece[][] getSpaces();

    /**
    * Copies the spaces array to facilitate generation of follow-up positions wtihout affecting this position.
    * <br><br>
    * <b>Note:</b> Because pieces are immutable the pieces themselves are not copied (the same piece instances are returned within the copied array).
    * @return a copy of the two dimensional array representing the chess pieces' positions.
    */
    // public abstract Piece[][] copySpaces();
    
    public abstract Board copyBoard();
    
}
