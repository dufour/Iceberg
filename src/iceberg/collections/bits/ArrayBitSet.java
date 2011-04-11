package iceberg.collections.bits;

import java.util.NoSuchElementException;

public class ArrayBitSet extends AbstractBitSet {
    private static final int DEFAULT_CAPACITY = 64;
    private static final int WORD_SIZE = 64;
    private static final int LOG_WORD_SIZE = 6;

    private long[] bits;
    private int size;

//    private int max_size = 0;

    public ArrayBitSet() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayBitSet(int capacity) {
        this.bits = new long[capacity >> LOG_WORD_SIZE];
        this.size = 0;
    }
    
    private ArrayBitSet(long[] bits, int size) {
        this.bits = bits;
        this.size = size;
    }

    // Helpers

    private static int indexOf(int bit) {
        return bit >> LOG_WORD_SIZE;
    }

    private static long mask(int bit) {
        return 1L << (bit & (WORD_SIZE - 1));
    }

    // API

    private void expand(int bit) {
        int n = indexOf(bit) + 1;
        if (n > this.bits.length) {
//            if (this.bits.length * 2 > n) {
//                n = this.bits.length * 2;
//            }

            long[] new_bits = new long[n];
            System.arraycopy(this.bits, 0, new_bits, 0, this.bits.length);
            this.bits = new_bits;

//            if (this.bits.length > max_size) {
//                max_size = this.bits.length;
//                System.err.println("|BIT SET| = " + max_size + " (" + Integer.toHexString(bit) + ")");
//                if (this.bits.length > 100000) {
//                    throw new RuntimeException("Screeech");
//                }
//            }
        }
    }

    public boolean add(int v) {
        if (v < 0) {
            throw new IllegalArgumentException(this.getClass().getName()
                    + " can only hold non-negative values");
        }
        this.expand(v);
        int i = indexOf(v);
        long x = this.bits[i];
        long y = x | mask(v);
        if (x != y) {
            // v was not present
            this.bits[i] = y;
            this.size += 1;

//            if (this.size > this.max_size) {
//                this.max_size = this.size;
//            }

            return true;
        }

        return false;
    }

    public int capacity() {
        return this.bits.length << LOG_WORD_SIZE;
    }

    public void clear() {
        for (int i = 0; i < this.bits.length; i++) {
            this.bits[i] = 0L;
        }

        this.size = 0;
    }

    public boolean contains(int v) {
        if (v < 0) {
            throw new IllegalArgumentException(this.getClass().getName()
                    + " can only hold non-negative values");
        }
        int i = indexOf(v);
        if (i < this.bits.length) {
            return (this.bits[i] & mask(v)) != 0;
        }

        return false;
    }


    public boolean copy(ArrayBitSet set) {
        boolean changed = false;

        if (set.bits.length > this.bits.length) {
            this.expand(set.capacity() - 1);
        }

        int i = 0;
        for (; i < set.bits.length; i++) {
            if (this.bits[i] != set.bits[i]) {
                changed = true;
                this.bits[i] = set.bits[i];
            }
        }

        for (; i < this.bits.length; i++) {
            if (this.bits[i] != 0L) {
                this.bits[i] = 0L;
                changed = true;
            }
        }

        this.size = set.size;

//        if (this.size > this.max_size) {
//            this.max_size = this.size;
//        }

        return changed;
    }

    public boolean copy(BitSet set) {
        if (set instanceof ArrayBitSet) {
            return this.copy((ArrayBitSet) set);
        } else {
            return super.copy(set);
        }
    }

    public boolean equals(ArrayBitSet set) {
        ArrayBitSet small;
        ArrayBitSet large;

        if (this.size != set.size) {
            return false;
        }

        if (this.bits.length < set.bits.length) {
            small = this;
            large = set;
        } else {
            small = set;
            large = this;
        }

        int i = 0;
        for (; i < small.bits.length; i++) {
            if (small.bits[i] != large.bits[i]) {
                return false;
            }
        }

        for (; i < large.bits.length; i++) {
            if (large.bits[i] != 0L) {
                return false;
            }
        }

        return true;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof ArrayBitSet) {
            return this.equals((ArrayBitSet) obj);
        }

        return super.equals(obj);
    }

    public IntIterator iterator() {
        return new BasicBitSetIterator(this.bits);
    }

    public boolean remove(int v) {
        if (v < 0) {
            throw new IllegalArgumentException(this.getClass().getName()
                    + " can only hold non-negative values");
        }
        int i = indexOf(v);
        if (i < this.bits.length) {
            long x = this.bits[i];
            long y = x & (~mask(v));
            if (x != y) {
                // v was present
                this.bits[i] = y;
                this.size -= 1;

                return true;
            }
        }

        return false;
    }

    public boolean retainAll(BitSet v) {
        boolean r = false;

        IntIterator i = new BasicBitSetIterator(this.bits);
        while (i.hasNext()) {
            if (!v.contains(i.next())) {
                i.remove();
                r = true;
            }
        }

        return r;
    }

    public int size() {
        return this.size;
    }
    
    public BitSet clone() {
        long[] newBits = bits.clone();
        return new ArrayBitSet(newBits, this.size);
    }

    private class BasicBitSetIterator extends BitSetIterator implements IntIterator {
        public BasicBitSetIterator(long[] bits) {
            super(bits);
        }

        public void remove() {
            if (this.last != 0L) {
                long b = this.bits[this.last_index];
                this.bits[this.last_index] = b & (~this.last);
                if ((b & this.last) != 0L) {
                    this.last = 0L;
                    ArrayBitSet.this.size -= 1;
                }
            } else {
                throw new NoSuchElementException();
            }
        }
    }
    
    
    private static BitSetFactory FACTORY = null;
    
    public static BitSetFactory factory() {
        BitSetFactory factory = FACTORY;
        if (factory == null) {
            factory = new BitSetFactory() {
                public BitSet make() {
                    return new ArrayBitSet();
                }
                
                public BitSet make(int capacity) {
                    return new ArrayBitSet(capacity);
                }                
            };
            FACTORY = factory;
        }
        
        return factory;
    }
}
