package helper;

import java.util.ArrayList;
import java.util.List;

import gametree.ComputeChildrenException;
import gametree.GameNode;
import gametree.Node;
import model.Position;

public class GameNodeHelper {
    
    /**
     * Extracts the positions stored by the nodes' children.
     * Calls queryChildren() to retrieve the nodes' children.
     * @param parent the node whose childrens positions should be extracted
     * @return a list of the positions stored by the node's children
     */
    public static List<Position> extractChildPositions(GameNode parent) throws ComputeChildrenException {
        List<? extends Node<Position>> children = parent.queryChildren();
        List<Position> childPositions = new ArrayList<Position>(children.size());
        for (int i = 0; i < children.size(); i++) {
            childPositions.add(children.get(i).getContent());
        }
        return childPositions;
    }
}
