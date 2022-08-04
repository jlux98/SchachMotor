package positionevaluator;

public interface Evaluator<T extends Evaluable> {
    public abstract int evaluate(T evaluable);
}
