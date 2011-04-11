package iceberg.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingletonIterator<E> implements Iterator<E> {
    private E element;
    private boolean consumed;
    
    public SingletonIterator(E element) {
        this.element = element;
        this.consumed = false;
    }

    public boolean hasNext() {
        return !this.consumed;
    }

    public E next() {
        if (this.consumed) {
            throw new NoSuchElementException();
        }
        
        this.consumed = true;
        return this.element;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
