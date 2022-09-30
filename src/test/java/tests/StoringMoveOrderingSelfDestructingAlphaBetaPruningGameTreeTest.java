package tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import gametree.GameNode;
import gametree.GameTree;
import gametree.ImpGameTree;
import gametree.Node;
import helper.GameTreeEvaluationHelper;
import minimax.GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning;
import model.Position;
import uciservice.FenParser;

public class StoringMoveOrderingSelfDestructingAlphaBetaPruningGameTreeTest extends GameTreeEvaluationTest {
    public StoringMoveOrderingSelfDestructingAlphaBetaPruningGameTreeTest() {
        super(new GameTreeEvaluationHelper(() -> new GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning(3)));
    }

    @Test
    public void verifyStorageDepth2WhiteTest() {
        GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning evaluator = new GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning(
                2);
        GameTree tree = new ImpGameTree(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"), evaluator);
        evaluator.evaluateTree(tree, 4, true);

        GameNode root = tree.getRoot();
        assertTrue(root.hasChildren());

        for (Node<Position> layer1Child : root.getChildren()) {
            assertTrue(layer1Child.hasChildren());
            for (Node<Position> layer2Child : layer1Child.getChildren()) {
                assertFalse(layer2Child.hasChildren());
            }
        }
    }

    @Test
    public void verifyStorageDepth2BlackTest() {
        GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning evaluator = new GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning(
                2);
        GameTree tree = new ImpGameTree(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"), evaluator);
        evaluator.evaluateTree(tree, 4, false);

        GameNode root = tree.getRoot();
        assertTrue(root.hasChildren());

        for (Node<Position> layer1Child : root.getChildren()) {
            assertTrue(layer1Child.hasChildren());
            for (Node<Position> layer2Child : layer1Child.getChildren()) {
                assertFalse(layer2Child.hasChildren());
            }
        }
    }

    @Test
    public void verifyStorageDepth1WhiteTest() {
        GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning evaluator = new GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning(
                1);
        GameTree tree = new ImpGameTree(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"), evaluator);
        evaluator.evaluateTree(tree, 4, true);

        GameNode root = tree.getRoot();
        assertTrue(root.hasChildren());

        for (Node<Position> layer1Child : root.getChildren()) {
            assertFalse(layer1Child.hasChildren());
        }
    }

    @Test
    public void verifyStorageDepth1BlackTest() {
        GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning evaluator = new GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning(
                1);
        GameTree tree = new ImpGameTree(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"), evaluator);
        evaluator.evaluateTree(tree, 4, false);

        GameNode root = tree.getRoot();
        assertTrue(root.hasChildren());

        for (Node<Position> layer1Child : root.getChildren()) {
            assertFalse(layer1Child.hasChildren());
        }
    }

    @Test
    public void verifyStorageDepth0WhiteTest() {
        GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning evaluator = new GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning(
                0);
        GameTree tree = new ImpGameTree(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"), evaluator);
        evaluator.evaluateTree(tree, 4, true);

        GameNode root = tree.getRoot();
        assertFalse(root.hasChildren());
    }

    @Test
    public void verifyStorageDepth0BlackTest() {
        GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning evaluator = new GameNodeStoringMoveOrderingSelfDestructingAlphaBetaPruning(
                0);
        GameTree tree = new ImpGameTree(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"), evaluator);
        evaluator.evaluateTree(tree, 4, false);

        GameNode root = tree.getRoot();
        assertFalse(root.hasChildren());
    }
}
