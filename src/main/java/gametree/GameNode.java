package gametree;

import model.Position;
import movegenerator.MoveGenerator;

/**
 * Class implementing Nodes containing Positions.
 * Extends BaseNode < Position > for node operations,
 * implements {@link #computeChildren()} to generate follow-up moves
 * and implements {@link #evaluateStatically()} to calculate the position's point value.
 * Additionally, this class narrows the return types of node methods from Node < Position > to GameNode.
 * <br><br>
 * <b>Important Notes:</b>
 * <ul>
 *      <li>
 *          While some methods of this class accept any Node &lt; Position &gt;
 *          only GameNodes and subtypes of GameNode should be passed to these methods.
 *      </li>
 *      <li>
 *          This class performs casts from Node < Position > to GameNode which should be
 *          safe so long as the containing tree consists only of gamenodes and subtypes of
 *          gamenode.
 *      </li>
 * </ul>
 */
public class GameNode extends BaseNode<Position> {

    /**
     * Creates a root node.
     * 
     * @param position position stored by the node
     */
    private GameNode(Position position) {
        super(position);
    }

    /**
     * Creates a child node.
     * 
     * @param position position stored by the node
     * @param parent   parent of the created node
     */
    private GameNode(Position position, GameNode parent) {
        super(position, parent);
        // other attributes e.g. children are null here
    }

    /**
     * Creates a node without parent.
     * 
     * @param position the position to be stored in the node
     * @return a node serving as root for a gametree
     */
    public static GameNode createRoot(Position position) {
        return new GameNode(position);
    }

    /**
     * @param position the position to be stored in the node
     * @param parent   this node's parent
     * @return a node with parent
     */
    public static GameNode createNode(Position position, GameNode parent) {
        return new GameNode(position, parent);
    }

    @Override
    // narrows return type from Node<T> to GameNode
    public GameNode getParent() {
        return (GameNode) super.getParent();
    }

    @Override
    protected void computeChildren() throws ComputeChildrenException {
        if (hasChildren()) {
            throw new IllegalStateException("node already has children");
        }
        createChildListIfNotExists();
        Position[] followUpPositions = MoveGenerator.generatePossibleMoves(this.getContent());

        if (followUpPositions.length == 0) {
            // no moves were generated
            throw new ComputeChildrenException(
                    "no children could be generated for this position: " + this.getContent().toString());
        }

        // add follow-up moves as child nodes to this node
        for (Position position : followUpPositions) {
            createNode(position, this);
        }
    }

    @Override
    public int evaluateStatically(boolean isNaturalLeaf, int depth) {
        if (getContent() == null) {
            throw new NullPointerException("cannot evaluate because position was already detached");
        }
        int value = getContent().evaluateBoard(isNaturalLeaf, depth);
        setValue(value);
        return value;
    }

}
