package gametree;

import java.util.ArrayList;
import java.util.List;

import model.Position;
import movegenerator.MoveGenerator;

public class ImpGameNode implements GameNode {

    private GameNode parent;
    private List<GameNode> children;
    private Position gameState;
    private boolean evaluated = false;

    private ImpGameNode(Position position) {
        this.gameState = position;
    }

    private ImpGameNode(Position position, GameNode parent) {
        this(position);
        this.parent = parent;
        // other attributes are null here
    }

    /**
     * Creates a node without parent.
     * 
     * @param position the position to be stored in the node
     * @return a node serving as root for a gametree
     */
    public static GameNode createRoot(Position position) {
        return new ImpGameNode(position);
    }

    /**
     * @param position  the position to be stored in the node
     * @param parent this node's parent
     * @return a node with parent
     */
    public static GameNode createNode(Position position, GameNode parent) {
        return new ImpGameNode(position, parent);
    }

    /**
     * Creates a list to store children of this node, if it does not yet exist.
     */
    private void createChildListIfNotExists() {
        if (this.children == null) {
            this.children = new ArrayList<GameNode>(20);
        }
    }

    @Override
    public boolean hasChildren() {
        return this.children == null || this.children.size() != 0;
    }

    @Override
    public void insertChild(GameNode node) {
        createChildListIfNotExists();
        this.children.add(node);

    }

    @Override
    public void deleteSelf() {
        try {
            this.parent.deleteChild(this);
        } catch (IllegalArgumentException exception) {
            //if this node cannot be found as a child of it's parent,
            //it is deemed to already have been removed -> void exception
        }
    }

    @Override
    public void deleteChild(GameNode node) {
        // removing with ArrayList.remove(node) would require gamenode.equals()
        // which would probably have to compare positions which is inefficient

        // removing by equals also does not guarantee removal of the correct node if 2
        // equal nodes are present,
        // though in that case either all or none of the nodes should be removed
        GameNode child = null;
        for (int i = 0; i < children.size(); i++) {
            child = children.get(i);
            if (child == node) {
                children.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("child to remove could not be found");
    }

    @Override
    public void deleteChildren() {
        this.children.clear();
    }

    @Override
    public List<GameNode> queryChildren() {
        if (!hasChildren()) {
            computeChildren();
        }
        return children;
    }

    /**
     * Computes this node's children and overwrites its current child list.
     */
    private void computeChildren() {
        //TODO testing!
        createChildListIfNotExists();
        if (this.children.size() != 0) {
            throw new IllegalStateException("node already has children");
        }
        Position[] followUpPositions = MoveGenerator.generatePossibleMoves(gameState);
        for (Position position : followUpPositions) {            
            this.children.add(createNode(position, this));
        }
    }

    @Override
    public int queryValue() {
        return this.gameState.queryValue();
    }

    /**
     * Sets the child list of this node.
     * <br>
     * <br>
     * <b>Note:</b>
     * Be aware that this removes (overwrites) all formerly attached child nodes.
     * 
     * @param children list of nodes that should be attached to this node
     */
    private void setChildren(List<GameNode> children) {
        this.children = children;
    }

    @Override
    public GameNode getParent() {
        return this.parent;
    }

    @Override
    public Position getPosition() {
        return this.gameState;
    }

    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public Position getContent() {
        return gameState;
    }

}
