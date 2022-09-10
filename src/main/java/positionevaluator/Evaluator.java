package positionevaluator;

public interface Evaluator<T> {
    public abstract int evaluate(T evaluable, boolean isNaturalLeaf);
}
