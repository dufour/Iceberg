package iceberg.io;

public interface DataStream {
    public void print(boolean x);
    public void print(char x);
    public void print(char[] x);
    public void print(double x);
    public void print(float x);
    public void print(int x);
    public void print(Object x);
    public void print(String x);
    public void print(long x);

    public void println();
    public void println(boolean x);
    public void println(char x);
    public void println(char[] x);
    public void println(double x);
    public void println(float x);
    public void println(int x);
    public void println(Object x);
    public void println(String x);
    public void println(long x);
    
    public void flush();
    public void close();
}
