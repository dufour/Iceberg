package iceberg.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An iterator that iterates over a snapshot to allow concurrent modifications.
 */
public class SnapshotIterator<E> implements Iterator<E> {
    private Iterator<E> it;

    public SnapshotIterator(Collection<? extends E> collection) {
        ArrayList<E> l = new ArrayList<E>(collection);
        this.it = l.iterator();
    }

    public SnapshotIterator(Iterable<? extends E> iterable) {
    	this(iterable.iterator());
    }
    
    public SnapshotIterator(Iterator<? extends E> iterator) {
        ArrayList<E> l = new ArrayList<E>();
        while (iterator.hasNext()) {
            l.add(iterator.next());
        }

        this.it = l.iterator();
    }

    public boolean hasNext() {
        return this.it.hasNext();
    }

    public E next() {
        return this.it.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    public static <E> SnapshotIterator<E> make(Collection<? extends E> c) {
    	return new SnapshotIterator<E>(c);
    }
    
    public static <E> SnapshotIterator<E> make(Iterable<? extends E> i) {
    	return new SnapshotIterator<E>(i);
    }
    
    public static <E> SnapshotIterator<E> make(Iterator<? extends E> i) {
    	return new SnapshotIterator<E>(i);
    }
}
