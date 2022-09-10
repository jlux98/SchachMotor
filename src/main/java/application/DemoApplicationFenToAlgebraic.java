package application;

import java.util.NoSuchElementException;
import java.util.Scanner;

import gametree.GameNode;
import gametree.ImpGameTree;
import minimax.GameNodeAlphaBetaPruning;
import minimax.GameTreeEvaluator;
import model.Position;
import uciservice.FenParseException;
import uciservice.FenParser;

public class DemoApplicationFenToAlgebraic {
    private Scanner scanner;
    private boolean debugMode = false;
    private boolean printBoards = false;

    /**
     * depth will not be configurable per fen-string if this is set
     */
    private boolean setDefaultDepth = false;
    private int depth = -1;

    /**
    * algorithm will not be configurable per fen-string if this is set
    */
    private boolean setDefaultAlgorithm = false;
    private String algorithmName;
    private GameTreeEvaluator evaluator;

    public DemoApplicationFenToAlgebraic() {
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        System.out.println("""
                This application reads FEN-strings from stdin and answers with a move in algebraic form.
                Enter exit or hit enter without entering anything to exit.
                Possible options are:
                    -debug - use debug mode (prints additional information)
                    -depth <n> - always use a depth of n
                    -algorithm <algorithm> - always use the specified algorithm
                    -printboards - print the board representing the calculated move
                """);
        DemoApplicationFenToAlgebraic demo = new DemoApplicationFenToAlgebraic();
        demo.readArguments(args);
        demo.run();
    }

    /**
     * Must be called with the command line arguments after instantiation.
     * @param args the command line arguments passed to the program
     */
    private void readArguments(String[] args) {
        String arg;
        for (int i = 0; i < args.length; i++) {
            arg = args[i].toLowerCase(); //ignore case

            switch (arg) {
                case "-debug" -> debugMode = true;
                case "-depth", "-defaultDepth" -> {
                    parseDefaultDepthArgument(args, i);
                    i++;
                }
                case "-algorithm" -> {
                    parseDefaultAlgorithmArgument(args, i);
                    i++;
                }
                case "-printboards" -> {
                    printBoards = true;
                }
                default -> {
                    System.out.println("not recognized: " + arg);
                    shutdown();
                }
            }

        }
        printConfiguration();
    }

    /**
     * Sets setDefaultDepth to true.
     * Attempts parsing the passed String to a number and using it
     * as default depth.
     * @param args the command line arguments
     * @param index the index of the "-depth" switch in the argument array
     */
    private void parseDefaultDepthArgument(String[] args, int index) {
        //remember that changing index here will not affect the calling method
        boolean failure = false;
        try {
            if (args.length > index + 1) {
                depth = Integer.parseInt(args[index + 1]);
                setDefaultDepth = true;
            } else {
                failure = true;
            }
        } catch (NumberFormatException exception) {
            failure = true;
        }
        if (failure) {
            System.out.println("argument after -depth or -defaultDepth must be a number");
            shutdown();
        }

    }

    /**
     * Sets setDefaultAlgorithm to true.
     * Attempts to parse the String argument to one of the supported evaluation strategies.
     * Will use the specified algorithm if supported and shutdown otherwise.
     * @param args the command line arguments
     * @param index the index of the "-algorithm" switch in the argument array
     */
    private void parseDefaultAlgorithmArgument(String[] args, int index) {

        boolean failure = false;
        if (args.length > index + 1) {
            String algorithmArg = args[index + 1].toLowerCase();
            setDefaultAlgorithm = true;
            try {
                useAlgorithm(algorithmArg);
            } catch (NoSuchElementException exception) {
                failure = true;
            }
        } else {
            failure = true;
        }

        if (failure) {
            System.out.println("argument after -algorithm must be a supported algorithm name");
            shutdown();
        }
    }

    /**
     * Checks if the String represents a known algorithm.
     * If it is, a new instance will be set as Evaluator.
     * @throws NoSuchElementException if the String does not match
     * an algorithm
     * @param algorithm the name of the algorithm to use
     */
    private void useAlgorithm(String algorithm) {
        switch (algorithm) {
            case "alphabeta", "alpha-beta", "alpha-beta-pruning" -> {
                algorithmName = "Alpha-Beta-Pruning";
                evaluator = new GameNodeAlphaBetaPruning();
            }
            default -> {
                throw new NoSuchElementException("algorithm could not be found");
            }
        }
    }

    private void useDefaultAlgorithm() {
        algorithmName = "limited Alpha-Beta"; //TODO update default algorithm
        evaluator = new GameNodeAlphaBetaPruning();
    }

    /**
     * Prints information about the application's configuration.
     */
    private void printConfiguration() {
        System.out.println(
                "configuration:"
                        + "\n\tdebug mode: " + debugMode
                        + "\n\talgorithm: " + algorithmName
                        + "\n\tuse default depth: " + setDefaultDepth
                        + "\n\tdepth: " + depth
                        + "\n\tprint boards: " + printBoards);

    }

    /**
     * Runs the demo application.
     * Make sure to call {@link #readArguments(String[])} first.
     */
    private void run() {
        while (true) {
            try {
                Position readPosition = readPosition();
                if (!setDefaultDepth) {
                    readDepth();
                }
                if (!setDefaultAlgorithm) {
                    readAlgorithm();
                }
                Position calculatedMove = calculateFollowUpPosition(readPosition);
                output(calculatedMove, readPosition.getWhiteNextMove());
                evaluator.resetEvaluatedNodeCount();
                System.gc();
            } catch (Exception exception) {
                System.out.println("\tfailure: ");
                exception.printStackTrace();
            }
        }
    }

    /**
     * run before shutting down
     */
    private void cleanUp() {
        //nothing to clean up so far
    }

    /**
     * cleans up and exits the application
     */
    private void shutdown() {
        cleanUp();
        System.exit(0); // normal termination
    }

    /**
     * Prints information about an exception.
     * Displays the passed message to the user.
     * Prints the exception's stacktrace if debug mode is activated.
     * @param exception the exception that was thrown
     * @param message will be displayed to the user
     */
    private void printExceptionInfo(Exception exception, String message) {
        System.out.println(message);
        if (debugMode) {
            exception.printStackTrace();
        }
    }

    /**
     * Reads one line of input from stdin.
     * Exits the application if
     * nothing or "exit" is entered.
     * 
     * @return the next line of input
     */
    private String readInput() {
        String readLine = scanner.nextLine();
        if (readLine.equals("") || readLine.equals("exit")) {
            shutdown();
        }
        return readLine;
    }

    /**
     * Reads one line of input from stdin.
     * Exits the application if
     * nothing or "exit" is entered.
     * 
     * @return the next line of input
     * @param prompt prompt printed before reading from stdin
     */
    private String readInput(String prompt) {
        System.out.println("\n" + prompt);
        return readInput();
    }

    /**
     * Prompts the user to enter an integer.
     * Prompts the user repeatedly until an integer is
     * successfully entered. 
     * @param prompt displayed to the user
     * @return the read integer
     */
    private int readInt(String prompt) {
        int result;
        while (true) {
            try {
                result = Integer.parseInt(readInput(prompt));
                return result;
            } catch (NumberFormatException exception) {
                printExceptionInfo(exception, "input could not be parsed as integer");
            }
        }
    }

    /**
     * Prompts the user to enter a fen string and
     * returns the corresponding Position object.
     * @return the Position entered by the user
     */
    private Position readPosition() {
        while (true) {
            try {
                return FenParser.parseFen(readInput("enter fen:"));
            } catch (FenParseException exception) {
                System.out.println("fen could not be parsed");
            }
        }
    }

    /**
     * Prompts the user to enter the calculation depth.
     */
    private void readDepth() {
        if (setDefaultDepth) {
            //do not read depth if default depth is set
            return;
        }
        depth = readInt("enter depth: ");
    }

    /**
     * Prompts the user to enter an algorithm and stores it.
     */
    private void readAlgorithm() {
        while (true) {
            try {
                useAlgorithm(readInput("enter algorithm: "));
                return;
            } catch (NoSuchElementException exception) {
                System.out.println("algorithm not recognized");
            }
        }
    }

    /**
     * Uses the stored GameTreeEvaluator to calculate a
     * follow-up move to the passed position.
     * @param position
     * @return the follow-up position
     */
    private Position calculateFollowUpPosition(Position position) {
        return evaluator.evaluateTree(new ImpGameTree(GameNode.createRoot(position), evaluator), depth,
                position.getWhiteNextMove()).getContent();
    }

    /**
     * Prints the move associated with the passed position.
     * @param position
     * @param colorMoved true if the move is white's move, false if the move is black's move
     */
    private void printMove(Position position, boolean whitesMove) {

        String movedColor = whitesMove ? "white" : "black";

        String algebraic = position.getMove().toStringAlgebraic();

        String startingSpace = algebraic.substring(0, 2);
        String targetSpace = algebraic.substring(2, 4);
        String promoted = "";
        if (algebraic.length() > 4) {
            promoted = " promoted to " + algebraic.substring(4);
        }

        System.out.println("\n" + movedColor + " : " + startingSpace + " -> " + targetSpace + promoted);

    }

    /**
     * Prints the board stored by the passed position.
     * @param position
     */
    private void printBoard(Position position) {
        System.out.println("\ninternal board:\n" + position.toString());
    }

    /**
    * Prints additional information used in debugging.
    * Only called if debug mode is activated.
    * @param position
    */
    private void printDebugInfo(Position position) {
        System.out.println("\ndebug info:");
        System.out.println("\tused depth: " + depth);
        System.out.println("\tused algorithm: " + algorithmName);
        System.out.println("\tevaluated positions: " + evaluator.getEvaluatedNodeCount());
    }

    /**
     * Prints the move associated with the passed position.
     * Prints additional information if debug mode
     * or printBoards is activated.
     * @param position the position storing the move to be played
     * @param whitesMove whether the move is played by white
     */
    private void output(Position position, boolean whitesMove) {

        if (debugMode) {
            printDebugInfo(position);
        }

        if (printBoards) {
            printBoard(position);
        }
  
        printMove(position, whitesMove);
    }
}
