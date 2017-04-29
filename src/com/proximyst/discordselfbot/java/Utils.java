package com.proximyst.discordselfbot.java;

import clojure.lang.IFn;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Utils {
    public static <T> Consumer<T> fnConsumer(IFn method) {
        return method::invoke;
    }

    public static <T> Supplier<T> fnSupplier(IFn method) {
        return () -> (T) method.invoke();
    }

    public static <T, U> Function<T, U> fnFunction(IFn method) {
        return (l) -> (U) method.invoke(l);
    }
}
