package helper;

import minimax.GenericAlphaBetaPruning;
import minimax.TreeEvaluator;

/**
 * Subtype of {@link TreeEvaluationHelper} creating 
 * instances of GenericAlphaBetaPruning.
 */
public class AlphaBetaHelper extends TreeEvaluationHelper {

    @Override
    public TreeEvaluator<Integer> instantiateTreeEvaluator() {
        return new GenericAlphaBetaPruning<Integer>();
    }
}
