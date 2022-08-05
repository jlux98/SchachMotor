package gametree;

import java.util.List;

import model.Position;
import positionevaluator.Evaluable;

/**
 * Interface for the nodes of a game tree.
 */
public interface GameNode extends Node<Position>, Evaluable{ //must implement Node<Evaluable> as well

    @Override
    public abstract List<GameNode> queryChildren();
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
