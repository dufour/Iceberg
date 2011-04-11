package iceberg.collections;

import java.util.Iterator;

public class LookaheadIterator<T> implements Iterator<T> {
    private Iterator<T> delegate;
    private T buffer;
    private boolean full;

    public LookaheadIterator(Iterator<T> delegate) {
        this.delegate = delegate;
        this.buffer = null;
        this.full = false;
    }

    public boolean hasNext() {
        return this.full || this.delegate.hasNext();
    }

    public T next() {
        if (this.full) {
            this.full = false;
            T obj = this.buffer;
            this.buffer = null;
            return obj;
        }

        return this.delegate.next();
    }

    public void remove() {
        if (this.full) {
            this.buffer = null;
            this.full = false;
        } else {
            this.delegate.remove();
        }
    }

    public T peek() {
        if (!this.full) {
            this.buffer = this.delegate.next();
            this.full = true;
        }

        return this.buffer;
    }
}
