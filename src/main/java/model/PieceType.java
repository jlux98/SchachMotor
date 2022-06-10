package model;

/**
 * Enum representing piece types such as Knight or Rook.
 * This enum should contain all data that is shared by all pieces of the same type.
 * <br><br>
 * For example piece value is stored here as all Rooks have the same point value.
 * Contrary, the color of a chess piece must be stored within the wrapper class, as it may differ between two Rooks.
 * 
 */
public enum PieceType {
    //TODO use a more elaborate piece value system
    Pawn(1),
    Knight(3),
    Bishop(3),
    Rook(5),
    Queen(9),
    King(0);

    private int pointValue;
    private PieceType(int pointValue) {
        this.pointValue = pointValue;
    }
}
