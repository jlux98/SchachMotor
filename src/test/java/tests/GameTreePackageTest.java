package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gametree.GameNode;
import gametree.GameNodeAlphaBetaPruning;
import gametree.ImpGameTree;
import model.Position;
import uciservice.FenParser;

/**
 * tests for game tree generation and evaluation using
 * GameTree
 * GameNode
 * GenericAlphaBetaPruning
 */
public class GameTreePackageTest {
    
    @Test
    public void evaluatorEvaluateTreeEqualsTreeCalculateBestMovetest() {
        Position pos = FenParser.parseFen("2K5/8/8/8/8/3RRR2/n2RRR2/2k5 w - - 0 1");
        GameNodeAlphaBetaPruning pruningA = new GameNodeAlphaBetaPruning();
        GameNode faulty = pruningA.evaluateTree(new ImpGameTree(pos, pruningA), 5, true);

        pos = FenParser.parseFen("2K5/8/8/8/8/3RRR2/n2RRR2/2k5 w - - 0 1");
        GameNodeAlphaBetaPruning pruningB = new GameNodeAlphaBetaPruning();
        GameNode sensible = new ImpGameTree(pos, pruningB).calculateBestMove(5);
        // calls pruningB.evaluateTree(Tree, depth,
        // this.getRoot().getContent().getWhitesTurn());
        // calls pruningB.evaluateTree(Tree, depth, true);
        System.out.println(faulty.getContent().getMove());
        System.out.println(sensible.getContent().getMove());
        System.out.println(); // for breakpoint purposes
        assertEquals(sensible.getContent().getMove(), faulty.getContent().getMove());
    }
}
