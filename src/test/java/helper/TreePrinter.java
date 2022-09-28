package helper;

import java.util.List;

import classes.AlignmentNode;
import classes.IntNode;
import data.IntNodeAsymmetricTestTree;
import data.IntNodeWikipediaTestTree;
import gametree.ComputeChildrenException;
import gametree.Node;
import gametree.Tree;

public class TreePrinter {
    private static char OFFSET_CHARACTER = ' ';
    private static char CHILDREN_START_CHARACTER = '[';
    private static char CHILDREN_END_CHARACTER = ']';

    /**
    * Aligns a tree and prints it to standardout.
    * @param tree tree that should be printed
    * @param depth the tree's depth
    */
    public static void alignAndPrintTree(Tree<IntNode> tree, int depth) throws ComputeChildrenException {
        alignNode(tree.getRoot(), 0, depth);
        updateAlignments(tree.getRoot(), 0, depth);
        printTree(tree, depth);
    }

    
    public static void main(String[] args) throws ComputeChildrenException {
        /* Tree<IntNode> tree = IntNodeHelper.createIntNodeTree(2, 1, 2, 3, 4, 5, 6, 7, 8);
        alignAndPrintTree(tree, 3); */
        /* Tree<IntNode> tree = IntNodeHelper.createIntNodeTree(3, 1, 2, 3, 4, 5, 6, 7, 8);
        alignAndPrintTree(tree, 2); */
        /* IntNodeWikipediaTestTree tree = new IntNodeWikipediaTestTree();
        alignAndPrintTree(tree, 4); */
        IntNodeAsymmetricTestTree tree = new IntNodeAsymmetricTestTree();
        alignAndPrintTree(tree, 6);
    }

    /**
     * Encloses a string with OFFSET_CHARACTERs until its width is >= <code>width</code>.
     * Guarantess that the string is enclosed by at least one layer of OFFSET_CHARACTERs
     * (even if the string is already surpassing the specified width).
     * @param string the string that should be enclosed
     * @param width lower bound for final width
     * @return a StringBuilder containing the enclosed string
     */
    public static StringBuilder alignGuaranteeWhiteSpace(String string, int width) {
        StringBuilder builder = new StringBuilder(string);
        if (builder.charAt(0) != OFFSET_CHARACTER) {
            builder.insert(0, OFFSET_CHARACTER);
        }
        if (builder.charAt(builder.length() - 1) != OFFSET_CHARACTER) {
            builder.insert(builder.length(), OFFSET_CHARACTER);

        }
        return align(builder.toString(), width);
    }

    /**
     * Encloses a string  with OFFSET_CHARACTERs until its width is = <code>width</code>.
     * Does not make changes to the string if its length is already equal to or greater than
     * <code>width</code>.
     * @param string the string that should be enclosed
     * @param width width of the resulting string
     * @return a StringBuilder containing the enclosed string
     */
    private static StringBuilder align(String string, int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("width has to be greater than 0");
        }
        if (width <= string.length()) {
            return new StringBuilder(string);
        }
        int whiteSpaceWidth = width - string.length();
        int offset = whiteSpaceWidth / 2;
        StringBuilder aligned = new StringBuilder("");
        for (int i = 0; i < width; i++) {
            if (i == offset) {
                aligned.append(string);
                i += string.length();
            }
            aligned.append(OFFSET_CHARACTER);
        }
        return aligned;
    }

    /**
     * Injects the specified character to the left of the StringBuilder's content.
     * The content must be enclosed in OFFSET_CHARACTERs.
     * The character replaces an occurance of OFFSET_CHARACTER in the StringBuilder.
     * @param builder StringBuilder into which should be injected
     * @param character character that should replace an enclosing OFFSET_CHARACTER
     * @return
     */
    private static StringBuilder injectLeft(StringBuilder builder, char character) {
        if (builder.charAt(0) != OFFSET_CHARACTER) {
            throw new IllegalArgumentException("first char must be offset");
        }
        for (int i = 1; i < builder.length(); i++) {
            if (builder.charAt(i) != OFFSET_CHARACTER) {
                builder.setCharAt(i - 1, character);
                return builder;
            }
        }
        return builder;
    }

    /**
     * Injects the specified character to the right of the StringBuilder's content.
     * The content must be enclosed in OFFSET_CHARACTERs.
     * The character replaces an occurance of OFFSET_CHARACTER in the StringBuilder.
     * @param builder StringBuilder into which should be injected
     * @param character character that should replace an enclosing OFFSET_CHARACTER
     * @return
     */
    private static StringBuilder injectRight(StringBuilder builder, char character) {
        if (builder.charAt(builder.length() - 1) != OFFSET_CHARACTER) {
            throw new IllegalArgumentException("last char must be offset");
        }
        for (int i = builder.length() - 1; i >= 0; i--) {
            if (builder.charAt(i) != OFFSET_CHARACTER) {
                builder.setCharAt(i + 1, character);
                return builder;
            }
        }
        return builder;
    }

    /**
     * Updates the alignment of all AlignmentNodes in the tree by calling {@link AlignmentNode#updateAlignment()}.
     * @param node the root of the tree
     * @param currentDepth the depth of the current invocation. set this to 0 for any initial call
     * @param treeMaxDepth depth of the tree
     * @throws ComputeChildrenException
     */
    private static void updateAlignments(IntNode node, int currentDepth, int treeMaxDepth) throws ComputeChildrenException {
        if (currentDepth == treeMaxDepth) {
            //update alignments of nodes in lowest layer
            if (node instanceof AlignmentNode) {
                ((AlignmentNode) node).updateAlignment();
            }
            return;
        }

        List<? extends Node<Integer>> children = node.queryChildren();
        for (Node<Integer> child : children) {
            updateAlignments((IntNode) child, currentDepth + 1, treeMaxDepth);
        }
    }

    /**
     * Aligns a node and its children within a tree so it can be printed to standard out.
     * @param node the node that should be aligned to its children
     * @param currentDepth the current invocation's depth
     * @param treeMaxDepth the tree's depth
     * @return the node's width
     */
    private static int alignNode(IntNode node, int currentDepth, int treeMaxDepth) throws ComputeChildrenException {
        if (!node.hasChildren()) {
            //align representation
            String string = node.toString();
            node.setAlignedRepresentation(alignGuaranteeWhiteSpace(string, string.length()));

            if (currentDepth < treeMaxDepth) {
                //insert AlignmentNodes into childless nodes to keep the space below them "empty"
                node.insertChild(new AlignmentNode());
            } else {
                //return width of leaf nodes
                return node.getAlignedRepresentation().length();
            }
        }

        int childrenWidth = 0;
        List<? extends Node<Integer>> children = node.queryChildren();
        IntNode child;
        for (int i = 0; i < children.size(); i++) {
            child = (IntNode) children.get(i);
            childrenWidth += alignNode((IntNode) child, currentDepth + 1, treeMaxDepth);
        }

        IntNode firstChild = ((IntNode) children.get(0));
        injectLeft(firstChild.getAlignedRepresentation(), CHILDREN_START_CHARACTER);

        IntNode lastChild = ((IntNode) children.get(children.size() - 1));
        injectRight(lastChild.getAlignedRepresentation(), CHILDREN_END_CHARACTER);

        node.setAlignedRepresentation(alignGuaranteeWhiteSpace(node.toString(), childrenWidth));
        return node.getAlignedRepresentation().length();
    }

    /**
     * Prints a tree of IntNodes to standard out using {@link IntNode#getAlignedRepresentation()}.
     * @param tree the tree that should be printed
     * @param treeDepth the depth of the deepest node / the depth of the tree
     */
    private static void printTree(Tree<IntNode> tree, int treeDepth) throws ComputeChildrenException {
        //print each layer
        for (int i = 0; i < treeDepth + 1; i++) {
            printNodes(tree.getRoot(), i, 0, treeDepth);
            System.out.print("\n\n"); //newline
        }
    }

    /**
     * Prints all nodes in layer <code>layerDepth</code>.
     * @param node Node that is printed if it is located in layerDepth. 
     * If it is not, its children are recursively considered for printing.
     * @param layerDepth the layer that should be printed
     * @param currentDepth the current invocation's depth
     * @throws ComputeChildrenException
     */
    private static void printNodes(IntNode node, int layerDepth, int currentDepth, int treeMaxDepth)
            throws ComputeChildrenException {
        if (currentDepth == layerDepth) {
            System.out.print(node.getAlignedRepresentation());
            return;
        }
        List<? extends Node<Integer>> children = node.queryChildren();
        for (Node<Integer> child : children) {
            printNodes((IntNode) child, layerDepth, currentDepth + 1, treeMaxDepth);
        }
    }
}
