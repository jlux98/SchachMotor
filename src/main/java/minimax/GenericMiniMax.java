package minimax;

import java.util.List;

import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.Tree;

/**
 * Class implementing Minimax for trees consisting of Nodes that store any kind of Evaluable.
 */
public class GenericMiniMax<T> extends BaseTreeEvaluator<T> {

    @Override
    public Node<T> evaluateTree(Tree<? extends Node<T>> tree, int depth, boolean whitesTurn) {
        resetEvaluatedNodeCount();
        return evaluateNode(tree.getRoot(), depth, whitesTurn);
    }

    /**
     * @deprecated
     */
    @Override
    public Node<T> evaluateNode(Node<T> node, int depth, boolean whitesTurn) {
        return miniMax(node, depth, whitesTurn);
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
    private Node<T> miniMax(Node<T> parent, int depth, boolean whiteNextMove) {

        if (whiteNextMove) {
            // maximize this node
            return maximize(parent, depth);

        } else {
            // minimize this node
            return minimize(parent, depth);

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
    protected Node<T> minimize(Node<T> parent, int depth) {
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

            for (Node<T> child : children) {
                // evaluate all children
                // if this node is minimizing, child nodes are maximizing
                // child nodes are passed the determined alpha and beta values
                maximize(child, depth - 1);

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

            }
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
    protected Node<T> maximize(Node<T> parent, int depth) {
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

            for (Node<T> child : children) {

                // evaluate all children
                // if this node is maximizing, child nodes are minimizing
                // child nodes are passed the determined alpha and beta values
                minimize(child, depth - 1);

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

            }
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
