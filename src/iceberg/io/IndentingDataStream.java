package iceberg.io;

import iceberg.util.Strings;

public class IndentingDataStream implements DataStream {
    private static final int DEFAULT_INDENT_STEP = 4;

    private DataStream out;
    private int step;  // Indentation step
    private int level; // Current indentation level
    private boolean eol; // True if we need to indent before outputting anything
    private String[] indent_strings;

    public IndentingDataStream(DataStream out) {
        this(out, DEFAULT_INDENT_STEP);
    }

    public IndentingDataStream(DataStream out, int indentStep) {
        this.out = out;
        this.step = indentStep;

        this.level = 0;
        this.eol = true;
        this.indent_strings = new String[32];
    }

    public DataStream getStream() {
        return this.out;
    }

    public int getIndentStep() {
        return this.step;
    }

    protected void printIndent() {
        this.printIndent(this.level);
    }

    protected void printIndent(int level) {
        // Easy way out?
        if (level <= 0) {
            return;
        }

        int indent_level;
        if (level < this.indent_strings.length) {
            indent_level = level;
        } else {
            indent_level = this.indent_strings.length - 1;
        }

        String indent = this.indent_strings[level];
        if (indent == null) {
            indent = Strings.stringOf(' ', level * this.step);
            this.indent_strings[level] = indent;
        }

        this.out.print(indent);

        if (level != indent_level) {
            // Pad with single spaces
            // NOTE: this is not efficient, but chances that this code
            // will be executed are very slim. If we really get here too often
            // then the indent string cache size should be increased
            // appropriately.
            for (int i = indent.length(); i < level; i++) {
                this.out.print(' ');
            }
        }
        this.eol = false;
    }

    public void indent() {
        this.indent(1);
    }

    public void unindent() {
        this.unindent(1);
    }

    public void indent(int count) {
        if (count > 0) {
            this.level += count;
        }
    }

    public void unindent(int count) {
        if (count > 0) {
            this.level -= count;
            if (this.level < 0) {
                this.level = 0;
            }
        }
    }

    protected void print_internal(String s) {
        int i = 0;
        int j = s.indexOf('\n', i);
        while (j >= 0) {
            if (this.eol) {
                this.printIndent();
            }
            this.out.println(s.substring(i, j));
            this.eol = true;
            i = j + 1;
            j = s.indexOf('\n', i);
        }

        if (i < s.length()) {
            if (this.eol) {
                this.printIndent();
            }
            this.out.print(s.substring(i));
        }
    }

    public void print(boolean x) {
        this.print_internal(String.valueOf(x));
    }

    public void print(char x) {
        this.print_internal(String.valueOf(x));
    }

    public void print(char[] x) {
        this.print_internal(String.valueOf(x));
    }

    public void print(double x) {
        this.print_internal(String.valueOf(x));
    }

    public void print(float x) {
        this.print_internal(String.valueOf(x));
    }

    public void print(int x) {
        this.print_internal(String.valueOf(x));
    }

    public void print(Object x) {
        this.print_internal(String.valueOf(x));
    }

    public void print(String x) {
        this.print_internal(String.valueOf(x));
    }

    public void print(long x) {
        this.print_internal(String.valueOf(x));
    }

    public void println() {
        this.print_internal("\n");
    }

    public void println(boolean x) {
        this.print_internal(String.valueOf(x) + "\n");
    }

    public void println(char x) {
        this.print_internal(String.valueOf(x) + "\n");
    }

    public void println(char[] x) {
        this.print_internal(String.valueOf(x) + "\n");
    }

    public void println(double x) {
        this.print_internal(String.valueOf(x) + "\n");
    }

    public void println(float x) {
        this.print_internal(String.valueOf(x) + "\n");
    }

    public void println(int x) {
        this.print_internal(String.valueOf(x) + "\n");
    }

    public void println(Object x) {
        this.print_internal(String.valueOf(x) + "\n");
    }

    public void println(String x) {
        this.print_internal(String.valueOf(x) + "\n");
    }

    public void println(long x) {
        this.print_internal(String.valueOf(x) + "\n");
    }

    public void flush() {
        this.out.flush();
    }

    public void close() {
        this.out.close();
    }
}
