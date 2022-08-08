package gametree;

import model.Position;
/**
 * Interface for subtypes of TreeEvaluator < Position > that serve as wrappers to return GameNodes rather than Node < Position >.
 * <br><br>
 * <b>Note:</b>
 * Classes implementing this interface generally perform casts from Node < Position > to GameNode which should be safe
 * so long as the passed tree consists only of gamenodes and subtypes of gamenode.
 */
public interface GameTreeEvaluator extends TreeEvaluator<Position>{



    @Override
    public abstract GameNode evaluateTree(Tree<? extends Node<Position>> tree, int depth, boolean whitesTurn);

    @Override
    public abstract GameNode evaluateNode(Node<Position> node, int depth, boolean whitesTurn);
    
}
