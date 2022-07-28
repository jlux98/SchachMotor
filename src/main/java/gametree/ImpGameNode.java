package gametree;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import movegenerator.MoveGenerator;

public class ImpGameNode implements GameNode {

    private GameNode parent;
    private List<GameNode> children;
    private Board gameState;
    private int pointValue;

    private ImpGameNode(Board board) {
        this.gameState = board;
    }

    private ImpGameNode(Board board, GameNode parent) {
        this(board);
        this.parent = parent;
        // other attributes are null here
    }

    /**
     * Creates a node without parent.
     * 
     * @param board the board to be stored in the node
     * @return a node serving as root for a gametree
     */
    public static GameNode createRoot(Board board) {
        return new ImpGameNode(board);
    }

    /**
     * @param board  the board to be stored in the node
     * @param parent this node's parent
     * @return a node with parent
     */
    public static GameNode createNode(Board board, GameNode parent) {
        return new ImpGameNode(board, parent);
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
    public void computeChildren() {
        createChildListIfNotExists();
        if (this.children.size() != 0) {
            throw new IllegalStateException("node already has children");
        }
        Board[] followUpBoards = MoveGenerator.generatePossibleMoves(gameState);
        for (Board board : followUpBoards) {
            this.children.add(new ImpGameNode(board));
        }
    }
    
    @Override
    public GameNode findPlayableAncestor() {
        //TODO testing!
        if (this.parent == null) {
            throw new IllegalStateException("looking for ancestor of root");
        }
        return ((ImpGameNode) this.getParent()).findPlayableAncestor(this); //parent.findPlayableAncestor(this)
    }

    /**
     * @param lastNode the child from which this node was found
     * @return the node corresponding to the turn that has to be played for this node to be reachable
     */
    private ImpGameNode findPlayableAncestor(ImpGameNode lastNode) {
        // recursively traverse to root and return the last node before it
        
        //return previous node if this node is root
        if (this.parent == null) {
            return lastNode;
        }
        //repeat for parent node
        return ((ImpGameNode) lastNode.getParent()).findPlayableAncestor(this);
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
        this.parent.deleteChild(this);
    }


    @Override
    public boolean deleteChild(GameNode node) {
        // removing with ArrayList.remove(node) would require gamenode.equals()
        // which would probably have to compare boards which is inefficient

        // removing by equals also does not guarantee removal of the correct node if 2
        // equal nodes are present,
        // though in that case either all or none of the nodes should be removed
        boolean found = false;

        GameNode child = null;
        for (int i = 0; i < children.size(); i++) {
            child = children.get(i);
            if (child == node) {
                found = true;
                children.remove(i);
            }
        }
        return found;
    }

    @Override
    public void deleteChildExceptionIfNotFound(GameNode node) {
        boolean success = this.deleteChild(node);
        if (!success) {
            throw new IllegalArgumentException("child to remove could not be found");
        }
    }


    @Override
    public void deleteChildren() {
        this.children.clear();

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
    public void setValue(int value) {
        this.pointValue = value;

    }

    public int getValue() {
        return this.pointValue;
    }

    @Override
    public GameNode getParent() {
        return this.parent;
    }

}