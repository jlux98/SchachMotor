package tests;

import gametree.DetachingGameNode;
import gametree.GameNode;
import model.Position;

public class DetachingGameNodeTest extends GameNodeTestBase {

    @Override
    protected GameNode createRoot(Position position) {
        return new DetachingGameNode(position);
    }

}
