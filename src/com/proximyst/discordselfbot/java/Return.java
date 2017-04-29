package com.proximyst.discordselfbot.java;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Return wrapper for clojure methods.
 */
public class Return extends Exception {
    public Return() {
    }

    public Return(final String comment) {
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return new StackTraceElement[0];
    }

    @Override
    public String getMessage() {
        return "Returned out of method";
    }

    @Override
    public String getLocalizedMessage() {
        return "Returned out of method";
    }

    @Override
    public synchronized Throwable getCause() {
        return null;
    }

    @Override
    public void printStackTrace() {
    }

    @Override
    public void printStackTrace(final PrintStream s) {
    }

    @Override
    public void printStackTrace(final PrintWriter s) {
    }
}
