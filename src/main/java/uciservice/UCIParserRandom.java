package uciservice;

import java.util.List;
import java.util.Random;

import application.TestArena;
import gametree.GameNode;
import model.Move;
import model.Position;
import movegenerator.MoveGenerator;
import gametree.*;

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
                    // TODO: start searching
                    System.out.println("searching for best move for the next " +
                    Integer.parseInt(children.get(1).getData())/1000 + " seconds");
                    // Move testMove = new Move("a7a5");
                }

                // TODO: Find out why the certain UCIs generate Positions where check is ignored
                // e.g. "position startpos moves d2d3 d7d5 d3d4 e7e5 d4e5 f7f6 e5f6 c8f5 f6g7 b7b6 g7h8q b8d7 h8h7 f5e4 h7d7"
                // Position randomPosition = getRandomPosition(currentPosition);
                // UCIOperator.sendBestmove(randomPosition.getMove());
                // return randomPosition;
                GameNode b = new ImpGameTree(currentPosition, new GameNodeAlphaBetaPruning()).calculateBestMove(5);
                UCIOperator.sendBestmove(b.getContent().getMove());
                return b.getContent().clone();
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
                    currentPosition = FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR " +
                    "w KQkq - 0 1");
                    System.out.println("Starting position initialized");
                } else if (position.getType() == CommandType.CONSTANT){
                    // TODO: actually initialize this
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
                    for (int i = 2; i < children.size(); i++){
                        Command currentMove = children.get(i);
                        if (currentMove.getType() == CommandType.CONSTANT){
                            if(currentMove.getData().matches("[a-h][1-8][a-h][1-8](B|N|Q|R|b|n|q|r)?")){
                                if (currentMove.getData().length()==5 && currentPosition.getWhiteNextMove()){
                                    currentMove.setData(currentMove.getData().substring(0,4)
                                    + currentMove.getData().substring(4,5).toUpperCase());
                                }
                                // TODO: actually apply this
                                currentPosition = currentPosition.getFollowUpByMove(new Move(currentMove.getData()));
                                System.out.println("applying move "+currentMove.getData());
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

    private static Position getRandomPosition(Position position){
        // position.toggleWhiteNextMove();
        Position[] followUps = MoveGenerator.generatePossibleMoves(position);
        Random rand = new Random();
        return followUps[rand.nextInt(followUps.length)];
    }
}