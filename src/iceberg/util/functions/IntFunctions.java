package iceberg.util.functions;

public final class IntFunctions {
    private IntFunctions() {
        // No instances
    }
    
    public static IntFunction add(final int amount) {
        return new IntFunction() {
            public int apply(int x) {
                return x + amount;
            }            
        };
    }
    
    public static IntFunction multiply(final int factor) {
        return new IntFunction() {
            public int apply(int x) {
                return x * factor;
            }            
        };
    }
}
