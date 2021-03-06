package iceberg.io;

import java.io.PrintStream;

public class PrintStreamAdapter implements DataStream {
    private final PrintStream out;
    private final boolean autoflush;

    public PrintStreamAdapter(PrintStream out) {
        this(out, false);
    }

    public PrintStreamAdapter(PrintStream out, boolean autoFlush) {
        this.out = out;
        this.autoflush = autoFlush;
    }

    public PrintStream getPrintStream() {
        return this.out;
    }

    public void print(boolean x) {
        this.out.print(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void print(char x) {
        this.out.print(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void print(char[] x) {
        this.out.print(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void print(double x) {
        this.out.print(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void print(float x) {
        this.out.print(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void print(int x) {
        this.out.print(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void print(Object x) {
        this.out.print(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void print(String x) {
        this.out.print(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void print(long x) {
        this.out.print(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void println() {
        this.out.println();
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void println(boolean x) {
        this.out.println(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void println(char x) {
        this.out.println(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void println(char[] x) {
        this.out.println(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void println(double x) {
        this.out.println(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void println(float x) {
        this.out.println(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void println(int x) {
        this.out.println(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void println(Object x) {
        this.out.println(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void println(String x) {
        this.out.println(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void println(long x) {
        this.out.println(x);
        if (this.autoflush) {
            this.out.flush();
        }
    }

    public void flush() {
        this.out.flush();
    }

    public void close() {
        this.out.close();
    }
}
