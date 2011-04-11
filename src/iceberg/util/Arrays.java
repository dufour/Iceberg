package iceberg.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Arrays {
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<T> elementType, int size) {
        return (T[]) Array.newInstance(elementType, size);
    }

    public static <T> T[] newArray(T[] ref) {
        return newArray(ref, ref.length);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(T[] ref, int size) {
        return newArray((Class<T>) ref.getClass().getComponentType(), size);
    }

    public static List<Byte> asList(final byte[] array) {
        if (array == null) {
            return null;
        }
        List<Byte> list = new ArrayList<Byte>(array.length);
        for (byte v: array) {
            list.add(v);
        }

        return list;
    }

    public static List<Boolean> asList(final boolean[] array) {
        if (array == null) {
            return null;
        }
        List<Boolean> list = new ArrayList<Boolean>(array.length);
        for (boolean v: array) {
            list.add(v);
        }

        return list;
    }

    public static List<Character> asList(final char[] array) {
        if (array == null) {
            return null;
        }
        List<Character> list = new ArrayList<Character>(array.length);
        for (char v: array) {
            list.add(v);
        }

        return list;
    }

    public static List<Double> asList(final double[] array) {
        if (array == null) {
            return null;
        }
        List<Double> list = new ArrayList<Double>(array.length);
        for (double v: array) {
            list.add(v);
        }

        return list;
    }

    public static List<Float> asList(final float[] array) {
        if (array == null) {
            return null;
        }
        List<Float> list = new ArrayList<Float>(array.length);
        for (float v: array) {
            list.add(v);
        }

        return list;
    }

    public static List<Integer> asList(final int[] array) {
        if (array == null) {
            return null;
        }
        List<Integer> list = new ArrayList<Integer>(array.length);
        for (int v: array) {
            list.add(v);
        }

        return list;
    }

    public static List<Long> asList(final long[] array) {
        if (array == null) {
            return null;
        }
        List<Long> list = new ArrayList<Long>(array.length);
        for (long v: array) {
            list.add(v);
        }

        return list;
    }

    public static List<Short> asList(final short[] array) {
        if (array == null) {
            return null;
        }
        List<Short> list = new ArrayList<Short>(array.length);
        for (short v: array) {
            list.add(v);
        }

        return list;
    }

    public static <E> List<E> asList(final E[] array) {
    	if (array == null) {
    		return null;
    	}

    	List<E> list = new ArrayList<E>(array.length);
    	for (E e: array) {
    		list.add(e);
    	}

    	return list;
    }

    public static void reverse(boolean[] array) {
        if (array == null) return;

        for (int i = 0; i < array.length / 2; i++) {
            int j = array.length - 1 - i;

            boolean tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void reverse(byte[] array) {
        if (array == null) return;

        for (int i = 0; i < array.length / 2; i++) {
            int j = array.length - 1 - i;

            byte tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void reverse(char[] array) {
        if (array == null) return;

        for (int i = 0; i < array.length / 2; i++) {
            int j = array.length - 1 - i;

            char tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void reverse(double[] array) {
        if (array == null) return;

        for (int i = 0; i < array.length / 2; i++) {
            int j = array.length - 1 - i;

            double tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void reverse(float[] array) {
        if (array == null) return;

        for (int i = 0; i < array.length / 2; i++) {
            int j = array.length - 1 - i;

            float tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void reverse(int[] array) {
        if (array == null) return;

        for (int i = 0; i < array.length / 2; i++) {
            int j = array.length - 1 - i;

            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void reverse(long[] array) {
        if (array == null) return;

        for (int i = 0; i < array.length / 2; i++) {
            int j = array.length - 1 - i;

            long tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void reverse(short[] array) {
        if (array == null) return;

        for (int i = 0; i < array.length / 2; i++) {
            int j = array.length - 1 - i;

            short tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static byte[] concat(byte[] first, byte[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;

        byte[] result = new byte[first.length + second.length];

        int i = 0;
        for (byte v: first) {
            result[i++] = v;
        }
        for (byte v: second) {
            result[i++] = v;
        }

        return result;
    }

    public static boolean[] concat(boolean[] first, boolean[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;

        boolean[] result = new boolean[first.length + second.length];

        byte i = 0;
        for (boolean v: first) {
            result[i++] = v;
        }
        for (boolean v: second) {
            result[i++] = v;
        }

        return result;
    }

    public static char[] concat(char[] first, char[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;

        char[] result = new char[first.length + second.length];

        int i = 0;
        for (char v: first) {
            result[i++] = v;
        }
        for (char v: second) {
            result[i++] = v;
        }

        return result;
    }

    public static double[] concat(double[] first, double[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;

        double[] result = new double[first.length + second.length];

        int i = 0;
        for (double v: first) {
            result[i++] = v;
        }
        for (double v: second) {
            result[i++] = v;
        }

        return result;
    }

    public static float[] concat(float[] first, float[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;

        float[] result = new float[first.length + second.length];

        int i = 0;
        for (float v: first) {
            result[i++] = v;
        }
        for (float v: second) {
            result[i++] = v;
        }

        return result;
    }

    public static int[] concat(int[] first, int[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;

        int[] result = new int[first.length + second.length];

        int i = 0;
        for (int v: first) {
            result[i++] = v;
        }
        for (int v: second) {
            result[i++] = v;
        }

        return result;
    }

    public static long[] concat(long[] first, long[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;

        long[] result = new long[first.length + second.length];

        int i = 0;
        for (long v: first) {
            result[i++] = v;
        }
        for (long v: second) {
            result[i++] = v;
        }

        return result;
    }

    public static short[] concat(short[] first, short[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;

        short[] result = new short[first.length + second.length];

        int i = 0;
        for (short v: first) {
            result[i++] = v;
        }
        for (short v: second) {
            result[i++] = v;
        }

        return result;
    }

    public static <T> T[] concat(T[] first, T[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;

        T[] result = newArray(first, first.length + second.length);

        int i = 0;
        for (T v: first) {
            result[i++] = v;
        }
        for (T v: second) {
            result[i++] = v;
        }

        return result;
    }

    public static void swap(byte[] array, int i, int j) {
        byte tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(boolean[] array, int i, int j) {
        boolean tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(char[] array, int i, int j) {
        char tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(double[] array, int i, int j) {
        double tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(float[] array, int i, int j) {
        float tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(long[] array, int i, int j) {
        long tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(short[] array, int i, int j) {
        short tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static <T> void swap(T[] array, int i, int j) {
        T tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void shuffle(byte[] array) {
        shuffle(array, new Random());
    }

    public static void shuffle(byte[] array, Random random) {
        if (array == null || array.length == 0) return;

        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    public static void shuffle(boolean[] array) {
        shuffle(array, new Random());
    }

    public static void shuffle(boolean[] array, Random random) {
        if (array == null || array.length == 0) return;

        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    public static void shuffle(char[] array) {
        shuffle(array, new Random());
    }

    public static void shuffle(char[] array, Random random) {
        if (array == null || array.length == 0) return;

        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    public static void shuffle(double[] array) {
        shuffle(array, new Random());
    }

    public static void shuffle(double[] array, Random random) {
        if (array == null || array.length == 0) return;

        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    public static void shuffle(float[] array) {
        shuffle(array, new Random());
    }

    public static void shuffle(float[] array, Random random) {
        if (array == null || array.length == 0) return;

        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    public static void shuffle(int[] array) {
        shuffle(array, new Random());
    }

    public static void shuffle(int[] array, Random random) {
        if (array == null || array.length == 0) return;

        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    public static void shuffle(long[] array) {
        shuffle(array, new Random());
    }

    public static void shuffle(long[] array, Random random) {
        if (array == null || array.length == 0) return;

        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    public static void shuffle(short[] array) {
        shuffle(array, new Random());
    }

    public static void shuffle(short[] array, Random random) {
        if (array == null || array.length == 0) return;

        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    public static <T> void shuffle(T[] array) {
        shuffle(array, new Random());
    }

    public static <T> void shuffle(T[] array, Random random) {
        if (array == null || array.length == 0) return;

        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    public static byte max(byte[] array) {
        if (array == null || array.length == 0) throw new IllegalArgumentException();
        byte max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static char max(char[] array) {
        if (array == null || array.length == 0) throw new IllegalArgumentException();
        char max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static double max(double[] array) {
        if (array == null || array.length == 0) throw new IllegalArgumentException();
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static float max(float[] array) {
        if (array == null || array.length == 0) throw new IllegalArgumentException();
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static int max(int[] array) {
        if (array == null || array.length == 0) throw new IllegalArgumentException();
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static long max(long[] array) {
        if (array == null || array.length == 0) throw new IllegalArgumentException();
        long max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static short max(short[] array) {
        if (array == null || array.length == 0) throw new IllegalArgumentException();
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static byte max(byte[] array, byte defaultValue) {
        if (array == null || array.length == 0) return defaultValue;
        byte max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static char max(char[] array, char defaultValue) {
        if (array == null || array.length == 0) return defaultValue;
        char max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static double max(double[] array, double defaultValue) {
        if (array == null || array.length == 0) return defaultValue;
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static float max(float[] array, float defaultValue) {
        if (array == null || array.length == 0) return defaultValue;
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static int max(int[] array, int defaultValue) {
        if (array == null || array.length == 0) return defaultValue;
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static long max(long[] array, long defaultValue) {
        if (array == null || array.length == 0) return defaultValue;
        long max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static short max(short[] array, short defaultValue) {
        if (array == null || array.length == 0) return defaultValue;
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }

        return max;
    }

    public static <E> void populate(E[] array, Iterable<? extends E> i) {
    	populate(array, i, 0);
    }

    public static <E> void populate(E[] array, Iterable<? extends E> i, int start) {
    	for (E element: i) {
    		array[start++] = element;
    	}
    }
    
    public static <E> void populate(E[] array, Iterator<? extends E> i) {
    	populate(array, i, 0);
    }
    
    public static <E> void populate(E[] array, Iterator<? extends E> i, int start) {
    	while (i.hasNext()) {
    		array[start++] = i.next();
    	}
    }

    public static boolean[] append(boolean b, boolean[] a) {
        boolean[] r;
        if (a != null) {
            r = new boolean[a.length + 1];
            System.arraycopy(a, 0, r, 1, a.length);
        } else {
            r = new boolean[1];
        }
        r[0] = b;

        return r;
    }

    public static byte[] append(byte b, byte[] a) {
        byte[] r;
        if (a != null) {
            r = new byte[a.length + 1];
            System.arraycopy(a, 0, r, 1, a.length);
        } else {
            r = new byte[1];
        }
        r[0] = b;

        return r;
    }

    public static char[] append(char c, char[] a) {
        char[] r;
        if (a != null) {
            r = new char[a.length + 1];
            System.arraycopy(a, 0, r, 1, a.length);
        } else {
            r = new char[1];
        }
        r[0] = c;

        return r;
    }

    public static double[] append(double d, double[] a) {
        double[] r;
        if (a != null) {
            r = new double[a.length + 1];
            System.arraycopy(a, 0, r, 1, a.length);
        } else {
            r = new double[1];
        }
        r[0] = d;

        return r;
    }

    public static float[] append(float f, float[] a) {
        float[] r;
        if (a != null) {
            r = new float[a.length + 1];
            System.arraycopy(a, 0, r, 1, a.length);
        } else {
            r = new float[1];
        }
        r[0] = f;

        return r;
    }

    public static int[] append(int i, int[] a) {
        int[] r;
        if (a != null) {
            r = new int[a.length + 1];
            System.arraycopy(a, 0, r, 1, a.length);
        } else {
            r = new int[1];
        }
        r[0] = i;

        return r;
    }

    public static long[] append(long x, long[] a) {
        long[] r;
        if (a != null) {
            r = new long[a.length + 1];
            System.arraycopy(a, 0, r, 1, a.length);
        } else {
            r = new long[1];
        }
        r[0] = x;

        return r;
    }

    public static short[] append(short s, short[] a) {
        short[] r;
        if (a != null) {
            r = new short[a.length + 1];
            System.arraycopy(a, 0, r, 1, a.length);
        } else {
            r = new short[1];
        }
        r[0] = s;

        return r;
    }
}
