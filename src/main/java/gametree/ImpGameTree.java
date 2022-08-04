package gametree;

import java.util.List;

import model.Position;

public class ImpGameTree implements GameTree {
    /**
     * The current depth of this tree.
     * If tree consists of a root only, it's depth is 0.
     */
    private int depth = -1;
    private GameNode root;
    private List<GameNode> leafList; //TODO will this actually be used?
    private TreeEvaluator<GameNode> evaluator;

    /**
     * Used to create the initial game tree from a position.
     * @param gameState the game state that the root node should represent
     */
    public ImpGameTree(Position position, TreeEvaluator<GameNode> evaluator) {
        this.root = ImpGameNode.createRoot(position);
        this.evaluator = evaluator;
    }


    /**
     * Used to create a game tree from an existing game tree.
     * The tree is created as a subtree of the existing tree with the specified node as root.
     * The tree is then deepened further.
     * @param root the root node of the new tree
     */
    public ImpGameTree(GameNode root, TreeEvaluator<GameNode> evaluator) {
        this.root = root;
        this.evaluator = evaluator;
    }

    
    @Override
    public GameNode getRoot() {
        //does not need to be null checked since no game tree can be constructed without setting the root node
        return this.root;
    }  


    @Override
    public List<GameNode> getLeafList() {
        if (this.leafList == null || this.leafList.size() == 0) {
            throw new IllegalStateException("leaf list is empty");
        }
        return this.leafList;
    }


    /**
     * Guarantees that the tree has the desired depth after this call.
     * If the current depth of the tree is less than targetDepth a new level of nodes is calculated.
     * If the tree's depth is already sufficient when calling this methode, nothing happens.
     * @param targetDepth depth that this tree must have
     */
    private void deepenIfNecessary(int targetDepth) {
        //TODO implement
    }

    private boolean hasTime(int maxTime) {
        //TODO implement time management; extract this into its own different class
        return false;
    }

    @Override
    // Ich würde den calculateBestMove evtl einen Namen geben wie kickOffCalculation oder startCalculatingBestMove
    // und dann irgendwo in ner privaten Methode im GameTree eine Hauptschleife für das Iterative Deepening haben
    public GameNode calculateBestMove(int maxTime) {
        //TODO outdated pseudocode
        GameNode nextMove = null;
        int iteratingDepth = 1;
        while (hasTime(maxTime)) {
            deepenIfNecessary(iteratingDepth);
            iteratingDepth++;
            nextMove = evaluator.evaluateTree(this, depth, this.root.getPosition().getWhitesTurn()); 
        }
        if (nextMove == null) {
            //FIXME emergency calculation if time is not even sufficient for the first iteration
            //or "surrender" (not supported by UCI but we could stop playing)
            throw new IllegalArgumentException("time was not sufficient to calculate a move");
        }
        return nextMove;    
    }
}
