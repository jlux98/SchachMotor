package gametree;

import model.Move;
import model.Position;

public class DetachingGameNode extends GameNode {
    private Move representedMove;

    /**
    * Creates a root node storing the position
    * and the corresponding move.
    * @param position position stored by the node
    */
    public DetachingGameNode(Position position) {
        super(position);
        representedMove = position.getMove();
    }

    /**
     * Creates a child node storing the position
     * and the corresponding move.
     * The nodes are properly linked to each other by this constructor.
     * @param position position stored by the node
     * @param parent   parent of the created node
     */
    public DetachingGameNode(Position position, GameNode parent) {
        super(position, parent);
        representedMove = position.getMove();
    }

    @Override
    public GameNode createChild(Position position) {
        //makes computeChildren() generate DetachingGameNodes
        return new DetachingGameNode(position, this);
    }

    @Override
    public Move getRepresentedMove() {
        return representedMove;
    }

    @Override
    protected void detachChildGenerationData() {
        this.deleteContent(); //position = null
    }

    @Override
    public int evaluateStatically(boolean isNaturalLeaf, int depth) {
        if (getContent() == null) {
            throw new NullPointerException("cannot evaluate because position was already detached");
        }
        int value = getContent().evaluateBoard(isNaturalLeaf, depth);
        setValue(value);
        //this.deleteContent(); //FIXME re-enable
        return value;
    }
}
