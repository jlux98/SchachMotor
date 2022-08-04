package gametree;

import java.util.List;

import positionevaluator.PositionEvaluator;

public class AlphaBetaPruning implements TreeEvaluator {

    @Override
    public GameNode evaluateTree(GameTree gameTree) {
        // TODO Auto-generated method stub
        return null;
    }

    //TODO somehow invoke alphaBetaPruningMinMax and determine the node to be played
    //issue: values are not stored in the tree -> cant iterate over level 1 nodes to pick the one with root value
    //solutions:
    //a) store values in tree
    //b) change alphaBetaPruningMiniMax somehow to make it work e.g. if depth max (root layer) put the node with highest value in some data structure

    /**
     * Determines the value of the root node and returns it.
     * The child node with the same value as the root node should be played.
     * @param parent
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    public int alphaBetaPruningMiniMax(GameNode parent, int depth, int alpha, int beta) {
        //assign static evaluation to leaves
        if (depth == 0) { //FIXME nodes may be leaves before depth = 0 e.g. check mate boards have no children
            return PositionEvaluator.evaluatePosition(parent.getPosition());
        }

        List<GameNode> children = parent.getChildren(); //get or calculate children
        if (parent.getPosition().getWhiteNextMove()) {
            
            //maximize
            int parentValue = Integer.MIN_VALUE;
            int childValue;
            for (GameNode child : children) {
                //evaluate all children
                childValue = alphaBetaPruningMiniMax(child, depth-1, alpha, beta);
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

        } else {
            //minimize
            int parentValue = Integer.MAX_VALUE;
            int childValue;
            for (GameNode child : children) {
                //evaluate all children
                childValue = alphaBetaPruningMiniMax(child, depth-1, alpha, beta);
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

    }

    
}
