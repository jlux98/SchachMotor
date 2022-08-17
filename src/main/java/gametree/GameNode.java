package gametree;

import model.Position;
import movegenerator.MoveGenerator;

/**
 * Class implementing Nodes containing Positions.
 * Extends BaseNode < Position > for node operations and implements the Evaluable interface on top.
 * Additionally, this class narrows the return types of node methods from Node < Position > to GameNode.
 * <br><br>
 * <b>Important Notes:</b>
 * <ul>
 *      <li>
 *          While some methods of this class accept any Node &lt; Position &gt;
 *          only GameNodes and subtypes of GameNode should be passed to these methods.
 *      </li>
 *      <li>
 *          This class performs casts from Node < Position > to GameNode which should be safe
 *          so long as the containing tree consists only of gamenodes and subtypes of gamenode.
 *      </li>
 * </ul>
 */
public class GameNode extends BaseNode<Position> {

    /**
     * Creates a root node.
     * @param position position stored by the node
     */
    private GameNode(Position position) {
        super(position);
    }

    /**
     * Creates a child node.
     * @param position position stored by the node
     * @param parent parent of the created node
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
     * @param position  the position to be stored in the node
     * @param parent this node's parent
     * @return a node with parent
     */
    public static GameNode createNode(Position position, GameNode parent) {
        return new GameNode(position, parent);
    }

    @Override
    //narrows return type from Node<T> to GameNode
    public GameNode getParent() {
        return (GameNode) super.getParent();
    }

    @Override
    protected void computeChildren() {
        //TODO testing!
        if (hasChildren()) {
            throw new IllegalStateException("node already has children");
        }
        createChildListIfNotExists();
        Position[] followUpPositions = MoveGenerator.generatePossibleMoves(this.getContent());
        for (Position position : followUpPositions) {
            createNode(position, this);
        }
    }
}
