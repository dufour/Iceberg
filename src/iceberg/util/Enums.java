package iceberg.util;

import java.util.Arrays;
import java.util.Collection;

public final class Enums {
    private Enums() {
        // No instances
    }
    
    public static <T extends Enum<T>> T getSibling(T e, int index) {
        return getConstant(e.getDeclaringClass(), index);
    }

    public static <T extends Enum<T>> T getSibling(T e, String name) {
        return Enum.valueOf(e.getDeclaringClass(), name);
    }

    public static <T extends Enum<T>> T getConstant(Class<T> c, int index) {
        T[] constants = c.getEnumConstants();
        return constants[index];
    }

    public static <T extends Enum<?>> int getEnumCount(T e) {
        return getEnumCount(e.getDeclaringClass());
    }

    public static <T extends Enum<?>> int getEnumCount(Class<T> c) {
        return c.getEnumConstants().length;
    }

//    public static <T extends Enum<T>> Iterator<T> iterateConstants(T e) {
//        return iterateConstants(e.getDeclaringClass());
//    }
//
//    public static <T extends Enum<T>> Iterator<T> iterateConstants(Class<T> c) {
//        T[] constants = c.getEnumConstants();
//        return new ArrayIterator<T>(constants);
//    }

    public static <T extends Enum<T>> Collection<T> getConstants(T e) {
        return getConstants(e.getDeclaringClass());
    }

    public static <T extends Enum<T>> Collection<T> getConstants(Class<T> c) {
        return Arrays.asList(c.getEnumConstants());
    }

    public static boolean isEnum(Class<?> c) {
        return c.getEnumConstants() != null;
    }
}
