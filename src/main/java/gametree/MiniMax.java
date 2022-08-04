package gametree;

import java.util.List;

import positionevaluator.PositionEvaluator;

public class MiniMax implements TreeEvaluator {

    //white maximizes score
    //black minimizes score

    //FIXME replace with an implementation based on alpha beta implementation

    @Override
    public GameNode evaluateTree(GameTree gameTree) {
        GameNode root = gameTree.getRoot();
        evaluateLeaves(gameTree);
        updateNode(root);
        int rootValue = root.getValue();
        for (GameNode playableChild : root.getChildren()) {
            if (playableChild.getValue() == rootValue) {
                return playableChild;
            }
        }
        throw new IllegalStateException("root does not have children or none of the chilren match root's value");
    }


    private void evaluateLeaves(GameTree gameTree) {
        List<GameNode> leaves = gameTree.getLeafList();
        for(GameNode leaf : leaves) {
            leaf.setValue(PositionEvaluator.evaluatePosition(leaf.getPosition()));
        }
    }

    private void updateNode(GameNode node) {
        //maximize if next turn is white's, minimize if next turn is black's
        boolean maximize = node.getPosition().getWhiteNextMove();
        for (GameNode child : node.getChildren()) {
            //already evaluated inner nodes and leaves are skipped (leaves: isEvaluated = true after calling evaluateLeaves())
            if (!child.isEvaluated()) { 
                //recursively update children
                updateNode(child);
            }
            //update this node
            if (maximize) {
                //maximize
                int maxValue = Integer.MIN_VALUE;
                if (child.getValue() > maxValue) {
                    maxValue = child.getValue();
                    //getting to parent node guarantees being able to play this turn,
                    //thus getting to parent node has the same value as playing this turn
                    node.setValue(maxValue);
                }
            } else {
                //minimize
                int minValue = Integer.MAX_VALUE;
                if (child.getValue() < minValue) {
                    minValue = child.getValue();
                    //getting to parent node guarantees being able to play this turn,
                    //thus getting to parent node has the same value as playing this turn
                    node.setValue(minValue);

                }
            }
        }
    }
}
