package iceberg.collections.bits;

import iceberg.util.procedures.IntProcedure;

public class SingletonBitSet extends AbstractBitSet {
    private int value;
    
    public SingletonBitSet(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }
        this.value = value;
    }
    
    public boolean add(int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(BitSet set) {
        throw new UnsupportedOperationException();
    }

    public int capacity() {
        return 1;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(int v) {
        return this.value == v;
    }
    
    @Override
    public boolean containsAll(BitSet set) {
        return set.size() == 1 && set.iterator().next() == this.value;
    }

    @Override
    public boolean copy(BitSet set) {
        throw new UnsupportedOperationException();
    }

    public void forEach(IntProcedure procedure) {
        procedure.execute(this.value);
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }

    public IntIterator iterator() {
        return new SingletonIntIterator(this.value);
    }

    public boolean remove(int v) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(BitSet set) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(BitSet set) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return 1;
    }

    @Override
    public int[] toArray() {
        return new int[] { this.value };
    }

    @Override
    public int[] toArray(int[] a) {
        if (a.length == 0) {
            a = new int[1];
        }
        a[0] = this.value;
        return a;
    }
    
    public BitSet clone() {
        return this;
    }
}
