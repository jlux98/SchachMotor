package data;

import org.junit.jupiter.api.Test;

import classes.IntNode;
import gametree.ComputeChildrenException;
import gametree.Tree;
import helper.IntNodeHelper;
import helper.NodeHelper;

/**
 * A Tree < IntNode > build to represent <code>diagrams/AsymmetricTestTree.drawio.svg</code>.
 */
public class IntNodeAsymmetricTestTree implements Tree<IntNode> {

    @Override
    public IntNode getRoot() {
        return this.root;
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("test trees do not support deletion so value changes can be examined");
    }

    //layers and nodes are 0 based
    //root would be layer0Node0
    //children of root are layer1Node0 and layer1Node1...

    //layer 6
    public final IntNode layer6Node0;
    //layer 5
    public final IntNode layer5Node0;
    public final IntNode layer5Node1;
    public final IntNode layer5Node2;
    public final IntNode layer5Node3;
    public final IntNode layer5Node4;
    //layer 4
    public final IntNode layer4Node0;
    public final IntNode layer4Node1;
    public final IntNode layer4Node2;
    public final IntNode layer4Node3;
    public final IntNode layer4Node4;
    public final IntNode layer4Node5;
    public final IntNode layer4Node6;
    public final IntNode layer4Node7;
    public final IntNode layer4Node8;
    public final IntNode layer4Node9;
    public final IntNode layer4Node10;
    public final IntNode layer4Node11;
    public final IntNode layer4Node12;
    //layer 3
    public final IntNode layer3Node0;
    public final IntNode layer3Node1;
    public final IntNode layer3Node2;
    public final IntNode layer3Node3;
    public final IntNode layer3Node4;
    public final IntNode layer3Node5;
    public final IntNode layer3Node6;
    public final IntNode layer3Node7;
    public final IntNode layer3Node8;
    public final IntNode layer3Node9;
    public final IntNode layer3Node10;
    public final IntNode layer3Node11;
    public final IntNode layer3Node12;
    public final IntNode layer3Node13;
    public final IntNode layer3Node14;
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
    //layer1
    public final IntNode layer1Node0;
    public final IntNode layer1Node1;
    public final IntNode layer1Node2;
    public final IntNode layer1Node3;
    public final IntNode layer1Node4;
    //layer0
    public final IntNode root;

    /**
     * Constructs a tree matching <code>diagrams/AsymmetricTestTree.drawio.svg</code>.
     */
    public IntNodeAsymmetricTestTree() {
        //layer 6
        layer6Node0 = new IntNode(-9);
        //layer5
        layer5Node0 = new IntNode(21);
        layer5Node1 = IntNodeHelper.createParent(layer6Node0);
        layer5Node2 = new IntNode(14);
        layer5Node3 = new IntNode(-14);
        layer5Node4 = new IntNode(-20);
        //layer 4
        layer4Node0 = new IntNode(22);
        layer4Node1 = new IntNode(-3);
        layer4Node2 = IntNodeHelper.createParent(layer5Node0, layer5Node1);
        layer4Node3 = IntNodeHelper.createParent(layer5Node2);
        layer4Node4 = new IntNode(6);
        layer4Node5 = new IntNode(10);
        layer4Node6 = new IntNode(3);
        layer4Node7 = IntNodeHelper.createParent(layer5Node3);
        layer4Node8 = new IntNode(-11);
        layer4Node9 = IntNodeHelper.createParent(layer5Node4);
        layer4Node10 = new IntNode(2);
        layer4Node11 = new IntNode(12);
        layer4Node12 = new IntNode(9);
        //layer 3
        layer3Node0 = new IntNode(9);
        layer3Node1 = IntNodeHelper.createParent(layer4Node0);
        layer3Node2 = new IntNode(-5);
        layer3Node3 = IntNodeHelper.createParent(layer4Node1);
        layer3Node4 = new IntNode(14);
        layer3Node5 = IntNodeHelper.createParent(layer4Node2);
        layer3Node6 = new IntNode(-24);
        layer3Node7 = IntNodeHelper.createParent(layer4Node3, layer4Node4, layer4Node5);
        layer3Node8 = IntNodeHelper.createParent(layer4Node6);
        layer3Node9 = new IntNode(-8);
        layer3Node10 = IntNodeHelper.createParent(layer4Node7, layer4Node8);
        layer3Node11 = IntNodeHelper.createParent(layer4Node9);
        layer3Node12 = new IntNode(23);
        layer3Node13 = IntNodeHelper.createParent(layer4Node10, layer4Node11, layer4Node12);
        layer3Node14 = new IntNode(-15);
        //layer 2
        layer2Node0 = IntNodeHelper.createParent(layer3Node0, layer3Node1);
        layer2Node1 = new IntNode(8);
        layer2Node2 = IntNodeHelper.createParent(layer3Node2, layer3Node3, layer3Node4);
        layer2Node3 = IntNodeHelper.createParent(layer3Node5, layer3Node6);
        layer2Node4 = IntNodeHelper.createParent(layer3Node7, layer3Node8);
        layer2Node5 = IntNodeHelper.createParent(layer3Node9);
        layer2Node6 = IntNodeHelper.createParent(layer3Node10, layer3Node11, layer3Node12, layer3Node13);
        layer2Node7 = IntNodeHelper.createParent(layer3Node14);
        layer2Node8 = new IntNode(-22);
        //layer1
        layer1Node0 = IntNodeHelper.createParent(layer2Node0, layer2Node1, layer2Node2);
        layer1Node1 = IntNodeHelper.createParent(layer2Node3, layer2Node4, layer2Node5);
        layer1Node2 = new IntNode(-10);
        layer1Node3 = IntNodeHelper.createParent(layer2Node6);
        layer1Node4 = IntNodeHelper.createParent(layer2Node7, layer2Node8);
        //layer0
        root = IntNodeHelper.createParent(layer1Node0, layer1Node1, layer1Node2, layer1Node3, layer1Node4);
    }

    /**
    * verifies that the tree starting with this.root was successfully build to represent
    * <code>diagrams/AsymmetricTestTree.drawio.svg</code>
    */
    @Test
    public void verifyTestTree() throws ComputeChildrenException {
        //layer 6
        IntNodeHelper.compareStaticIntNodeValue(-9, layer6Node0);
        //layer 5
        IntNodeHelper.compareStaticIntNodeValue(21, layer5Node0);
        NodeHelper.verifyChildren(layer5Node1, layer6Node0);
        IntNodeHelper.compareStaticIntNodeValue(14, layer5Node2);
        IntNodeHelper.compareStaticIntNodeValue(-14, layer5Node3);
        IntNodeHelper.compareStaticIntNodeValue(-20, layer5Node4);
        //layer 4
        IntNodeHelper.compareStaticIntNodeValue(22, layer4Node0);
        IntNodeHelper.compareStaticIntNodeValue(-3, layer4Node1);
        NodeHelper.verifyChildren(layer4Node2, layer5Node0, layer5Node1);
        NodeHelper.verifyChildren(layer4Node3, layer5Node2);
        IntNodeHelper.compareStaticIntNodeValue(6, layer4Node4);
        IntNodeHelper.compareStaticIntNodeValue(10, layer4Node5);
        IntNodeHelper.compareStaticIntNodeValue(3, layer4Node6);
        NodeHelper.verifyChildren(layer4Node7, layer5Node3);
        IntNodeHelper.compareStaticIntNodeValue(-11, layer4Node8);
        NodeHelper.verifyChildren(layer4Node9, layer5Node4);
        IntNodeHelper.compareStaticIntNodeValue(2, layer4Node10);
        IntNodeHelper.compareStaticIntNodeValue(12, layer4Node11);
        IntNodeHelper.compareStaticIntNodeValue(9, layer4Node12);
        //layer 3
        IntNodeHelper.compareStaticIntNodeValue(9, layer3Node0);
        NodeHelper.verifyChildren(layer3Node1, layer4Node0);
        IntNodeHelper.compareStaticIntNodeValue(-5, layer3Node2);
        NodeHelper.verifyChildren(layer3Node3, layer4Node1);
        IntNodeHelper.compareStaticIntNodeValue(14, layer3Node4);
        NodeHelper.verifyChildren(layer3Node5, layer4Node2);
        IntNodeHelper.compareStaticIntNodeValue(-24, layer3Node6);
        NodeHelper.verifyChildren(layer3Node7, layer4Node3, layer4Node4, layer4Node5);
        NodeHelper.verifyChildren(layer3Node8, layer4Node6);
        IntNodeHelper.compareStaticIntNodeValue(-8, layer3Node9);
        NodeHelper.verifyChildren(layer3Node10, layer4Node7, layer4Node8);
        NodeHelper.verifyChildren(layer3Node11, layer4Node9);
        IntNodeHelper.compareStaticIntNodeValue(23, layer3Node12);
        NodeHelper.verifyChildren(layer3Node13, layer4Node10, layer4Node11, layer4Node12);
        IntNodeHelper.compareStaticIntNodeValue(-15, layer3Node14);
        //layer 2
        NodeHelper.verifyChildren(layer2Node0, layer3Node0, layer3Node1);
        IntNodeHelper.compareStaticIntNodeValue(8, layer2Node1);
        NodeHelper.verifyChildren(layer2Node2, layer3Node2, layer3Node3, layer3Node4);
        NodeHelper.verifyChildren(layer2Node3, layer3Node5, layer3Node6);
        NodeHelper.verifyChildren(layer2Node4, layer3Node7, layer3Node8);
        NodeHelper.verifyChildren(layer2Node5, layer3Node9);
        NodeHelper.verifyChildren(layer2Node6, layer3Node10, layer3Node11, layer3Node12, layer3Node13);
        NodeHelper.verifyChildren(layer2Node7, layer3Node14);
        IntNodeHelper.compareStaticIntNodeValue(-22, layer2Node8);
        //layer1
        NodeHelper.verifyChildren(layer1Node0, layer2Node0, layer2Node1, layer2Node2);
        NodeHelper.verifyChildren(layer1Node1, layer2Node3, layer2Node4, layer2Node5);
        IntNodeHelper.compareStaticIntNodeValue(-10, layer1Node2);
        NodeHelper.verifyChildren(layer1Node3, layer2Node6);
        NodeHelper.verifyChildren(layer1Node4, layer2Node7, layer2Node8);
        //root
        NodeHelper.verifyChildren(root, layer1Node0, layer1Node1, layer1Node2, layer1Node3, layer1Node4);
    }

}
