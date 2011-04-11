package iceberg.collections;

import iceberg.collections.bits.BitSet;
import iceberg.collections.bits.IntIterator;

import java.util.Iterator;

public class ArraySubsetIterator<E> implements Iterator<E> {
	private final E[] array;
	private final IntIterator delegate;

	public ArraySubsetIterator(E[] array, BitSet indices) {
		this.array = array;
		this.delegate = indices.iterator();
	}
	
	public boolean hasNext() {
		return this.delegate.hasNext();
	}

	public E next() {
		return this.array[this.delegate.next()];
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
