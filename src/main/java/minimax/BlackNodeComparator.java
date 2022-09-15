package minimax;

import java.util.Comparator;

import gametree.Node;
//FIXME make comparator for evaluables instead
/**
 * A comparator ordering nodes by their static evaluation {@link Node#evaluateStatically(boolean, int)}
 *  in ascending order.
 */
public class BlackNodeComparator<T> implements Comparator<Node<T>> {

    /**
     * Orders the data structure from smallest to greatest.
     * @return 
     * <ul>
     * <li>negative int if o1 greater than o2</li>
     * <li>0 if o1 equals o2</li>
     * <li>positive int  if o1 less than o2</li>
     * </ul>
     */
    @Override
    public int compare(Node<T> o1, Node<T> o2) {
        //FIXME inefficient for gamenodes becaus they are statically evaluated multiple times
        //possibly multiple evluations with comparator
        //additional evaluation if leaf
        return o1.evaluateStatically(false, 0) - o2.evaluateStatically(false, 0);
    }

}
