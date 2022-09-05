package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gametree.GameNode;
import gametree.GameNodeAlphaBetaPruning;
import gametree.ImpGameTree;
import model.Move;
import model.Position;
import uciservice.FenParser;

/**
 * tests for game tree generation and evaluation using
 * GameTree
 * GameNode
 * GenericAlphaBetaPruning
 */
public class GameTreePackageTest {
public static void main(String[] args) {
    
}
    private void evaluatorEvaluateTreeConsistencyTest(String fen, int depth, boolean whitesTurn) {
        Position pos1 = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruning1 = new GameNodeAlphaBetaPruning();
        Move result1 = pruning1.evaluateTree(new ImpGameTree(pos1, pruning1), depth, whitesTurn).getContent().getMove();

        Position pos2 = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruning2 = new GameNodeAlphaBetaPruning();
        Move result2 = pruning2.evaluateTree(new ImpGameTree(pos2, pruning2), depth, whitesTurn).getContent().getMove();

        // assertEquals(result1.getContent().getMove(), result2.getContent().getMove());
        assertEquals(result1, result2);
    }

    private void treeCalculateBestMoveConsistencyTest(String fen, int depth, boolean whitesTurn) {
        Position pos1 = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruning1 = new GameNodeAlphaBetaPruning();
        GameNode result1 = new ImpGameTree(pos1, pruning1).calculateBestMove(depth);
        // FIXME storing result1 stores a big part of the tree = a lot of memory,
        // does behavior change if only the move is stored?
        Position pos2 = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruning2 = new GameNodeAlphaBetaPruning();
        GameNode result2 = new ImpGameTree(pos2, pruning2).calculateBestMove(depth);

        assertEquals(result1.getContent().getMove(), result2.getContent().getMove());
    }

    
    @Test
    public void evaluatorEvaluateTreeConsistentTest1() {
        String fen = "k7/3r4/1p1b4/4n3/1R6/8/2N1P3/K7 b - - 0 1";
        evaluatorEvaluateTreeConsistencyTest(fen, 5, false);
    }

    @Test
    public void evaluatorEvaluateTreeConsistentTest2() {
        String fen = "2K5/8/8/8/8/3RRR2/n2RRR2/2k5 w - - 0 1";
        evaluatorEvaluateTreeConsistencyTest(fen, 5, true);
    }


    @Test
    public void treeCalculateBestMoveConsistentTest1() {
        String fen = "1r1b4/6k1/5n2/6p1/3N4/4B3/3R4/K7 w - - 0 1";
        treeCalculateBestMoveConsistencyTest(fen, 5, true);
    }

    @Test
    public void treeCalculateBestMoveConsistentTest2() {
        String fen = "k7/6r1/8/3n1P2/8/1P3N1r/3NB3/K7 b - - 0 1";
        treeCalculateBestMoveConsistencyTest(fen, 5, false);
    }

    @Test
    public void evaluatorEvaluateTreeEqualsTreeCalculateBestMovetest() {
        String fen = "2K5/8/8/8/8/3RRR2/n2RRR2/2k5 w - - 0 1";
        int depth = 5;
        Position pos = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruningA = new GameNodeAlphaBetaPruning();
        GameNode faulty = pruningA.evaluateTree(new ImpGameTree(pos, pruningA), depth, true);

        pos = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruningB = new GameNodeAlphaBetaPruning();
        GameNode sensible = new ImpGameTree(pos, pruningB).calculateBestMove(depth);

        pos = FenParser.parseFen(fen);
        GameNode sensible2 = new ImpGameTree(pos, pruningB).calculateBestMove(depth);

        pos = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruningC = new GameNodeAlphaBetaPruning();
        GameNode sensible3 = new ImpGameTree(pos, pruningC).calculateBestMove(depth);

        // calls pruningB.evaluateTree(Tree, depth,
        // this.getRoot().getContent().getWhitesTurn());
        // calls pruningB.evaluateTree(Tree, depth, true);
        System.out.println(faulty.getContent().getMove());
        System.out.println(sensible.getContent().getMove());
        System.out.println(); // for breakpoint purposes
        assertEquals(sensible.getContent().getMove(), sensible3.getContent().getMove());
        assertEquals(sensible.getContent().getMove(), sensible2.getContent().getMove());
        assertEquals(sensible.getContent().getMove(), faulty.getContent().getMove());
    }
}
