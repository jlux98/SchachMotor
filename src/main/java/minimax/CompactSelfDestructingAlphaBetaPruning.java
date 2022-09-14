package minimax;

import model.Move;
import model.Position;
import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.Tree;

/**
 * Class implementing Alpha-Beta-Pruning-Minimax for trees consisting of Nodes
 * that store any kind of Object. This implementation deletes child nodes
 * after evaluating their parent to save memory.
 */
public class CompactSelfDestructingAlphaBetaPruning<T> extends GenericAlphaBetaPruning<T> {

    // Note on storing values in nodes:
    // values stored by nodes do not have to be marked as invalid
    // leaves overwrite their old value (they can have an old value because
    // iterative deepening doesn't start with old
    // max depth, so nodes that are leaves for this iteration might not actually be
    // leaves in the gametree)
    // inner nodes overwrite their own value with values of their children

    @Override
    public Node<T> evaluateTree(Tree<? extends Node<T>> tree, int depth, boolean whitesTurn) {
        return evaluateNode(tree.getRoot(), depth, whitesTurn);
    }

    /**
     * @deprecated
     */
    @Override
    public Node<T> evaluateNode(Node<T> node, int depth, boolean whitesTurn) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        return alphaBetaPruningMiniMax(node, depth, alpha, beta, whitesTurn);
    }

    /**
     * Applies alpha-beta-pruning minimax to the passed node and returns the child
     * node that should be played.
     * 
     * @param parent the node whose value should be determined
     * @param depth  the additional depth to which the tree should be evaluated
     *               (e.g. depth = 2 means that children of the passed node and
     *               their children should be evaluated
     *               to determine the value of the passed node)
     * @param alpha  minimum score that white player is already guaranteed of
     *               (if the parent of the node passed to this method is reached)
     * @param beta   maximum score that black player is already guaranteed of
     *               (if the parent of the node passed to this method is reached)
     * @return the child node that has the best value
     */
    private Node<T> alphaBetaPruningMiniMax(Node<T> parent, int depth, int alpha, int beta,
            boolean whiteNextMove) {

        if (whiteNextMove) {
            // maximize this node
            return alphaBetaMaximize(parent, depth, alpha, beta);

        } else {
            // minimize this node
            return alphaBetaMinimize(parent, depth, alpha, beta);

        }
    }

    /**
     * Determines whether the passed node is a leaf node when inspected by
     * alpha-beta-pruning.
     * <br><br>
     * A node is a leaf if <b>at least one</b> of these conditions is true:
     * <ul>
     *      <li>
     *          depth = 0 or
     *      </li>
     *      <li>
     *          no children can be calculated for it
     *      </li>
     * </ul>
     * <br><br>
     * If this method returns false, it is guaranteed that calling
     * parent.queryChildren() will not throw an ComputeChildrenException.
     * 
     * @param parent the inspected node
     * @param depth  the current depth of alpha-beta pruning
     * @return 0 if the passed node is not a leaf node, 1 if is a leaf because
     * depth == 0 and 2 if it is a leaf because no children could be generated
     */
    private int isLeaf(Node<T> parent, int depth) {
        //FIXME cleanup and move position specific code to GameNodeAlphaBetaPruning
        if (parent.getContent().getClass() == Position.class) {
            Position position = (Position) parent.getContent();
            if (depth != 0 && position.getMove() != null && position.getMove().equals(new Move("d3b3"))) {
                boolean test = true;
            }
        }
        if (depth == 0) {
            return 1;
        }
        try {
            // attempt to retrieve or if none are stored calculate children
            parent.queryChildren();
        } catch (ComputeChildrenException exception) {
            // node could not generate children -> is a leaf
            return 2;
        }
        // node is a leaf if it has no children
        if (parent.hasChildren()) {
            return 0;
        } else {
            return 2;
        }
    }

    /**
     * Minimizes the passed node (value = min(child values)) and returns the child
     * node with the best (smallest) value.
     * 
     * @param parent the node whose value should be determined
     * @param depth  the additional depth to which the tree should be evaluated
     *               (e.g. depth = 2 means that children of the passed node and
     *               their children should be evaluated
     *               to determine the value of the passed node)
     * @param alpha  minimum score that white player is already guaranteed of
     *               (if the parent of the node passed to this method is reached)
     * @param beta   maximum score that black player is already guaranteed of
     *               (if the parent of the node passed to this method is reached)
     * @return the child node that has the best (smallest) value
     */
    protected Node<T> alphaBetaMinimize(Node<T> parent, int depth, int alpha, int beta) {
        Node<T> bestMove = super.alphaBetaMinimize(parent, depth, alpha, beta);
        parent.deleteChildren();
        return bestMove;
    }

    /**
     * Maximizes the passed node (value = min(child values)) and returns the child
     * node with the best (greatest) value.
     * 
     * @param parent the node whose value should be determined
     * @param depth  the additional depth to which the tree should be evaluated
     *               (e.g. depth = 2 means that children of the passed node and
     *               their children should be evaluated
     *               to determine the value of the passed node)
     * @param alpha  minimum score that white player is already guaranteed of
     *               (if the parent of the node passed to this method is reached)
     * @param beta   maximum score that black player is already guaranteed of
     *               (if the parent of the node passed to this method is reached)
     * @return the child node that has the best (greatest) value
     */
    protected Node<T> alphaBetaMaximize(Node<T> parent, int depth, int alpha, int beta) {
        Node<T> bestMove = super.alphaBetaMaximize(parent, depth, alpha, beta);
        parent.deleteChildren();
        return bestMove;
    }

}
