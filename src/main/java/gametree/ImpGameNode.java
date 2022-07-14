package gametree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Board;
import movegenerator.MoveGenerator;

public class ImpGameNode implements GameNode {

    private GameNode parent;
    private List<GameNode> children;
    private Board gameState;
    private int pointValue;

    public ImpGameNode(Board board) {
        this.gameState = board;
    }

    public ImpGameNode(Board board, GameNode parent) {
        this(board);
        this.children = new ArrayList<GameNode>(20); //TODO think about initial capacity
        //FIXME most attributes are null here
    }

    private void setChildren(List<GameNode> children){
        this.children = children;
    }
    @Override
    public void insertChild(GameNode node) {
        this.children.add(node);
        
    }

    @Override
    public void deleteChild(GameNode node) {
        //TODO equals in game node nötig
        this.children.remove(node);
        
    }

    @Override
    public void deleteChildren() {
        this.children.clear();
        
    }

    @Override
    public boolean hasChildren() {
        return this.children.size() != 0;
    }

    @Override
    public void setValue(int value) {
        this.pointValue = value;
        
    }

    @Override
    public void computeChildren() {
        if (this.children.size() != 0) {
            throw new IllegalStateException("node already has children");
        }
        Board[] followUpBoards = MoveGenerator.generatePossibleMoves(gameState);
        for (Board board : followUpBoards) {
            this.children.add(new ImpGameNode(board));
        }
    }

    @Override
    public GameNode findPlayableAncestor() {
        if (this.parent == null) {
            return this;
        }
        return this.parent.findPlayableAncestor();
    }
    
}
