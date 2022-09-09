package uciservice;

import java.util.List;

import application.Conductor;
import model.Move;
import model.Position;

public abstract class UCIParserAlphaBetaPruning{

    private final static String STARTPOS = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static Position executeCommand(Command command, Position currentPosition, Conductor conductor){
        List<Command> children = command.getChildren();
        switch(command.getType()){
            case BINC:
                break;
            case BTIME:
                break;
            case CODE:
                break;
            case CONSTANT:
                break;
            case DEBUG:
                break;
            case DEPTH:
                break;
            case FEN:
                break;
            case GO:
                /*
                 * Supported arguments:
                 * - searchmoves
                 * - ponder (if there is time to implement this)
                 * - depth
                 * - nodes?
                 * - movetime
                 * - infinite
                 */

                // For debugging purposes we verify, that the engine has reade the number of remaining seconds without error
                if (children.size()>0 && children.get(0).getType() == CommandType.MOVETIME) {
                    System.out.println("searching for best move for the next " +
                    Integer.parseInt(children.get(1).getData())/1000 + " seconds");
                }

                return conductor.calculateBestMove(currentPosition);
            case INFINITE:
                break;
            case ISREADY:
                System.out.println("readyok");
                break;
            case LATER:
                break;
            case MATE:
                break;
            case MOVES:
                break;
            case MOVESTOGO:
                break;
            case MOVETIME:
                break;
            case NAME:
                break;
            case NODES:
                break;
            case PONDER:
                break;
            case PONDERHIT:
                break;
            case POSITION:
                if (children == null){
                    System.out.println("Error: nothing found after position");
                }
                /* 
                 * The first child of a position-command should be startpos or a
                 * constant containing a fen string
                 */
                if (!isTheSameGame(conductor.getStartingPosition(), conductor.getMoves(), children)){
                    Command position = children.get(0);
                    if (position.getType() == CommandType.STARTPOS){
                        // if it is startpos we initiate the default starting position
                        currentPosition = FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR " +
                        "w KQkq - 0 1");
                        if (conductor.getStartingPosition() == null){
                            conductor.setStartingPosition("startpos");
                        }
                        System.out.println("Starting position initialized");
                    } else if (position.getType() == CommandType.CONSTANT){
                        // if it is a constant we initialize that fen string
                        currentPosition = FenParser.parseFen(position.getData());
                        if (conductor.getStartingPosition() == null){
                            conductor.setStartingPosition(position.getData());
                        }
                        System.out.println("Initializing position from FEN string");
                    } else {
                        System.out.println("Error: no position specified after \"position\" keyword");
                    }
                }
                /* 
                 * If there is more than 1 child then those children are moves
                 * to apply to the position
                 */
                if (children.size() > 1){
                    currentPosition = applyPositionMoves(command, conductor, currentPosition);
                }
                break;
            case QUIT:
                conductor.quit();
                return currentPosition;
            case REGISTER:
                break;
            case SEARCHMOVES:
                break;
            case SETOPTION:
                break;
            case STARTPOS:
                break;
            case STOP:
                // UCIOperator.sendBestmove(new Move(new Coordinate(1, 0), new Coordinate(3, 0)));
                conductor.stop();
                return currentPosition;
            case UCI:
                UCIOperator.sendId("SchachMotor", "lux&schoenenberger");
                System.out.println("uciok");
                break;
            case UCINEWGAME:
                System.out.println("*insert preparations for a new game");
                break;
            case VALUE:
                break;
            case WINC:
                break;
            case WTIME:
                break;
            default:
                break;

        }
        return currentPosition;
    }

    private static Position applyPositionMoves(Command command, Conductor conductor, Position currentPosition) {
        List<Command> children = command.getChildren();
        Command moves = children.get(1);
        System.out.println("applying moves");
        if (moves.getType() != CommandType.MOVES){
            System.out.println("Error: expected \"moves\" keyword. Current Commandtype: "+moves.getType());
        }
        if (isTheSameGame(conductor.getStartingPosition(), conductor.getMoves(), children)){
            // for (int i = 0; i < 2; i++) {
                Command currentMoveCommand = children.get(children.size()-1);
                if (currentMoveCommand.getData().length()==5 && currentPosition.getWhiteNextMove()){
                    currentMoveCommand.setData(currentMoveCommand.getData().substring(0,4)
                    + currentMoveCommand.getData().substring(4,5).toUpperCase());
                }
                Move currentMove = new Move(currentMoveCommand.getData());
                currentPosition = currentPosition.getFollowUpByMove(currentMove);
                conductor.appendMove(currentMove);
            // }
            System.out.println("only " + currentMove.toStringAlgebraic() + " was applied.");
        } else {
            if (children.get(0).getType() == CommandType.STARTPOS){
                conductor.setStartingPosition("startpos");
            } else {
                conductor.setStartingPosition(children.get(0).getData());
            }
            for (int i = 2; i < children.size(); i++){
                Command currentMoveCommand = children.get(i);
                if (currentMoveCommand.getType() == CommandType.CONSTANT){
                    if(currentMoveCommand.getData().matches("[a-h][1-8][a-h][1-8](B|N|Q|R|b|n|q|r)?")){
                        if (currentMoveCommand.getData().length()==5 && currentPosition.getWhiteNextMove()){
                            currentMoveCommand.setData(currentMoveCommand.getData().substring(0,4)
                            + currentMoveCommand.getData().substring(4,5).toUpperCase());
                        }
                        // TODO: actually apply this
                        Move currentMove = new Move(currentMoveCommand.getData());
                        currentPosition = currentPosition.getFollowUpByMove(currentMove);
                        conductor.appendMove(currentMove);
                        System.out.println("applying move "+currentMoveCommand.getData());
                    }
                }
            }
        }
        return currentPosition;
    }

    // TODO: make private after testing
    public static boolean isTheSameGame(String startingPosition, List<Move> oldTurn, List<Command> children) {
        if (startingPosition == null){
            // System.out.println("Not the same because starting position not set");
            return false;
        }
        if (children.get(0).getType() == CommandType.STARTPOS && !startingPosition.equals("startpos")){
            if (children.get(0).getType() == CommandType.STARTPOS && !startingPosition.equals(STARTPOS)){
                if (!startingPosition.equals(children.get(0).getData())){
                    // System.out.println("Not the same because starting position differs");
                    return false;
                }
            }
        }
        if (oldTurn.size() != children.size() - 3){
            // System.out.println("not the same because wrong length");
            return false;
        }
        for (int i = 0; i + 3 < children.size(); i++){
            if (!oldTurn.get(i).toStringAlgebraic().toLowerCase().equals(children.get(i+2).getData())){
                // System.out.println("Not the same because turn " + i + "differs");
                return false;
            }
        }
        return true;
    }
}