package minimax;

import java.util.Comparator;

import gametree.Node;
import utility.PerformanceData;

//FIXME make comparator for evaluables instead
/**
 * A comparator ordering nodes by their static evaluation {@link Node#evaluateStatically(boolean, int)}
 *  in descending order.
 */
public class DescendingStaticValueComparator<ContentType> implements Comparator<Node<ContentType>> {
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
    public int compare(Node<ContentType> evaluable1, Node<ContentType> evaluable2) {
        PerformanceData.descendingComparisons += 1;
        //evaluation may not require child generation here
        return -(evaluable1.computeOrGetStaticValueOrBetter() - evaluable2.computeOrGetStaticValueOrBetter());
    }

}
