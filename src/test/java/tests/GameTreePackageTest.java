package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gametree.GameNode;
import gametree.GameNodeAlphaBetaPruning;
import gametree.ImpGameTree;
import gametree.TreeEvaluator;
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

    /**
     * Calculates the next move for the given fen twice using
     * {@link TreeEvaluator#evaluateTree(gametree.Tree, int, boolean)}
     * and compares both results.
     * 
     * @param fen        the position to calculate the move to be plaed for
     * @param depth      the depth used by alpha-beta pruning
     * @param whitesTurn whether the move to be played is played by white
     */
    private void testEvaluatorEvaluateTreeConsistency(String fen, int depth, boolean whitesTurn) {
        Position pos1 = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruning1 = new GameNodeAlphaBetaPruning();
        Move result1 = pruning1.evaluateTree(new ImpGameTree(pos1, pruning1), depth, whitesTurn).getContent().getMove();

        Position pos2 = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruning2 = new GameNodeAlphaBetaPruning();
        Move result2 = pruning2.evaluateTree(new ImpGameTree(pos2, pruning2), depth, whitesTurn).getContent().getMove();

        // assertEquals(result1.getContent().getMove(), result2.getContent().getMove());
        assertEquals(result1, result2);
        System.out.println(result1 + " " + result2);
    }

    /**
     * Calculates the next move for the given fen twice using
     * {@link ImpGameTree#calculateBestMove(int)}
     * and compares both results.
     * 
     * @param fen        the position to calculate the move to be plaed for
     * @param depth      the depth used by alpha-beta pruning
     * @param whitesTurn whether the move to be played is played by white
     */
    private void testTreeCalculateBestMoveConsistency(String fen, int depth, boolean whitesTurn) {
        Position pos1 = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruning1 = new GameNodeAlphaBetaPruning();
        Move result1 = new ImpGameTree(pos1, pruning1).calculateBestMove(depth).getContent().getMove();
        // FIXME storing result1 stores a big part of the tree = a lot of memory,
        // does behavior change if only the move is stored?
        Position pos2 = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruning2 = new GameNodeAlphaBetaPruning();
        Move result2 = new ImpGameTree(pos2, pruning2).calculateBestMove(depth).getContent().getMove();

        assertEquals(result1, result2);
        System.out.println(result1 + " " + result2);
    }

    /**
     * Calculates the next move for the given fen using
     * {@link ImpGameTree#calculateBestMove(int)}
     * and {@link TreeEvaluator#evaluateTree(gametree.Tree, int, boolean)}
     * and comapares both results.
     * 
     * @param fen        the position to calculate the move to be plaed for
     * @param depth      the depth used by alpha-beta pruning
     * @param whitesTurn whether the move to be played is played by white
     */
    private void testEvaluatorEvaluateTreeEqualsTreeCalculateBestMove(String fen, int depth, boolean whitesTurn) {

        Position pos1 = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruning1 = new GameNodeAlphaBetaPruning();
        Move result1 = pruning1.evaluateTree(new ImpGameTree(pos1, pruning1), depth, whitesTurn).getContent().getMove();

        Position pos2 = FenParser.parseFen(fen);
        GameNodeAlphaBetaPruning pruning2 = new GameNodeAlphaBetaPruning();
        Move result2 = new ImpGameTree(pos2, pruning2).calculateBestMove(depth).getContent().getMove();

        assertEquals(result1, result2);
        System.out.println(result1 + " " + result2);
    }

    @Test
    public void evaluatorEvaluateTreeConsistentTest1() {
        String fen = "k7/3r4/1p1b4/4n3/1R6/8/2N1P3/K7 b - - 0 1";
        testEvaluatorEvaluateTreeConsistency(fen, 5, false);
    }

    @Test
    public void evaluatorEvaluateTreeConsistentTest2() {
        String fen = "2K5/8/8/8/8/3RRR2/n2RRR2/2k5 w - - 0 1";
        testEvaluatorEvaluateTreeConsistency(fen, 5, true);
    }

    @Test
    public void treeCalculateBestMoveConsistentTest1() {
        String fen = "1r1b4/6k1/5n2/6p1/3N4/4B3/3R4/K7 w - - 0 1";
        testTreeCalculateBestMoveConsistency(fen, 5, true);
    }

    @Test
    public void treeCalculateBestMoveConsistentTest2() {
        String fen = "k7/6r1/8/3n1P2/8/1P3N1r/3NB3/K7 b - - 0 1";
        testTreeCalculateBestMoveConsistency(fen, 5, false);
    }

    @Test
    public void evaluatorEvaluateTreeEqualsTreeCalculateBestMoveTest1() {
        String fen = "2K5/8/8/8/8/3RRR2/n2RRR2/2k5 w - - 0 1";
        testEvaluatorEvaluateTreeEqualsTreeCalculateBestMove(fen, 5, true);
    }

    @Test
    public void evaluatorEvaluateTreeEqualsTreeCalculateBestMoveTest3() {
        String fen = "k7/2n5/3b1b2/3p4/8/2N3P1/4R3/K7 b - - 0 1";
        testEvaluatorEvaluateTreeEqualsTreeCalculateBestMove(fen, 5, false);
    }

    /**
     * apparently never fails
     */
    @Test
    public void evaluatorEvaluateTreeEqualsTreeCalculateBestMoveTestApparentlyWorkingFen() {
        String fen = "k7/8/6p1/3rR3/2n5/1P2Bb2/8/K7 b - - 0 1";
        testEvaluatorEvaluateTreeEqualsTreeCalculateBestMove(fen, 5, false);
    }

    @Test
    public void evaluatorEvaluateTreeConsistencyTestApparentlyWorkingFen() {
        String fen = "k7/8/6p1/3rR3/2n5/1P2Bb2/8/K7 b - - 0 1";
        testEvaluatorEvaluateTreeConsistency(fen, 5, false);
    }

    @Test
    public void treeCalculateBestMoveConsistencyTestApparentlyWorkingFen() {
        String fen = "k7/8/6p1/3rR3/2n5/1P2Bb2/8/K7 b - - 0 1";
        testTreeCalculateBestMoveConsistency(fen, 5, false);
    }

    @Test
    public void chaosTest() {
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
