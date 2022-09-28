package helper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import gametree.ComputeChildrenException;
import gametree.GameNode;
import gametree.ImpGameTree;
import gametree.Node;
import gametree.Tree;
import minimax.GameTreeEvaluator;
import minimax.TreeEvaluator;
import model.Position;
import uciservice.FenParser;

/**
 * Class used to help with testing instances of {@link TreeEvaluator TreeEvaluator < Position >}.
 * This class offers a factory method {@link #instantiateTreeEvaluator()}
 * which allows for testing various implementations of TreeEvaluators
 * while only writing tests once.
 * 
 * <p>
 * The class containing the tests just has to store an instance of TreeEvaluationHelper
 * and can then use it to instantiate TreeEvaluators as needed for testing.
 * </p>
 * 
 * <p>
 * This class can be instantiated by providing its constructor with a suitable lambda expression,
 * for example: 
 * <pre>new GameTreeEvaluationHelper(() -> new GenericAlphaBetaPruning < Position > ())</pre>
 * </p>
 * 
 * <p>
 * See the documentation of {@link TreeEvaluationTest} for a code example.
 * </p>
 */
public class GameTreeEvaluationHelper {

    private Supplier<GameTreeEvaluator> treeEvaluatorSupplier;

    public GameTreeEvaluationHelper(Supplier<GameTreeEvaluator> treeEvaluatorSupplier) {
        this.treeEvaluatorSupplier = treeEvaluatorSupplier;
    }

    /**
     * Constructs an instance of the TreeEvaluator implementation that
     * should be tested (e.g. miniax, alpha-beta-pruning).
     * @return an instance of the TreeEvaluator
     */
    public GameTreeEvaluator instantiateTreeEvaluator() {
        return treeEvaluatorSupplier.get();
    }

    /**
     * Asserts that the evaluation of the position returns one of the expected moves.
     * @param position position to evaluate
     * @param depth depth to which the position should be evaluated
     * @param whitesTurn whether it's white's turn
     * @param moves the expected moves in algebraic notation
     * @return the move that was evaluated to be best
     */
    public GameNode assertBestMoveIn(Position position, int depth, boolean whitesTurn, String... moves)
            throws ComputeChildrenException {
        GameNode bestMove = evaluate(position, depth, whitesTurn);
        String bestMoveAlgebraic = bestMove.getRepresentedMove().toStringAlgebraic();
        List<String> expectedMoves = Arrays.asList(moves);
        assertTrue(expectedMoves.contains(bestMoveAlgebraic), "move was " + bestMoveAlgebraic);
        return bestMove;
    }

    /**
     * Asserts that the evaluation of the position returns one of the expected moves.
     * @param position position to evaluate as FEN-string
     * @param depth depth to which the position should be evaluated
     * @param whitesTurn whether it's white's turn
     * @param moves the expected moves in algebraic notation
     * @return the move that was evaluated to be best
     */
    public GameNode assertBestMoveIn(String fen, int depth, boolean whitesTurn, String... moves) throws ComputeChildrenException {
        return assertBestMoveIn(FenParser.parseFen(fen), depth, whitesTurn, moves);
    }

    /**
    * Asserts that the evaluation of the position does <b>not</b> return one of the expected moves.
    * @param position position to evaluate
    * @param depth depth to which the position should be evaluated
    * @param whitesTurn whether it's white's turn
    * @param moves "banned" moves in algebraic notation
    * @return the move that was evaluated to be best
    */
    public GameNode assertMoveNotIn(Position position, int depth, boolean whitesTurn, String... moves) throws ComputeChildrenException {
        GameNode bestMove = evaluate(position, depth, whitesTurn);
        String bestMoveAlgebraic = bestMove.getRepresentedMove().toStringAlgebraic();
        List<String> expectedMoves = Arrays.asList(moves);
        assertFalse(expectedMoves.contains(bestMoveAlgebraic), "move was " + bestMoveAlgebraic);
        return bestMove;
    }

    /**
    * Asserts that the evaluation of the position does <b>not</b> return one of the expected moves.
    * @param position position to evaluate as FEN-string
    * @param depth depth to which the position should be evaluated
    * @param whitesTurn whether it's white's turn
    * @param moves "banned" moves in algebraic notation
    * @return the move that was evaluated to be best
    */
    public GameNode assertBestMoveNotIn(String fen, int depth, boolean whitesTurn, String... moves) throws ComputeChildrenException {
        return assertMoveNotIn(FenParser.parseFen(fen), depth, whitesTurn, moves);
    }

    /**
     * Evaluates the tree using the TreeEvaluator provided by {@link IntTreeEvaluationHelper}.
     * @param tree the tree to be evaluated
     * @param depth the depth to which the tree should be evaluated
     * @param whitesTurn whether the node searched for is played by white
     * @return the node that should be played
     */
    public GameNode evaluateTree(Tree<? extends Node<Position>> tree, int depth, boolean whitesTurn) {
        return instantiateTreeEvaluator().evaluateTree(tree, depth, whitesTurn);
    }

    /**
     * Evaluates the GameNode using the TreeEvaluator provided by {@link IntTreeEvaluationHelper}.
     * @param node the node to be evaluated
     * @param depth the depth to which the tree should be evaluated
     * @param whitesTurn whether the node searched for is played by white
     * @return the node that should be played
     */
    public GameNode evaluate(GameNode node, int depth, boolean whitesTurn) {
        return instantiateTreeEvaluator().evaluateTree(new ImpGameTree(node, instantiateTreeEvaluator()), depth, whitesTurn);
    }

    /**
    * Evaluates the position using the TreeEvaluator provided by {@link IntTreeEvaluationHelper}.
    * @param position the position to be evaluated
    * @param depth the depth to which the tree should be evaluated
    * @param whitesTurn whether the node searched for is played by white
    * @return the node that should be played
    */
    public GameNode evaluate(Position position, int depth, boolean whitesTurn) {
        return instantiateTreeEvaluator().evaluateTree(new ImpGameTree(position, instantiateTreeEvaluator()), depth, whitesTurn);
    }

    /**
    * Evaluates the position using the TreeEvaluator provided by {@link IntTreeEvaluationHelper}.
    * @param fen the position as fen
    * @param depth the depth to which the tree should be evaluated
    * @param whitesTurn whether the node searched for is played by white
    * @return the node that should be played
    */
    public GameNode evaluate(String fen, int depth, boolean whitesTurn) {
        return instantiateTreeEvaluator().evaluateTree(new ImpGameTree(FenParser.parseFen(fen), instantiateTreeEvaluator()), depth,
                whitesTurn);
    }
}
