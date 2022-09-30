package gametree;

/**
 * Generic class providing a basic tree implementation.
 */
public class ImpTree<NodeType extends Node<?>> implements Tree<NodeType> {

    private NodeType root;

    /**
     * Creates a tree with the specified node as root.
     * @param root
     */
    public ImpTree(NodeType root) {
        this.root = root;
    }

    @Override
    public NodeType getRoot() {
        return this.root;
    }

    @Override
    public void delete() {
        root.deleteChildren();
        root = null;
    }

}
