package minimax;

import java.util.Comparator;

import gametree.Node;
import positionevaluator.Evaluable;

//FIXME make comparator for evaluables instead
/**
 * A comparator ordering nodes by their static evaluation {@link Node#evaluateStatically(boolean, int)}
 *  in ascending order.
 */
public class AscendingStaticValueComparator implements Comparator<Evaluable> {

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
    public int compare(Evaluable evaluable1, Evaluable evaluable2) {
        //FIXME inefficient for gamenodes becaus they are statically evaluated multiple times
        //possibly multiple evluations with comparator
        //additional evaluation if leaf
        return evaluable1.evaluateStatically(false, 0) - evaluable2.evaluateStatically(false, 0);
    }

}
