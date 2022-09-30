package minimax;

import gametree.GameTree;
import model.Position;

public class IterativeDeepeningRunner implements Runnable{

    GameTree tree;
    TreeEvaluator<Position> evaluator;
    boolean whitesTurn;
    int secondsToCompute;
    int depth;


    public IterativeDeepeningRunner(GameTree tree, TreeEvaluator<Position> evaluator, boolean whitesTurn, int secondsToCompute, int depth) {
        this.tree = tree;
        this.evaluator = evaluator;
        this.whitesTurn = whitesTurn;
        this.secondsToCompute = secondsToCompute;
        this.depth = depth;
    }


    @Override
    public void run() {
        new IterativeDeepening<Position>().evaluateTree(tree, evaluator, whitesTurn, -1, depth);
    }
    
}
