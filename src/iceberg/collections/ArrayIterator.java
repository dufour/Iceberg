package iceberg.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<E> implements Iterator<E> {
	protected final E[] array;
	protected int pos;
	protected final int end;
  
    public ArrayIterator(E[] array) {
        this(array, 0, array.length - 1);
    }
    
    public ArrayIterator(E[] array, int start) {
        this(array, start, array.length - 1);
    }
    
    public ArrayIterator(E[] array, int start, int end) {
        this.array = array;
        this.pos = start;
        this.end = end;
    }
    
    public boolean hasNext() {
        return this.pos <= this.end;
    }
    
    public E next() {
        if (this.pos > this.end) {
            throw new NoSuchElementException();
        }
        
        return this.array[this.pos++];
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
