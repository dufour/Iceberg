package iceberg.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class UniverseSet<E> implements Set<E> {
    private static UniverseSet<?> instance = new UniverseSet<Object>();

    private UniverseSet() {
        // Singleton instance
    }

    public boolean add(E o) {
        return false;
    }

    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object o) {
        return true;
    }

    public boolean containsAll(Collection<?> c) {
        return true;
    }

    public boolean isEmpty() {
        return false;
    }

    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static <T> UniverseSet<T> instance() {
        return (UniverseSet<T>) UniverseSet.instance;
    }
}
