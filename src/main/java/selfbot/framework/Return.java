package selfbot.framework;

import java.io.PrintStream;
import java.io.PrintWriter;

public class Return extends Exception {
    public static final Return instance = new Return();

    @Override
    public StackTraceElement[] getStackTrace() {
        return new StackTraceElement[0];
    }

    @Override
    public String getMessage() {
        return "Returned out of method.";
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return null;
    }

    @Override
    public void printStackTrace() {
    }

    @Override
    public void printStackTrace(PrintStream s) {
    }

    @Override
    public void printStackTrace(PrintWriter s) {
    }
}
