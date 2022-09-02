package uciservice;

import java.util.List;

import model.Coordinate;
import model.Move;

public abstract class UCIParser{
    public static boolean executeCommand(Command command){
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
                if (children.get(0).getType() == CommandType.MOVETIME) {
                    // TODO: start searching
                    System.out.println("searching for best move for the next " +
                    Integer.parseInt(children.get(1).getData())/1000 + " seconds");
                    Move testMove = new Move("a7a5");
                    UCIOperator.sendBestmove(testMove);
                }
                break;
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
                Command position = children.get(0);
                if (position.getType() == CommandType.STARTPOS){
                    // TODO: actually initialize this
                    System.out.println("Starting position initialized");
                } else if (position.getType() == CommandType.CONSTANT){
                    // TODO: actually initialize this
                    System.out.println("Initializing position from FEN string");
                    break;
                } else {
                    System.out.println("Error: no position specified after \"position\" keyword");
                }
                if (children.size() > 1){
                    Command moves = children.get(1);
                    System.out.println("applying moves");
                    if (moves.getType() != CommandType.MOVES){
                        System.out.println("Error: expected \"moves\" keyword");
                    }
                    for (int i = 2; i < children.size(); i++){
                        Command currentMove = children.get(i);
                        if (currentMove.getType() == CommandType.CONSTANT){
                            if(currentMove.getData().matches("[a-h][1-8][a-h][1-8]")){
                                // TODO: actually apply this
                                System.out.println("applying move "+currentMove.getData());
                            }
                        }
                    }
                }
                break;
            case QUIT:
                break;
            case REGISTER:
                break;
            case SEARCHMOVES:
                break;
            case SETOPTION:
                break;
            case STARTPOS:
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
            case VALUE:
                break;
            case WINC:
                break;
            case WTIME:
                break;
            default:
                break;

        }
        return true;
    }
}