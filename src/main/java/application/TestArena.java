package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.ArrayBoard;
import model.ByteBoard;
import model.Move;
import model.Position;
import uciservice.Tokenizer;
import uciservice.UCIParserRandom;
import uciservice.UCITokenizer;

public class TestArena {
    private boolean contd = true;
    private List<Move> pastMoves;

    public TestArena(){
        pastMoves = new ArrayList<Move>();
    }

    private void start(){
        Position currentPosition = null;
        Scanner inputScanner = new Scanner(System.in);
        Tokenizer tokenizer = new UCITokenizer();
        while (contd){
            String input = inputScanner.nextLine();
            try {
                currentPosition = UCIParserRandom.executeCommand(tokenizer.tokenize(input), currentPosition, this);                
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
        new TestArena().start();
        return;
    }

    public void stop(){
        contd = false;
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
}
