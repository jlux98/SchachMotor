package minimax;

import gametree.GameNode;
import gametree.Node;
import gametree.Tree;
import model.Position;

/**
 * Interface for subtypes of TreeEvaluator < Position > that serve as wrappers
 * to return GameNodes rather than Node < Position >.
 * <br><br>
 * <b>Note:</b>
 * Classes implementing this interface generally perform casts from Node <
 * Position > to GameNode which should be safe
 * so long as the passed tree consists only of gamenodes and subtypes of
 * gamenode.
 */
public interface GameTreeEvaluator extends TreeEvaluator<Position> {

    /**
     * Evaluates the game tree and returns the GameNode that should be played.
     * <br><br>
     * <b>Note:</b> Trees passed to this method should consist of GameNodes (and
     * subtypes of GameNode) only.
     * 
     * @param gameTree   the tree to be evaluated
     * @param depth      the maximum depth of the tree
     * @param whitesTurn whether the turn to be searched is played by white
     * @return the GameNode representing the turn to be played
     */
    @Override
    public abstract GameNode evaluateTree(Tree<? extends Node<Position>> tree, int depth, boolean whitesTurn);

    /**
     * Evaluates the sub tree starting with the passed node and returns the GameNode
     * that should be played.
     * 
     * @param node       the subtree to be evaluated
     * @param depth      the maximum depth of the tree
     * @param whitesTurn whether the turn to be searched is played by white
     * @return the GameNode representing the turn to be played
     * @deprecated will probably be removed from interfaces and changed to a private
     *             method, use {@link #evaluateTree(Tree, int, boolean)} instead
     */
    @Override
    public abstract GameNode evaluateNode(Node<Position> node, int depth, boolean whitesTurn);

}
