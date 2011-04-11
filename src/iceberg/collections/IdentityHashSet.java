package iceberg.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public class IdentityHashSet<E> extends AbstractSet<E> {
    private static final Object PRESENT = new Object();
    
    private transient Map<E, Object> map;
    
    public IdentityHashSet() {
        this.map = new IdentityHashMap<E, Object>();
    }
    
    public IdentityHashSet(Collection<? extends E> c) {
        this.map = new IdentityHashMap<E, Object>(
                Math.max((int) (c.size()/.75f) + 1, 16));
        this.addAll(c);
    }
    
    public IdentityHashSet(int expectedMaxSize) {
        this.map = new IdentityHashMap<E, Object>(expectedMaxSize);
    }

    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    public int size() {
        return this.map.size();
    }
    
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    public boolean contains(Object o) {
        return this.map.containsKey(o);
    }
    
    public boolean add(E o) {
        return this.map.put(o, PRESENT) == null;
    }
    
    public boolean remove(Object o) {
        return this.map.remove(o) == PRESENT;
    }
    
    public void clear() {
        this.map.clear();
    }
}
