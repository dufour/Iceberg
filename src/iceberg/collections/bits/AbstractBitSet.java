package iceberg.collections.bits;

import iceberg.util.procedures.IntProcedure;


public abstract class AbstractBitSet implements BitSet {
    public boolean addAll(BitSet set) {
        boolean r = false;
        for (IntIterator i = set.iterator(); i.hasNext(); ) {
            r = this.add(i.next()) || r;
        }

        return r;
    }

    public boolean containsAll(BitSet set) {
        for (IntIterator i = set.iterator(); i.hasNext(); ) {
            if (!this.contains(i.next())) {
                return false;
            }
        }

        return true;
    }

    public boolean copy(BitSet set) {
        if (!this.equals(set)) {
            this.clear();
            this.addAll(set);
            return true;
        }

        return false;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof BitSet) {
            BitSet set = (BitSet) obj;

            return this.size() == set.size() && this.containsAll(set);
        }

        return false;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int hashCode() {
        int hash = 0;
        for (IntIterator i = this.iterator(); i.hasNext(); ) {
            hash += i.next();
        }

        return hash;
    }

    public boolean removeAll(BitSet set) {
        boolean r = false;

        for (IntIterator i = set.iterator(); i.hasNext(); ) {
            r = this.remove(i.next()) || r;
        }

        return r;
    }

    public int[] toArray() {
        int[] a = new int[this.size()];
        return this.toArray(a);
    }

    public int[] toArray(int[] a) {
        if (a.length < this.size()) {
            a = new int[this.size()];
        }

        int p = 0;
        for (IntIterator i = this.iterator(); i.hasNext(); ) {
            a[p++] = i.next();
        }

        return a;
    }

    public String toString() {
        String r = "[";
        for (IntIterator i = this.iterator(); i.hasNext(); ) {
            r += i.next();
            if (i.hasNext()) {
                r += ", ";
            }
        }
        return r + "]";
    }
    
    public void forEach(IntProcedure procedure) {
    	for (IntIterator i = this.iterator(); i.hasNext(); ) {
    		procedure.execute(i.next());
    	}
    }
    
    public abstract BitSet clone();
}
