package iceberg.util.filters;

public interface Filter<T> {
    public boolean accepts(T o);
}
