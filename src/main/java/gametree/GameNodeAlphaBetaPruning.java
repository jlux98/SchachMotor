package gametree;

import model.Position;

public class GameNodeAlphaBetaPruning extends AlphaBetaPruningBase<Position> {

    /**
     * <b>Only type safe if thesub tree starting with node consists only of GameNodes or subtypes of GameNodes.
     * If a subtype of Node<Position> that is not also subtype of GameNode is used a CastException might arise. </b>
     */
    @Override
    public GameNode evaluateTree(Node<Position> node, int depth, boolean whitesTurn) {
        // TODO Auto-generated method stub
        //returns Node<Position> which could subtypes other than GameNode

        return (GameNode) super.evaluateTree(node, depth, whitesTurn);
    }


    
}
