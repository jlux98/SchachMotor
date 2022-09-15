package gametree;

import model.Move;
import model.Position;
import movegenerator.MoveGenerator;

/**
 * Class implementing Nodes containing Positions.
 * Extends BaseNode < Position > for node operations,
 * implements {@link #computeChildren()} to generate follow-up moves
 * and implements {@link #evaluateStatically()} to calculate the position's point value.
 * Additionally, this class narrows the return types of node methods from Node < Position > to GameNode.
 * <p>
 * {@link #computeChildren()} instantiates GameNodes by invoking {@link #createChild(Position)}.
 * This method can be overriden by subtypes to instantiate their class instead.
 * </p>
 * <p>
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
 * </p>
 */
public class GameNode extends BaseNode<Position> {

    /**
     * Creates a root node.
     * @param position position stored by the node
     */
    public GameNode(Position position) {
        super(position);
    }

    /**
     * Creates a child node.
     * The nodes are properly linked to each other by this constructor. 
     * @param position position stored by the node
     * @param parent   parent of the created node
     */
    public GameNode(Position position, GameNode parent) {
        super(position, parent);
        // other attributes e.g. children are null here
    }

    @Override
    public GameNode createChild(Position position) {
        return new GameNode(position, this);
    }

    @Override
    // narrows return type from Node<T> to GameNode
    public GameNode getParent() {
        return (GameNode) super.getParent();
    }

    /**
     * Returns the move represented by this node.
     * May return null if the stored position was not created by move generation.
     * @return the move represented by this node
     */
    public Move getRepresentedMove() {
        return getContent().getMove();
    }

    /**
    * Computes this node's children and overwrites its current child list accordingly.
    * Uses {@link #createChild(Position)} to instantiate children.
    * <br><br>
    * <b>Note:</b> Do not use this method directly to generate children of this node.
    * This is a helper method that is implemented individually by subtypes and called by {@link #queryChildren()}.
    * Use queryChildren() to generate children of this node.
    * @throws ComputeChildrenException if no children can be computed
    */
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
            createChild(position);
            //instantiates GameNodes in GameNode but DetachingGameNodes in DetachingGameNode
        }
    }

    @Override
    protected int computeStaticValue(boolean isNaturalLeaf, int depth) {
        if (getContent() == null) {
            throw new NullPointerException("cannot evaluate because position was already detached");
        }
        return getContent().evaluateBoard(isNaturalLeaf, depth);        
    }

}
