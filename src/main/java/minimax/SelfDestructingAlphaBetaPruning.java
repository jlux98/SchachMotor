package minimax;

import gametree.Node;

/**
 * Class implementing Alpha-Beta-Pruning-Minimax for trees consisting of Nodes
 * that store any kind of Object. This implementation deletes child nodes
 * after evaluating their parent to save memory.
 */
public class SelfDestructingAlphaBetaPruning<ContentType> extends GenericAlphaBetaPruning<ContentType> {

    @Override
    protected Node<ContentType> alphaBetaMinimize(Node<ContentType> parent, int depth, int alpha, int beta) {
        Node<ContentType> bestMove = super.alphaBetaMinimize(parent, depth, alpha, beta);
        parent.deleteChildren();
        return bestMove;
    }

    @Override
    protected Node<ContentType> alphaBetaMaximize(Node<ContentType> parent, int depth, int alpha, int beta) {
        Node<ContentType> bestMove = super.alphaBetaMaximize(parent, depth, alpha, beta);
        parent.deleteChildren();
        return bestMove;
    }

}
