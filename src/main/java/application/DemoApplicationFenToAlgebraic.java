package application;

import java.util.NoSuchElementException;
import java.util.Scanner;

import gametree.GameNode;
import gametree.GameTree;
import gametree.ImpGameTree;
import minimax.GameNodeAlphaBetaPruning;
import minimax.GameTreeEvaluator;
import model.Position;
import uciservice.FenParseException;
import uciservice.FenParser;
import utility.TimeUtility;

public class DemoApplicationFenToAlgebraic {
    private Scanner scanner;

    /**
     * enables output of performance information 
     */
    private boolean benchMarkMode = false;

    /**
     * enables output of additional information 
     */
    private boolean debugMode = false;

    /**
     * causes internal board to be printed to console
     */
    private boolean printBoards = false;

    /**
     * depth will not be configurable if this is set
     */
    private boolean setDefaultDepth = false;
    private int depth = -1;

    /**
    * algorithm will not be configurable if this is set
    */
    private boolean setDefaultAlgorithm = false;
    private String algorithmName;
    private GameTreeEvaluator evaluator;

    /**
     * fen will not be configurable if this is set
     */
    private boolean setDefaultPosition = false;
    private String FEN; //TODO make lowercase again
    private Position position;

    private long calculationTime = -1;

    public DemoApplicationFenToAlgebraic() {
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        System.out.println("""

                This application reads FEN-strings from stdin and answers with a move in algebraic form.
                Enter exit or hit enter without entering anything to exit.
                Possible options are:
                    -debug - use debug mode (prints all available information and enables stack traces)
                    -benchmark - use benchmark mode (prints performance related info)
                    -depth <n> - always use a depth of n
                    -algorithm <algorithm> - always use the specified algorithm
                    -fen <fen> - always use the specified fen
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
        String argument;
        for (int i = 0; i < args.length; i++) {
            argument = args[i].toLowerCase(); //ignore case

            switch (argument) {
                case "-debug" -> debugMode = true;
                case "-benchmark" -> benchMarkMode = true;
                case "-depth", "-defaultDepth" -> {
                    parseDefaultDepthArgument(args, i);
                    i++;
                }
                case "-algorithm" -> {
                    parseDefaultAlgorithmArgument(args, i);
                    i++;
                }
                case "-fen" -> {
                    parseDefaultPositionArgument(args, i);
                    i++;
                }
                case "-printboards" -> {
                    printBoards = true;
                }
                default -> {
                    System.out.println("not recognized: " + argument);
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
            System.out.println("argument after -depth must be a number");
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
    //TODO reduce ccode redundancy

    private void parseDefaultPositionArgument(String[] args, int index) {
        boolean failure = false;
        if (args.length > index + 1) {
            String fenArg = args[index + 1]; //do not use lower case for fen strings
            FEN = fenArg; //store the fen representing the board
            setDefaultPosition = true;
            try {
                usePosition(fenArg);
            } catch (FenParseException exception) {
                failure = true;
            }
        } else {
            failure = true;
        }

        if (failure) {
            System.out.println("argument after -fen must be a valid fen string");
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

    /**
     * @throws FenParseException if the fen is invalid
     * @param fen
     */
    private void usePosition(String fen) {
        //TODO update doc
        position = FenParser.parseFen(fen);
    }

    private void useDefaultAlgorithm() {
        algorithmName = "limited Alpha-Beta"; //TODO update default algorithm
        evaluator = new GameNodeAlphaBetaPruning();
    }

    /**
     * Runs the demo application.
     * Make sure to call {@link #readArguments(String[])} first.
     */
    private void run() {
        while (true) {
            try {
                if (!setDefaultDepth) {
                    readDepth();
                }
                if (!setDefaultAlgorithm) {
                    readAlgorithm();
                }
                if (!setDefaultPosition) {
                    readPosition();
                }

                Position calculatedMove = calculateFollowUpPosition(position);
                output(calculatedMove, position.getWhiteNextMove());
                prepareNextRun();

            } catch (Exception exception) {
                System.out.println("\tfailure: ");
                exception.printStackTrace();
            }
        }
    }

    /**
     * Executed after every "run" (= calulation of a move) of the application.
     * Resets the evaluated node counter of the used evaluator.
     * Encourages the garbage collection to free up memory.
     */
    private void prepareNextRun() {
        evaluator.resetEvaluatedNodeCount();
        calculationTime = -1; //not required, but better safe than sorry
        System.gc();
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
     * If no default fen is set,
     * prompts the user to enter a fen string and
     * returns it.
     * Otherwise, returns the default fen string.
     * @return 
     * <ul>
     *       <li>the default fen, if set</li>
     *       <li>a fen entered by the user, if no default fen is set</li>       
     * </ul>
     */
    private String readFen() {
        //TODO update doc
        return readInput("enter fen:");
    }

    /**
     * If no default fen is set,
     * prompts the user to enter a fen string and
     * returns the corresponding Position object.
     * Otherwise, returns the position represented by the
     * default fen string.
     * This method prompts for a fen repeatedly, 
     * until a valid one is entered.
     * @return
     * <ul>
     *       <li>the position represented by the default fen, if set</li>
     *       <li>
     *          the position represented by a fen entered by the user, 
     *          if no default fen is set
     *      </li>       
     * </ul>
     */
    private void readPosition() {
        //TODO update doc 
        while (true) {
            try {
                position = FenParser.parseFen(readFen());
                return;
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
        TimeUtility<GameNode> timer = new TimeUtility<GameNode>();

        GameTree tree = new ImpGameTree(new GameNode(position), evaluator);
        GameNode bestChild = timer.time(() -> evaluator.evaluateTree(tree, depth, position.getWhiteNextMove()));
        Position bestMove = bestChild.getContent();
        //save rough time spent calculating
        calculationTime = timer.getElapsedTime();
        return bestMove;
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
     * Adds the configuration of the setDefault switches. 
     * @param builder
     * @return
     */
    private StringBuilder addDefaultSettings(StringBuilder builder) {
        return builder
                .append("\n\tuse default algorithm:" + setDefaultAlgorithm)
                .append("\n\tuse default depth: " + setDefaultDepth)
                .append("\n\tuse default position: " + setDefaultPosition);
    }

    /**
     * Adds algorithm name, depth and fen string.
     * @param builder
     * @return
     */
    private StringBuilder addCoreInfo(StringBuilder builder) {
        return builder
                .append("\n\talgorithm: " + algorithmName)
                .append("\n\tdepth: " + depth)
                .append("\n\tposition: " + FEN);
    }

    /**
     * Adds algorithm name, depth, fen string, and number of evaluated nodes.
     * Includes all information provided by {@link #addCoreInfo(StringBuilder)}.
     * @param builder
     * @return
     */
    private StringBuilder addPerformanceInfo(StringBuilder builder) {
        return addCoreInfo(builder)
                .append("\n\tevaluated positions: " + evaluator.getEvaluatedNodeCount())
                .append("\n\ttime spent: " + TimeUtility.nanoToSeconds(calculationTime));
    }

    /**
    * Prints information about the application's configuration.
    * Used to display the application's initial configuration.
    */
    private void printConfiguration() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("\nconfiguration:")
                .append("\n\tdebug mode: " + debugMode) //add configuration of debug and benchmark mode
                .append("\n\tbenchmark mode: " + benchMarkMode);
        addCoreInfo(addDefaultSettings(builder));

        System.out.println(builder.toString());
    }

    /**
    * Prints additional information used in debugging.
    * Called after every calculation if debug mode is activated.
    */
    private void printDebugInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("\ndebug info:");
        addPerformanceInfo(addDefaultSettings(builder));

        System.out.println(builder.toString());
    }

    /**
     * Prints performance related info.
     * Only called in benchmark mode.
     */
    private void printPerformanceInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nperformance info:");
        addPerformanceInfo(builder);

        System.out.println(builder.toString());
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
            printDebugInfo();
        } else {
            if (benchMarkMode) {
                //debug info contains performance info
                //so only print performance info if debug is not already being printed
                printPerformanceInfo();
            }
        }

        if (printBoards) {
            printBoard(position);
        }

        printMove(position, whitesMove);
    }
}
