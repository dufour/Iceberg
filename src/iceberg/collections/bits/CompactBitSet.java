package iceberg.collections.bits;

import iceberg.util.Assertions;
import iceberg.util.procedures.IntProcedure;

import java.util.Arrays;
import java.util.NoSuchElementException;

public final class CompactBitSet extends AbstractBitSet {
    private static final boolean SORTED = true;

    private static final byte MODE_ARRAY = (byte) 1;
    private static final byte MODE_BITS = (byte) 2;

    private static final int WORD_SIZE = 64;
    private static final int LOG_WORD_SIZE = 6;
    private static final int MODE_SWITCH_THRESHOLD = 10;

    private long[] data;
    private int size;
    private byte mode;

    public CompactBitSet() {
        this.mode = MODE_ARRAY;
        this.data = new long[MODE_SWITCH_THRESHOLD];
        Arrays.fill(this.data, -1L);

        this.size = 0;
    }
    
    private CompactBitSet(long[] data, int size, byte mode) {
        this.data = data;
        this.size = size;
        this.mode = mode;
    }

    // Helpers

    private static int indexOf(int bit) {
        return bit >> LOG_WORD_SIZE;
    }

    private static long mask(int bit) {
        return 1L << (bit & (WORD_SIZE - 1));
    }

    private static int asInt(long value) {
        return (int) (value & Integer.MAX_VALUE);
    }

    private static long asLong(int value) {
        return (long) value;
    }

    // API

//    private void inflate() {
//        this.inflate(0);
//    }

    private void inflate(int maxValue) {
        if (this.mode != MODE_ARRAY) return;

        int max = Math.max(maxValue, this.getMaxValue());
        int new_size = indexOf(max) + 1;

        // Allocate storage
        long[] old_data = this.data;

        this.mode = MODE_BITS;
        this.data = new long[new_size];
        this.size = 0;

        // Add previous values
        for (long value: old_data) {
            if (value >= 0L) {
                this.add(asInt(value));
            }
        }
    }

    private void expand(int bit) {
        final int n = indexOf(bit) + 1;
        if (this.mode == MODE_ARRAY) {
            if (this.contains(bit)) {
                return;
            }

            if (this.size < this.data.length) {
                return;
            }

            this.inflate(bit);
        } else { // this.mode == MODE_BITS
            if (n > this.data.length) {
                long[] new_bits = new long[n + 2];
                System.arraycopy(this.data, 0, new_bits, 0, this.data.length);
                this.data = new_bits;
            }
        }
    }

    public boolean add(int v) {
        if (v < 0) {
            throw new IllegalArgumentException(this.getClass().getName()
                    + " can only hold non-negative values");
        }
        this.expand(v);
        if (this.mode == MODE_ARRAY) {
            if (SORTED) {
                boolean inserted = false;
                for (int i = 0; i < this.data.length; i++) {
                    long value = this.data[i];

                    if (value < 0) {
                        // Values exhausted, insert at this location
                        this.data[i] = asLong(v);
                        inserted = true;
                        break;
                    } else if (asInt(value) == v) {
                        // Value already in set
                        return false;
                    } else if (asInt(value) > v) {
                        // insert value here
                        shiftRight(this.data, i);
                        this.data[i] = asLong(v);
                        inserted = true;
                        break;
                    }
                }

                if (!inserted) {
                	Assertions.unreachable("expand() failed");
                }
            } else {
                int index = -1;
                for (int i = 0; i < this.data.length; i++) {
                    long value = this.data[i];
                    if (asInt(value) == v) {
                        return false;
                    } else if (index < 0 && value < 0L) {
                        index = i;
                    }
                }
                if (index < 0) {
                	Assertions.unreachable("expand() failed");
                }
                this.data[index] = asLong(v);
            }

            this.size += 1;
            return true;
        } else {
            int i = indexOf(v);
            long x = this.data[i];
            long y = x | mask(v);
            if (x != y) {
                // v was not present
                this.data[i] = y;
                this.size += 1;

                return true;
            }

            return false;
        }
    }

    private static void shiftRight(long[] array, int pos) {
        for (int i = array.length - 2; i >= pos && i >= 0; i--) {
            array[i + 1] = array[i];
        }
    }

    private static void shiftLeft(long[] array, int pos) {
        for (int i = pos + 1; i < array.length; i++) {
            array[i - 1] = array[i];
        }
    }

    public int capacity() {
        if (this.mode == MODE_ARRAY) {
            return this.data.length;
        } else {
            return this.data.length << LOG_WORD_SIZE;
        }
    }

    public void clear() {
        Arrays.fill(this.data, (this.mode == MODE_ARRAY) ? -1L : 0L);
        this.size = 0;
    }

    public boolean contains(int v) {
        if (v < 0) {
            throw new IllegalArgumentException(this.getClass().getName()
                    + " can only hold non-negative values");
        } else if (this.isEmpty()) {
        	return false; // Check easy case first
        }

        if (this.mode == MODE_ARRAY) {
            if (SORTED) {
                for (long value: this.data) {
                    if (asInt(value) > v) {
                        return false;
                    } else if (value == v) {
                        return true;
                    } else if (value < 0L) {
                    	return false;
                    }
                }
            } else {
                for (long value: this.data) {
                    if (v == value) {
                        return true;
                    }
                }
            }
        } else {
            int i = indexOf(v);
            if (i < this.data.length) {
                return (this.data[i] & mask(v)) != 0;
            }
        }

        return false;
    }

    public int getMaxValue() {
        if (this.mode == MODE_ARRAY) {
            int max = -1;
            if (SORTED) {
                int last = this.size() - 1;
                if (last >= 0) {
                    return asInt(this.data[last]);
                }
            } else {
                for (long value: this.data) {
                    if (value >= 0 && value > max) {
                        max = asInt(value);
                    }
                }
            }

            return max;
        } else {
            if (this.size == 0) {
                return -1;
            }

            for (int i = this.data.length - 1; i >= 0; i--) {
                long bits = this.data[i];
                if (bits != 0) {
                    int index = WORD_SIZE - 1;
                    while (bits > 0L) {
                        bits = bits << 1;
                        index -= 1;
                    }
                    return (i * WORD_SIZE) + index;
                }
            }

            Assertions.unreachable();
            return -1;
        }
    }

    private void rawCopy(CompactBitSet set) {
        this.mode = set.mode;
        this.size = set.size;
        this.data = new long[set.data.length];
        System.arraycopy(set.data, 0, this.data, 0, set.data.length);

//        if (this.mode == MODE_ARRAY) {
//            if (set.mode == MODE_ARRAY) {
//                if (this.data.length < set.data.length) {
//                    this.data = new long[set.data.length];
//                }
//                if (this.data.length > set.data.length) {
//                    Arrays.fill(this.data, set.data.length, this.data.length, -1L);
//                }
//                System.arraycopy(set.data, 0, this.data, 0, set.data.length);
//            } else {
//                this.mode = MODE_BITS;
//                this.data = set.data.clone();
//            }
//        } else if (set.mode == MODE_ARRAY) {
//            this.mode = MODE_ARRAY;
//            this.data = set.data.clone();
//        } else {
//            if (set.data.length > this.data.length) {
//                this.expand(set.capacity() - 1);
//            }
//
//            System.arraycopy(set.data, 0, this.data, 0, set.data.length);
//            if (this.data.length > set.data.length) {
//                Arrays.fill(this.data, set.data.length, this.data.length, 0L);
//            }
//        }
    }

    public boolean copy(CompactBitSet set) {
        boolean changed;

        if (this.size() != set.size()) {
            // Optimization, since we already know that the sets differ
            this.rawCopy(set);
            changed = true;
        } else {
            changed = false;

            if (set.mode == MODE_ARRAY) {
                // Optimistically assume that the sets are equal, and perform
                // a raw copy if we get evidence of the contrary.
                for (long value: set.data) {
                    if (value >= 0L && !this.contains(asInt(value))) {
                        // Assumption was incorrect, perform a raw copy
                        this.rawCopy(set);
                        changed = true;
                        break;
                    }
                }
            } else { // set.mode == MODE_BITS
                if (this.mode == MODE_ARRAY) {
                    // Modes don't match, inflate this set
                    this.inflate(set.getMaxValue());
                }

                if (set.data.length > this.data.length) {
                    this.expand(set.getMaxValue());
                }

                int i = 0;
                for (; i < set.data.length && i < this.data.length; i++) {
                    if (this.data[i] != set.data[i]) {
                        changed = true;
                        this.data[i] = set.data[i];
                    }
                }

                // Clear the extra array slots
                for (; i < this.data.length; i++) {
                    if (this.data[i] != 0L) {
                        this.data[i] = 0L;
                        changed = true;
                    }
                }
            }
        }

        return changed;
    }

    public boolean copy(BitSet set) {
        if (set instanceof CompactBitSet) {
            return this.copy((CompactBitSet) set);
        } else {
            return super.copy(set);
        }
    }

    public boolean equals(CompactBitSet set) {
        CompactBitSet small;
        CompactBitSet large;

        if (this.size() != set.size()) {
            return false;
        }

        if (this.data.length < set.data.length) {
            small = this;
            large = set;
        } else {
            small = set;
            large = this;
        }

        if (small.mode == MODE_ARRAY) {
            for (long value: small.data) {
                if (value >= 0L && !large.contains(asInt(value))) {
                    return false;
                }
            }
        } else if (large.mode == MODE_ARRAY) {
            for (long value: large.data) {
                if (value >= 0L && !small.contains(asInt(value))) {
                    return false;
                }
            }
        } else {
            int i = 0;
            for (; i < small.data.length; i++) {
                if (small.data[i] != large.data[i]) {
                    return false;
                }
            }

            for (; i < large.data.length; i++) {
                if (large.data[i] != 0L) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof CompactBitSet) {
            return this.equals((CompactBitSet) obj);
        }

        return super.equals(obj);
    }

    /* Computes log_2(x) modulo 67. This uses the fact that 2 is a
     * primitive root modulo 67 */
    private final static int[] lookup = {
            -1, 0, 1, 39, 2, 15, 40, 23, 3, 12, 16,
            59, 41, 19, 24, 54,  4, -1, 13, 10, 17,
            62, 60, 28, 42, 30, 20, 51, 25, 44, 55,
            47,  5, 32, -1, 38, 14, 22, 11, 58, 18,
            53, -1,  9, 61, 27, 29, 50, 43, 46, 31,
            37, 21, 57, 52,  8, 26, 49, 45, 36, 56,
            7, 48, 35, 6, 34, 33};

    @Override
    public void forEach(IntProcedure procedure) {
    	if (this.mode == MODE_ARRAY) {
			for (long value: this.data) {
                if (value >= 0) {
                	procedure.execute(asInt(value));
                } else if (SORTED) {
                	break;
                }
			}
    	} else {
    		for (int i = 0; i < this.data.length; i++) {
    			long value = this.data[i];
    			while (value != 0L) {
    				long k = (value & (value - 1)); // Clears the last non-zero bit. w is guaranteed non-zero.
    		        long diff = value ^ k;      // Finds out which bit it is. diff has exactly one bit set.

    		        int bit = 64 * i + ((diff < 0) ? 63 : lookup[(int)(diff % 67)]);
    		        procedure.execute(bit);

    		        value = k;
    			}
    		}
    	}
    }

    public IntIterator iterator() {
        if (this.mode == MODE_ARRAY) {
            return new ArrayBitSetIterator(this.data);
        } else {
            return new BasicBitSetIterator(this.data);
        }
    }

    public boolean remove(int v) {
        if (v < 0) {
            throw new IllegalArgumentException(this.getClass().getName()
                    + " can only hold non-negative values");
        }

        if (this.mode == MODE_ARRAY) {
            if (SORTED) {
                for (int i = 0; i < this.data.length; i++) {
                    if (this.data[i] >= 0L && this.data[i] == v) {
                        shiftLeft(this.data, i);
                        this.data[this.size - 1] = -1L;
                        this.size -= 1;
                        return true;
                    }
                }
            } else {
                for (int i = 0; i < this.data.length; i++) {
                    if (this.data[i] >= 0L && this.data[i] == v) {
                        this.data[i] = -1L;
                        this.size -= 1;
                        return true;
                    }
                }
            }
        } else {
            int i = indexOf(v);
            if (i < this.data.length) {
                long x = this.data[i];
                long y = x & (~mask(v));
                if (x != y) {
                    // v was present
                    this.data[i] = y;
                    this.size -= 1;

                    return true;
                }
            }
        }

        return false;
    }

    public boolean retainAll(BitSet v) {
        boolean r = false;

        IntIterator i = this.iterator();
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
        return new CompactBitSet(this.data.clone(), this.size, this.mode);
    }

    private class ArrayBitSetIterator implements IntIterator {
        private int[] values;
        private int index;

        public ArrayBitSetIterator(long[] data) {
            int count = 0;
            for (long v: data) {
                if (v >= 0L) {
                    count += 1;
                }
            }

            int[] values = new int[count];
            count = 0;
            for (long v: data) {
                if (v >= 0L) {
                    values[count++] = asInt(v);
                }
            }

//            Arrays.sort(values);
//            for (int i = 1; i < values.length; i++) {
//                if (values[i] == values[i-1]) {
//                    throw new RuntimeException("Set contains duplicates!");
//                }
//            }

            this.values = values;
            this.index = 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return this.index < this.values.length;
        }

        public int next() {
            if (this.index < this.values.length) {
                return this.values[this.index++];
            }

            throw new NoSuchElementException();
        }
    }

//    private class ArrayBitSetIterator implements MutableIntIterator {
//        private int index;
//        private int prev_index;
//        private long[] data;
//
//        public ArrayBitSetIterator(long[] data) {
//            this.data = data;
//            this.prev_index = -1;
//            this.index = -1;
//            this.advance();
//        }
//
//        private void advance() {
//            this.index += 1;
//            while (this.index < this.data.length && this.data[this.index] < 0L) {
//                this.index += 1;
//            }
//        }
//
//        public void remove() {
//            Assertions.UNREACHABLE("Disabled for now");
//            if (this.prev_index >= 0) {
//                this.data[this.prev_index] = -1L;
//                this.prev_index = -1;
//                CompactBitSet.this.size -= 1;
//            } else {
//                throw new NoSuchElementException();
//            }
//        }
//
//        public boolean hasNext() {
//            return this.index < this.data.length;
//        }
//
//        public int next() {
//            if (this.index < this.data.length) {
//                int value = asInt(this.data[this.index]);
//                this.prev_index = this.index;
//                this.advance();
//                return value;
//            } else {
//                this.prev_index = -1;
//            }
//
//            throw new NoSuchElementException();
//        }
//    }

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
                    CompactBitSet.this.size -= 1;
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
                    return new CompactBitSet();
                }

                public BitSet make(int capacity) {
                    return new CompactBitSet();
                }
            };
            FACTORY = factory;
        }

        return factory;
    }
}
