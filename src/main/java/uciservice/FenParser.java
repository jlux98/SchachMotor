package uciservice;

import java.util.Arrays;
import java.util.regex.Pattern;

import model.Board;
import model.Piece;

/**
 * Class used to translate FEN-Strings to the internal representation of a board.
 */
public class FenParser {
    private static final String DELIMITER = " ";

    private String fen;

    /* from chess wiki:
         <FEN> ::=  <Piece Placement>
    ' ' <Side to move>
    ' ' <Castling ability>
    ' ' <En passant target square>
    ' ' <Halfmove clock>
    ' ' <Fullmove counter> */

    //parser attributes
    private String positionToken;
    private String activePlayerToken;
    private String castlingAbilitiesToken;
    private String enPassantTargetSquareToken;
    private String halfMoveCountToken;
    private String fullMoveCountToken;

    //board attributes
    private Piece[][] piecePositions;
    private boolean isWhiteNextMove;
    private boolean whiteCastlingKingside = false;
    private boolean whiteCastlingQueenside = false;
    private boolean blackCastlingKingside = false;
    private boolean blackCastlingQueenside = false;
    private int enPassantTargetRank;
    private int enPassantTargetFile;
    private int halfMoves;
    private int fullMoves;

    public FenParser(String fen) {
        if (fen == null) {
            throw new NullPointerException("fen string may not be null");
        }
        this.fen = fen;
        this.piecePositions = new Piece[8][8];
    }

    public Board parseFen() {
        breakIntoTokens();
        parseTokens();
        return createBoard();
    }

    private void breakIntoTokens() {
        String[] tokens = fen.split(Pattern.quote(DELIMITER));
        positionToken = tokens[0];
        activePlayerToken = tokens[1];
        castlingAbilitiesToken = tokens[2];
        enPassantTargetSquareToken = tokens[3];
        halfMoveCountToken = tokens[4];
        fullMoveCountToken = tokens[5];
    }

    private void parseTokens() {
        parsePosition();
        parseActivePlayer();
        parseCastlingAbilities();
        parseEnPassantTargetSquare();
        parseHalfMoveCount();
        parseFullMoveCount();
    }

    private Board createBoard() {
        return new Board(piecePositions, isWhiteNextMove, whiteCastlingKingside, whiteCastlingQueenside, blackCastlingKingside,
                blackCastlingQueenside, enPassantTargetRank, enPassantTargetFile, halfMoves, fullMoves);

    }

    /**
     * Parses the position of chess pieces from a fen string.
     * <br><br>
     * FEN-Strings store the position as follows:
     * <ul> 
     *      <li>the board is represented from a8 to h1</li>
     *      <li>pieces are represented by a single character
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
     *      </li>
     *      <li>consecutive empty squares are denoted by a single digit
     *          <ul>
     *              <li>p3r means: pawn, 3 empty squares, rook </li>
     *          </ul>
     *      </li>
     *      <li>the end of a rank is denoted by "/"</li>
     * </ul>
     * Example:
     * r7\3pR2r\8\8\8\8\8\R7 
     * <ul>
     *      <li>black rook (on a8), followed by 7 empty squares</li>
     *      <li>three empty squares, black pawn (on d7), white rook (e7), two empty squares, black rook (h7)</li>
     *      <li>8 empty squares</li>
     *      <li>8 empty squares</li>
     *      <li>8 empty squares</li>
     *      <li>8 empty squares</li>
     *      <li>8 empty squares</li>
     *      <li>white rook (on a1), followed by 7 empty squares</li>
     * </ul>
     */
    private void parsePosition() {
        //split token into ranks
        String[] ranks = positionToken.split(Pattern.quote("/"));
        int placeInRank = 0;
        for (String rank : ranks) {
            parseRank(rank, placeInRank);
            placeInRank++;
        }
    }

    /**
     * Parses a single rank provided as a fen string.
     * @param rank a part of a fen string representing one rank
     * @param yPosition determines which rank on the board the pieces are placed in,
     * yPosition = 0 places in the first rank, yPosition = 7 in the last rank
     */
    private void parseRank(String rank, int yPosition) {
        //A8 IS SPACES[0][0]
        int xPosition = 0;
        for (char character : rank.toCharArray()) {
            if (Character.isDigit(character)) {
                xPosition += Character.getNumericValue(character);
            } else {
                Piece piece = new Piece(character);
                piecePositions[xPosition][yPosition] = piece;
            }
        }
    }

    private void parseActivePlayer() {
        if (activePlayerToken.length() != 1) {
            throw new IllegalStateException(
                    "side to move / active player must be one character, not " + activePlayerToken.length());
        }
        char activePlayerChar = activePlayerToken.charAt(0);
        if (activePlayerChar == 'w') {
            isWhiteNextMove = true;
            return;
        }
        if (activePlayerChar == 'b') {
            isWhiteNextMove = false;
            return;
        }
        throw new IllegalStateException(
                "side to move / active player must be denoted by \"w\" or \"b\", not by " + activePlayerChar);
    }

    /**
     * Parses the castling ability portion of the fen string.
     * <br><br>
     * Castling abilites are denoted by up to 4 characters.
     * <ul>
     *      <li>presence of a character means that this castling side is still available to the specified player</li>
     *      <li>K,k denotes castling kingside</li>
     *      <li> Q,q denotes castling queenside</li>
     *      <li>uppercase denotes white's abilities</li>
     *      <li>lowercase denotes black's abilities</li>
     *      <li> if no player has the ability to castle, '-' is written instead (only one character)
     * </ul>
     * 
     * @throws IllegalStateException if the token length is not within 1 to 4 characters
     */
    private void parseCastlingAbilities() {
        int tokenLength = castlingAbilitiesToken.length();

        if (tokenLength < 1 || tokenLength > 4) {
            throw new IllegalStateException(
                    "castling abilities must be denoted by 1 to 4 characters, not " + castlingAbilitiesToken.length());
        }

        char[] castlingAbilities = castlingAbilitiesToken.toCharArray();

        //no castling rights
        //'-' may not appear if tokenLenght
        //therefore it is not a switch case
        if (tokenLength == 1) {
            if (castlingAbilities[0] == '-') {
                //not strictly necessary as the flags are initialized as false
                whiteCastlingKingside = false;
                whiteCastlingQueenside = false;
                blackCastlingKingside = false;
                blackCastlingQueenside = false;
                return;
            }
        }

        //iterate over characters and set corresponding flags
        for (char castlingAbility : castlingAbilities) {
            switch (castlingAbility) {
            //uppercase characters are white 
            case 'K' -> whiteCastlingKingside = true;
            case 'Q' -> whiteCastlingQueenside = true;
            //lowercase characters are black
            case 'k' -> blackCastlingKingside = true;
            case 'q' -> blackCastlingQueenside = true;

            }

        }
    }

    /**
     * Parses the en passant target square portion of the fen string.
     * @throws IllegalStateException if the token extracted from fen does not consist of two characters
     */
    private void parseEnPassantTargetSquare() {

        if (enPassantTargetSquareToken.length() != 2) {
            //length != 2 => only "-" is a valid token
            if (enPassantTargetSquareToken.length() == 1 && enPassantTargetSquareToken.charAt(0) == '-') {
                enPassantTargetFile = -1;
                enPassantTargetRank = -1;
                return;
            }
            //skipped by above return if token is "-"
            throw new IllegalStateException(
                    "en passant target square must consist of two characters or \"-\", not " + enPassantTargetSquareToken.length());
        }

        if (enPassantTargetSquareToken.length() != 2) {
            throw new IllegalStateException(
                    "en passant target square must consist of two characters or \"-\", not " + enPassantTargetSquareToken.length());
        }

        //parse first character as file (column)
        //this character is in the range of a to h
        enPassantTargetFile = translateFileCharacter(enPassantTargetSquareToken.charAt(0));

        //parse second character as rank (row)
        //this character is already a number, but represented as character (e.g. '2')
        enPassantTargetRank = translateRankCharacter(enPassantTargetSquareToken.charAt(1));
    }

    /**
    * Translates the character denoting a square's rank (row) to the corresponding number.
    * This character is a number from 1 to 8 (e.g. '2').
    * @param c the character denoting a file
    * @return the corresponding number
    * @throws IllegalArgumentException if the character is not within '1' to '8'
    */
    private int translateRankCharacter(char c) {
        int rank = Character.getNumericValue(c);
        if (rank < 0 || rank > 8) {
            throw new IllegalArgumentException(
                    "en passant target rank must be a number (represented as character) in the range of '1' to '8', not " + c);
        }
        return rank;
    }

    /**
     * Translates the character denoting a square's file (column) to the corresponding number.
     * This character is a letter from a to h.
     * <br><br>
     * Characters in the range of a to h are mapped to integers in the range of 1 to 8.
     * <br><br>
     * E.g.: a -> 1, b -> 2, ... g -> 7, h -> 8
     * @param c the character denoting a file
     * @return the corresponding number
     * @throws IllegalArgumentException if the character is not within 'a' to 'h'
     */
    private int translateFileCharacter(char c) {
        int file = Character.getNumericValue(c) - 9;
        if (file < 0 || file > 8) {
            throw new IllegalArgumentException("en passant target file must be a character in the range of a to h, not " + c);
        }
        return file;
    }

    /**
     * Parses the half move count provided by a fen string.
     * @throws NumberFormatException if the token extracted from fen string is not a number
     */
    private void parseHalfMoveCount() {
        halfMoves = Integer.parseInt(halfMoveCountToken);
    }

    /**
     * Parses the half move count provided by a fen string.
    * @throws NumberFormatException if the token extracted from fen string is not a number
    */
    private void parseFullMoveCount() {
        fullMoves = Integer.parseInt(fullMoveCountToken);
    }

}