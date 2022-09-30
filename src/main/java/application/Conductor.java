package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import gametree.GameTree;
import gametree.ImpGameTree;
import minimax.GameNodeMoveOrderingSelfDestructingAlphaBetaPruning;
import minimax.GameTreeEvaluator;
import minimax.IterativeDeepeningRunner;
import model.ArrayBoard;
import model.ByteBoard;
import model.Move;
import model.Position;
import movegenerator.MoveGenerator;
import uciservice.Tokenizer;
import uciservice.UCIOperator;
import uciservice.UCIParserAlphaBetaPruning;
import uciservice.UCITokenizer;
import utility.TimerRunner;

public class Conductor {
    private boolean contd = true;
    /**
     * clarify if this list should just contain Moves or the whole
     * position (would cost more memory but make finding draws easier when 
     * checking if the same Position occurs thrice in the list)*/
    private static List<Move> pastMoves = new ArrayList<Move>();
    private static List<Position> pastPositions = new ArrayList<>();

    public static volatile Position bestFollowUp;
    public static volatile boolean stopCalculating;
    public static volatile int depthCompleted;

    private String startingPosition;
    // TODO: make sure only one calculation runs at a time

    private void start(){
        stopCalculating = true;
        System.out.println("New Conductor entering the stage.");
        Position currentPosition = null;
        Scanner inputScanner = new Scanner(System.in);
        Tokenizer tokenizer = new UCITokenizer();
        while (contd){
            String input = inputScanner.nextLine();
            try {
                currentPosition = UCIParserAlphaBetaPruning.executeCommand(tokenizer.tokenize(input), currentPosition, this);                
                if (currentPosition != null){
                    if (currentPosition.getBoard() instanceof ArrayBoard){
                        System.out.println("Using ArrayBoards");
                    } else if (currentPosition.getBoard() instanceof ByteBoard){
                        System.out.println("Using ByteBoards");
                    }
                }    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        inputScanner.close();
        return;



    }
    public static void main(String[] args) {
        new Conductor().start();
        return;
    }

    public static void stop(){
        stopCalculating = true;
        System.out.println("Highest depth completed: " + depthCompleted);
        UCIOperator.sendBestmove(bestFollowUp.getMove());
        appendPosition(bestFollowUp.clone());
        appendMove(bestFollowUp.getMove().clone());
        cleanup();
    }

    public void quit(){
        contd = false;
    }

    public static void cleanup(){
        System.out.println("cleaning up");
        bestFollowUp = null;
        depthCompleted = 0;
    }

    public String getStartingPosition(){
        return startingPosition;
    }

    public void setStartingPosition(String startingPosition){
        this.startingPosition = startingPosition;
    }

    public static void appendPosition(Position position){
        pastPositions.add(position);
    }

    public static void appendMove(Move move){
        pastMoves.add(move);
    }

    public static void deleteLastPosition(){
        pastPositions.remove(pastPositions.size()-1);
    }

    public static List<Move> getMoves (){
        return pastMoves;
    }

    public static void emptyMoveList(){
        pastMoves.clear();
    }

    public static void emptyPositionList(){
        pastPositions.clear();
    }

    public static List<Position> getPastPositions(){
        return pastPositions;
    }

    public void calculateBestMove(Position currentPosition) {
        if(!stopCalculating){
            return;
        }
        stopCalculating = false;
        GameTreeEvaluator evaluator = new GameNodeMoveOrderingSelfDestructingAlphaBetaPruning();
        GameTree tree = new ImpGameTree(currentPosition, evaluator);
        MoveGenerator.executor.submit(new TimerRunner(30));
        MoveGenerator.executor.submit(new IterativeDeepeningRunner(tree, evaluator, currentPosition.getWhitesTurn(), 30, 7));
    }
}
