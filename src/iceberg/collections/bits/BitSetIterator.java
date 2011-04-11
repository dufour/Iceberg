package iceberg.collections.bits;

import java.util.NoSuchElementException;

/**
 * An efficient iterator over bit sets. This class is strongly inspired from the
 * equivalent Soot class, soot.util.BitSetIterator.
 *
 */
abstract class BitSetIterator implements IntIterator {
    protected long[] bits;
    private long word;
    private int curr_index;
    protected int last_index;
    protected long last;
    
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
    
    public BitSetIterator(long[] bits) {
        this.bits = bits;
        
        int i;
        for (i = 0; i < this.bits.length; i++) {
            if (bits[i] != 0L) {
                this.word = bits[i];
                break;
            }
        }
        this.curr_index = i;
        
        this.last_index = -1;
        this.last = 0L;
    }

    public boolean hasNext() {
        return this.curr_index < this.bits.length;
    }

    public int next() {
        if (this.curr_index >= this.bits.length) {
            throw new NoSuchElementException();
        }
        
        long w = this.word;
        int i = this.curr_index;
        
        long k = (w & (w - 1)); // Clears the last non-zero bit. w is guaranteed non-zero.
        long diff = w ^ k;      // Finds out which bit it is. diff has exactly one bit set.
        
        // Save information for remove() 
        this.last = diff;
        this.last_index = i;

        // Computes the position of the set bit.
        int result = 64 * i + ((diff < 0) ? 63 : lookup[(int)(diff % 67)]);

        if (k == 0) {
            i++;
            while (i < this.bits.length && this.bits[i]==0L) { 
                i++;
            }
            if (i < this.bits.length)
                k = this.bits[i];
        }
        this.word = k;
        this.curr_index = i;
        
        return result;
    }
}
