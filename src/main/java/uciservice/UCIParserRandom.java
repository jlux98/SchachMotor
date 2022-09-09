package uciservice;

import java.util.List;
import java.util.Random;

import application.TestArena;
import model.Move;
import model.Position;
import movegenerator.MoveGenerator;

public abstract class UCIParserRandom{
    public static Position executeCommand(Command command, Position currentPosition, TestArena testArena){
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

                if (children.size()>0 && children.get(0).getType() == CommandType.MOVETIME) {
                    System.out.println("searching for best move for the next " +
                    Integer.parseInt(children.get(1).getData())/1000 + " seconds");
                    // Move testMove = new Move("a7a5");
                }

                Position randomPosition = getRandomPosition(currentPosition);
                UCIOperator.sendBestmove(randomPosition.getMove());
                return randomPosition;
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
                    currentPosition = FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR " +
                    "w KQkq - 0 1");
                    System.out.println("Starting position initialized");
                } else if (position.getType() == CommandType.CONSTANT){
                    currentPosition = FenParser.parseFen(position.getData());
                    System.out.println("Initializing position from FEN string");
                } else {
                    System.out.println("Error: no position specified after \"position\" keyword");
                }
                if (children.size() > 1){
                    Command moves = children.get(1);
                    System.out.println("applying moves");
                    if (moves.getType() != CommandType.MOVES){
                        System.out.println("Error: expected \"moves\" keyword. Current Commandtype: "+moves.getType());
                    }
                    if (isTheSameGame(testArena, children)){
                        Command currentMoveCommand = children.get(children.size()-1);
                        if (currentMoveCommand.getData().length()==5 && currentPosition.getWhiteNextMove()){
                            currentMoveCommand.setData(currentMoveCommand.getData().substring(0,4)
                            + currentMoveCommand.getData().substring(4,5).toUpperCase());
                        }
                        Move currentMove = new Move(currentMoveCommand.getData());
                        currentPosition = currentPosition.getFollowUpByMove(currentMove);
                        testArena.appendMove(currentMove);
                        System.out.println("only move "+currentMoveCommand.getData() + " applied.");
                    } else {
                        for (int i = 2; i < children.size(); i++){
                            Command currentMoveCommand = children.get(i);
                            if (currentMoveCommand.getType() == CommandType.CONSTANT){
                                if(currentMoveCommand.getData().matches("[a-h][1-8][a-h][1-8](B|N|Q|R|b|n|q|r)?")){
                                    if (currentMoveCommand.getData().length()==5 && currentPosition.getWhiteNextMove()){
                                        currentMoveCommand.setData(currentMoveCommand.getData().substring(0,4)
                                        + currentMoveCommand.getData().substring(4,5).toUpperCase());
                                    }
                                    Move currentMove = new Move(currentMoveCommand.getData());
                                    currentPosition = currentPosition.getFollowUpByMove(currentMove);
                                    testArena.appendMove(currentMove);
                                    System.out.println("applying move "+currentMoveCommand.getData());
                                }
                            }
                        }
                    }
                }
                break;
            case QUIT:
                testArena.stop();
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
                testArena.stop();
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

    private static boolean isTheSameGame(TestArena testArena, List<Command> children) {
        String oldTurn = "";
        for (int i = 0; i < testArena.getMoves().size(); i++){
            oldTurn += testArena.getMoves().get(i) + " ";
        }
        oldTurn = oldTurn.toLowerCase();
        return false;
    }

    private static Position getRandomPosition(Position position){
        // position.toggleWhiteNextMove();
        Position[] followUps = MoveGenerator.generatePossibleMoves(position);
        Random rand = new Random();
        return followUps[rand.nextInt(followUps.length)];
    }
}