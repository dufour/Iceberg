package iceberg.collections.bits;

import iceberg.util.procedures.BinaryIntProcedure;
import iceberg.util.procedures.IntProcedure;

public final class Collections {
	private Collections() {
		// No instances
	}
	
	public static BitSet unmodifiableSet(final BitSet s) {
		return new AbstractBitSet() {
			public boolean add(int v) {
				throw new UnsupportedOperationException();
			}

			public int capacity() {
				return s.capacity();
			}

			public void clear() {
				throw new UnsupportedOperationException();
			}

			public boolean contains(int v) {
				return s.contains(v);
			}

			public IntIterator iterator() {
				return new UnmodifiableIntIterator(s.iterator());
			}

			public boolean remove(int v) {
				throw new UnsupportedOperationException();
			}

			public boolean retainAll(BitSet set) {
				throw new UnsupportedOperationException();
			}

			public int size() {
				return s.size();
			}			
			
			public BitSet clone() {
		        return s.clone();
		    }
		};
	}
	
	public static BitRelation unmodifiableBitRelation(final BitRelation r) {
		return new AbstractBitRelation() {
			public boolean add(int x, int y) {
				throw new UnsupportedOperationException();
			}

			public boolean addAll(int x, BitSet bits) {
				throw new UnsupportedOperationException();
			}

			public void clear() {
				throw new UnsupportedOperationException();
			}

			public boolean contains(int x) {
				return r.contains(x);
			}

			public boolean contains(int x, int y) {
				return r.contains(x, y);
			}

			public void forEach(BinaryIntProcedure proc) {
				r.forEach(proc);
			}

			public void forEach(int x, IntProcedure proc) {
				r.forEach(x, proc);
			}

			public void forEach(int x, BinaryIntProcedure proc) {
				r.forEach(x, proc);
			}

			public void forEachKey(IntProcedure proc) {
				r.forEachKey(proc);
			}

			public void forEachValue(IntProcedure proc) {
				r.forEachValue(proc);
			}

			public BitSet getRelated(int x) {
				return unmodifiableSet(r.getRelated(x));
			}

			public int getRelatedCount(int x) {
				return r.getRelatedCount(x);
			}

			public IntIterator iterate(int x) {
				return new UnmodifiableIntIterator(r.iterate(x));
			}

			public BitSet keySet() {
				return unmodifiableSet(r.keySet());
			}

			public boolean remove(int x, int y) {
				throw new UnsupportedOperationException();
			}

			public boolean removeAll(int x) {
				throw new UnsupportedOperationException();
			}

			public int size() {
				return r.size();
			}

			public BitSet valueSet() {
				return unmodifiableSet(r.valueSet());
			}			
		};
	}
}
