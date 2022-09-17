package minimax;

import java.util.Comparator;

import gametree.Node;

//FIXME make comparator for evaluables instead
/**
 * A comparator ordering nodes by their static evaluation {@link Node#evaluate(boolean, int)}
 *  in ascending order.
 */
public class AscendingStaticValueComparator<T> implements Comparator<Node<T>> {
    public static int comparisons = 0;
    /**
     * Orders the data structure from smallest to greatest.
     * @return 
     * <ul>
     * <li>positive int if evaluable1 greater than evaluable2</li>
     * <li>0 if evaluable1 equals evaluable2</li>
     * <li>negative int if evaluable1 less than evaluable2</li>
     * </ul>
     */
    @Override
    public int compare(Node<T> evaluable1, Node<T> evaluable2) {
        comparisons += 1;
        //FIXME is it a problem that this does not provide depth to evaluation function (provides depth=0)
        //evaluation may not require child generation here
        return evaluable1.cachedEvaluateStatically() - evaluable2.cachedEvaluateStatically();
    }

}
