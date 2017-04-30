package selfbot.java;

public class MutableVar<T> {
    private T value;

    public MutableVar(T t) {
        this.value = t;
    }

    public MutableVar() {
        this(null);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
