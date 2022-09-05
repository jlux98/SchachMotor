package application;

import java.util.List;
import java.util.Scanner;

import model.Position;
import uciservice.Command;
import uciservice.Tokenizer;
import uciservice.UCIParserRandom;
import uciservice.UCITokenizer;

public class TestArena {
    private boolean contd = true;
    // TODO: track past moves so that we don't need to recalculate the board from scratch each time
    private List<Command> pastMoves;

    private void start(){
        Position currentPosition = null;
        Scanner inputScanner = new Scanner(System.in);
        Tokenizer tokenizer = new UCITokenizer();
        while (contd){
            String input = inputScanner.nextLine();
            try {
                currentPosition = UCIParserRandom.executeCommand(tokenizer.tokenize(input), currentPosition, this);                
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
}
