package tests;

import gametree.GameNode;
import model.Position;

public class GameNodeTest extends GameNodeTestBase {

    @Override
    protected GameNode createRoot(Position position) {
        return new GameNode(position);
    }
}