package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import minimax.GameNodeSelfDestructingAlphaBetaPruning;
import minimax.GameTreeEvaluator;
import minimax.MoveOrderingSelfDestructingAlphaBetaPruning;
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
    private List<Move> pastMoves;
    private String startingPosition;
    private GameTree currentGameTree;

    public Conductor(){
        pastMoves = new ArrayList<Move>();
    }

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

    public void appendMove(Move move){
        pastMoves.add(move);
    }

    public List<Move> getMoves (){
        return pastMoves;
    }

    public void emptyList(){
        pastMoves = new ArrayList<Move>();
    }

    public Position calculateBestMove(Position currentPosition) {
        GameTree tree = new ImpGameTree(currentPosition, (GameTreeEvaluator) new MoveOrderingSelfDestructingAlphaBetaPruning<Position>());
        GameNode bestChild = tree.calculateBestMove(6);
        UCIOperator.sendBestmove(bestChild.getRepresentedMove());
        appendMove(new Move(bestChild.getRepresentedMove().toStringAlgebraic()));
        return bestChild.getContent().clone();
    }
}
