package gametree;

import positionevaluator.Evaluable;

/**
 * Class implementing Minimax for trees consisting of Nodes that store any kind of Evaluable.
 */
public class MiniMaxBase<T extends Evaluable> implements TreeEvaluator<T> {

    @Override
    public Node<T> evaluateTree(Node<T> gameTree, int depth, boolean whitesTurn) {
        // TODO use trimmed down alpha-beta code (useful for testing)
        return null;
    }
 
}
