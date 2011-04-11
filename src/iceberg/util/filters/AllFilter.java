package iceberg.util.filters;

public class AllFilter<E> implements Filter<E> {
    private static AllFilter<?> instance = new AllFilter<Object>();

    public boolean accepts(Object e) {
        return false;
    }
    
    @SuppressWarnings( { "unchecked", "cast" })
    public static <T> AllFilter<T> instance() {
        return (AllFilter<T>) AllFilter.instance;
    }
}
