package iceberg.util.functions;

public interface Function<D, I> {
    public I evaluate(D x);
}
