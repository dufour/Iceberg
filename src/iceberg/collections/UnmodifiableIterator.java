package iceberg.collections;

import java.util.Iterator;

public class UnmodifiableIterator<E> implements Iterator<E> {
    private Iterator<E> delegate;

    public UnmodifiableIterator(Iterator<E> delegate) {
        this.delegate = delegate;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    public E next() {
        return this.delegate.next();
    }
}
