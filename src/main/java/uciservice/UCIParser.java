package uciservice;

import java.util.List;

import model.Coordinate;
import model.Move;

public abstract class UCIParser {
    public static boolean executeCommand(Command command) {
        List<Command> children = command.getChildren();
        switch (command.getType()) {
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
                if (children.get(0).getType() == CommandType.MOVETIME) {
                    System.out.println("searching for best move for the next " +
                        Integer.parseInt(children.get(1).getData()) /
                        1000 + "seconds");
                }
                break;
            case ISREADY:
                System.out.println("readyok");
                break;
            case POSITION:
                if (children == null) {
                    System.out.println("Error: nothing found after position");
                }
                Command position = children.get(0);
                if (position.getType() == CommandType.STARTPOS) {
                    System.out.println("Starting position initialized");
                } else if (position.getType() == CommandType.CONSTANT) {
                    System.out.println("Initializing position from FEN string");
                    break;
                } else {
                    System.out.println("Error: no position specified after \"position\" keyword");
                }
                if (children.size() > 1) {
                    Command moves = children.get(1);
                    System.out.println("applying moves");
                    if (moves.getType() != CommandType.MOVES) {
                        System.out.println("Error: expected \"moves\" keyword.");
                        return true;
                    }
                    for (int i = 2; i < children.size(); i++) {
                        Command currentMove = children.get(i);
                        if (currentMove.getType() == CommandType.CONSTANT) {
                            if (currentMove.getData().matches("[a-h][1-8][a-h][1-8]")) {
                                System.out.println("applying move " + currentMove.getData());
                            }
                        }
                    }
                }
                break;
            case STOP:
                UCIOperator.sendBestmove(new Move(new Coordinate(1, 0), new Coordinate(3, 0)));
                return false;
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
        return true;
    }
}