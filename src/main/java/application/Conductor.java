package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import minimax.GameNodeSelfDestructingAlphaBetaPruning;
import minimax.GameTreeEvaluator;
import minimax.MoveOrderingSelfDestructingAlphaBetaPruningPastPositions;
import minimax.GameNodeMoveOrderingSelfDestructingAlphaBetaPruning;
import model.ArrayBoard;
import model.ByteBoard;
import model.Move;
import model.Position;
import positionevaluator.Evaluator;
import uciservice.Tokenizer;
import uciservice.UCIOperator;
import uciservice.UCIParserAlphaBetaPruning;
import uciservice.UCITokenizer;

import gametree.*;

public class Conductor {
    private boolean contd = true;
    /**
     * clarify if this list should just contain Moves or the whole
     * position (would cost more memory but make finding draws easier when 
     * checking if the same Position occurs thrice in the list)*/
    private static List<Move> pastMoves = new ArrayList<Move>();
    private static List<Position> pastPositions = new ArrayList<>();
    private String startingPosition;
    private GameTree currentGameTree;
    // TODO: make sure only one calculation runs at a time
    private boolean isCalculating;

    private void start(){
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

    public void stop(){
        currentGameTree.stop();
    }

    public void quit(){
        contd = false;
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

    public Position calculateBestMove(Position currentPosition) {
        GameTreeEvaluator evaluator = new GameNodeMoveOrderingSelfDestructingAlphaBetaPruning();
        GameTree tree = new ImpGameTree(currentPosition, evaluator);
        GameNode bestChild = tree.calculateBestMove(7);
        UCIOperator.sendBestmove(bestChild.getRepresentedMove());
        appendPosition(bestChild.getContent().clone());
        appendMove(bestChild.getRepresentedMove().clone());
        return bestChild.getContent().clone();
    }
}
