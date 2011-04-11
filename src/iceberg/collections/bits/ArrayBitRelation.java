package iceberg.collections.bits;

import iceberg.util.procedures.BinaryIntProcedure;
import iceberg.util.procedures.IntProcedure;

import java.util.Arrays;

public class ArrayBitRelation extends AbstractBitRelation {
	private BitSet[] data;
	private int size;
	
	public ArrayBitRelation() {
		this(5);
	}
	
	public ArrayBitRelation(int size) {
		this.data = new BitSet[size];
		this.size = 0;
	}
	
	protected BitSet makeBitSet() {
		return new CompactBitSet();
	}
	
	private void grow(int max) {
		BitSet[] new_data = new BitSet[max + 5];
		System.arraycopy(this.data, 0, new_data, 0, this.data.length);
		this.data = new_data;
	}
	
	private BitSet getOrMakeBitSet(int x) {
		if (x < 0) throw new IllegalArgumentException("x is negative");
		if (x >= this.data.length) {
			this.grow(x);
		}
		BitSet set = this.data[x];
		if (set == null) {
			set = this.makeBitSet();
			this.data[x] = set;
		}
		return set;
	}
	
	public boolean add(int x, int y) {
		if (y < 0) throw new IllegalArgumentException("y is negative");
		BitSet set = this.getOrMakeBitSet(x);
		if (set.add(y)) {
			this.size += 1;
			return true;
		}
		
		return false;
	}

	public boolean addAll(int x, BitSet bits) {
		BitSet set = this.getOrMakeBitSet(x);
		int prev_size = set.size();
		if (set.addAll(bits)) {
			this.size += (set.size() - prev_size);
			return true;
		}
		return false;
	}

	public boolean addAll(BitRelation r) {
		if (r.getClass() == this.getClass()) {
			ArrayBitRelation other = (ArrayBitRelation) r;
			
			boolean changed = false;
			for (int i = 0; i < other.data.length; i++) {
				BitSet set = other.data[i];
				if (set != null && !set.isEmpty()) {
					changed = this.addAll(i, set) || changed;
				}
			}
			return changed;
		} else {
			return super.addAll(r);
		}
	}

	public void clear() {
		Arrays.fill(this.data, null);
		this.size = 0;
	}

	public boolean contains(int x) {
		if (x >= 0 && x < this.data.length) {
			BitSet set = this.data[x];
			return set != null && !set.isEmpty();
		}
		
		return false;
	}

	public boolean contains(int x, int y) {
		if (x >= 0 && x < this.data.length && y >= 0) {
			BitSet set = this.data[x];
			return set != null && set.contains(y);
		}
		
		return false;
	}

	public boolean copy(BitRelation r) {
		if (this.getClass() == r.getClass()) {
			ArrayBitRelation other = (ArrayBitRelation) r;
			
			boolean changed = false;
			
			int i;
			for (i = 0; i < this.data.length && i < other.data.length; i++) {
				BitSet mine = this.data[i];
				BitSet theirs = other.data[i];
				if (mine == null) {
					if (theirs != null) {
						mine = this.makeBitSet();
						changed = mine.copy(theirs) || changed;
						this.data[i] = mine;
					}
				} else if (theirs == null) {
					this.data[i] = null;
				} else {
					changed = mine.copy(theirs) || changed;
				}
			}
			
			for ( ; i < this.data.length; i++) {
				if (this.data[i] != null && !this.data[i].isEmpty()) {
					this.data[i] = null;
					changed = true;
				}
			}
			
			for ( ; i < other.data.length; i++) {
				BitSet set = other.data[i];
				if (set != null && !set.isEmpty()) {
					if (i >= this.data.length) {
						this.grow(other.data.length - 1);
					}
					this.data[i] = this.makeBitSet();
					this.data[i].copy(set);
					changed = true;
				}
			}
			
			this.size = other.size;
			
			return changed;
		} else {
		    return super.copy(r);
		}
	}

	private static class BridgeProcedure implements IntProcedure {
		public int x;
		private final BinaryIntProcedure proc;
		
		public BridgeProcedure(BinaryIntProcedure proc) {
			this.proc = proc;
		}

		public void execute(int y) {
			this.proc.execute(this.x, y);
		}
	}
	
	public void forEach(final BinaryIntProcedure proc) {
		BridgeProcedure p = new BridgeProcedure(proc);
		for (int i = 0; i < this.data.length; i++) {
			BitSet set = this.data[i];
			if (set != null && !set.isEmpty()) {
				p.x = i;
				set.forEach(p);
			}
		}
	}

	public void forEach(int x, IntProcedure proc) {
		if (x >= 0 && x < this.data.length) {
			BitSet set = this.data[x];
			if (set != null && !set.isEmpty()) {
				set.forEach(proc);
			}
		}
	}

	public void forEach(final int x, final BinaryIntProcedure proc) {
		if (x >= 0 && x < this.data.length) {
			BitSet set = this.data[x];
			if (set != null && !set.isEmpty()) {
				set.forEach(new IntProcedure() {
					public void execute(int y) {
						proc.execute(x, y);
					}					
				});
			}
		}
	}

	public void forEachKey(IntProcedure proc) {
		for (int i = 0; i < this.data.length; i++) {
			BitSet set = this.data[i];
			if (set != null && !set.isEmpty()) {
				proc.execute(i);
			}
		}
	}

	public void forEachValue(IntProcedure proc) {
		// FIXME: this may visit the same value multiple times
		for (int i = 0; i < this.data.length; i++) {
			BitSet set = this.data[i];
			if (set != null && !set.isEmpty()) {
				set.forEach(proc);
			}
		}
	}

	public BitSet getRelated(int x) {
		if (x >= 0 && x < this.data.length) {
			BitSet set = this.data[x];
			if (set != null) {
				return set;
			}
		}
		
		return EmptyBitSet.instance();
	}

	public int getRelatedCount(int x) {
		return this.getRelated(x).size();
	}

	public IntIterator iterate(int x) {
		return this.getRelated(x).iterator();
	}

	public BitSet keySet() {
		BitSet keys = this.makeBitSet();
		
		for (int i = 0; i < this.data.length; i++) {
			BitSet set = this.data[i];
			if (set != null && !set.isEmpty()) {
				keys.add(i);
			}
		}
		
		return keys;
	}

	public boolean remove(int x, int y) {
		if (x >= 0 && x < this.data.length) {
			BitSet set = this.data[x];
			if (set != null) {
				if (set.remove(y)) { 
					this.size -= 1;
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean removeAll(int x) {
		if (x >= 0 && x < this.data.length) {
			BitSet set = this.data[x];
			if (set != null && !set.isEmpty()) {
				this.size -= set.size();
				set.clear();
				return true;
			}
		}
		
		return false;
	}

	public int size() {
		return this.size;
	}

	public BitSet valueSet() {
		BitSet values = this.makeBitSet();
		
		for (int i = 0; i < this.data.length; i++) {
			BitSet set = this.data[i];
			if (set != null && !set.isEmpty()) {
				values.addAll(set);
			}
		}
		
		return values;
	}
	
	private static BitRelationFactory FACTORY = null;
    
    public static BitRelationFactory factory() {
        BitRelationFactory factory = FACTORY;
        if (factory == null) {
            factory = new BitRelationFactory() {
                public BitRelation make() {
                    return new ArrayBitRelation();
                }
                
                public BitRelation make(int capacity) {
                    return new ArrayBitRelation(capacity);
                }                
            };
            FACTORY = factory;
        }
        
        return factory;
    }
}
