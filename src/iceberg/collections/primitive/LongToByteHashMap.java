package iceberg.collections.primitive;

import java.util.Arrays;

public class LongToByteHashMap {
    private static final int DEFAULT_CAPACITY = 7;
    private static final float DEFAULT_LOAD_FACTOR = 0.7f;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    
    /** Move entry to front on get? */
    private static final boolean MOVE_TO_FRONT = true;
    /** Insert new entries in front? */
    private static final boolean INSERT_IN_FRONT = true;
    
    private Entry[] entries;
    private int size = 0;
    private float load_factor;
    private int threshold;
    
    public LongToByteHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    public LongToByteHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }
    
    public LongToByteHashMap(float loadFactor) {
        this(DEFAULT_CAPACITY, loadFactor);
    }
    
    public LongToByteHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        }
        
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        
        if (loadFactor <= 0f || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        }

        // Round capacity to a power of 2
        int capacity = 1;
        while (capacity < initialCapacity) { 
            capacity <<= 1;
        }        
        
        this.load_factor = loadFactor;
        this.threshold = (int)(capacity * this.load_factor);
        this.entries = new Entry[capacity];
    }
    
    // Obtained from http://www.concentric.net/~Ttwang/tech/inthash.htm
    private static int hash(long key) {                
        key = (~key) + (key << 18); // key = (key << 18) - key - 1;
        key = key ^ (key >>> 31);
        key = key * 21; // key = (key + (key << 2)) + (key << 4);
        key = key ^ (key >>> 11);
        key = key + (key << 6);
        key = key ^ (key >>> 22);
        return (int) key;
    }
    
    private static int indexFor(int hash, int length) {
        return hash & (length - 1);
//        return hash % length;
    }
    
    /** Resize the entry array.
     * 
     *  @param newCapacity The new capacity for the map, must be a power of two */
    private void resize(int newCapacity) {
        Entry[] old_entries = this.entries;
        int old_capacity = old_entries.length;
        if (old_capacity == MAXIMUM_CAPACITY) {
            this.threshold = Integer.MAX_VALUE;
        } else {          
            Entry[] new_entries = new Entry[newCapacity];
            transfer(this.entries, new_entries);
            this.entries = new_entries;
            threshold = (int)(newCapacity * this.load_factor);
        }
    }
    
    private void transfer(Entry[] from, Entry[] to) {
        int newCapacity = to.length;
        for (int i = 0; i < from.length; i++) {
            Entry e = from[i];
            if (e != null) {
                from[i] = null;
                do {
                    Entry next = e.next;
                    int h = hash(e.key);
                    int index = indexFor(h, newCapacity);  
                    e.next = to[index];
                    to[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }
    
    public void clear() {
        this.size = 0;
        Arrays.fill(this.entries, null);
    }
    
    public boolean containsKey(long key) {
        Entry[] entries = this.entries;
        int h = hash(key);
        int index = indexFor(h, entries.length);        
        
        for (Entry e = entries[index]; e != null; e = e.next) {
            if (e.key == key) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean containsValue(byte value) {
        Entry[] entries = this.entries;
        
        // Exhaustive search, this will be slow in most cases        
        for (int i = 0; i < entries.length; i++) {
            for (Entry e = entries[i]; e != null; e = e.next) {
                if (e.value == value) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean contains(long key, byte value) {
        Entry[] entries = this.entries;
        int h = hash(key);
        int index = indexFor(h, entries.length);        
        
        for (Entry e = entries[index]; e != null; e = e.next) {
            if (e.key == key) {
                return e.value == value;
            }
        }
        
        return false;
    }
    
//    public Set<Entry> entrySet() {
//        
//    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        
        LongToByteHashMap other = (LongToByteHashMap) o;
        if (this.size() != other.size()) return false;
        
        Entry[] entries = this.entries;        
        for (int i = 0; i < entries.length; i++) {
            for (Entry e = entries[i]; e != null; e = e.next) {
                if (!other.contains(e.key, e.value)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public byte get(long key, byte defaultValue) {
        Entry[] entries = this.entries;
        int h = hash(key);
        int index = indexFor(h, entries.length);
        
        for (Entry e = entries[index]; e != null; e = e.next) {
            if (e.key == key) {
                return e.value;
            }
        }
        
        return defaultValue;
    }
    
    @Override    
    public int hashCode() {
        int h = 0;
        
        Entry[] entries = this.entries;
           
        for (int i = 0; i < entries.length; i++) {
            for (Entry e = entries[i]; e != null; e = e.next) {
                h += e.hashCode();
            }
        }
        
        return h;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
//    public LongSet keySet() {
//        
//    }
    
    public byte put(long key, byte value, byte defaultValue) {
        Entry[] entries = this.entries;
        int h = hash(key);
        int index = indexFor(h, entries.length);
        
        Entry prev = null;
        for (Entry e = entries[index]; e != null; prev = e, e = e.next) {
            if (e.key == key) {
                if (MOVE_TO_FRONT && prev != null) {
                    prev.next = e.next;
                    e.next = entries[index];
                    entries[index] = e;
                }
                
                byte prev_value = e.value;
                e.value = value;
                return prev_value;
            }
        }
        
        // add the new entry
        Entry new_entry = new Entry(key, value);
        if (entries[index] == null) {
            entries[index] = new_entry;
        } else if (INSERT_IN_FRONT) {
            new_entry.next = entries[index];
            entries[index] = new_entry;
        } else {
            prev.next = new_entry;
        }
        
        if (this.size++ > this.threshold) {
            this.resize(this.entries.length * 2);
        }
        
        return defaultValue;
    }
    
//    public void putAll(LongToByteHashMap map) {
//        
//    }
    
    public byte remove(long key, byte defaultValue) {
        Entry[] entries = this.entries;
        int h = hash(key);
        int index = indexFor(h, entries.length);
        
        Entry prev = null;
        for (Entry e = entries[index]; e != null; prev = e, e = e.next) {
            if (e.key == key) {
                if (prev == null) {
                    entries[index] = e.next;
                } else {
                    prev.next = e.next;
                }
                e.next = null;
                this.size--;
                return e.value;
            }
        }
        
        return defaultValue;
    }
    
    public int size() {
        return this.size;
    }
    
//    public Collection<V> values() {
//        
//    }
    
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        boolean first = true;

        
        Entry[] entries = this.entries;        
        for (int i = 0; i < entries.length; i++) {
            for (Entry e = entries[i]; e != null; e = e.next) {
                if (first) {
                    first = false;
                } else {
                    buf.append(", ");
                }
                buf.append(e.key);
                buf.append("=");
                buf.append(e.value);
            }
        }       

        buf.append("}");
        return buf.toString();
    }
    
    private static class Entry {        
        public long key;
        public byte value;
        public Entry next;
        
        public Entry(long key, byte value) {
            this.key = key;
            this.value = value;
        }
    }
}
