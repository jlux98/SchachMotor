package gametree;

import java.util.List;

import positionevaluator.Evaluable;

/**
 * Class implementing Alpha-Beta-Pruning-Minimax for trees consisting of Nodes that store any kind of Evaluable.
 */
public class AlphaBetaPruningBase<T extends Evaluable> implements TreeEvaluator<T> {

    //FIXME replace with clean implementation from git

    //Note on storing values in nodes:
    //values stored by nodes do not have to be marked as invalid
    //leaves overwrite their old value (they could have an old value because iterative deepening doesnt start with old
    //max depth, so nodes that are leaves for this iteration might not actually be leaves in the gametree)
    //inner nodes do not read their own value and overwrite it with values of their children

    @Override
    //TODO accept Tree instead of node 
    public Node<T> evaluateTree(Node<T> node, int depth, boolean whitesTurn) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        return alphaBetaPruningMiniMax(node, depth, alpha, beta, whitesTurn);
    }

    /**
     * Applies alpha-beta-pruning minimax to the passed node and returns the child node that should be played.
     * @param parent the node whose value should be determined
     * @param depth the additional depth to which the tree should be evaluated
     * (e.g. depth = 2 means that children of the passed node and their children should be evaluated 
     * to determine the value of the passed node)
     * @param alpha minimum score that white player is already guaranteed of 
     * (if the parent of the node passed to this method is reached)
     * @param beta maximum score that black player is already guaranteed of
     * (if the parent of the node passed to this method is reached)
     * @return the child node that has the best value
     */
    public Node<T> alphaBetaPruningMiniMax(Node<T> parent, int depth, int alpha, int beta, boolean whiteNextMove) {
        //assign static evaluation to leaves
        if (isLeaf(parent, depth)) {
            //return PositionEvaluator.evaluatePosition(parent.getPosition());
            parent.getContent().evaluate();
            return parent;
        }

        if (whiteNextMove) {
            //maximize this node
            return alphaBetaMaximize(parent, depth - 1, alpha, beta);

        } else {
            //minimize this node
            return alphaBetaMinimize(parent, depth - 1, alpha, beta);

        }
    }

    /**
     * Returns whether the passed node is a leaf node when inspected by alpha-beta-pruning.
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
     * @param parent
     * @param depth the
     * @return whether the passed node is a leaf node
     */
    private boolean isLeaf(Node<T> parent, int depth) {
        if (depth == 0) {
            return true;
        }
        parent.queryChildren(); //TODO specify behavior of querychildren if no children can be calculated
        return parent.hasChildren();
    }

    /**
     * Minimizes the passed node (value = min(child values)) and returns the child node with
     * the best (smallest) value.
     * @param parent the node whose value should be determined
     * @param depth the additional depth to which the tree should be evaluated
     * (e.g. depth = 2 means that children of the passed node and their children should be evaluated 
     * to determine the value of the passed node)
     * @param alpha minimum score that white player is already guaranteed of 
     * (if the parent of the node passed to this method is reached)
     * @param beta maximum score that black player is already guaranteed of
     * (if the parent of the node passed to this method is reached)
     * @return the child node that has the best (smallest) value
     */
    private Node<T> alphaBetaMinimize(Node<T> parent, int depth, int alpha, int beta) {
        //assign static evaluation to leaves
        if (isLeaf(parent, depth)) {
            //return PositionEvaluator.evaluatePosition(parent.getPosition());
            parent.getContent().evaluate();
            return parent;
        }

        //minimize
        int parentValue = Integer.MAX_VALUE;
        int childValue;
        Node<T> bestChild = null;
        Node<T> childBestMove = null;
        List<? extends Node<T>> children = parent.queryChildren(); //get or calculate children
        for (Node<T> child : children) {
            //evaluate all children
            //if this node is minimizing child nodes are maximizing
            //child nodes are passed the determined alpha and beta values
            childBestMove = alphaBetaMinimize(child, depth - 1, alpha, beta);
            //read value of child node = value of the node returned by alphaBetaMinimize(child ...)
            // = value of the Evaluable stored by that node (retrieved with getContent())
            childValue = childBestMove.getContent().getValue();
            if (childValue <= alpha) {
                //minimizing player can achieve a lower score than maximizing player is already assured of if this parent node is reached
                //prune this subtree = stop evaluating children of this node
                //return current best child whose value is guaranteed not to affect the value of root
                //one could also return a node with value of Integer.MIN_VALUE instead
                //as any node with value < alpha will never be played by the maximizing player
                break;
            }
            if (childValue < beta) {
                //maximizing player has new best guaranteed score if parent node is reached
                alpha = childValue;
            }
            if (childValue < parentValue) {
                //minimizing player's turn -> value of this parent node = min of child values
                parentValue = childValue;
                //store current child as best child
                bestChild = child;
            }

        }
        //return the best child node
        //that value stored by that node also is the value of this parent node
        //or if alpha-cutoff (break statement reached) return some node that will be "ignored"
        return bestChild;

    }

    /**
     * Maximizes the passed node (value = min(child values)) and returns the child node with
     * the best (greatest) value.
     * @param parent the node whose value should be determined
     * @param depth the additional depth to which the tree should be evaluated
     * (e.g. depth = 2 means that children of the passed node and their children should be evaluated 
     * to determine the value of the passed node)
     * @param alpha minimum score that white player is already guaranteed of 
     * (if the parent of the node passed to this method is reached)
     * @param beta maximum score that black player is already guaranteed of
     * (if the parent of the node passed to this method is reached)
     * @return the child node that has the best (greatest) value
     */
    private Node<T> alphaBetaMaximize(Node<T> parent, int depth, int alpha, int beta) {
        //assign static evaluation to leaves
        if (isLeaf(parent, depth)) {
            //return PositionEvaluator.evaluatePosition(parent.getPosition());
            parent.getContent().evaluate();
            return parent;
        }

        //maximize
        int parentValue = Integer.MIN_VALUE;
        int childValue;
        List<? extends Node<T>> children = parent.queryChildren(); //get or calculate children
        Node<T> bestChild = null; //the child that determines the value of this parent node
        Node<T> childBestMove; //the node that determines the value of the current child of this parent
        for (Node<T> child : children) {
            //evaluate all children
            //if this node is maximizing child nodes are minimizing
            //child nodes are passed the determined alpha and beta values
            childBestMove = alphaBetaMinimize(child, depth - 1, alpha, beta);
            //read value of child node = value of the node returned by alphaBetaMinimize(child ...)
            // = value of the Evaluable stored by that node (retrieved with getContent())
            childValue = childBestMove.getContent().getValue();
            if (childValue >= beta) {
                //maximizing player can achieve a higher score than minimizing player is already assured of if this parent node is reached
                //prune this subtree = stop evaluating children of this node
                //return current best child whose value is guaranteed not to affect the value of root
                //one could also return a node with value of Integer.MAX_VALUE instead
                //as any node with value > beta will never be played by the minimizing player
                break;
            }
            if (childValue > alpha) {
                //maximizing player has new best guaranteed score if parent node is reached
                alpha = childValue;
            }
            if (childValue > parentValue) {
                //maximizing player's turn -> value of this parent node = max of child values
                parentValue = childValue;
                //store current child as best child
                bestChild = child;
            }

        }
        //return the best child node
        //that value stored by that node also is the value of this parent node
        //or if beta-cutoff (break statement reached) return some node that will be "ignored"
        return bestChild; //FIXME null if node has no children but depth isnt reached

    }

}
