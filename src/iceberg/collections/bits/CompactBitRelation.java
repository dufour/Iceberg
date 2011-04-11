package iceberg.collections.bits;

import iceberg.util.procedures.BinaryIntProcedure;
import iceberg.util.procedures.IntProcedure;

import java.util.Arrays;

public class CompactBitRelation extends AbstractBitRelation {
    private static final int EMPTY = -1;
    private static final int BIT_SET = -2;

    private static final BitSet[] NO_SETS = new BitSet[0];
    private int[] singletons;
	private BitSet[] sets = NO_SETS;
	private int size;

	public CompactBitRelation() {
		this(5);
	}

	public CompactBitRelation(int size) {
	    this.singletons = new int[size];
	    Arrays.fill(this.singletons, EMPTY);
		this.size = 0;
	}

	protected BitSet makeBitSet() {
        return new CompactBitSet();
    }

	protected BitSet makeBitSet(BitSet set) {
        BitSet copy = this.makeBitSet();
        copy.copy(set);
        return copy;
    }

	private void grow(int max) {
	    int[] a = new int[max + 5];
	    System.arraycopy(this.singletons, 0, a, 0, this.singletons.length);
	    Arrays.fill(a, this.singletons.length, a.length, EMPTY);
	    this.singletons = a;
	}

	private BitSet getOrMakeBitSet(int x) {
	    if (x >= this.sets.length) {
	        BitSet[] new_sets = new BitSet[x + 5];
	        System.arraycopy(this.sets, 0, new_sets, 0, this.sets.length);
	        this.sets = new_sets;
	    }

	    BitSet set = this.sets[x];
	    if (set == null) {
	        set = this.makeBitSet();
	        this.sets[x] = set;
	    }

	    return set;
	}

	private BitSet inflate(int x) {
	    BitSet set = this.getOrMakeBitSet(x);
	    set.add(this.singletons[x]);
	    this.singletons[x] = BIT_SET;
	    return set;
	}

    public boolean add(int x, int y) {
        if (x < 0) throw new IllegalArgumentException("x is negative");
        if (y < 0) throw new IllegalArgumentException("y is negative");

        if (x >= this.singletons.length) {
            this.grow(x);
        }

        int val = this.singletons[x];
        if (val == EMPTY) {
            this.singletons[x] = y;
            this.size += 1;
            return true;
        } else if (val == BIT_SET) {
            BitSet set = this.sets[x];
            if (set.add(y)) {
                this.size += 1;
                return true;
            }
        } else if (y == val) {
            return false;
        } else {
            BitSet set = this.inflate(x);
            set.add(y);
            this.size += 1;
            return true;
        }

        return false;
    }

    public boolean addAll(int x, BitSet bits) {
        if (x < 0) throw new IllegalArgumentException("x is negative");
        if (bits.isEmpty()) return false;

        if (x >= this.singletons.length) {
            this.grow(x);
        }

        int val = this.singletons[x];
        if (val == EMPTY) {
            if (bits.size() == 1) {
                this.singletons[x] = bits.iterator().next();
                this.size += 1;
                return true;
            } else {
                this.singletons[x] = BIT_SET;
                BitSet set = this.getOrMakeBitSet(x);
                set.addAll(bits);
                this.size += set.size();
                return true;
            }
        } else if (val == BIT_SET) {
            BitSet set = this.sets[x];
            int old_size = set.size();
            if (set.addAll(bits)) {
                this.size += (set.size() - old_size);
                return true;
            } else {
                return false;
            }
        } else {
            if (bits.size() == 1) {
                int y = bits.iterator().next();
                if (val == y) {
                    return false;
                }
            }

            BitSet set = this.inflate(x);
            set.addAll(bits);
            this.size += set.size() - 1;
            return true;
        }
    }

    public void clear() {
        Arrays.fill(this.singletons, EMPTY);
        this.sets = NO_SETS;
        this.size = 0;
    }

    public boolean contains(int x) {
        if (x >= 0 && x < this.singletons.length) {
            int val = this.singletons[x];
            if (val >= 0) {
                return true;
            } else if (val == BIT_SET) {
                return !this.sets[x].isEmpty();
            }
        }

        return false;
    }

    public boolean contains(int x, int y) {
        if (x >= 0 && x < this.singletons.length) {
            int val = this.singletons[x];
            if (val == EMPTY) {
                return false;
            } else if (val == BIT_SET) {
                return this.sets[x].contains(y);
            } else if (val == y && val >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean copy(BitRelation r) {
        if (r == this) return false;
        if (this.getClass() == r.getClass()) {
            CompactBitRelation other = (CompactBitRelation) r;
            boolean changed = !this.equals(other);

            // Copy singletons
            if (this.singletons.length < other.singletons.length) {
                this.singletons = other.singletons.clone();
            } else {
                System.arraycopy(other.singletons, 0, this.singletons, 0, other.singletons.length);
                if (this.singletons.length > other.singletons.length) {
                    Arrays.fill(this.singletons, other.singletons.length, this.singletons.length, EMPTY);
                }
            }

            // Copy bit sets
            if (this.sets.length < other.sets.length) {
                this.sets = other.sets.clone();
            } else {
                System.arraycopy(other.sets, 0, this.sets, 0, other.sets.length);
                if (this.sets.length > other.sets.length) {
                    Arrays.fill(this.sets, other.sets.length, this.sets.length, null);
                }
            }

            for (int i = 0; i < this.sets.length; i++) {
            	BitSet s = this.sets[i];
            	if (s != null) {
            		this.sets[i] = this.makeBitSet(s);
            	}
            }

            this.size = other.size;

            // assert this.equals(r);

            return changed;
        }

        return super.copy(r);
    }

    public void forEach(BinaryIntProcedure proc) {
        BridgeProcedure bridge = null;
        for (int i = 0; i < this.singletons.length; i++) {
            int val = this.singletons[i];
            if (val == BIT_SET) {
                if (bridge == null) {
                    bridge = new BridgeProcedure(proc);
                }
                bridge.x = i;
                this.sets[i].forEach(bridge);
            } else if (val >= 0) {
                proc.execute(i, val);
            }
        }
    }

    public void forEach(int x, IntProcedure proc) {
        if (x >= 0 && x < this.singletons.length) {
            int val = this.singletons[x];
            if (val == BIT_SET) {
                this.sets[x].forEach(proc);
            } else if (val >= 0) {
                proc.execute(val);
            }
        }
    }

    public void forEach(int x, BinaryIntProcedure proc) {
        if (x >= 0 && x < this.singletons.length) {
            int val = this.singletons[x];
            if (val == BIT_SET) {
                BridgeProcedure bridge = new BridgeProcedure(proc, x);
                this.sets[x].forEach(bridge);
            } else if (val >= 0) {
                proc.execute(x, val);
            }
        }
    }

    public void forEachKey(IntProcedure proc) {
        for (int i = 0; i < this.singletons.length; i++) {
            int val = this.singletons[i];
            if (val != EMPTY) {
                proc.execute(i);
            }
        }
    }

    public void forEachValue(IntProcedure proc) { // FIXME: Can visit the same value multiple times
        for (int i = 0; i < this.singletons.length; i++) {
            int val = this.singletons[i];
            if (val == BIT_SET) {
                this.sets[i].forEach(proc);
            } else if (val >= 0) {
                proc.execute(val);
            }
        }
    }

    public BitSet getRelated(int x) {
        if (x >= 0 && x < this.singletons.length) {
            int val = this.singletons[x];
            if (val == BIT_SET) {
                return Collections.unmodifiableSet(this.sets[x]);
            } else if (val >= 0) {
                return new SingletonBitSet(val);
            }
        }

        return EmptyBitSet.instance();
    }

    public int getRelatedCount(int x) {
        if (x >= 0 && x < this.singletons.length) {
            int val = this.singletons[x];
            if (val == BIT_SET) {
                return this.sets[x].size();
            } else if (val >= 0) {
                return 1;
            }
        }

        return 0;
    }

    public IntIterator iterate(int x) {
        if (x >= 0 && x < this.singletons.length) {
            int val = this.singletons[x];
            if (val == BIT_SET) {
                return new UnmodifiableIntIterator(this.sets[x].iterator());
            } else if (val >= 0) {
                return new SingletonIntIterator(val);
            }
        }

        return EmptyIntIterator.instance();
    }

    public BitSet keySet() {
        BitSet keys = this.makeBitSet();
        for (int i = 0; i < this.singletons.length; i++) {
            if (this.singletons[i] != EMPTY) {
                keys.add(i);
            }
        }

        return keys;
    }

    public boolean remove(int x, int y) {
        if (x >= 0 && x < this.singletons.length) {
            int val = this.singletons[x];
            if (val == BIT_SET) {
                if (this.sets[x].remove(y)) {
                    this.size -= 1;
                    return true;
                }
            } else if (val == y && y >= 0) {
                this.singletons[x] = EMPTY;
                this.size -= 1;
                return true;
            }
        }

        return false;
    }

    public boolean removeAll(int x) {
        if (x >= 0 && x < this.singletons.length) {
            int val = this.singletons[x];
            if (val == BIT_SET) {
                BitSet values = this.sets[x];
                this.size -= values.size();
                this.sets[x] = null;
                this.singletons[x] = EMPTY;
                return !values.isEmpty();
            } else if (val >= 0) {
                this.size -= 1;
                this.singletons[x] = EMPTY;
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
        for (int i = 0; i < this.singletons.length; i++) {
            int val = this.singletons[i];
            if (val == BIT_SET) {
                values.addAll(this.sets[i]);
            } else if (val >= 0) {
                values.add(val);
            }
        }

        return values;
    }

	private static class BridgeProcedure implements IntProcedure {
		public int x;
		private final BinaryIntProcedure proc;

		public BridgeProcedure(BinaryIntProcedure proc) {
			this.proc = proc;
		}

		public BridgeProcedure(BinaryIntProcedure proc, int x) {
            this(proc);
            this.x = x;
        }

		public void execute(int y) {
			this.proc.execute(this.x, y);
		}
	}

	private static BitRelationFactory FACTORY = null;

    public static BitRelationFactory factory() {
        BitRelationFactory factory = FACTORY;
        if (factory == null) {
            factory = new BitRelationFactory() {
                public BitRelation make() {
                    return new CompactBitRelation();
                }

                public BitRelation make(int capacity) {
                    return new CompactBitRelation(capacity);
                }
            };
            FACTORY = factory;
        }

        return factory;
    }
}
