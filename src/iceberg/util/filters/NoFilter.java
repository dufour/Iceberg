package iceberg.util.filters;

public class NoFilter<E> implements Filter<E> {
    private static NoFilter<?> instance = new NoFilter<Object>();

    public boolean accepts(Object e) {
        return true;
    }
    
    @SuppressWarnings( { "unchecked", "cast" })
    public static <T> NoFilter<T> instance() {
        return (NoFilter<T>) NoFilter.instance;
    }
}
