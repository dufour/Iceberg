package iceberg.util.functions;

import java.util.ArrayList;
import java.util.List;

public final class Functional {
    private Functional() {
        // No instances
    }
    
    public static <D,I> List<I> map(Function<D,I> f, List<D> values) {
        if (values == null) return null;
        
        List<I> result = new ArrayList<I>(values.size());
        for (D value: values) {
            result.add(f.evaluate(value));
        }
        
        return result;
    }
    
    public static int[] map(IntFunction f, int[] values) {
        if (values == null) return null;
        
        int[] result = new int[values.length];
        int index = 0;
        for (int value: values) {
            result[index++] = f.apply(value);
        }
        
        return result;
    }
}
