package iceberg.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class UserInput {
    private static final UserInput stdin = new UserInput(System.in);

    private BufferedReader in;
    private PrintStream out;

    public UserInput(InputStream in) {
        this(in, System.out);
    }

    public UserInput(InputStream in, PrintStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = out;
    }

    public String read() throws IOException {
        return this.in.readLine();
    }

    public String ask(String prompt) throws IOException {
        try {
            this.out.print(prompt);
            return this.read();
        } catch (IOException e) {
            this.out.println();
            throw e; // rethrow
        }
    }

    public static UserInput stdin() {
        return UserInput.stdin;
    }
}
