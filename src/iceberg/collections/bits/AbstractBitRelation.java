package iceberg.collections.bits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import iceberg.collections.Box;
import iceberg.collections.UnmodifiableIterator;
import iceberg.util.Strings;
import iceberg.util.procedures.BinaryIntProcedure;
import iceberg.util.procedures.InterruptedProcedureException;

public abstract class AbstractBitRelation implements BitRelation {
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public boolean containsAll(BitRelation r) {
        try {
            r.forEach(new BinaryIntProcedure() {
                public void execute(int x, int y) {
                    if (!contains(x, y)) {
                        throw new InterruptedProcedureException();
                    }
                }           
            });
            return true;
        } catch (InterruptedProcedureException e) {
            return false;
        }
    }
    
    public boolean copy(BitRelation r) {
        if (!this.equals(r)) {
            this.clear();
            this.addAll(r);
            return true;
        } else {
            return false;           
        }
    }
    
    public boolean addAll(BitRelation r) { // TODO: Optimize
        final Box<Boolean> result = new Box<Boolean>(Boolean.FALSE); 
        r.forEach(new BinaryIntProcedure() {
            public void execute(int x, int y) {
                if (add(x, y)) {
                    result.set(Boolean.TRUE);
                }
            }           
        });
        return result.get().booleanValue();
    }
        
    public Iterator<IntPair> iterator() {
        final List<IntPair> pairs = new ArrayList<IntPair>(this.size());
        this.forEach(new BinaryIntProcedure() {
            public void execute(int x, int y) {
                pairs.add(new IntPair(x, y));
            }
        });
        return new UnmodifiableIterator<IntPair>(pairs.iterator());
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        for (IntPair p: this) {
            h += p.hashCode();
        }

        return h;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else  if (obj instanceof BitRelation) {
            BitRelation other = (BitRelation) obj;          

            if (this.size() != other.size()) {
                return false;
            }

            return this.containsAll(other) && other.containsAll(this);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "{" + Strings.join(this, ", ") + "}";        
    }
}
