package gametree;

import model.Position;
import positionevaluator.Evaluable;

/**
 * Interface for the nodes of a game tree.
 */
public interface GameNode extends Node<Position>, Evaluable {

    /**
     * @return the position stored by this node
     */
    public abstract Position getPosition();

    /**
     * @return true - if a value has been assigned to this node,
     * false - otherwise
     */
    public abstract boolean isEvaluated();

}
