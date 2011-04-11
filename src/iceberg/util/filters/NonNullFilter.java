package iceberg.util.filters;

public class NonNullFilter<E> implements Filter<E> {
    private static NonNullFilter<?> instance = new NonNullFilter<Object>();

    public boolean accepts(E e) {
        return e != null;
    }
    
    @SuppressWarnings( { "unchecked", "cast" })
    public static <T> NonNullFilter<T> instance() {
        return (NonNullFilter<T>) NonNullFilter.instance;
    }
}
