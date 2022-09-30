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

    public static volatile Position bestFollowUp;
    public static volatile boolean stopCalculating;
    public static volatile int depthCompleted;


    private static List<String> pastPositions = new ArrayList<>();
    private String startingPosition;

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
        appendPosition(bestFollowUp.clone().toStringLight());
        appendMove(bestFollowUp.getMove().clone());
        cleanup();
        System.gc();
    }

    public void quit(){
        contd = false;
    }

    public static void cleanup(){
        // System.out.println("cleaning up");
        bestFollowUp = null;
        depthCompleted = 0;
        stopCalculating = false;
    }

    public String getStartingPosition(){
        return startingPosition;
    }

    public void setStartingPosition(String startingPosition){
        this.startingPosition = startingPosition;
    }

    public static void appendPosition(String position){
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

    public static List<String> getPastPositions(){
        return pastPositions;
    }

    public void calculateBestMove(Position currentPosition, int secondsToCompute) {
        if(!stopCalculating){
            return;
        }
        stopCalculating = false;
        GameTreeEvaluator evaluator = new GameNodeMoveOrderingSelfDestructingAlphaBetaPruning();
        GameTree tree = new ImpGameTree(currentPosition, evaluator);
        MoveGenerator.executor.submit(new IterativeDeepeningRunner(tree, evaluator, currentPosition.getWhiteNextMove(), secondsToCompute, 7));
    }
}
