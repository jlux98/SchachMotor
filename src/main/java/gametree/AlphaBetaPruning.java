package gametree;

import java.util.List;

import positionevaluator.Evaluable;

public class AlphaBetaPruning implements TreeEvaluator<Node<Evaluable>> {
    

    
    @Override
    public Node<Evaluable> evaluateTree(Tree<Node<Evaluable>> gameTree, int depth, boolean whitesTurn) {

        //do one non recursive iteration of alpha beta so you can reference the gamenode that should be played and return it
        Node<Evaluable> bestMove = null; //specific to this first iteration
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        Node<Evaluable> root = gameTree.getRoot();
        List<? extends Node<Evaluable>> children = root.queryChildren(); //get or calculate children
        if (whitesTurn) {

            //maximize
            int parentValue = Integer.MIN_VALUE;
            int childValue;
            for (Node<Evaluable> child : children) {
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
            for (Node<Evaluable> child : children) {
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
    private int alphaBetaMinimize(Node<Evaluable> parent, int depth, int alpha, int beta) {
        //assign static evaluation to leaves
        if (depth == 0) { //FIXME nodes may be leaves before depth = 0 e.g. check mate boards have no children
            //return PositionEvaluator.evaluatePosition(parent.getPosition());
            return parent.getContent().queryValue();
        }

        //minimize
        int parentValue = Integer.MAX_VALUE;
        int childValue;
        List<? extends Node<Evaluable>> children = parent.queryChildren(); //get or calculate children
        for (Node<Evaluable> child : children) {
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
    private int alphaBetaMaximize(Node<Evaluable> parent, int depth, int alpha, int beta) {
        //assign static evaluation to leaves
        if (depth == 0) { //FIXME nodes may be leaves before depth = 0 e.g. check mate boards have no children
            //return PositionEvaluator.evaluatePosition(parent.getPosition());
            return parent.getContent().queryValue();
        }

        //maximize
        int parentValue = Integer.MIN_VALUE;
        int childValue;
        List<? extends Node<Evaluable>> children = parent.queryChildren(); //get or calculate children
        for (Node<Evaluable> child : children) {
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
