package iceberg.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptyIterator<E> implements Iterator<E> {
	private static final EmptyIterator<?> instance = new EmptyIterator<Object>();
	
	private EmptyIterator() {
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> EmptyIterator<T> instance() {
		return (EmptyIterator<T>) EmptyIterator.instance;
	}

	public boolean hasNext() {
		return false;
	}

	public E next() {
		throw new NoSuchElementException();
	}

	public void remove() {
		throw new IllegalStateException("No element to remove");
	}
}
