package gametree;

//TODO doc
public interface GameNode {
    public abstract void insertChild(GameNode node);

    public abstract void deleteChild(GameNode node);

    public abstract void deleteChildren();

    public abstract boolean hasChildren();

    public abstract void setValue();

    public abstract void computeChildren();

    public abstract GameNode findPlayableAncestor();

}
