package iceberg.io;

public class Tee implements DataStream {
    private DataStream out1;
    private DataStream out2;

    public Tee(DataStream out1, DataStream out2) {
        this.out1 = out1;
        this.out2 = out2;
    }

    public void flush() {
    	try {
    		this.out1.flush();
    	} finally {
    		this.out2.flush();
    	}
    }

    public void close() {
    	try {
    		this.out1.close();
    	} finally {
    		this.out2.close();
    	}
    }

    public void print(boolean b) {
        this.out1.print(b);
        this.out2.print(b);
    }

    public void print(char c) {
        this.out1.print(c);
        this.out2.print(c);
    }

    public void print(char[] s) {
        this.out1.print(s);
        this.out2.print(s);
    }

    public void print(double d) {
        this.out1.print(d);
        this.out2.print(d);
    }

    public void print(float f) {
        this.out1.print(f);
        this.out2.print(f);
    }

    public void print(int i) {
        this.out1.print(i);
        this.out2.print(i);
    }

    public void print(long l) {
        this.out1.print(l);
        this.out2.print(l);
    }

    public void print(Object obj) {
        this.out1.print(obj);
        this.out2.print(obj);
    }

    public void print(String s) {
        this.out1.print(s);
        this.out2.print(s);
    }

    public void println() {
        this.out1.println();
        this.out2.println();
    }

    public void println(boolean x) {
        this.out1.println(x);
        this.out2.println(x);
    }

    public void println(char x) {
        this.out1.println(x);
        this.out2.println(x);
    }

    public void println(char[] x) {
        this.out1.println(x);
        this.out2.println(x);
    }

    public void println(double x) {
        this.out1.println(x);
        this.out2.println(x);
    }

    public void println(float x) {
        this.out1.println(x);
        this.out2.println(x);
    }

    public void println(int x) {
        this.out1.println(x);
        this.out2.println(x);
    }

    public void println(long x) {
        this.out1.println(x);
        this.out2.println(x);
    }

    public void println(Object x) {
        this.out1.println(x);
        this.out2.println(x);
    }

    public void println(String x) {
        this.out1.println(x);
        this.out2.println(x);
    }
}
