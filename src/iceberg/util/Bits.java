package iceberg.util;

/**
 * Various bit manipulation routines.
 * 
 * @author bdufour
 * @see http://graphics.stanford.edu/~seander/bithacks.html
 */
public final class Bits {    
    private Bits() {
        // No instances
    }
    
    // -------------------------------------------------------------------------
    
    public static String toBinary(int value, int width) {
        return Strings.padLeft(Integer.toBinaryString(value), '0', width);
    }
    
    public static String toOctal(int value, int width) {
        return Strings.padLeft(Integer.toOctalString(value), '0', width);
    }
    
    public static String toHex(int value, int width) {
        return Strings.padLeft(Integer.toHexString(value), '0', width);
    }
    
    // -------------------------------------------------------------------------    
    
    public static int setBits(int v, int bits) {
        return v | bits;
    }
    
    public static int clearBits(int v, int bits) {
        return v & (~bits);
    }
    
    public static boolean hasBits(int v, int bits) {
        return (v & bits) == bits;
    }
    
    public static boolean hasSomeBits(int v, int bits) {
        return (v & bits) != 0;
    }
    
    public static int setOrClearBits(boolean condition, int v, int bits) {
        return condition ? (v | bits) : (v & (~bits));
    }
    
    // -------------------------------------------------------------------------
    
    public static short concat(byte a, byte b) {
        return (short) (((short) a << Byte.SIZE) | b);
    }
    
    public static int concat(short a, short b) {
        return (short) (((int) a << Short.SIZE) | b);
    }
    
    public static long concat(int a, int b) {
        return (short) (((long) a << Integer.SIZE) | b);
    }
    
    // -------------------------------------------------------------------------
    
    /* (a & ~mask) | (b & mask) */
    
    public static short mergeWithMask(short a, short b, short mask) {
        return (short) (a ^ ((a ^ b) & mask));
    }
    
    public static int mergeWithMask(int a, int b, int mask) {
        return a ^ ((a ^ b) & mask);
    }
    
    public static long mergeWithMask(long a, long b, long mask) {
        return a ^ ((a ^ b) & mask);
    }
    
    // -------------------------------------------------------------------------
    
    public static short interleave(byte a, byte b) {
        short result = 0;
        for (int i = 0; i < Byte.SIZE; i++) {
            result |= (a & 1 << i) << i | (b & 1 << i) << (i + 1);
        }
        return result;
    }
    
    public static int interleave(short a, short b) {
        int result = 0;
        for (int i = 0; i < Short.SIZE; i++) {
            result |= (a & 1 << i) << i | (b & 1 << i) << (i + 1);
        }
        return result;
    }
    
    public static long interleave(int a, int b) {
        long result = 0;
        for (int i = 0; i < Integer.SIZE; i++) {
            result |= (a & 1 << i) << i | (b & 1 << i) << (i + 1);
        }
        return result;
    }
    
    // -------------------------------------------------------------------------
    
    public static int countBits(byte v) {
        int count;
        for (count = 0; v != 0; count++) {
            v &= (v - 1);
        }
        return count;
    }
    
    public static int countBits(short v) {
        int count;
        for (count = 0; v != 0; count++) {
            v &= (v - 1);
        }
        return count;
    }
    
    public static int countBits(int v) {
        int count;
        for (count = 0; v != 0; count++) {
            v &= (v - 1);
        }
        return count;
    }
    
    public static int countBits(long v) {
        int count;
        for (count = 0; v != 0; count++) {
            v &= (v - 1);
        }
        return count;
    }
    
    public static boolean getParity(byte v) {
        boolean parity = false;
        
        while (v != 0) {
            parity = !parity;
            v &= (v - 1);            
        }
        
        return parity;
    }
    
    public static boolean getParity(short v) {
        boolean parity = false;
        
        while (v != 0) {
            parity = !parity;
            v &= (v - 1);            
        }
        
        return parity;
    }
    
    public static boolean getParity(int v) {
        boolean parity = false;
        
        while (v != 0) {
            parity = !parity;
            v &= (v - 1);            
        }
        
        return parity;
    }
    
    public static boolean getParity(long v) {
        boolean parity = false;
        
        while (v != 0) {
            parity = !parity;
            v &= (v - 1);            
        }
        
        return parity;
    }
}
