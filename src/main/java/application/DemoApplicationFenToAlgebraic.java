package application;

import java.util.Scanner;

import gametree.GameNode;
import gametree.GameNodeAlphaBetaPruning;
import gametree.GameTree;
import gametree.ImpGameTree;
import model.Move;
import model.Position;
import uciservice.FenParser;

public class DemoApplicationFenToAlgebraic {
    private Scanner scanner;

    public DemoApplicationFenToAlgebraic() {
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        System.out.println("""
                This application reads FEN-strings from stdin and answers with a move in algebraic form.
                Enter exit or hit enter without entering anything to exit.
                """);
        new DemoApplicationFenToAlgebraic().run();
    }

    private Position readPosition() {
        while (true) {
            System.out.println("\nenter fen:");
            String readFen = scanner.nextLine();
            if (readFen.equals("") || readFen.equals("exit")) {
                return null;
            }
            Position readPosition = FenParser.parseFen(readFen);
            return readPosition;
        }
    }

    private Move calculateMove(Position position) {
        GameTree tree = new ImpGameTree(GameNode.createRoot(position), new GameNodeAlphaBetaPruning());
        return tree.calculateBestMove(5).getContent().getMove();
    }

    private void run() {
        while (true) {
            try {
                Position readPosition = readPosition();
                if (readPosition == null) {
                    System.out.println("...exiting");
                    return; //exit
                }
                String movedColor = readPosition.getWhiteNextMove() ? "white" : "black";
                Move calculatedMove = calculateMove(readPosition);
                System.out.println(movedColor + " -> " + calculatedMove.toStringAlgebraic());
            } catch (Exception exception) {
                System.out.println("\tfailure: " + exception.getMessage());
            }
        }
    }
}
