package gametree;

import model.Position;

public class ImpGameTree extends BaseTree<GameNode> implements GameTree {

    private GameTreeEvaluator evaluator;

    /**
     * Used to create the initial game tree from a position.
     * @param gameState the game state that the root node should represent
     */
    public ImpGameTree(Position position, GameTreeEvaluator evaluator) {
        super(GameNode.createRoot(position));
        this.evaluator = evaluator;
    }

    /**
     * Used to create a game tree from an existing game tree.
     * The tree is created as a subtree of the existing tree with the specified node as root.
     * The tree is then deepened further.
     * @param root the root node of the new tree
     */
    public ImpGameTree(GameNode root, GameTreeEvaluator evaluator) {
        super(root);
        this.evaluator = evaluator;
    }

    @Override
    // Ich würde den calculateBestMove evtl einen Namen geben wie kickOffCalculation oder startCalculatingBestMove
    // und dann irgendwo in ner privaten Methode im GameTree eine Hauptschleife für das Iterative Deepening haben
    public GameNode calculateBestMove(int maxTime) {
        int calculationDepth = 3; //TODO determine depth from maxTime instead of hardwiring it
        return this.evaluator.evaluateTree(this, calculationDepth, this.getRoot().getContent().getWhitesTurn());
    }
}
