package iceberg.util.filters;

public class AndFilter<T> implements Filter<T> {
    private Filter<? super T> first;
    private Filter<? super T> second;
    
    public AndFilter(Filter<? super T> first, Filter<? super T> second) {
        this.first = first;
        this.second = second;
    }
    
    public Filter<? super T> getFirst() {
        return this.first;
    }
    
    public Filter<? super T> getSecond() {
        return this.second;
    }
    
    public boolean accepts(T e) {
        boolean b = this.first.accepts(e);
        if (b && this.second != null) {
            b = this.second.accepts(e);
        }
        
        return b;
    }

    public String toString() {
        return this.first + " AND " + this.second; 
    }
}
