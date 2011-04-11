package iceberg.collections.bits;

import iceberg.util.procedures.IntProcedure;

public class EmptyBitSet implements BitSet {
	private static final int[] NO_ELEMENTS = new int[0];
	private static EmptyBitSet instance = new EmptyBitSet();

	private EmptyBitSet() {
		// Singleton instance
	}

	public static EmptyBitSet instance() {
		return EmptyBitSet.instance;
	}
	
	public boolean add(int v) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(BitSet set) {
		throw new UnsupportedOperationException();
	}

	public int capacity() {
		return 0;
	}

	public void clear() {
		// noop
	}

	public boolean contains(int v) {
		return false;
	}

	public boolean containsAll(BitSet set) {
		return set.isEmpty();
	}

	public boolean copy(BitSet set) {
		throw new UnsupportedOperationException();
	}

	public void forEach(IntProcedure procedure) {
		// noop
	}

	public boolean isEmpty() {
		return true;
	}

	public IntIterator iterator() {
		return EmptyIntIterator.instance();
	}

	public boolean remove(int v) {
		return false;
	}

	public boolean removeAll(BitSet set) {
		return false;
	}

	public boolean retainAll(BitSet set) {
		return false;
	}

	public int size() {
		return 0;
	}

	public int[] toArray() {
		return NO_ELEMENTS;
	}

	public int[] toArray(int[] a) {
		return a;
	}
	
	@Override
	public int hashCode() {
		return 8837;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BitSet) {
			return ((BitSet) obj).isEmpty();
		}
		
		return false;
	}
	
	public String toString() {
		return "{}";
	}
	
	public BitSet clone() {
	    return this;
	}
}
