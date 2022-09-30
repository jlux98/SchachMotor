package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static model.PieceEncoding.*;

/**
 * An implementation of Board based on a two-dimensional array of Pieces representing the board.
 */
public class ArrayBoard implements Board {
    private Piece[][] squares;

    public ArrayBoard(Piece[][] squares) {
        this.squares = squares;
    }

    @Override
    public Piece getPieceAt(int rank, int file){
        return squares[rank][file];
    }

    

    @Override
    public Coordinate getKingPosition(boolean isWhite){
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                Piece currentPiece = squares[rank][file];
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

    public Piece[][] copySquares() {
        Piece[][] copy = new Piece[8][8];
        for (int index = 0; index < squares.length; index++) {
            //copy the 8 inner arrays
            copy[index] = squares[index].clone();
        }
        return copy;
    }

    @Override
    public String toString() {
        String[] resultArray = new String[8];
        for (int rank = 0; rank < 8; rank++) {
            String result = "";
            for (int file = 0; file < 8; file++) {
                Piece currentPiece = squares[rank][file];
                if (currentPiece != null) {
                    result += currentPiece.toString();
                } else {
                    result += "0";
                }
            }
            resultArray[rank] = result;
        }
        String result = addChar(Arrays.toString(resultArray).replace(", ", ",\n"), '\n', 1);
        return result;
    }

    private String addChar(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
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
        squares[rank][file] = piece;
        return;
    }

    @Override
    public List<Piece> getRank(int rank) {
        List<Piece> result = new ArrayList<>();
        for (int i = 0; i < squares.length; i++) {
            result.add(squares[rank][i]);
        }
        return result;
    }

    @Override
    public Board copyBoard() {
        return new ArrayBoard(copySquares());
    }

    @Override
    public Piece getPieceAt(Coordinate square) {
        return getPieceAt(square.getRank(), square.getFile());
    }

    @Override
    public void setPieceAt(Coordinate square, Piece piece) {
        this.setPieceAt(square.getRank(), square.getFile(), piece);        
    }

	@Override
	public byte getByteAt(Coordinate square) {
        return getByteAt(square.getRank(),square.getFile());
	}

	@Override
	public void setByteAt(Coordinate square, byte b) {
        setByteAt(square.getRank(), square.getFile(), b);		
	}

	@Override
	public byte getByteAt(int rank, int file) {
        return pieceToByte(squares[rank][file]);
	}

	private byte pieceToByte(Piece piece) {
        byte result = -1;
        if (piece == null){
            return 0;
        }
        switch (piece.getPieceType()){
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
        if (!piece.getIsWhite()){
            result += PIECE_OFFSET;
        }
        return result;
    }

    @Override
	public void setByteAt(int rank, int file, byte b) {
		squares[rank][file] = byteToPiece(b);
	}

    private Piece byteToPiece(byte b) {
        if (b == 0){
            return null;
        }
        char result = ' ';
        switch (b%6){
            case 1:
                result = 'B';
                break;
            case 2:
                result = 'K';
                break;
            case 3:
                result = 'N';
                break;
            case 4:
                result = 'P';
                break;
            case 5:
                result = 'Q';
                break;
            case 0:
                result = 'R';
                break;
        }
        if (b > 6){
            result = Character.toLowerCase(result);
        }
        return new Piece(result);
    }

    @Override
    public String toStringFen() {
        throw new UnsupportedOperationException("array board does not support converting to fen");
    }

}
