package iceberg.graph;

import java.util.HashMap;

public class HashNodeIndexer<N> implements NodeIndexer<N> {    
    private HashMap<N, Integer> map;

    public HashNodeIndexer() {
        this.map = new HashMap<N, Integer>();
    }
    
    public int getIndex(N node) {
        Integer index = this.map.get(node);
        if (index == null) {
            index = this.map.size();
            this.map.put(node, index);
        }
        
        return index;
    }
}
