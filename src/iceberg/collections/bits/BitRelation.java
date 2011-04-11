package iceberg.collections.bits;

import iceberg.util.procedures.BinaryIntProcedure;
import iceberg.util.procedures.IntProcedure;

import java.util.Iterator;

public interface BitRelation extends Iterable<IntPair> {
    public boolean add(int x, int y);
    public boolean addAll(int x, BitSet bits);
    public boolean addAll(BitRelation r);
    public void clear();
    public boolean copy(BitRelation r);
    public boolean contains(int x);
    public boolean contains(int x, int y);
    public boolean containsAll(BitRelation r);
    public BitSet getRelated(int x);
    public int getRelatedCount(int x);
    public boolean isEmpty();
    public Iterator<IntPair> iterator();
    public IntIterator iterate(int x);
    public BitSet keySet();
    public BitSet valueSet();
    public boolean remove(int x, int y);
    public boolean removeAll(int x);
    public int size();

    public void forEach(BinaryIntProcedure proc);
    public void forEach(int x, IntProcedure proc);
    public void forEach(int x, BinaryIntProcedure proc);
    public void forEachKey(IntProcedure proc);
    public void forEachValue(IntProcedure proc);
}
