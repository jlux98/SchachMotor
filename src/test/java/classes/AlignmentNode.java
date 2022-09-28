package classes;

import helper.TreePrinter;

/**
 * Nodes used as placeholder for "missing" nodes in incomplete trees by {@link TreePrinter}.
 */
public class AlignmentNode extends IntNode {
    private static String TO_STRING = "x";

    /**
     * Creates a new AlignmentNode.
     * The super instance of IntNode stores a content of 0.
     */
    public AlignmentNode() {
        super(0);
    }

    /**
     * Updates this node's alignment by copying the width of its parent's alignment.
     * @return this node's aligned representation (see {@link #getAlignedRepresentation()})
     */
    public StringBuilder updateAlignment() {
        int parentWidth = 0;
        if (getParent() instanceof AlignmentNode) {
            //if parent is an AlignMentNode, update its Alignment and copy it
            parentWidth = (((AlignmentNode) getParent()).updateAlignment()).length();
        } else {
            //if parent is a node other than AlignMentNode, copy the width of its alignment
            parentWidth = getParent().getAlignedRepresentation().length();
        }
        this.setAlignedRepresentation(TreePrinter.alignGuaranteeWhiteSpace(toString(), parentWidth));
        return getAlignedRepresentation();
    }

    /**
     * Hardcoded to always return the same placeholder string,
     * so AlignmentNodes can be identified as originally missing nodes in TreePrinter output.
     */
    @Override
    public String toString() {
        return TO_STRING;
    }
}
