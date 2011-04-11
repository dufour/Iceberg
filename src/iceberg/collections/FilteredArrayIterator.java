package iceberg.collections;

import iceberg.util.filters.Filter;

public class FilteredArrayIterator<E> extends ArrayIterator<E> {
	private Filter<E> filter;
	
    public FilteredArrayIterator(E[] array, Filter<E> filter) {
        super(array);
        this.filter = filter;
        this.advance();
    }
    
    public FilteredArrayIterator(E[] array, Filter<E> filter, int start) {
        super(array, start);
        this.filter = filter;
        this.advance();
    }
    
    public FilteredArrayIterator(E[] array, Filter<E> filter, int start, int end) {
        super(array, start, end);
        this.filter = filter;
        this.advance();
    }
    
    private void advance() {
    	while (this.pos <= this.end && !this.filter.accepts(this.array[this.pos])) {
    		this.pos += 1;
    	}
    }
    
    public E next() {
        E element = super.next();
        this.advance();
        return element;
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
