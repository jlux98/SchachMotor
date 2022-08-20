package testclasses;

import gametree.Tree;

/**
   * Tree consisting of IntNodes.
   * Leaf values are the same as in https://en.wikipedia.org/wiki/File:Minimax.svg.
   * Inner nodes have a value of 0.
   * Tree structure is the same as in the image.
   */
public class TestTree implements Tree<IntNode> {

    /**
     * Constructs a tree matching https://en.wikipedia.org/wiki/File:Minimax.svg.
     */
    public TestTree() {
        setUpTree();
    }

    //layers and nodes are 0 based
    //root would be layer0Node0
    //children of root are layer1Node0 and layer1Node1
    //layer 4 = leaves
    public IntNode root;
    //layer 4
    public IntNode layer4Node0;
    public IntNode layer4Node1;
    public IntNode layer4Node2;
    public IntNode layer4Node3;
    public IntNode layer4Node4;
    public IntNode layer4Node5;
    public IntNode layer4Node6;
    public IntNode layer4Node7;
    public IntNode layer4Node8;
    //layer 3
    public IntNode layer3Node0;
    public IntNode layer3Node1;
    public IntNode layer3Node2;
    public IntNode layer3Node3;
    public IntNode layer3Node4;
    public IntNode layer3Node5;
    //layer 2
    public IntNode layer2Node0;
    public IntNode layer2Node1;
    public IntNode layer2Node2;
    public IntNode layer2Node3;
    //layer1
    public IntNode layer1Node0;
    public IntNode layer1Node1;

    //tree data source: https://en.wikipedia.org/wiki/File:Minimax.svg
    public void setUpTree() {
        layer4Node0 = new IntNode(10);
        layer4Node1 = new IntNode(Integer.MAX_VALUE);
        layer4Node2 = new IntNode(5);
        layer4Node3 = new IntNode(-10);
        layer4Node4 = new IntNode(7);
        layer4Node5 = new IntNode(5);
        layer4Node6 = new IntNode(Integer.MIN_VALUE);
        layer4Node7 = new IntNode(-7);
        layer4Node8 = new IntNode(-5);
        //layer 3
        layer3Node0 = TestHelper.createParent(layer4Node0, layer4Node1);
        layer3Node1 = TestHelper.createParent(layer4Node2);
        layer3Node2 = TestHelper.createParent(layer4Node3);
        layer3Node3 = TestHelper.createParent(layer4Node4, layer4Node5);
        layer3Node4 = TestHelper.createParent(layer4Node6);
        layer3Node5 = TestHelper.createParent(layer4Node7, layer4Node8);
        //layer 2
        layer2Node0 = TestHelper.createParent(layer3Node0, layer3Node1);
        layer2Node1 = TestHelper.createParent(layer3Node2);
        layer2Node2 = TestHelper.createParent(layer3Node3, layer3Node4);
        layer2Node3 = TestHelper.createParent(layer3Node5);
        //layer1
        layer1Node0 = TestHelper.createParent(layer2Node0, layer2Node1);
        layer1Node1 = TestHelper.createParent(layer2Node2, layer2Node3);
        //root
        root = TestHelper.createParent(layer1Node0, layer1Node1);

    }

    @Override
    public IntNode getRoot() {
        return this.root;
    }

}
