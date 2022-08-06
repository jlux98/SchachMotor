package gametree;

import java.util.List;

import positionevaluator.Evaluable;

/**
 * Class implementing Alpha-Beta-Pruning-Minimax for trees consisting of Nodes that store any kind of Evaluable.
 */
public class AlphaBetaPruningBase<T extends Evaluable> implements TreeEvaluator<T> {

    //FIXME replace with clean implementation from git
    //TODO doc
    //FIXME remove redundant code.
    //originally implemented it this way because root node has to use the full alpha-beta code to properly
    //update alpha/beta of its children and method calls can return only one value (in this case determined node value)
    //option A: create a class storing a value and a node to pass the determined value and best child to parent?
    //option B: make nodes store values again, return best child instead of value, value can be determined from child
    //issue: if the tree is reused, values have to actively be reset
    //unless validity of a node value depends on a counter that is static and increased after every evaluated tree
    //this way all node values would be invalid when the next evaluation starts, but the tree would not have to be iterated
    //over to reset a flag or the stored values
    //option C: like B write value to nodes, but iterate over children of root and look for one with the determined value
    //instead of having a mechanism to invalidate node values
    @Override
    public Node<T> evaluateTree(Node<T> node, int depth, boolean whitesTurn) {

        //do one non recursive iteration of alpha beta so you can reference the gamenode that should be played and return it
        Node<T> bestMove = null; //specific to this first iteration
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        List<? extends Node<T>> children = node.queryChildren(); //get or calculate children
        if (whitesTurn) {

            //maximize
            int parentValue = Integer.MIN_VALUE;
            int childValue;
            for (Node<T> child : children) {
                //evaluate all children recursively by minimizing them
                childValue = alphaBetaMinimize(child, depth - 1, alpha, beta);
                if (childValue >= beta) {
                    //maximizing player can achieve a higher score than minimizing player is already assured of if this parent node is reached
                    //prune this subtree = stop evaluating children of this node
                    //return current value of parentValue which is guaranteed not to affect the value of root
                    //one could also return Integer.MAX_VALUE instead as any node with value > beta will never be played by the minimizing player
                    break;
                }
                if (childValue > alpha) {
                    //maximizing player has new best guaranteed score if parent node is reached
                    alpha = childValue;
                }
                if (childValue > parentValue) {
                    //maximizing player's turn -> value of this parent node = max of child values
                    parentValue = childValue;
                    bestMove = child; //save this node/move
                }

            }
            //return the determined value of this parent node
            //or if alpha-cutoff (break statement reached) return some value that will be "ignored"
            return bestMove; //FIXME possibly null

        } else {
            //minimize
            int parentValue = Integer.MAX_VALUE;
            int childValue;
            for (Node<T> child : children) {
                //evaluate all children recursively by maximizing them
                childValue = alphaBetaMaximize(child, depth - 1, alpha, beta);
                if (childValue <= alpha) {
                    //minimizing player can achieve a lower score than maximizing player is already assured of if this parent node is reached
                    //prune this subtree = stop evaluating children of this node
                    //return current value of parentValue which is guaranteed not to affect the value of root
                    //one could also return Integer.MIN_VALUE instead as any node with value < alpha will never be played by the maximizing player
                    break;
                }
                if (childValue < beta) {
                    //maximizing player has new best guaranteed score if parent node is reached
                    alpha = childValue;
                }
                if (childValue < parentValue) {
                    //minimizing player's turn -> value of this parent node = min of child values
                    parentValue = childValue;
                    bestMove = child; //save this move/node
                }

            }
            //return the determined value of this parent node
            //or if alpha-cutoff (break statement reached) return some value that will be "ignored"
            return bestMove; //FIXME null if root has no children
        }

    }

    /**
     * What evaluateTree code could look like if you did not have to reference the node to be played.
     */
    /*     private int alphaBetaPruningMiniMax(GameNode node, int depth, int alpha, int beta) {
        //assign static evaluation to leaves
        if (depth == 0) { //FIXME nodes may be leaves before depth = 0 e.g. check mate boards have no children
            return PositionEvaluator.evaluatePosition(node.getPosition());
        }
    
        if (node.getPosition().getWhiteNextMove()) {
            //maximize this node
            return alphaBetaMaximize(node, depth - 1, alpha, beta);
    
        } else {
            //minimize this node
            return alphaBetaMinimize(node, depth - 1, alpha, beta);
    
        }
    } */

    /**
     * Minimizes this node (value = min(child values)) and returns its value.
     * @param parent
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    private int alphaBetaMinimize(Node<T> parent, int depth, int alpha, int beta) {
        //assign static evaluation to leaves
        if (depth == 0) { //FIXME nodes may be leaves before depth = 0 e.g. check mate boards have no children
            //return PositionEvaluator.evaluatePosition(parent.getPosition());
            //TODO replace with parent.evaluate()
            return parent.getContent().evaluate();
        }

        //minimize
        int parentValue = Integer.MAX_VALUE;
        int childValue;
        List<? extends Node<T>> children = parent.queryChildren(); //get or calculate children
        for (Node<T> child : children) {
            //evaluate all children
            //if this node is minimizing child nodes are maximizing
            childValue = alphaBetaMaximize(child, depth - 1, alpha, beta);
            if (childValue <= alpha) {
                //minimizing player can achieve a lower score than maximizing player is already assured of if this parent node is reached
                //prune this subtree = stop evaluating children of this node
                //return current value of parentValue which is guaranteed not to affect the value of root
                //one could also return Integer.MIN_VALUE instead as any node with value < alpha will never be played by the maximizing player
                break;
            }
            if (childValue < beta) {
                //maximizing player has new best guaranteed score if parent node is reached
                alpha = childValue;
            }
            if (childValue < parentValue) {
                //minimizing player's turn -> value of this parent node = min of child values
                parentValue = childValue;
            }

        }
        //return the determined value of this parent node
        //or if alpha-cutoff (break statement reached) return some value that will be "ignored"
        return parentValue;

    }

    /**
     * Maximizes this node (value = max(child values)) and returns its value.
     * @param parent
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    private int alphaBetaMaximize(Node<T> parent, int depth, int alpha, int beta) {
        //assign static evaluation to leaves
        if (depth == 0) { //FIXME nodes may be leaves before depth = 0 e.g. check mate boards have no children
            //return PositionEvaluator.evaluatePosition(parent.getPosition());
            //TODO replace with parent.evaluate()
            return parent.getContent().evaluate();
        }

        //maximize
        int parentValue = Integer.MIN_VALUE;
        int childValue;
        List<? extends Node<T>> children = parent.queryChildren(); //get or calculate children
        for (Node<T> child : children) {
            //evaluate all children
            //if this node is maximizing child nodes are minimizing
            //child nodes are passed the determined alpha and beta values
            childValue = alphaBetaMinimize(child, depth - 1, alpha, beta);
            if (childValue >= beta) {
                //maximizing player can achieve a higher score than minimizing player is already assured of if this parent node is reached
                //prune this subtree = stop evaluating children of this node
                //return current value of parentValue which is guaranteed not to affect the value of root
                //one could also return Integer.MAX_VALUE instead as any node with value > beta will never be played by the minimizing player
                break;
            }
            if (childValue > alpha) {
                //maximizing player has new best guaranteed score if parent node is reached
                alpha = childValue;
            }
            if (childValue > parentValue) {
                //maximizing player's turn -> value of this parent node = max of child values
                parentValue = childValue;
            }

        }
        //return the determined value of this parent node
        //or if alpha-cutoff (break statement reached) return some value that will be "ignored"
        return parentValue;

    }

}
