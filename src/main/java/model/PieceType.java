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
    PAWN(1),
    KNIGHT(3),
    BISHOP(3),
    ROOK(5),
    QUEEN(9),
    // As proposed by https://www.chessprogramming.org/Point_Value the King gets a large constant
    KING(100);

    private int pointValue;

    /**
     * Constructs a Piecetype with the specified value.
     * @param pointValue the piece's value
     */
    private PieceType(int pointValue) {
        this.pointValue = pointValue;
    }

    public int getPointValue() {
        return pointValue;
    }
}
