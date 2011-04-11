package iceberg.collections.bits;

import iceberg.util.procedures.IntProcedure;


public interface BitSet extends Cloneable {
	// From an adaptation of java.util.Set
    public boolean add(int v);
    public boolean addAll(BitSet set);
    public int capacity();
    public void clear();
    public boolean contains(int v);
    public boolean containsAll(BitSet set);
    public boolean isEmpty();
    public IntIterator iterator();
    public boolean remove(int v);
    public boolean removeAll(BitSet set);
    public boolean retainAll(BitSet set);
    public int size();
    public int[] toArray();
    public int[] toArray(int[] a);

    // Additional operations
    public boolean copy(BitSet set);
	public void forEach(IntProcedure procedure);
	public BitSet clone();
}
