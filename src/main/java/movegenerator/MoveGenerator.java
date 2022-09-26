package movegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import static model.PieceEncoding.*;

import model.Position;
import model.Board;

/**
 * In the end the only public method will be generatePossibleMoves, but for
 * testing purposes I have decided to keep the other methods public until then
 *      -johannes
 * it might be better to have the methods private and write a test class that extends this class
 * making the private methods publicly available for testing. this way the "production" class MoveGenerator
 * would not have to be adapted for testing
 *      - Moritz
 */
public abstract class MoveGenerator {

    public static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(9);

    /**
     * A method that generates all possible follow-up-positions for a given game
     * state
     * @param position the given game state
     * @return an array with all possible follow-up-positions (empty if no moves can be made)
     */
    // public static Position[] generatePossibleMoves(Position position) {
    //     // List<Position> followUpPositions = Collections.synchronizedList(new ArrayList<>());
    //     List<Position> followUpPositions = new ArrayList<>();
    //     List<Future> futureList = new ArrayList<>();
    //     List<List<Position>> resultListList = new ArrayList<>();
    //     Semaphore sem = new Semaphore(1);
    //     for (int rank = 0; rank < 8; rank++) {
    //         for (int file = 0; file < 8; file++) {
    //             List<Position> resultList = new ArrayList<>();
    //             Future f = generatePossibleMovesPerPieceMultithreaded(position, rank, file, resultList);
    //             if (f != null) {
    //                 futureList.add(f);
    //                 resultListList.add(resultList);
    //             }
    //         }
    //     }
    //     for (int i = 0; i < futureList.size(); i++){
    //         if (futureList.get(i) != null){
    //             try {
    //                 futureList.get(i).get();
    //                 followUpPositions.addAll(resultListList.get(i));
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //             }
    //         } else {
    //             throw new IllegalArgumentException();
    //         }
    //     }
    //     Position[] output = new Position[followUpPositions.size()];
    //     followUpPositions.toArray(output);
    //     return output;
    // }

    public static Position[] generatePossibleMoves(Position position) {
        List<Position> followUpPositions = new ArrayList<>();
        if (!position.isDraw()){
            List<Future<?>> futureList = new ArrayList<>();
            List<List<Position>> resultListList = new ArrayList<>();
            for (int rank = 0; rank < 8; rank++) {
                List<Position> resultList = new ArrayList<>();
                Future<?> f = generatePossibleMovesPerRow(position, rank, resultList);
                if (f != null) {
                    futureList.add(f);
                    resultListList.add(resultList);
                }
            }
            for (int i = 0; i < futureList.size(); i++){
                if (futureList.get(i) != null){
                    try {
                        futureList.get(i).get();
                        followUpPositions.addAll(resultListList.get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
        Position[] output = new Position[followUpPositions.size()];
        followUpPositions.toArray(output);
        return output;
    }

    /**
     * @param position the game for which to compute follow-ups
     * @param rank the horizontal coordinate of a given piece
     * @param file the vertical coordinate of a given piece
     * @return a set with all possible follow-up-positions for the given position
     * and the given piece
     */
    public static Future<?> generatePossibleMovesPerPieceMultithreaded(Position position, int rank, int file, List<Position> resultList) {
        byte currentPiece = position.getByteAt(rank, file);
        Future<?> result = null;
        Runnable runner = null;
        if (currentPiece == 0) {
            return result;
        }
        if (currentPiece < BLACK_BISHOP != position.getWhitesTurn()){
            return result;
        }
        switch (currentPiece) {
            case BLACK_BISHOP:
            case WHITE_BISHOP:
                runner = new BishopMoveGenerator(position, rank, file, resultList);
                result = executor.submit(runner);
                break;
            case BLACK_KING:
            case WHITE_KING:
                runner = new KingMoveGenerator(position, rank, file, resultList);
                result = executor.submit(runner);
                break;
            case BLACK_KNIGHT:
            case WHITE_KNIGHT:
                runner = new KnightMoveGenerator(position, rank, file, resultList);
                result = executor.submit(runner);
                break;
            case BLACK_PAWN:
            case WHITE_PAWN:
                runner = new PawnMoveGenerator(position, rank, file, resultList);
                result = executor.submit(runner);
                break;
            case BLACK_QUEEN:
            case WHITE_QUEEN:
                runner = new QueenMoveGenerator(position, rank, file, resultList);
                result = executor.submit(runner);
                break;
            case BLACK_ROOK:
            case WHITE_ROOK:
                runner = new RookMoveGenerator(position, rank, file, resultList);
                result = executor.submit(runner);
                break;
            default:
                break;
        }
        
        return result;
    }

    public static Future<?> generatePossibleMovesPerRow(Position position, int rank, List<Position> resultList) {
        Future<?> f = executor.submit(new RowMoveGenerator(position, rank, resultList));
        return f;
    }

    public static int generatePossibleMovesPerPiece(Position position, int rank, int file, List<Position> resultList){
        byte currentPiece = position.getByteAt(rank, file);
        Runnable runner = null;
        if (currentPiece == 0) {
            return -1;
        }
        if (currentPiece < BLACK_BISHOP != position.getWhitesTurn()){
            return -1;
        }
        switch (currentPiece) {
            case BLACK_BISHOP:
            case WHITE_BISHOP:
                runner = new BishopMoveGenerator(position, rank, file, resultList);
                break;
            case BLACK_KING:
            case WHITE_KING:
                runner = new KingMoveGenerator(position, rank, file, resultList);
                break;
            case BLACK_KNIGHT:
            case WHITE_KNIGHT:
                runner = new KnightMoveGenerator(position, rank, file, resultList);
                break;
            case BLACK_PAWN:
            case WHITE_PAWN:
                runner = new PawnMoveGenerator(position, rank, file, resultList);
                break;
            case BLACK_QUEEN:
            case WHITE_QUEEN:
                runner = new QueenMoveGenerator(position, rank, file, resultList);
                break;
            case BLACK_ROOK:
            case WHITE_ROOK:
                runner = new RookMoveGenerator(position, rank, file, resultList);
                break;
            default:
                return -1;
        }
        runner.run();
        return 0;
    }

    /**
      * Generates all legal horizontal and vertical moves (used for rook and queen).
      * @param position the position for which a follow-up position should be generated
      * @param rank the rank of the piece to be moved
      * @param file the file of the piece to be moved
      * @param isRook whether the piece is a rook
      * @return the positions that the generated moves result in
      */
    public static List<Position> computeHorizontalAndVerticalMoves(Position position, int rank, int file, boolean isRook) {
        List<Position> moves = new ArrayList<Position>();
        boolean blackCastlingKingside = position.getBlackCastlingKingside();
        boolean blackCastlingQueenside = position.getBlackCastlingQueenside();
        boolean whiteCastlingKingSide = position.getWhiteCastlingKingside();
        boolean whiteCastlingQueenside = position.getWhiteCastlingQueenside();

        if (isRook) {
            byte rook = position.getByteAt(rank, file);
            //nullpointer possible here
            if (rook != BLACK_ROOK && rook != WHITE_ROOK) {
                throw new IllegalStateException("tried to move a rook, but the piece on rank " + rank + " file " + file + " is a " + rook);
            }
            if (rook < BLACK_BISHOP) {
                //white rook
                if (file == 0) {
                    whiteCastlingQueenside = false;
                } else if (file == 7){
                    whiteCastlingKingSide = false;
                }
            } else {
                //black rook
                if (file == 0) {
                    blackCastlingQueenside = false;
                } else if (file == 7) {
                    blackCastlingKingside = false;
                }
            }
        }
        //left
        moves.addAll(computeRay(position, rank, file, -1, 0, whiteCastlingKingSide, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside));
        //right
        moves.addAll(computeRay(position, rank, file, 1, 0, whiteCastlingKingSide, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside));
        //up
        moves.addAll(computeRay(position, rank, file, 0, -1, whiteCastlingKingSide, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside));
        //down
        moves.addAll(computeRay(position, rank, file, 0, 1, whiteCastlingKingSide, whiteCastlingQueenside, blackCastlingKingside, blackCastlingQueenside));
        return moves;
    }

    /**
     * Generates all legal diagonal moves (used for bishop and queen).
     * @param position the position for which a follow-up position should be generated
     * @param rank the rank of the piece to be moved
     * @param file the file of the piece to be moved
     * @return the positions that the generated moves result in
     */

    public static List<Position> computeDiagonalMoves(Position position, int rank, int file) {
        List<Position> diagonalMoves = new ArrayList<Position>();
        //upper left
        diagonalMoves.addAll(computeRay(position, rank, file, -1, -1));
        //bottom left
        diagonalMoves.addAll(computeRay(position, rank, file, -1, 1));
        //upper right
        diagonalMoves.addAll(computeRay(position, rank, file, 1, -1));
        //bottom right
        diagonalMoves.addAll(computeRay(position, rank, file, 1, 1));
        return diagonalMoves;
    }

    public static boolean targetLegal(int targetRank, int targetFile, boolean isWhite, Position position){
        if ((targetRank < 0) || (targetRank > 7) || 
            (targetFile < 0) || (targetFile > 7)) {
                return false;
            }
        byte targetPiece = position.getByteAt(targetRank, targetFile);
        boolean targetPieceIsWhite;
        if (targetPiece != 0){
            targetPieceIsWhite = targetPiece < BLACK_BISHOP;
        } else {
            targetPieceIsWhite = !isWhite;
        }
        return (isWhite != targetPieceIsWhite);
    }

    /**
     * Shorthand for {@link #computeRay(Position, int, int, int, int, boolean, boolean, boolean, boolean)}
     * using the current castling rights (= the castling rights stored in currentPosition) 
     * @return a set containing the positions generated by moving along the ray
     */
    private static List<Position> computeRay(Position currentPosition, int rank, int file, int xOffset, int yOffset) {
        return computeRay(currentPosition, rank, file, xOffset, yOffset, currentPosition.getWhiteCastlingKingside(),
        currentPosition.getWhiteCastlingQueenside(), currentPosition.getBlackCastlingKingside(),
        currentPosition.getBlackCastlingQueenside());
    }

    /**
     * Generates a piece's legal moves along a ray facing the direction specified by xOffset and yOffset (e.g. upper left).
     * @param currentPosition the position to generate follow-up moves / positions for
     * @param startingRank starting rank of the piece
     * @param startingFile starting file of the piece
     * @param xOffset value added to x coordinate in each step, should range from -1 to 1
     * @param yOffset value added to y coordinate in each step, should range from -1 to 1
     * @param newWhiteCastlingKingside whether white may still castle kingside after this move
     * @param newWhiteCastlingQueenside whether white may still castle queenside after this move
     * @param newBlackCastlingKingside whether black may still castle kingside after this move
     * @param newBlackCastlingQueenside whether black may still castle queenside after this move
     * @return a set containing the positions generated by moving along the ray
     * @throws IllegalArgumentException if both xOffset and yOffset are 0,
     * which would result in generation of the same space over and over
     *
     */
    private static List<Position> computeRay(Position currentPosition, int startingRank, int startingFile, int xOffset, int yOffset,
            boolean newWhiteCastlingKingside, boolean newWhiteCastlingQueenside, boolean newBlackCastlingKingside,
            boolean newBlackCastlingQueenside) {
        if (xOffset == 0 && yOffset == 0) {
            throw new IllegalArgumentException("must specify an offset (other than 0,0) for ray generation");
        }

        byte piece = currentPosition.getByteAt(startingRank, startingFile);
        if (piece == 0) {
            throw new IllegalArgumentException("the specified square does not contain a piece");
        }
        List<Position> moves = new ArrayList<Position>();
        Position generatedPosition = null;
        Board newSpaces = null;
        boolean didCapture = false;

        int rank = startingRank;
        int file = startingFile;

        //while next step legal and last step did not capture
        while (targetLegal(rank + yOffset, file + xOffset, piece < BLACK_BISHOP, currentPosition) && !didCapture) {
            //get a new copy every time
            newSpaces = currentPosition.copyBoard();

            //leave starting space
            newSpaces.setByteAt(startingRank, startingFile, (byte)0);

            int targetFile = file + xOffset;
            int targetRank = rank + yOffset;

            //move to next space, capture by overwriting existing pieces if needed
            if (newSpaces.getByteAt(targetRank,targetFile) != 0) {
                didCapture = true;
            }
            newSpaces.setByteAt(targetRank,targetFile,piece);

            generatedPosition = currentPosition.generateFollowUpPosition(newSpaces, -1, -1, newWhiteCastlingKingside,
                    newWhiteCastlingQueenside, newBlackCastlingKingside, newBlackCastlingQueenside, didCapture);
            if (currentPosition.getWhiteNextMove()){
                if (!generatedPosition.getWhiteInCheck()){
                    generatedPosition.setMove(startingRank, startingFile, targetRank, targetFile);
                    generatedPosition.appendAncestor(currentPosition);
                    moves.add(generatedPosition);
                }
            } else {
                if (!generatedPosition.getBlackInCheck()){
                    generatedPosition.setMove(startingRank, startingFile, targetRank, targetFile);
                    generatedPosition.appendAncestor(currentPosition);
                    moves.add(generatedPosition);
                }
            }
            //current position is the space that was moved on
            rank += yOffset;
            file += xOffset;
        }

        return moves;
    }

    /**
     * <p>
     * This method gets a position, the coordinates for a piece and the coordinates
     * for where to place a piece after it has been moved, capturing other 
     * pieces occupying the target space in the progress.<br>
     * </p><p>
     * This method does not check for the legality of a move since that is the 
     * job of the compute[Piece]Moves()-methods like computeQueenMoves
     * </p>
     * Does not automically capture a pawn during en-passant, this needs to be
     * handled in computePawnMoves()
     * @param spaces the position on which to execute the move
     * @param startingRank the horizontal line in which the moving figure starts
     * @param startingFile the vertical line in which the moving figure starts
     * @param targetRank the horizontal line in which the moving figure ends
     * @param targetFile the vertical line in which the moving figure ends
     * @return the position after the move has been executed
     */
    public static Board getBoardAfterMove(Board spaces, int startingRank, int startingFile, int targetRank, int targetFile){
        byte movingPiece = spaces.getByteAt(startingRank,startingFile);
        spaces.setByteAt(startingRank,startingFile,(byte)0);
        spaces.setByteAt(targetRank,targetFile,movingPiece);
        return spaces;
    }


    public static List<Position> generatePossibleMovesPerPiece(Position position, int rank, int file) {
        List<Position> resultList = new ArrayList<>();
        Future<?> f = generatePossibleMovesPerPieceMultithreaded(position, rank, file, resultList);
        if (f != null){
            try {
                f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
}
