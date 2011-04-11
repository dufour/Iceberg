package iceberg.collections.bits;

import iceberg.util.Arrays;
import iceberg.util.procedures.IntProcedure;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MappedBitSet<E> implements Set<E> {
	private final Indexer<E> indexer;
	private BitSet bitset;

    public MappedBitSet(Indexer<E> indexer) {
        this(indexer, CompactBitSet.factory());
    }
    
	public MappedBitSet(Indexer<E> indexer, BitSetFactory setFactory) {
		this(indexer, setFactory.make());
	}
	
	public MappedBitSet(Indexer<E> indexer, BitSet set) {
        this.indexer = indexer;
        this.bitset = set;
    }
	
	public boolean add(E node) {
		int id = this.indexer.get(node);
		if (id < 0) {
			throw new IllegalArgumentException();
		}

		return this.bitset.add(id);
	}

	public boolean addAll(Collection<? extends E> c) {
		if (c.isEmpty()) return false;
		if (c == this) return false;
		if (c.getClass() == this.getClass()) {
			MappedBitSet<?> other = (MappedBitSet<?>) c;
			if (this.indexer == other.indexer) {
				return this.bitset.addAll(other.bitset);
			}
		}

		boolean changed = false;
		for (E element: c) {
			changed = this.add(element) || changed;
		}
		return changed;
	}

	public void clear() {
		this.bitset.clear();
	}

	public boolean contains(Object node) {
		int id = this.indexer.get(node);
		if (id >= 0) {
			return this.bitset.contains(id);
		} else {
		    return false;
		}
	}

	public boolean containsAll(Collection<?> c) {
		if (c.isEmpty()) return true;
		if (this.size() < c.size()) return false;
		if (c == this) return true;
		if (c.getClass() == this.getClass()) {
			MappedBitSet<?> other = (MappedBitSet<?>) c;
			if (this.indexer == other.indexer) {
				return this.bitset.containsAll(other.bitset);
			}
		}

		for (Object n: c) {
			if (!this.contains(n)) {
				return false;
			}
		}

		return true;
	}

	public boolean isEmpty() {
		return this.bitset.isEmpty();
	}

	public Iterator<E> iterator() {		
		return new Iterator<E>() {            
            private IntIterator delegate = bitset.iterator();

            public boolean hasNext() {
                return this.delegate.hasNext();
            }

            public E next() {
                return indexer.get(delegate.next());
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }           
        };
	}

	public boolean remove(Object node) {
		int id = this.indexer.get(node);
		if (id >= 0) {
			return this.bitset.remove(id);
		} else {
		    return false;
		}
	}

	public boolean removeAll(Collection<?> c) {
		if (c == this) {
			if (this.isEmpty()) return false;
			this.clear();
			return true;
		} else if (c.getClass() == this.getClass()) {
			MappedBitSet<?> other = (MappedBitSet<?>) c;
			if (this.indexer == other.indexer) {
				return this.bitset.removeAll(other.bitset);
			}
		}

		boolean changed = false;
		for (Object n: c) {
			changed = this.remove(n) || changed;
		}
		return changed;
	}

	public boolean retainAll(Collection<?> c) {
		if (c == this) return false;
		if (c.getClass() == this.getClass()) {
		    MappedBitSet<?> other = (MappedBitSet<?>) c;
			if (this.indexer == other.indexer) {
				return this.bitset.retainAll(other.bitset);
			}
		}

		boolean changed = false;
		for (Object n: c) {
			changed = this.remove(n) || changed;
		}
		return changed;
	}

	public int size() {
		return this.bitset.size();
	}

	public Object[] toArray() {
		return this.toArray(new Object[this.size()]);
	}

	public <T> T[] toArray(T[] a) {
		if (a.length < this.size()) {
			a = Arrays.newArray(a, this.size());
		}

		this.bitset.forEach(new ArrayPopulator<T>(this.indexer, a));
		return a;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Set<?>)) {
			return false;
		}

		Set<?> other = (Set<?>) obj;
		if (other.size() != this.size()) {
			return false;
		}

		return this.containsAll(other);
	}

	@Override
	public int hashCode() {
		int hashcode = 0;
		Iterator<E> i = this.iterator();
		while (i.hasNext()) {
			hashcode += i.next().hashCode();
		}
		return hashcode;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("[");
		for (Iterator<?> i = this.iterator(); i.hasNext(); ) {
			buffer.append(i.next());
			if (i.hasNext()) {
				buffer.append(", ");
			}
		}
		buffer.append("]");
		return buffer.toString();
	}

	private static class ArrayPopulator<T> implements IntProcedure {
		private final Indexer<?> indexer;
		private final Object[] array;
		private int i;

		public ArrayPopulator(Indexer<?> indexer, T[] array) {
			this.indexer = indexer;
			this.array = array;
			this.i = 0;
		}

		public void execute(int value) {
			this.array[this.i++] = this.indexer.get(value);
		}
	}
}
