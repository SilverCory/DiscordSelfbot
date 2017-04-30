package selfbot.java;

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

    public static String numberStr(int number) {
        switch (number) {
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
            case 0:
            default:
                return "zero";
        }
    }
}
