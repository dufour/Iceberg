package iceberg.util.filters;

public class NegatingFilter<E> implements Filter<E> {
    private Filter<E> delegate;
    
    public NegatingFilter(Filter<E> delegate) {
        this.delegate = delegate;
    }
    
    public Filter<E> getDelegate() {
        return this.delegate;
    }

    public boolean accepts(E e) {
        return !this.delegate.accepts(e);
    }
    
    public String toString() {
        return "not " + this.delegate;
    }
}
