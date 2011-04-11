package iceberg.collections.bits;

public class UnmodifiableIntIterator implements IntIterator {
    private IntIterator delegate;
    
    public UnmodifiableIntIterator(IntIterator delegate) {
        this.delegate = delegate;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    public int next() {
        return this.delegate.next();
    }
}
