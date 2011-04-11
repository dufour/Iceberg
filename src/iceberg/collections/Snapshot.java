package iceberg.collections;

import iceberg.util.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Snapshot<E> implements Iterable<E> {
    private Collection<E> collection;

    public Snapshot(Collection<? extends E> collection) {
    	this.collection = new ArrayList<E>(collection);
    }

    public Snapshot(Iterable<? extends E> iterable) {
    	this(Collections.toList(iterable));
    }
    
    public Snapshot(Iterator<? extends E> iterator) {
    	this(Collections.toList(iterator));
    }

	public Iterator<E> iterator() {
		return this.collection.iterator();
	}
}
