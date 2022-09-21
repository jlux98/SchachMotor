package helper;

import java.util.function.Supplier;

import minimax.GameTreeEvaluator;
import minimax.TreeEvaluator;
import tests.TreeEvaluationTest;

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
}
