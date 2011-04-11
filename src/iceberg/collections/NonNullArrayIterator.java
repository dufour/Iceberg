package iceberg.collections;

public class NonNullArrayIterator<E> extends ArrayIterator<E> {
    public NonNullArrayIterator(E[] array) {
        super(array);
        this.advance();
    }
    
    public NonNullArrayIterator(E[] array, int start) {
        super(array, start);
        this.advance();
    }
    
    public NonNullArrayIterator(E[] array, int start, int end) {
        super(array, start, end);
        this.advance();
    }
    
    private void advance() {
    	while (this.pos <= this.end && this.array[this.pos] == null) {
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
