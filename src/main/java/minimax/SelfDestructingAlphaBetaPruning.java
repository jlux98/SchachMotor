package minimax;

import gametree.Node;

/**
 * Class implementing Alpha-Beta-Pruning-Minimax for trees consisting of Nodes
 * that store any kind of Object. This implementation deletes child nodes
 * after evaluating their parent to save memory.
 */
public class SelfDestructingAlphaBetaPruning<T> extends GenericAlphaBetaPruning<T> {

    @Override
    protected Node<T> alphaBetaMinimize(Node<T> parent, int depth, int alpha, int beta) {
        Node<T> bestMove = super.alphaBetaMinimize(parent, depth, alpha, beta);
        parent.deleteChildren();
        return bestMove;
    }

    @Override
    protected Node<T> alphaBetaMaximize(Node<T> parent, int depth, int alpha, int beta) {
        Node<T> bestMove = super.alphaBetaMaximize(parent, depth, alpha, beta);
        parent.deleteChildren();
        return bestMove;
    }

}
