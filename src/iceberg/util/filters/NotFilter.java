package iceberg.util.filters;

public class NotFilter<T> implements Filter<T> {
    private Filter<T> delegate;
    
    public NotFilter(Filter<T> delegate) {
        this.delegate = delegate;
    }
    
    public Filter<T> getDelegate() {
        return this.delegate;
    }
    
    public boolean accepts(T o) {
        return !this.getDelegate().accepts(o);
    }
    
}
