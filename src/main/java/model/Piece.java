package model;

import static model.PieceEncoding.*;

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
            case 'k' -> this.pieceType = PieceType.KING;
            case 'q' -> this.pieceType = PieceType.QUEEN;
            case 'r' -> this.pieceType = PieceType.ROOK;
            case 'b' -> this.pieceType = PieceType.BISHOP;
            case 'n' -> this.pieceType = PieceType.KNIGHT;
            case 'p' -> this.pieceType = PieceType.PAWN;
            default -> throw new IllegalArgumentException("piece characters must be k,q,r,b,n or p, not " + pieceCharacter);
        }
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public boolean getIsWhite() {
        return isWhite;
    }

    @Override
    public String toString() {
        String result;
        switch (pieceType){
            case BISHOP:
                result = "b";
                break;
            case KING:
                result = "k";
                break;
            case KNIGHT:
                result = "n";
                break;
            case PAWN:
                result = "p";
                break;
            case QUEEN:
                result = "q";
                break;
            case ROOK:
                result = "r";
                break;
            default:
                result = "error";
                break;
        }
        if (isWhite) {
            result = result.toUpperCase();
        }
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        }
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof Piece)) {
            return false;
        }
        Piece otherPiece = (Piece) otherObject;
        return this.getIsWhite() == otherPiece.getIsWhite() && this.getPieceType() == otherPiece.getPieceType();
    }

    public byte toByte() {
        byte result = 0;
        switch(pieceType){
            case BISHOP:
                result = WHITE_BISHOP;
                break;
            case KING:
                result = WHITE_KING;
                break;
            case KNIGHT:
                result = WHITE_KNIGHT;
                break;
            case PAWN:
                result = WHITE_PAWN;
                break;
            case QUEEN:
                result = WHITE_QUEEN;
                break;
            case ROOK:
                result = WHITE_ROOK;
                break;
            default:
                break;
        }
        if (!isWhite){
            result += WHITE_ROOK;
        }
        return result;
    }
}
