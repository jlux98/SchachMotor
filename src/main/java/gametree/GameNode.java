package gametree;


import model.Position;
import positionevaluator.Evaluable;

/**
 * Interface for the nodes of a game tree.
 * <br><br>
 * <b>Important Note:</b>
 * While some methods of this interface accept any Node &lt; Position &gt; only GameNodes and subtypes of GameNode should be passed to these methods.
 */
public interface GameNode extends Node<Position>, Evaluable{ //must implement Node<Evaluable> as well

    //@Override
    //public abstract List<GameNode> queryChildren();

}
