package iceberg.io;

public class NullDataStream implements DataStream {
    private static NullDataStream instance = null;

    public static NullDataStream instance() {
        NullDataStream x = NullDataStream.instance;
        if (x == null) {
            x = new NullDataStream();
            NullDataStream.instance = x;
        }

        return x;
    }
    
    protected NullDataStream() {
        // noop
    }

    public void print(boolean x) {
        // noop
    }

    public void print(char x) {
        // noop
    }

    public void print(char[] x) {
        // noop
    }

    public void print(double x) {
        // noop
    }

    public void print(float x) {
        // noop
    }

    public void print(int x) {
        // noop
    }

    public void print(Object x) {
        // noop
    }

    public void print(String x) {
        // noop
    }

    public void print(long x) {
        // noop
    }

    public void println() {
        // noop
    }

    public void println(boolean x) {
        // noop
    }

    public void println(char x) {
        // noop
    }

    public void println(char[] x) {
        // noop
    }

    public void println(double x) {
        // noop
    }

    public void println(float x) {
        // noop
    }

    public void println(int x) {
        // noop
    }

    public void println(Object x) {
        // noop
    }

    public void println(String x) {
        // noop
    }

    public void println(long x) {
        // noop
    }

    public void flush() {
        // noop
    }

    public void close() {
        // noop
    }
}
