package iceberg.collections.bits;

import java.util.NoSuchElementException;

public class EmptyIntIterator implements IntIterator {
	private static EmptyIntIterator instance = new EmptyIntIterator();
	
	private EmptyIntIterator() {
		// Singleton instance
	}
	
	public static EmptyIntIterator instance() {
		return EmptyIntIterator.instance;
	}
	
	public boolean hasNext() {
		return false;
	}

	public int next() {
		throw new NoSuchElementException();
	}

	public void remove() {
		throw new IllegalStateException("No element to remove");
	}
}
