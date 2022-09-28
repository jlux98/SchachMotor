package minimax;

import java.util.List;

import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.Tree;

/**
 * Class implementing Alpha-Beta-Pruning-Minimax for trees consisting of Nodes
 * that store any kind of Object. This implementation deletes child nodes
 * after evaluating their parent to save memory and applies basic move ordering
 * using the static evaluation of each child node.
 */
public class MoveOrderingSelfDestructingAlphaBetaPruningLegacy<T> extends BaseTreeEvaluator<T> {

    private DescendingStaticValueComparator<T> whiteComparator;
    private AscendingStaticValueComparator<T> blackComparator;

    public MoveOrderingSelfDestructingAlphaBetaPruningLegacy() {
        //TODO use singletons instead?
        whiteComparator = new DescendingStaticValueComparator<T>();
        blackComparator = new AscendingStaticValueComparator<T>();
    }

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
            List<? extends Node<T>> children = parent.getOrCompute();

            children.sort(blackComparator);

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

            // delete children from tree after parent was evaluated
            parent.deleteChildren();

            // return the best child node
            // the value stored by that node also is the value of this parent node
            // or if alpha-cutoff (break statement reached) return some node that will be
            // "ignored"
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
    protected Node<T> alphaBetaMaximize(Node<T> parent, int depth, int alpha, int beta) {
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
            List<? extends Node<T>> children = parent.getOrCompute();

            children.sort(whiteComparator);

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

            // delete children from tree after parent was evaluated
            parent.deleteChildren();

            // return the best child node
            // the value stored by that node also is the value of this parent node
            // or if beta-cutoff (break statement reached) return some node that will be
            // "ignored"
            return bestChild;

        } catch (ComputeChildrenException exception) {
            // queryChildren() is only called on nodes for which isLeaf(node, depth) = false
            // nodes that are not leaves may not throw ComputeChildrenException
            // -> this case is never allowed to occur
            throw new IllegalStateException("isLeaf() failed to recognise a leaf", exception);
        }
    }

}
