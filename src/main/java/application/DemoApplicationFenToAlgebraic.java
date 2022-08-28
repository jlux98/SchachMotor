package application;

import java.util.Scanner;

import gametree.GameNode;
import gametree.GameNodeAlphaBetaPruning;
import gametree.GameTree;
import gametree.ImpGameTree;
import model.Position;
import uciservice.FenParser;

public class DemoApplicationFenToAlgebraic {
    private Scanner scanner;
    private boolean debugMode = false;
    private static final int calculationDepth = 5;

    public DemoApplicationFenToAlgebraic(boolean debug) {
        this.scanner = new Scanner(System.in);
        this.debugMode = debug;
    }

    public static void main(String[] args) {

        System.out.println("""
                This application reads FEN-strings from stdin and answers with a move in algebraic form.
                Enter exit or hit enter without entering anything to exit.
                """);

        boolean debug = false;
        //use debug mode if started with argument "debug"
        if (args.length == 1) {
            if (args[0].equals("debug")) {
                debug = true;
                System.out.println("debug mode activated");
            }
        }

        new DemoApplicationFenToAlgebraic(debug).run();
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

    private Position calculateFollowUpPosition(Position position) {
        GameTree tree = new ImpGameTree(GameNode.createRoot(position), new GameNodeAlphaBetaPruning());
        return tree.calculateBestMove(calculationDepth).getContent();
    }

    private void output(Position position, String colorMoved) {
        System.out.println(colorMoved + " -> " + position.getMove().toStringAlgebraic());
        if (debugMode) {
            System.out.println("internal board:\n" + position.toString());
        }
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
                Position calculatedMove = calculateFollowUpPosition(readPosition);
                output(calculatedMove, movedColor);
                System.gc();
            } catch (Exception exception) {
                System.out.println("\tfailure: ");
                exception.printStackTrace();
            }
        }
    }
}
