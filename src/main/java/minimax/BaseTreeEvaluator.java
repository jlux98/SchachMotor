package minimax;

import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.Tree;

/**
 * Abstract class implementing TreeEvaluator,
 * providing partial implementation of the counter
 * for evaluated nodes.
 * <p>
 * Extending classes must call {@link #increaseEvaluatedNodeCount()}
 * whenever evaluating a node.
 * </p>
 */
public abstract class BaseTreeEvaluator<ContentType> implements TreeEvaluator<ContentType> {

    private int evaluatedNodeCount = 0;

    @Override
    public Node<ContentType> evaluateTree(Tree<? extends Node<ContentType>> tree, int depth, boolean whitesTurn) {
        if (depth < 1) {
            throw new IllegalArgumentException("evaluation depth must be at least one");
        }
        resetEvaluatedNodeCount();
        return evaluateNode(tree.getRoot(), depth, whitesTurn);
    }

     /**
     * Evaluates the sub tree starting with the passed node and returns the Node that should be played.
     * @param node the subtree to be evaluated
     * @param depth the maximum depth of the tree
     * @param whitesTurn whether the turn to be searched is played by white
     * @return the Node representing the turn to be played
     */
    protected abstract Node<ContentType> evaluateNode(Node<ContentType> node, int depth, boolean whitesTurn);

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
    protected boolean evaluateIfLeaf(Node<ContentType> parent, int depth) {
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
