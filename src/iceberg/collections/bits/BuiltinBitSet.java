package iceberg.collections.bits;

import iceberg.util.procedures.IntProcedure;

import java.util.NoSuchElementException;

public class BuiltinBitSet extends AbstractBitSet {
    private java.util.BitSet delegate;

    public BuiltinBitSet() {
        this.delegate = new java.util.BitSet();
    }
    
    public BuiltinBitSet(int capacity) {
        this.delegate = new java.util.BitSet(capacity);
    }
    
    private BuiltinBitSet(java.util.BitSet delegate) {
        this.delegate = delegate;
    }

    public boolean add(int v) {
        if (v < 0) {
            throw new IllegalArgumentException("BuiltinBitSet only handles non-negative values");
        }

        if (!this.delegate.get(v)) {
            this.delegate.set(v);
            return true;
        }

        return false;
    }

    public int capacity() {
        return this.delegate.size();
    }

    public void clear() {
        this.delegate.clear();
    }

    public boolean contains(int v) {
        if (v < 0) {
            throw new IllegalArgumentException("BuiltinBitSet only handles non-negative values");
        }

        return this.delegate.get(v);
    }

    public IntIterator iterator() {
        final java.util.BitSet set = this.delegate;
        return new IntIterator() {
            private int index = set.nextSetBit(0);

            public boolean hasNext() {
                return this.index >= 0;
            }

            public int next() {
                if (this.index >= 0) {
                    int result = this.index;
                    this.index = set.nextSetBit(this.index + 1);
                    return result;
                }

                throw new NoSuchElementException();
            }
            
            public void remove() {
            	throw new UnsupportedOperationException();
            }
        };
    }

    public boolean remove(int v) {
        if (v < 0) {
            throw new IllegalArgumentException("BuiltinBitSet only handles non-negative values");
        }

        if (this.delegate.get(v)) {
            this.delegate.clear(v);
            return true;
        }

        return false;
    }

    public boolean retainAll(BitSet set) {
        boolean changed = false;
        final java.util.BitSet delegate = this.delegate;
        for (int i = delegate.nextSetBit(0); i >= 0; i = delegate.nextSetBit(i + 1)) {
            if (!set.contains(i)) {
                delegate.clear(i);
                changed = true;
            }
        }

        return changed;
    }

    public int size() {
        return this.delegate.cardinality();
    }

	public void forEach(IntProcedure procedure) {
		final java.util.BitSet set = this.delegate;
		int bit = set.nextSetBit(0);
		while (bit >= 0) {
			procedure.execute(bit);
			bit = set.nextSetBit(bit + 1);
		}			
	}
	
	public BitSet clone() {
	    return new BuiltinBitSet((java.util.BitSet) this.delegate.clone());
	}
	
	private static BitSetFactory FACTORY = null;
    
    public static BitSetFactory factory() {
        BitSetFactory factory = FACTORY;
        if (factory == null) {
            factory = new BitSetFactory() {
                public BitSet make() {
                    return new BuiltinBitSet();
                }
                
                public BitSet make(int capacity) {
                    return new BuiltinBitSet(capacity);
                }                
            };
            FACTORY = factory;
        }
        
        return factory;
    }
}
