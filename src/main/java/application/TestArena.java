package application;

import java.util.Scanner;

import uciservice.Tokenizer;
import uciservice.UCIParser;
import uciservice.UCITokenizer;

public class TestArena {
    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        boolean contd = true;
        Tokenizer tokenizer = new UCITokenizer();
        while (contd){
            String input = inputScanner.nextLine();
            UCIParser.executeCommand(tokenizer.tokenize(input));
        }
        inputScanner.close();
    }
}
