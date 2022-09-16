package minimax;

import java.util.Comparator;

import gametree.Node;

//FIXME make comparator for evaluables instead
/**
 * A comparator ordering nodes by their static evaluation {@link Node#evaluate(boolean, int)}
 *  in descending order.
 */
public class DescendingStaticValueComparator<T> implements Comparator<Node<T>> {
    /**
     * Orders the data structure from greatest to smallest.
     * @return 
     * <ul>
     * <li>negative int if evaluable1 greater than evaluable2</li>
     * <li>0 if evaluable1 equals evaluable2</li>
     * <li>positive int if evaluable1 less than evaluable2</li>
     * </ul>
     */
    @Override
    public int compare(Node<T> evaluable1, Node<T> evaluable2) {
        //FIXME is it a problem that this does not provide depth to evaluation function (provides depth=0)
        //evaluation may not require child generation here
        return -(evaluable1.cachedEvaluateStatically() - evaluable2.cachedEvaluateStatically());
    }

}
