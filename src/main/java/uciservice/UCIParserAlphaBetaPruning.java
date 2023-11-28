package uciservice;

import java.util.List;

import application.Conductor;
import model.Move;
import model.Position;

public abstract class UCIParserAlphaBetaPruning{

    private final static String STARTPOS = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static Position executeCommand(Command command, Position currentPosition, Conductor conductor){
        if (command == null){
            return currentPosition;
        }
        List<Command> children = command.getChildren();
        switch(command.getType()){
            case GO:
                /* Supported arguments:
                   x searchmoves
                   x ponder (if there is time to implement this)
                   x depth
                   x nodes?
                   o movetime
                   x infinite*/
                // For debugging purposes we verify that the engine has read the number of remaining seconds without error
                if (children.size()>0 && children.get(0).getType() == CommandType.MOVETIME) {
                    System.out.println("searching for best move for the next " +
                    Integer.parseInt(children.get(1).getData())/1000 + " seconds");
                }

                conductor.calculateBestMove(currentPosition);
                break;

            case ISREADY:
                System.out.println("readyok");
                break;

            case POSITION:
                if (children == null){
                    System.out.println("Error: nothing found after position");
                }
            /*  The first child of a position-command should be startpos or a
                constant containing a fen string */
                Command position = children.get(0);
                if (position.getType() == CommandType.STARTPOS){
                    // if it is startpos we initiate the default starting position
                    currentPosition = FenParser.parseFen(STARTPOS);
                    System.out.println("Starting position initialized");
                } else if (position.getType() == CommandType.FEN){
                    // if it is a constant we initialize that fen string
                    currentPosition = FenParser.parseFen(position.getData());
                    System.out.println("Initializing position from FEN string");
                } else {
                    System.out.println("Error: no position specified after \"position\" keyword");
                }
                /*  If there is more than 1 child then those children are moves
                    to apply to the position*/
                if (children.size() > 1){
                    currentPosition = applyPositionMoves(command, conductor, currentPosition);
                }
                break;

            case QUIT:
                conductor.quit();
                break;

            case STOP:
                // UCIOperator.sendBestmove(new Move(new Coordinate(1, 0), new Coordinate(3, 0)));
                Conductor.stop();
                break;

            case UCI:
                UCIOperator.sendId("SchachMotor", "lux&schoenenberger");
                System.out.println("uciok");
                break;

            case UCINEWGAME:
                System.out.println("*insert preparations for a new game");
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
        // if (isTheSameGame(conductor.getStartingPosition(), Conductor.getMoves(), children)){
        //     // for (int i = 0; i < 2; i++) {
        //         Command currentMoveCommand = children.get(children.size()-1);
        //         if (currentMoveCommand.getData().length()==5 && currentPosition.getWhiteNextMove()){
        //             currentMoveCommand.setData(currentMoveCommand.getData().substring(0,4)
        //             + currentMoveCommand.getData().substring(4,5).toUpperCase());
        //         }
        //         Move currentMove = new Move(currentMoveCommand.getData());
        //         currentPosition = currentPosition.getFollowUpByMove(currentMove);
        //         Conductor.appendPosition(currentPosition.toStringLight());
        //         Conductor.appendMove(currentMove);
        //     // }
        //     System.out.println("only " + currentMove.toStringAlgebraic() + " was applied.");
        // } else {
            Conductor.emptyPositionList();
            Conductor.emptyMoveList();
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
                        Move currentMove = new Move(currentMoveCommand.getData());
                        Conductor.appendPosition(currentPosition.toStringLight());
                        currentPosition = currentPosition.getFollowUpByMove(currentMove);
                        Conductor.appendMove(currentMove);
                        System.out.println("applying move "+currentMoveCommand.getData());
                    }
                }
            }
        // }
        return currentPosition;
    }

    // public static boolean isTheSameGame(String startingPosition, List<Move> oldTurn, List<Command> children) {
    //     if (startingPosition == null){
    //         // System.out.println("Not the same because starting position not set");
    //         return false;
    //     }
    //     if (children.get(0).getType() == CommandType.STARTPOS && !startingPosition.equals("startpos")){
    //         if (children.get(0).getType() == CommandType.STARTPOS && !startingPosition.equals(STARTPOS)){
    //             if (!startingPosition.equals(children.get(0).getData())){
    //                 // System.out.println("Not the same because starting position differs");
    //                 return false;
    //             }
    //         }
    //     }
    //     if (oldTurn.size() != children.size() - 4){
    //         // System.out.println("not the same because wrong length");
    //         return false;
    //     }
    //     for (int i = 0; i + 4 < children.size(); i++){
    //         if (!oldTurn.get(i).toStringAlgebraic().toLowerCase().equals(children.get(i+2).getData())){
    //             // System.out.println("Not the same because turn " + i + "differs");
    //             return false;
    //         }
    //     }
    //     return true;
    // }
}