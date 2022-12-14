package uciservice;

import java.util.regex.Pattern;

import model.Position;
// import model.ArrayBoard;
import model.Board;
import model.ByteBoard;
import static model.PieceEncoding.*;

/**
 * Class used to translate FEN-Strings to instances of {@link Position}.
 */
public class FenParser {
    /**
     * separates portions of the fen string, such as position, castling rights, current player
     */
    private static final String FEN_DELIMITER = " ";
    /**
     * separates ranks in the position part of a fen string
     */
    private static final String RANK_DELIMITER = "/";

    //parser attributes
    private String fen;

    //position attributes
    private Board piecePositions;
    private boolean isWhiteNextMove;
    private boolean whiteCastlingKingside = false;
    private boolean whiteCastlingQueenside = false;
    private boolean blackCastlingKingside = false;
    private boolean blackCastlingQueenside = false;
    private int enPassantTargetRank;
    private int enPassantTargetFile;
    private byte halfMoves;
    private int fullMoves;

    /**
     * Constructs a new FenParser that can be used to parse the specified fen string to a Position by calling {@link #parseFen()}.
     * @param fen the fen string that should be translated into a position
     */
    public FenParser(String fen) {
        if (fen == null) {
            throw new NullPointerException("fen string may not be null");
        }
        this.fen = fen;
        this.piecePositions = ByteBoard.createEmpty();
    }

    /**
     * Parses the fen string and translates it into a instance of position.
     * <br><br>
     * This method is static and shorthand for
     * <pre>
     *FenParser parser = new FenParser(fen);
     *return parser.parseFen();
     * </pre>
     * @param fen the fen string that should be translated
     * @return an instance of Position representing the information that was stored in the fen string
     * @throws FenParseExcepton if the string could not be parsed
     */
    public static Position parseFen(String fen) {
        FenParser parser = new FenParser(fen);
        Position result = parser.parseFen();
        return result;
    }

    /**
     * Parses the fen string that was supplied to the constructor.
     * @return an instance of Position representing the information that was stored in the fen string
     */
    public Position parseFen() {
        parseTokens();
        return createPosition();
    }

    /**
     * Breaks up the fen string into blank-separated tokens and parses them accordingly.
     * The extracted information is stored in instance variables.
     */
    private void parseTokens() {
        String[] tokens = fen.split(Pattern.quote(FEN_DELIMITER));
        try {
            parsePosition(tokens[0]);
            parseActivePlayer(tokens[1]);
            parseCastlingAbilities(tokens[2]);
            parseEnPassantTargetSquare(tokens[3]);
            parseHalfMoveCount(tokens[4]);
            parseFullMoveCount(tokens[5]);
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new FenParseException("fen could not be split");
        }
    }

    /**
     * Constructs a position instance from the values extracted from a fen string.
     * @return an instance of Position representing the information that was stored in the fen string
     */
    private Position createPosition() {
        return new Position(piecePositions, isWhiteNextMove, whiteCastlingKingside, whiteCastlingQueenside,
                blackCastlingKingside, blackCastlingQueenside, enPassantTargetRank, enPassantTargetFile, halfMoves,
                fullMoves);

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
     * @param positionToken the fen token denoting the position of the pieces on the board
     */
    private void parsePosition(String positionToken) {
        //split token into ranks
        String[] ranks = positionToken.split(Pattern.quote(RANK_DELIMITER));
        int placeInRank = 0;
        for (String rank : ranks) {
            parseRank(rank, placeInRank);
            placeInRank++;
        }
    }

    /**
     * Parses a single rank extracted from a fen string.
     * @param rank a part of a fen string representing one rank
     * @param yPosition determines which rank on the board the pieces are placed in,
     * yPosition = 0 places in the first rank, yPosition = 7 in the last rank
     */
    private void parseRank(String rank, int yPosition) {
        int xPosition = 0;
        for (char character : rank.toCharArray()) {
            if (Character.isDigit(character)) {
                xPosition += Character.getNumericValue(character);
            } else {
                try {
                    byte piece = getBytePieceFromCharacter(character);
                    piecePositions.setByteAt(yPosition, xPosition, piece);
                    xPosition++;
                } catch (IllegalArgumentException exception) {
                    throw new FenParseException(exception);
                }
            }
        }
        if (xPosition != 8) {
            throw new FenParseException("rank is not filled");
        }
    }

    /**
     * Parses the active player portion of a fen string.
     * @param activePlayerToken the fen token denoting the active player
     * @throws FenParseException if the token is not "w" (white's turn) or "b" (black's turn)
     */
    private void parseActivePlayer(String activePlayerToken) {
        if (activePlayerToken.length() != 1) {
            throw new FenParseException(
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
        throw new FenParseException(
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
     * @param castlingAbilitiesToken the fen token denoting the castling abilities
     * @throws FenParseException if the token length is not within 1 to 4 characters
     */
    private void parseCastlingAbilities(String castlingAbilitiesToken) {
        int tokenLength = castlingAbilitiesToken.length();

        if (tokenLength < 1 || tokenLength > 4) {
            throw new FenParseException(
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
            default -> throw new FenParseException(
                    "castling abilities must be denoted by characters from KkQq, not " + castlingAbility);
            }

        }
    }

    /**
     * Parses the en passant target square portion of a fen string.
     * @param enPassantTargetSquareToken the fen token containing the en passant target square
     * @throws FenParseException if the token extracted from fen does not consist of two characters
     * and is not "-"
     */
    private void parseEnPassantTargetSquare(String enPassantTargetSquareToken) {

        if (enPassantTargetSquareToken.length() != 2) {
            //length != 2 => only "-" is a valid token
            if (enPassantTargetSquareToken.length() == 1 && enPassantTargetSquareToken.charAt(0) == '-') {
                enPassantTargetFile = -1;
                enPassantTargetRank = -1;
                return;
            }
            //skipped by above return if token is "-"
            throw new FenParseException(
                    "en passant target square must consist of two characters or \"-\", not "
                            + enPassantTargetSquareToken.length());
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
    * The character has to be a number from 1 to 8 (e.g. '2') and is mapped to 8 - its value.
    * This is neccessary because our array starts at index 0, ends at index 7 and counts upside down.
    * e.g.: 1 -> 7, 2 -> 6 ... 8 -> 0
    * @param c the character denoting a file
    * @return the corresponding number
    * @throws FenParseException if the character is not within '1' to '8'
    */
    private int translateRankCharacter(char c) {
        int rank = 8 - Character.getNumericValue(c);
        if (rank < 0 || rank > 7) {
            throw new FenParseException(
                    "en passant target rank must be a number (represented as character) in the range of '1' to '8', not "
                            + c);
        }
        return rank;
    }

    /**
     * Translates the character denoting a square's file (column) to the corresponding number.
     * This character is a letter from a to h.
     * <br><br>
     * Characters in the range of a to h are mapped to integers in the range of 0 to 7.
     * <br><br>
     * E.g.: a -> 0, b -> 1, ... g -> 6, h -> 7
     * @param c the character denoting a file
     * @return the corresponding number
     * @throws FenParseException if the character is not within 'a' to 'h'
     */
    private int translateFileCharacter(char c) {
        int file = Character.getNumericValue(c) - 10;
        if (file < 0 || file > 7) {
            throw new FenParseException(
                    "en passant target file must be a character in the range of a to h, not " + c);
        }
        return file;
    }

    /**
     * Attempts to parse the passed String as int using
     * <pre>Integer.parseInt(string)</pre>
     * Wraps any occuring NumberFormatExceptions in a FenParseException.
     * @param intString a string representing an int value
     * @throws FenParseException if parsing the string fails
     * @return the int represented by the string
     */
    private static int parseInt(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException exception) {
            throw new FenParseException(exception);
        }
    }

    /**
    * Attempts to parse the passed String as byte using
    * <pre>Byte.parseByte(byteString)</pre>
    * Wraps any occuring NumberFormatExceptions in a FenParseException.
    * @param byteString a string representing an byte value
    * @throws FenParseException if parsing the string fails
    * @return the byte represented by the string
    */
    private static byte parseByte(String byteString) {
        try {
            return Byte.parseByte(byteString);
        } catch (NumberFormatException exception) {
            throw new FenParseException(exception);
        }
    }

    /**
     * Parses the half move count provided by a fen string.
     * @param halfMoveCountToken the fen token denoting the half move count
     * @throws FenParseException if the token is not a number
     */
    private void parseHalfMoveCount(String halfMoveCountToken) {
        halfMoves = FenParser.parseByte(halfMoveCountToken);
        if (halfMoves < 0) {
            throw new FenParseException("full move count must be >= 0");
        }
    }

    /**
     * Parses the half move count provided by a fen string.
     * @param fullMoveCountToken the fen token denoting the full move count
    * @throws FenParseException if the token is not a number
    */
    private void parseFullMoveCount(String fullMoveCountToken) {
        fullMoves = FenParser.parseInt(fullMoveCountToken);
        if (fullMoves < 1) {
            throw new FenParseException("full move count must be >= 1");
        }
    }

}
