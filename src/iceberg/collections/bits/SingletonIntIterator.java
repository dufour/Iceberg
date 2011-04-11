package iceberg.collections.bits;

import java.util.NoSuchElementException;

public class SingletonIntIterator implements IntIterator {
    private int value;
    private boolean consumed;

    public SingletonIntIterator(int value) {
        this.value = value;
        this.consumed = false;
    }
    
    public boolean hasNext() {
        return !this.consumed;
    }

    public int next() {
        if (this.consumed) {
            throw new NoSuchElementException();
        }
        
        this.consumed = true;
        return this.value;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
