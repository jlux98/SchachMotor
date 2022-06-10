package model;

/**
 * Wrapper class for the PieceType enum.
 * This class contains all data that differs between pieces of the same type
 * (e.g. color)
 */
public class Piece {

    private PieceType pieceType;
    /*
     * Determines the Piece's color.
     */
    private boolean isWhite;

    /**
     * Constructs a new Piece of the specified type and color.
     * @param pieceType the type of the piece (e.g. Knight)
     * @param isWhite the piece's color
     * <ul>
     *      <li>true - the piece is white</li>
     *      <li>false - the piece is black</li>
     * </ul>
     */
    public Piece(PieceType pieceType, boolean isWhite) {
        this.pieceType = pieceType;
        this.isWhite = isWhite;
    }
}
