package gametree;

import model.Position;
import positionevaluator.Evaluable;

/**
 * Interface for the nodes of a game tree.
 * GameNodes are implemented as a subtype of Node < Position >.
 * <br><br>
 * <b>Important Note:</b>
 * While some methods of this interface accept any Node &lt; Position &gt; only GameNodes and subtypes of GameNode should be passed to these methods.
 */
public interface GameNode extends Node<Position>, Evaluable {

    //@Override
    //public abstract List<GameNode> queryChildren();

}
