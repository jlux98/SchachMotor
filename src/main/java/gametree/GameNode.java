package gametree;


/**
 * Interface for the nodes of a game tree. 
 */
public interface GameNode {

    //TODO doc
    public abstract void insertChild(GameNode node);

    public abstract void deleteChild(GameNode node);

    public abstract void deleteChildren();

    public abstract boolean hasChildren();

    public abstract void setValue(int value);

    public abstract void computeChildren();

    public abstract GameNode findPlayableAncestor();

}
