package minimax;

import java.util.List;

import model.Move;
import model.Position;
import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.Tree;

/**
 * Class implementing Alpha-Beta-Pruning-Minimax for trees consisting of Nodes
 * that store any kind of Object.
 */
public class GenericAlphaBetaPruning<T> extends BaseTreeEvaluator<T> {

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
    private Node<T> alphaBetaMinimize(Node<T> parent, int depth, int alpha, int beta) {
        /*
         * if (depth == 0 && parent.isInteresting()) {
         *      depth = depth + 1; //evaluate recursively
         * }
         */

        this.increaseEvaluatedNodeCount();

        // assign static evaluation to leaves
        switch (isLeaf(parent, depth)) {
            case 1:
                parent.evaluateStatically(false, depth);
                return parent;
            case 2:
                parent.evaluateStatically(true, depth);
                return parent;
        }

        try {
            // minimize
            parent.setValue(Integer.MAX_VALUE);

            int childValue;
            Node<T> bestChild = null; // the child that determines the value of this parent node

            // if queryChildren() throws ComputeChildrenException, isLeaf() failed to
            // recognise this node as a leaf
            List<? extends Node<T>> children = parent.queryChildren();

            for (Node<T> child : children) {
                // evaluate all children
                // if this node is minimizing, child nodes are maximizing
                // child nodes are passed the determined alpha and beta values
                alphaBetaMaximize(child, depth - 1, alpha, beta);

                // read value of child node = value of the node returned by alphaBetaMaximize(child ...)
                childValue = child.getValue();

                // parentValue has to be updated before alpha
                // because the value of the child causing the cut-off has to be propagated
                // as it is guaranteed not to affect the remaining tree
                // returning a previous child's value currently stored in childValue
                // might return a value that is not guaranteed to not affect the remaining tree
                // i.e. a value that is greater than all sibling's values
                if (childValue < parent.getValue()) {
                    // since parentValue is initialized to Integer.MAX_VALUE this will always be
                    // true for the first child (unless a child has a value of Integer.MIN_VALUE
                    // itself)
                    // minimizing player's turn -> value of this parent node = min of child values

                    // save value in node
                    parent.setValue(childValue);
                    // store current child as best child
                    bestChild = child;
                }
                if (childValue <= alpha) {
                    // minimizing player can achieve a lower score than maximizing player is already
                    // assured of if this parent node is reached
                    // prune this subtree = stop evaluating children of this node
                    // return current best child whose value is guaranteed not to affect the value
                    // of root
                    // one could also return a node with value of Integer.MIN_VALUE instead
                    // as any node with value < alpha will never be played by the maximizing player
                    break;
                }
                if (childValue < beta) {
                    // maximizing player has new best guaranteed score if parent node is reached
                    beta = childValue;
                }

            }
            // return the best child node
            // the value stored by that node also is the value of this parent node
            // or if alpha-cutoff (break statement reached) return some node that will be
            // "ignored"

            //delete children from tree after evaluation of parent
            parent.deleteChildren();
            return bestChild;

        } catch (ComputeChildrenException exception) {
            // queryChildren() is only called on nodes for which isLeaf(node, depth) = false
            // nodes that are not leaves may not throw ComputeChildrenException
            // -> this case is never allowed to occur
            throw new IllegalStateException("isLeaf() failed to recognise a leaf", exception);
        }
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
    private Node<T> alphaBetaMaximize(Node<T> parent, int depth, int alpha, int beta) {
        /*
         * if (depth == 0 && parent.isInteresting()) {
         *      depth = depth + 1; //evaluate recursively
         * }
         */

        this.increaseEvaluatedNodeCount();

        // assign static evaluation to leaves
        switch (isLeaf(parent, depth)) {
            case 1:
                parent.evaluateStatically(false, depth);
                return parent;
            case 2:
                parent.evaluateStatically(true, depth);
                return parent;
        }

        try {
            //maximize
            parent.setValue(Integer.MIN_VALUE);

            int childValue;
            Node<T> bestChild = null; // the child that determines the value of this parent node

            // if queryChildren() throws ComputeChildrenException, isLeaf() failed to
            // recognise this node as a leaf
            List<? extends Node<T>> children = parent.queryChildren();

            for (Node<T> child : children) {

                // evaluate all children
                // if this node is maximizing, child nodes are minimizing
                // child nodes are passed the determined alpha and beta values
                alphaBetaMinimize(child, depth - 1, alpha, beta);

                // read value of child node = value of the node returned by alphaBetaMinimize(child ...)
                childValue = child.getValue();

                // parentValue has to be updated before alpha
                // because the value of the child causing the cut-off has to be propagated
                // as it is guaranteed not to affect the remaining tree
                // returning a previous child's value currently stored in childValue
                // might return a value that is not guaranteed to not affect the remaining tree
                // i.e. a value that is less than all sibling's values
                if (childValue > parent.getValue()) {
                    // since parentValue is initialized to Integer.MIN_VALUE this will always be
                    // true for the first child (unless a child has a value of Integer.MIN_VALUE
                    // itself)
                    // maximizing player's turn -> value of this parent node = max of child values
                    //save value in node
                    parent.setValue(childValue);
                    // store current child as best child
                    bestChild = child;
                }
                if (childValue >= beta) {
                    // maximizing player can achieve a higher score than minimizing player is
                    // already assured of if this parent node is reached
                    // prune this subtree = stop evaluating children of this node
                    // return current best child whose value is guaranteed not to affect the value
                    // of root
                    // one could also return a node with value of Integer.MAX_VALUE instead
                    // as any node with value > beta will never be played by the minimizing player
                    break;
                }
                if (childValue > alpha) {
                    // maximizing player has new best guaranteed score if parent node is reached
                    alpha = childValue;
                }

            }
            // return the best child node
            // the value stored by that node also is the value of this parent node
            // or if beta-cutoff (break statement reached) return some node that will be
            // "ignored"

            //delete children from tree after evaluation of parent
            parent.deleteChildren();
            return bestChild;

        } catch (ComputeChildrenException exception) {
            // queryChildren() is only called on nodes for which isLeaf(node, depth) = false
            // nodes that are not leaves may not throw ComputeChildrenException
            // -> this case is never allowed to occur
            throw new IllegalStateException("isLeaf() failed to recognise a leaf", exception);
        }
    }

}
