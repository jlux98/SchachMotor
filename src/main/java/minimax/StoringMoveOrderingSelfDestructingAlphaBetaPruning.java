package minimax;

import java.util.List;

import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.UninitializedValueException;

/**
 * Class implementing Alpha-Beta-Pruning-Minimax for trees consisting of Nodes
 * that store any kind of Object. 
 * <p>
 * This implementation stores the first <code>STORED_LEVELS</code> levels of the tree,
 * but deletes child nodes of deeper layers
 * after evaluating their parent to save memory.
 * <p>
 * This implementation also applies move ordering
 * using the "best" available value for stored nodes (see {@link gametree.Evaluable})
 * and static evaluation of nodes that are not stored.
 */
public class StoringMoveOrderingSelfDestructingAlphaBetaPruning<ContentType> extends BaseTreeEvaluator<ContentType> {

    //FIXME initialization, see genericalphabetapruning

    private DescendingValueComparator whiteComparator;
    private AscendingValueComparator blackComparator;

    public static final int STORED_LEVELS = 4;

    public StoringMoveOrderingSelfDestructingAlphaBetaPruning() {
        whiteComparator = new DescendingValueComparator();
        blackComparator = new AscendingValueComparator();
    }

    @Override
    public Node<ContentType> evaluateNode(Node<ContentType> node, int depth, boolean whitesTurn) {
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
    private Node<ContentType> alphaBetaPruningMiniMax(Node<ContentType> parent, int depth, int alpha, int beta, boolean whiteNextMove) {
        if (whiteNextMove) {
            // maximize this node
            return alphaBetaMaximize(parent, depth, alpha, beta, 1);

        } else {
            // minimize this node
            return alphaBetaMinimize(parent, depth, alpha, beta, 1);

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
    protected Node<ContentType> alphaBetaMinimize(Node<ContentType> parent, int depth, int alpha, int beta, int currentDepth) {

        this.increaseEvaluatedNodeCount();

        parent.writeContentToHistory();

        // assign static evaluation to leaves
        // assign static evaluation to leaves
        boolean leaf = evaluateIfLeaf(parent, depth);
        if (leaf) {
            parent.deleteContentFromHistory();
            return parent;
        }

        try {
            // minimize
            parent.setValue(Integer.MAX_VALUE);

            int childValue;
            Node<ContentType> bestChild = null; // the child that determines the value of this parent node
            boolean firstChild = true;

            // if queryChildren() throws ComputeChildrenException, isLeaf() failed to
            // recognise this node as a leaf
            List<? extends Node<ContentType>> children = parent.getOrComputeChildren();

            children.sort(blackComparator);

            for (Node<ContentType> child : children) {
                // evaluate all children
                // if this node is minimizing, child nodes are maximizing
                // child nodes are passed the determined alpha and beta values
                alphaBetaMaximize(child, depth - 1, alpha, beta, currentDepth + 1);

                // read value of child node = value of the node returned by alphaBetaMaximize(child ...)
                childValue = child.getValue();

                // parentValue has to be updated before alpha
                // because the value of the child causing the cut-off has to be propagated
                // as it is guaranteed not to affect the remaining tree
                // returning a previous child's value currently stored in childValue
                // might return a value that is not guaranteed to not affect the remaining tree
                // i.e. a value that is greater than all sibling's values
                if (firstChild || childValue < parent.getValue()) {
                    // since parentValue is initialized to Integer.MAX_VALUE this will always be
                    // true for the first child (unless a child has a value of Integer.MIN_VALUE
                    // itself)
                    // minimizing player's turn -> value of this parent node = min of child values

                    // save value in node
                    parent.setValue(childValue);
                    // store current child as best child
                    bestChild = child;
                    firstChild = false;
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
            if (currentDepth > STORED_LEVELS) {
                parent.deleteChildren();
            }

            // return the best child node
            // the value stored by that node also is the value of this parent node
            // or if alpha-cutoff (break statement reached) return some node that will be
            // "ignored"
            parent.deleteContentFromHistory();
            return bestChild;
        } catch (UninitializedValueException exception) {
            //thrown by getValue()
            throw new IllegalStateException("tree evaluation attempted to read an unitialized value");

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
     * @param depthLeft  the additional depth to which the tree should be evaluated
     *               (e.g. depth = 2 means that children of the passed node and
     *               their children should be evaluated
     *               to determine the value of the passed node)
     * @param alpha  minimum score that white player is already guaranteed of
     *               (if the parent of the node passed to this method is reached)
     * @param beta   maximum score that black player is already guaranteed of
     *               (if the parent of the node passed to this method is reached)
     * @return the child node that has the best (greatest) value
     */
    protected Node<ContentType> alphaBetaMaximize(Node<ContentType> parent, int depthLeft, int alpha, int beta, int currentDepth) {
        this.increaseEvaluatedNodeCount();
        parent.writeContentToHistory();

        // assign static evaluation to leaves
        boolean leaf = evaluateIfLeaf(parent, depthLeft);
        if (leaf) {
            parent.deleteContentFromHistory();
            return parent;
        }

        try {
            //maximize
            parent.setValue(Integer.MIN_VALUE);

            int childValue;
            Node<ContentType> bestChild = null; // the child that determines the value of this parent node
            boolean firstChild = true;

            // if queryChildren() throws ComputeChildrenException, isLeaf() failed to
            // recognise this node as a leaf
            List<? extends Node<ContentType>> children = parent.getOrComputeChildren();

            children.sort(whiteComparator);

            for (Node<ContentType> child : children) {

                // evaluate all children
                // if this node is maximizing, child nodes are minimizing
                // child nodes are passed the determined alpha and beta values
                alphaBetaMinimize(child, depthLeft - 1, alpha, beta, currentDepth + 1);

                // read value of child node = value of the node returned by alphaBetaMinimize(child ...)
                childValue = child.getValue();

                // parentValue has to be updated before alpha
                // because the value of the child causing the cut-off has to be propagated
                // as it is guaranteed not to affect the remaining tree
                // returning a previous child's value currently stored in childValue
                // might return a value that is not guaranteed to not affect the remaining tree
                // i.e. a value that is less than all sibling's values
                if (firstChild || childValue > parent.getValue()) {
                    // since parentValue is initialized to Integer.MIN_VALUE this will always be
                    // true for the first child (unless a child has a value of Integer.MIN_VALUE
                    // itself)
                    // maximizing player's turn -> value of this parent node = max of child values
                    //save value in node
                    parent.setValue(childValue);
                    // store current child as best child
                    bestChild = child;
                    firstChild = false;
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
            if (currentDepth > STORED_LEVELS) {
                parent.deleteChildren();
            }

            // return the best child node
            // the value stored by that node also is the value of this parent node
            // or if beta-cutoff (break statement reached) return some node that will be
            // "ignored"
            parent.deleteContentFromHistory();
            return bestChild;

        } catch (UninitializedValueException exception) {
            //thrown by getValue()
            throw new IllegalStateException("tree evaluation attempted to read an unitialized value");

        } catch (ComputeChildrenException exception) {
            // queryChildren() is only called on nodes for which isLeaf(node, depth) = false
            // nodes that are not leaves may not throw ComputeChildrenException
            // -> this case is never allowed to occur
            throw new IllegalStateException("isLeaf() failed to recognise a leaf", exception);
        }
    }

}
