package iceberg.util.filters;

import java.util.Collection;

public class CollectionFilter<E> implements Filter<E> {
    private Collection<? extends E> collection;
    
    public CollectionFilter(Collection<? extends E> c) { 
        this.collection = c;
    }
    
    public Collection<? extends E> getCollection() {
        return this.collection;
    }
    
    public boolean accepts(E e) {
        return this.collection.contains(e);
    }
}
