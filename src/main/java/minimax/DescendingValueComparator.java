package minimax;

import java.util.Comparator;

import gametree.Evaluable;
import utility.PerformanceData;

/**
 * A comparator ordering evaluables in descending order.
 * Evaluables are compared using their "best" available value.
 * If no value is stored by the evaluable, it will be evaluated statically.
 */
public class DescendingValueComparator implements Comparator<Evaluable> {
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
    public int compare(Evaluable evaluable1, Evaluable evaluable2) {
        PerformanceData.descendingComparisons += 1;
        //evaluation may not require child generation here
        return -(evaluable1.computeOrGetStaticValueOrBetter() - evaluable2.computeOrGetStaticValueOrBetter());
    }

}
