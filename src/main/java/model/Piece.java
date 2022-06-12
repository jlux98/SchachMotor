package model;

/**
 * Wrapper class for the PieceType enum.
 * This class contains all data that differs between pieces of the same type
 * (e.g. color)
 */
public class Piece {

    private final PieceType pieceType;
    /*
     * Determines the Piece's color.
     */
    private final boolean isWhite;

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

    /**
    * Constructs the piece represented by the passed character.   
    *          <ul>
    *              <li>upper case characters are white pieces</li>
    *              <li>lower case characters are black pieces</li>
    *              <li>knight - k,K</li>
    *              <li>queen - q,Q</li>
    *              <li>rook - r,R</li>
    *              <li>bishop - b,B</li>
    *              <li>knight - n,N</li>
    *              <li>pawn - p,P</li>
    *          </ul>
    * @throws IllegalArgumentException if the character does not represent a chess piece
    */
    public Piece(char pieceCharacter) {
        //determine color
        this.isWhite = Character.isUpperCase(pieceCharacter);

        //determine type
        pieceCharacter = Character.toLowerCase(pieceCharacter);
        switch (pieceCharacter) {
            case 'k' -> this.pieceType = PieceType.King;
            case 'q' -> this.pieceType = PieceType.Queen;
            case 'r' -> this.pieceType = PieceType.Rook;
            case 'b' -> this.pieceType = PieceType.Bishop;
            case 'n' -> this.pieceType = PieceType.Knight;
            case 'p' -> this.pieceType = PieceType.Pawn;
            default -> throw new IllegalArgumentException("piece characters must be k,q,r,b,n or p, not " + pieceCharacter);
        }
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public boolean getIsWhite() {
        return isWhite;
    }
}
