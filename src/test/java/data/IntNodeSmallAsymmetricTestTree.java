package data;

import classes.IntNode;
import gametree.ComputeChildrenException;
import gametree.Tree;
import helper.IntNodeHelper;

/**
 * A Tree < IntNode > build to represent <code>diagrams/SmallAsymmetricTestTree.drawio.svg</code>.
 */
public class IntNodeSmallAsymmetricTestTree implements Tree<IntNode> {

    @Override
    public IntNode getRoot() {
        return this.root;
    }

    //layers and nodes are 0 based
    //root would be layer0Node0
    //children of root are layer1Node0 and layer1Node1...

    //layer 3
    public final IntNode layer3Node0;
    public final IntNode layer3Node1;
    public final IntNode layer3Node2;
    public final IntNode layer3Node3;
    public final IntNode layer3Node4;
    public final IntNode layer3Node5;
    public final IntNode layer3Node6;
    public final IntNode layer3Node7;
    //layer 2
    public final IntNode layer2Node0;
    public final IntNode layer2Node1;
    public final IntNode layer2Node2;
    public final IntNode layer2Node3;
    public final IntNode layer2Node4;
    public final IntNode layer2Node5;
    public final IntNode layer2Node6;
    public final IntNode layer2Node7;
    public final IntNode layer2Node8;
    public final IntNode layer2Node9;
    public final IntNode layer2Node10;
    public final IntNode layer2Node11;
    public final IntNode layer2Node12;
    //layer1
    public final IntNode layer1Node0;
    public final IntNode layer1Node1;
    public final IntNode layer1Node2;
    public final IntNode layer1Node3;
    public final IntNode layer1Node4;
    //layer0
    public final IntNode root;

    /**
     * Constructs a tree matching <code>diagrams/SmallAsymmetricTestTree.drawio.svg</code>.
     */
    public IntNodeSmallAsymmetricTestTree() {
        //layer 3
        layer3Node0 = new IntNode(-6);
        layer3Node1 = new IntNode(6);
        layer3Node2 = new IntNode(0);
        layer3Node3 = new IntNode(-8);
        layer3Node4 = new IntNode(2);
        layer3Node5 = new IntNode(-9);
        layer3Node6 = new IntNode(-6);
        layer3Node7 = new IntNode(9);
        //layer 2
        layer2Node0 = new IntNode(-2);
        layer2Node1 = new IntNode(3);
        layer2Node2 = IntNodeHelper.createParent(layer3Node0);
        layer2Node3 = new IntNode(5);
        layer2Node4 = IntNodeHelper.createParent(layer3Node1);
        layer2Node5 = IntNodeHelper.createParent(layer3Node2, layer3Node3);
        layer2Node6 = new IntNode(6);
        layer2Node7 = new IntNode(3);
        layer2Node8 = new IntNode(4);
        layer2Node9 = IntNodeHelper.createParent(layer3Node4, layer3Node5);
        layer2Node10 = IntNodeHelper.createParent(layer3Node6, layer3Node7);
        layer2Node11 = new IntNode(-7);
        layer2Node12 = new IntNode(7);
        //layer1
        layer1Node0 = IntNodeHelper.createParent(layer2Node0, layer2Node1, layer2Node2);
        layer1Node1 = IntNodeHelper.createParent(layer2Node3, layer2Node4, layer2Node5);
        layer1Node2 = IntNodeHelper.createParent(layer2Node6, layer2Node7, layer2Node8);
        layer1Node3 = IntNodeHelper.createParent(layer2Node9, layer2Node10);
        layer1Node4 = IntNodeHelper.createParent(layer2Node11, layer2Node12);
        //layer0
        root = IntNodeHelper.createParent(layer1Node0, layer1Node1, layer1Node2, layer1Node3, layer1Node4);
    }

   public void printTree() throws ComputeChildrenException {
    //TreePrinter.alignAndPrintTree(this, 3);
   }

}
