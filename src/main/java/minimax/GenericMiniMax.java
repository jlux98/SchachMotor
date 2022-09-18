package minimax;

import gametree.Node;
import gametree.Tree;

/**
 * Class implementing Minimax for trees consisting of Nodes that store any kind of Evaluable.
 */
public class GenericMiniMax<T> extends BaseTreeEvaluator<T> {

    @Override
    public Node<T> evaluateTree(Tree<? extends Node<T>> tree, int depth, boolean whitesTurn) {
        return evaluateNode(tree.getRoot(), depth, whitesTurn);
    }

    @Override
    public Node<T> evaluateNode(Node<T> gameTree, int depth, boolean whitesTurn) {
        // TODO implement using trimmed down alpha-beta code (useful for testing)
        return null;
    }

}
