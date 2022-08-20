package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gametree.GenericAlphaBetaPruning;
import gametree.TreeEvaluator;
import testclasses.EvaluableInteger;
import testclasses.IntNode;
import testclasses.TestHelper;
import testclasses.TestTree;

public class AlphaBetaTest {
    private TestTree testTree;

    @BeforeEach
    public void setUpTree() {
        this.testTree = new TestTree();
    }


    @Test
    public void alphaBetaPruningTest() {
        TreeEvaluator<EvaluableInteger> alphaBeta = new GenericAlphaBetaPruning<EvaluableInteger>();
        alphaBeta.evaluateTree(testTree, 4, true);
        //root
        TestHelper.compareIntNodeValue(-7, testTree.root);
        //layer1
        TestHelper.compareIntNodeValue(-10, testTree.layer1Node0);
        TestHelper.compareIntNodeValue(-7, testTree.layer1Node1);
        //layer2
        TestHelper.compareIntNodeValue(10, testTree.layer2Node0);
        TestHelper.compareIntNodeValue(-10, testTree.layer2Node1);
        TestHelper.compareIntNodeValue(5, testTree.layer2Node2);
        TestHelper.compareIntNodeValue(-7, testTree.layer2Node3);
        //layer3
        TestHelper.compareIntNodeValue(10, testTree.layer3Node0);
        TestHelper.compareIntNodeValue(5, testTree.layer3Node1);
        TestHelper.compareIntNodeValue(-10, testTree.layer3Node2);
        TestHelper.compareIntNodeValue(5, testTree.layer3Node3);
        TestHelper.compareIntNodeValue(Integer.MIN_VALUE, testTree.layer3Node4);
        TestHelper.compareIntNodeValue(-7, testTree.layer3Node5);
        //assure that leaf layer was not changed
        TestHelper.compareIntNodeValue(10, testTree.layer4Node0);
        TestHelper.compareIntNodeValue(Integer.MAX_VALUE, testTree.layer4Node1);
        TestHelper.compareIntNodeValue(5, testTree.layer4Node2);
        TestHelper.compareIntNodeValue(-10, testTree.layer4Node3);
        TestHelper.compareIntNodeValue(7, testTree.layer4Node4);
        TestHelper.compareIntNodeValue(5, testTree.layer4Node5);
        TestHelper.compareIntNodeValue(Integer.MIN_VALUE, testTree.layer4Node6);
        TestHelper.compareIntNodeValue(-7, testTree.layer4Node7);
        TestHelper.compareIntNodeValue(-5, testTree.layer4Node8);
    }
}
