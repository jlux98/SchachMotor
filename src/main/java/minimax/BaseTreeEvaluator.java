package minimax;

import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.Tree;
import utility.TimeUtility;

/**
 * Abstract class implementing TreeEvaluator,
 * providing partial implementation of the counter
 * for evaluated nodes.
 * <p>
 * Extending classes must call {@link #increaseEvaluatedNodeCount()}
 * whenever evaluating a node.
 * </p>
 */
public abstract class BaseTreeEvaluator<T> implements TreeEvaluator<T> {

    private long stopTime;
    private int evaluatedNodeCount = 0;


    protected void isTimeLeft() throws OutOfTimeException {
        if (System.nanoTime() >= stopTime + TimeUtility.SECOND_TO_NANO) {
            throw new OutOfTimeException();
        }
    }

    /**
     * Used to save an intermediate result of iterative deepning.
     * <p>
     * This method is empty by default.
     * If intermediate results should be stored, subclasses have to implement this method.
     * @param bestMove the move that should be saved
     */
    protected void saveMove(Node<T> bestMove) {
        //empty by default
    }

    @Override
    public Node<T> evaluateTreeIterativeDeepening(Tree<? extends Node<T>> tree, long secondsToCompute, boolean whitesTurn) {
        long start = System.nanoTime();
        stopTime = start + secondsToCompute * TimeUtility.SECOND_TO_NANO;
        int depth = 1;
        while (true) {
            Node<T> bestMove = evaluateTree(tree, depth, whitesTurn);
            saveMove(bestMove);
            depth += 1;
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
    protected boolean evaluateIfLeaf(Node<T> parent, int depth) {
        if (depth == 0) {
            parent.computeOrGetStaticValueOrBetter(); //evaluate statically if no value is stored
            return true;
        }
        try {
            // attempt to retrieve or if none are stored calculate children
            parent.getOrComputeChildren();
        } catch (ComputeChildrenException exception) {
            // node could not generate children -> is a leaf
            parent.computeOrGetLeafValueOrBetter(depth);
            return true;
        }
        // node is a leaf if it has no children
        if (parent.hasChildren()) {
            return false;
        } 
        //getOrComputeChildren guarantees ComputeChildrenException or parent.hasChildren()
        throw new IllegalStateException("branching error");
    }

    /**
     * Increases the evaluated node counter by one.
     */
    protected void increaseEvaluatedNodeCount() {
        this.evaluatedNodeCount += 1;
    }

    @Override
    public int getEvaluatedNodeCount() {
        return this.evaluatedNodeCount;
    }

    @Override
    public void resetEvaluatedNodeCount() {
        this.evaluatedNodeCount = 0;
    }

}
